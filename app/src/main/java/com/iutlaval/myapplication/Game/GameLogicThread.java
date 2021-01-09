package com.iutlaval.myapplication.Game;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.iutlaval.myapplication.Game.Cards.Card;
import com.iutlaval.myapplication.Game.Cards.CardRegistery;
import com.iutlaval.myapplication.GameActivity;
import com.iutlaval.myapplication.MainActivity;
import com.iutlaval.myapplication.PopupRunable;
import com.iutlaval.myapplication.R;
import com.iutlaval.myapplication.Video.Drawables.DrawableBitmap;
import com.iutlaval.myapplication.Video.Drawables.DrawableCard;
import com.iutlaval.myapplication.Video.Drawables.DrawableSelfRemoving;
import com.iutlaval.myapplication.Video.Drawables.DrawableText;
import com.iutlaval.myapplication.Video.Renderer;
import com.iutlaval.myapplication.exception.UnrecognizedCard;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OptionalDataException;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

public class GameLogicThread extends Thread implements SoundPool.OnLoadCompleteListener,MediaPlayer.OnCompletionListener {

    public static final float BUTTON_Y_POS = 45F;
    public static final int BUTTON_X_POS = 0;
    public static final float BUTTON_X_SIZE = 20F;
    public static final float BUTTON_Y_SIZE = 10F;
    public static final String BUTTONTURN_DRAWABLE_NAME = "buttonturn";
    private Context cont;
    private Renderer renderer;
    private boolean ready;
    private TouchHandler touch;
    private GameActivity gameActivity;


    private NetworkDeck deck;
    private Hand hand;
    private Board board;
    private boolean isYourMainPhase;
    private boolean isYourBattlePhase;
    private boolean cancelled;
    private String deckName;
    private int mana=0;
    private Communication coms;


    //ces variable permette la comunication avec le thread android du tactile et le reseau en passant par ce thread
    //cette variable controle la demande de gestion de placement de carte
    private boolean requestPlaceCardDone =true;
    //cette variable controle la demande de gestion de l'attaque
    boolean requestAttackDone = true;
    private DrawableCard requestDrawableCard =null;
    private int requestCardZone =-1;
    private boolean requestPlaceCardResult;

    private boolean soundPoolLoaded = false;
    private HashMap<String, Integer> soundMap;
    private int loopMusic;
    private SoundPool soundPool;
    private MediaPlayer backgroundMusicPlayer;

    //si mise a true alors le thread va envoyer un message au serveur lui disant de passer a la phase suivante
    private boolean requestEnd = false;

    public GameLogicThread(GameActivity gameActivity, String deckName, Renderer renderer)
    {
        this.deckName=deckName;
        Log.e("RESOLUTION:",""+GameActivity.screenWidth+"x"+GameActivity.screenHeight);
        ready=false;
        this.cont = gameActivity;
        this.renderer = renderer;
        board=new Board();
        touch = new TouchHandler(renderer,this,gameActivity);
        hand = new Hand();
        this.gameActivity=gameActivity;
        GameActivity.setGameEngine(this);
        isYourMainPhase =false;
        cancelled=false;
        isYourBattlePhase=false;
    }

    @Override
    public void run() {
        //pickDeck();

        //loading textures
        Log.i("ressources","loading bitmaps");
        Bitmap bitmapYourTurn = BitmapFactory.decodeResource(renderer.getResources(),R.drawable.t_pb_your_turn);
        Bitmap bitmapEnemyTurn = BitmapFactory.decodeResource(renderer.getResources(),R.drawable.t_pb_enemy_turn);
        Bitmap bitmapBattlePhase = BitmapFactory.decodeResource(renderer.getResources(),R.drawable.t_pb_battlephase);
        Bitmap bitmapButtonEnd = BitmapFactory.decodeResource(renderer.getResources(),R.drawable.t_btn_boutonfintour);
        Bitmap bitmapVictory =  BitmapFactory.decodeResource(cont.getResources(), R.drawable.t_pb_victory);
        Bitmap bitmapDefeat =  BitmapFactory.decodeResource(cont.getResources(), R.drawable.t_pb_defeat);

        Log.i("loading","prefs");
        SharedPreferences sharedPref = gameActivity.getSharedPreferences("handicape",Context.MODE_PRIVATE);
        boolean modeHandicape = sharedPref.getBoolean(gameActivity.getString(R.string.handicape), false);

        Bitmap bitmapBackground;
        if(!modeHandicape)
        {
            bitmapBackground = BitmapFactory.decodeResource(cont.getResources(), R.drawable.t_b_board_background);
        }else{
            bitmapBackground = BitmapFactory.decodeResource(cont.getResources(), R.drawable.t_b_board_background_handicapted);
        }

        final int maxAudioStream = 2;
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //déprecier aprés android lolipop
            soundPool = new SoundPool(maxAudioStream, AudioManager.STREAM_MUSIC,100);
        }else{
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(maxAudioStream);
            soundPool = builder.build();
        }
        soundPool.setOnLoadCompleteListener(this);


        int startSound;
        switch (deckName.trim())
        {
            default:
                Log.e("Music","unrecognized deck loading default");
            case "mythes et legendes grecs":
                startSound=R.raw.mytes_start;
                loopMusic =R.raw.mytes_loop;
                break;
            case "moyen-age francais":
                startSound=R.raw.moyenage_start;
                loopMusic =R.raw.moyenage_loop;
                break;
            case  "renaissance":
                startSound=R.raw.renaissence_start;
                loopMusic =R.raw.renaissence_loop;
                break;
        }
        backgroundMusicPlayer = MediaPlayer.create(cont,startSound);
        backgroundMusicPlayer.setOnCompletionListener(this);
        backgroundMusicPlayer.setScreenOnWhilePlaying(true);
        backgroundMusicPlayer.start();



        Log.i("ressources","base ressources loaded");

        //adding the background
        renderer.addToDraw(new DrawableBitmap(bitmapBackground, 0,0, "background", 100, 100 ));

        ready=true;

        final String host = "2.tcp.ngrok.io";//192.168.43.251tcp://2.tcp.ngrok.io:
        final int port = 17057;


        //on fait trois essay de connection
        for(int i = 0 ; i < 3 ; i++)
        {
            try {
                Socket client = new Socket(host, port);
                coms= new Communication(client);
                //connection reussie
                break;
            } catch (IOException e) {}
        }
        //si la connection a échouer on print un log + un toast
        if(coms == null)
        {
            Log.e("ERROR SERVER DEAD","srv");
            gameActivity.runOnUiThread(new PopupRunable("erreur de comunication avec le serveur",gameActivity));
            //on arette le moteur de rendu
            renderer.terminate();
            //et on retourne la 'activité precedente
            returnToTheMainActivity();
            return;
        }


        renderer.updateFrame();

        //principe de fonctionnement : on attends une comande du server et on l'execute
        //si quelquechose se passe alors c'est le server qu'il l'a dit
        while(ready && !cancelled)
        {
            if(!requestPlaceCardDone && requestCardZone != -1 && requestDrawableCard != null)
            {
                try {//on allonge le delais car sinon on skip l'autorisation serveur
                    coms.setUnlimitedTimeOut();
                } catch (SocketException e) {}
                requestPlaceCardResult = onCardPlayed(requestDrawableCard, requestCardZone);
                requestCardZone =-1;
                requestDrawableCard =null;
                Log.e("request","done");
                requestPlaceCardDone =true;
            }

            if(!requestAttackDone && requestCardZone != -1 && requestDrawableCard != null)
            {
                int zoneAttaquant = board.getCardZone(requestDrawableCard);

                try {
                    coms.send(Command.ATTACK);
                    coms.send(zoneAttaquant);
                    coms.send(requestCardZone);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                requestAttackDone=true;
            }

            if(requestEnd)
            {
                try {
                    Log.e("TURN","PASSED");
                    coms.send(Command.PASS_TURN);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                requestEnd=false;
                isYourBattlePhase=false;
            }




            try {
                int zonecartecible;
                //on limite le temps du readobject pour eviter les blockaeg
                coms.setLimitedTimeOut();
                String serveurCmd = coms.recieve();
                coms.setUnlimitedTimeOut();
                if(!serveurCmd.equals("timeout"))Log.e("command",serveurCmd);
                switch (serveurCmd)
                {
                    case Command.GET_DECK:
                        coms.send(deckName);
                        String deckstr = coms.recieve();
                        deck = new NetworkDeck(deckstr,gameActivity.getBaseContext());
                        Log.e("got deck",deckstr);
                        break;
                    case Command.DRAW:
                        int nbcard = coms.recieveInt();
                        hand.pickCardFromDeck(deck,nbcard);
                        Log.e("picked",nbcard+"card");
                        drawHandPreview();
                        break;

                    case Command.YOURTURN:
                        isYourMainPhase =true;
                        isYourBattlePhase=false;
                        renderer.addToDrawWithoutUpdate(new DrawableSelfRemoving(new DrawableBitmap(bitmapYourTurn,0,0,"yourTurn",100F,50F),1));
                        renderer.addToDraw(new DrawableBitmap(bitmapButtonEnd, BUTTON_X_POS, BUTTON_Y_POS, BUTTONTURN_DRAWABLE_NAME, BUTTON_X_SIZE, BUTTON_Y_SIZE));
                        break;

                    case Command.SETMANA:
                        renderer.removeToDrawWithoutUpdate("mana");
                        mana = coms.recieveInt();
                        renderer.addToDraw(new DrawableText("Mana : "+mana,85F,60F,"mana",13.3F,6.65F,20, 100,30,Color.WHITE));
                        break;

                    case Command.ENEMYTURN:
                        isYourMainPhase =false;

                        renderer.removeToDraw(BUTTONTURN_DRAWABLE_NAME);
                        renderer.removeToDraw("battlePhase");
                        renderer.addToDraw(new DrawableSelfRemoving(new DrawableBitmap(bitmapEnemyTurn,0,0,"enemyTurn",100F,50F),1));
                        break;

                    case Command.POPUP:
                        String recivedMessage = coms.recieve();
                        gameActivity.runOnUiThread(new PopupRunable(recivedMessage,gameActivity));
                        Log.e("popup",recivedMessage);
                        break;

                    case Command.WIN:
                        //on termine la parite
                        gameActivity.runOnUiThread(new PopupRunable("bravo ! vous avez gagné",gameActivity));
                        renderer.addToDraw(new DrawableBitmap(bitmapVictory,0,0,"victory",100F,50F));
                        renderer.updateFrame();
                        cancelled = true;
                        currentThread().sleep(2000);
                        returnToTheMainActivity();
                        break;
                    case Command.LOSE:
                        //on termine la partie
                        gameActivity.runOnUiThread(new PopupRunable("vous avez perdu!",gameActivity));
                        renderer.addToDraw(new DrawableBitmap(bitmapDefeat,0,0,"defeat",100F,50F));
                        renderer.updateFrame();
                        cancelled=true;
                        currentThread().sleep(2000);
                        //gameativity.finish() n'etait pas utilisable il fermais l'application
                        returnToTheMainActivity();
                        break;
                    case Command.PING:
                        coms.send(Command.PONG);
                        break;

                    case Command.UPDATE:
                        renderer.updateFrame();

                    case Command.PUT_ENEMY_CARD:
                        int cardId = coms.recieveInt();
                        int zone = coms.recieveInt();

                        //on instancie la carte recu
                        Class<? extends Card> c = CardRegistery.get(cardId);
                        Constructor con = c.getConstructor(String.class, Context.class);
                        Card cardPlayed = (Card) con.newInstance("enemy"+zone,gameActivity);
                        cardPlayed.getDrawableCard().setOnBoard(true);

                        //ajout de la carte au rendu
                        cardPlayed.getDrawableCard().setCoordinates(((DrawableCard.getCardWith()+1)*zone+20),10F);
                        renderer.addToDraw(cardPlayed.getDrawableCard());

                        //ajout la card au terain
                        cardPlayed.getDrawableCard().setDraggable(false);
                        board.setEnemyCard(zone,cardPlayed);
                        break;

                    case Command.MEULE:
                        //la carte pioché va être perdu vu que l'on ne la met pas dans une main
                        //c'est le role de la command meule de faire perde des cartes au joeur
                        int nbmeule = coms.recieveInt();
                        deck.draw(nbmeule);
                        break;

                    case Command.BATTLE:
                        renderer.addToDraw(new DrawableSelfRemoving(new DrawableBitmap(bitmapBattlePhase,0,0,"battlePhase",100F,50F),1));
                        isYourBattlePhase=true;
                        isYourMainPhase=false;
                        break;

                    case Command.DESTROY_CARD:
                        //detruit la carte sur le terrain du joueur
                        int zoneOfTheDestroyedCard = coms.recieveInt();
                        Card cardToDestroy = board.removeCardOnPlayerBoard(zoneOfTheDestroyedCard);
                        renderer.removeToDraw(cardToDestroy);
                        break;

                    case Command.DESTROY_ADV_CARD:
                        //detruit la carte sur le terrain du joueur adverse
                        int zoneOfTheDestroyedEnemyCard = coms.recieveInt();
                        Card enemyCardToDestroy = board.removeCardOnEnemyPlayerBoard(zoneOfTheDestroyedEnemyCard);
                        renderer.removeToDraw(enemyCardToDestroy);
                        break;

                    case Command.SET_ENEMY_HP:
                        renderer.removeToDrawWithoutUpdate("advHp");
                        renderer.addToDraw(new DrawableText(" Hp  : " + coms.recieve(),1F,10F,"advHp",13.3F,6.65F,20, 100,30,Color.WHITE));

                        break;

                    case Command.SET_HP:
                        renderer.removeToDrawWithoutUpdate("playerHp");
                        renderer.addToDraw(new DrawableText(" Hp  : " + coms.recieve(),85F,80F,"playerHp",13.3F,6.65F,20, 100,30,Color.WHITE));
                        break;

                    case Command.SET_CARD_HP :
                        zonecartecible = coms.recieveInt();
                        int health = coms.recieveInt();
                        if(board.getPlayerCardsOnBoard()[zonecartecible] == null)
                        {
                            Log.e("erro card",""+zonecartecible);
                            coms.recieveInt();
                        }else{
                            board.getPlayerCardsOnBoard()[zonecartecible].setHealth(health);
                        }

                        renderer.updateFrame();
                        break;

                    case Command.SET_ADV_CARD_HP:
                        zonecartecible = coms.recieveInt();
                        if(board.getAdvCardsOnBoard()[zonecartecible] == null)
                        {
                            Log.e("erro card",""+zonecartecible);
                        }
                        board.getAdvCardsOnBoard()[zonecartecible].setHealth(coms.recieveInt());
                        renderer.updateFrame();
                        break;

                    case Command.SET_CARD_ATK:
                        zonecartecible = coms.recieveInt();
                        board.getPlayerCardsOnBoard()[zonecartecible].setAttack(coms.recieveInt());
                        renderer.updateFrame();
                        break;

                    case Command.SET_ADV_CARD_ATK:
                        zonecartecible = coms.recieveInt();
                        board.getAdvCardsOnBoard()[zonecartecible].setAttack(coms.recieveInt());
                        renderer.updateFrame();
                        break;

                    case "timeout":
                        //ceci est une erreur et non une commande elle se doit donc de ne rien faire
                        break;

                    default:
                        Log.e("UNKOWN COMMAND",serveurCmd);
                }
            }catch (SocketTimeoutException e){}
            catch (InterruptedIOException e)
            {
                if(e.getStackTrace().length != 0)
                {
                    e.printStackTrace();
                    Log.e("Stopping","dead thread");
                }

            }catch(IOException e)
            {
            }
            catch (Exception e) {
                Log.e("ERROR DURING COMS","SERVER DIED");
                e.printStackTrace();
                if(e instanceof OptionalDataException)
                {
                    System.out.println(((OptionalDataException)e).eof);
                }
                break;
            }
        }
    }

    private void returnToTheMainActivity() {
        //gameativity.finish() n'etait pas utilisable il fermais l'application
        gameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(gameActivity,MainActivity.class);
                //on reset le top et recrée l'activité principale
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                gameActivity.startActivity(intent);
            }
        });
    }

    /**
     * cette methode permet d'afficher la main
     */
    private void drawHandPreview() {
        int i = 0;
        for(Card c : hand.getHand())
        {
            renderer.addToDraw(c.getDrawableCard());
            renderer.moveToDraw(i* DrawableCard.getCardWith(),90F,c.getDrawableCard().getName());
            i++;
        }

    }

    /**
     * cette evenement est appelé indirectement par la fonction request onCardPlayed et permet d'envoyer au serveur le fait qu'un carte a été jouer
     * CE CODE CONTIEN DE l'ACCES RESEAU QUI N'APPARTIEN QU'A CE THREAD IS DOIT RESTER PRIVE
     * @param card la carte jouer
     * @param zone ou elle a été joué sur le terrain
     * @return retourne si la carte a put être jouer
     */
    private boolean onCardPlayed(DrawableCard card,int zone)
    {
        if(isYourMainPhase)
        {
            try {
                //on veut jouer une carte
                coms.send(Command.PUT_CARD);


                //on lui dit quelle carte on veut
                int cardId = CardRegistery.indexOf(card.getCard());
                if (cardId == -1) {
                    coms.send("CLIENT REGISTRY ERROR");
                    throw new UnrecognizedCard();
                }
                coms.send(cardId);
                coms.send(zone);

                if (coms.recieve().equals(Command.OK)) {
                    board.setCard(zone, card.getCard());
                    hand.removeCard(card.getCard());
                    return true;
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    protected synchronized boolean setOnCardPlayedRequest(DrawableCard card,int zone)
    {
        Log.e("request","recived");

        //on ne peut pas avoir 2 requette simultané
        while(!requestPlaceCardDone)
        {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) { }
        }

        this.requestDrawableCard =card;
        this.requestCardZone =zone;
        requestPlaceCardDone =false;

        //on attends la fin de la requette
        while(!requestPlaceCardDone) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return requestPlaceCardResult;
    }

    public synchronized boolean setOnCardAttackRequest(DrawableCard card, int zoneCible) {
        if(zoneCible == -1 || zoneCible != 100 && board.getAdvCardsOnBoard()[zoneCible] == null
        || card.getCard().getAttack() == 0)
        {
            return false;
        }


        //on ne peut pas avoir 2 requette simultané
        while(!requestAttackDone) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.requestCardZone =zoneCible;
        this.requestDrawableCard=card;
        requestAttackDone =false;

        //on attends la fin de la requette
        while(!requestAttackDone) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    /**
     * retourne vrai sir le moteur est pret a avoir sont evenemnt onFrameDoneRendering d'appeler
     * @return
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * délegue l'evenement directement au touch Handler
     * @param event
     */
    public void onTouchEvent(MotionEvent event) {
        if(isReady())touch.onTouchEvent(event);
    }

    public boolean isYourMainPhase() {
        return isYourMainPhase;
    }


    public Board getBoard() {
        return board;
    }

    /**
     * cette fonction a pour rôle de kill le thread
     * vu que Thread.destroy() est déprecier c'est la seul methode possible;
     */
    public void terminate() {
        ready=false;
        try {
            if(coms != null)coms.close();
        } catch (IOException e) {}
        cancelled=true;

        interrupt();
    }

    /**
     * déclanche une demande de debut de tours
     */
    public void requestEndTurn() {
        if(isYourMainPhase || isYourBattlePhase)
        {
            requestEnd=true;
        }else{
            Toast t = new Toast(gameActivity);
            t.setText("IT'S NOT YOUR TURN BE PATIENT");
            t.setDuration(Toast.LENGTH_SHORT);
            t.show();
        }
    }

    public boolean isYourBattlePhase() {
        return isYourBattlePhase;
    }


    @Override
    public void onLoadComplete(final SoundPool soundPool, int sampleId, int status) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.reset();
        backgroundMusicPlayer = MediaPlayer.create(cont, loopMusic);
        backgroundMusicPlayer.start();
        backgroundMusicPlayer.setLooping(true);
    }
}
