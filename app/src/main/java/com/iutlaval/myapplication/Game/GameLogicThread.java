package com.iutlaval.myapplication.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.iutlaval.myapplication.Game.Cards.Card;
import com.iutlaval.myapplication.Game.Cards.CardRegistery;
import com.iutlaval.myapplication.GameActivity;
import com.iutlaval.myapplication.R;
import com.iutlaval.myapplication.Video.Drawables.DrawableBitmap;
import com.iutlaval.myapplication.Video.Drawables.DrawableCard;
import com.iutlaval.myapplication.Video.Drawables.DrawableSelfRemoving;
import com.iutlaval.myapplication.Video.Drawables.DrawableText;
import com.iutlaval.myapplication.Video.Renderer;
import com.iutlaval.myapplication.exception.UnrecognizedCard;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class GameLogicThread extends Thread{

    public static final float BUTTON_Y_POS = 50F;
    public static final int BUTTON_X_POS = 0;
    public static final float BUTTON_X_SIZE = 5F;
    public static final float BUTTON_Y_SIZE = 2.5F;
    private Context cont;
    private Renderer renderer;
    private boolean ready;
    private TouchHandler touch;
    private GameActivity gameActivity;


    private NetworkDeck deck;
    private Hand hand;
    private Board board;
    private boolean isYourTurn;
    private boolean cancelled;
    private String deckName;
    private int mana=0;
    private Communication coms;


    //ces variable permette la comunication avec le thread android du tactile et le reseau en passant par ce thread
    boolean requestDone=true;
    DrawableCard requestCard=null;
    int requestZone=-1;
    boolean requestResult;


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
        isYourTurn=false;
        cancelled=false;
    }

    @Override
    public void run() {
        //pickDeck();

        //loading textures
        Bitmap bitmapYourTurn = BitmapFactory.decodeResource(renderer.getResources(),R.drawable.t_pb_your_turn);
        Bitmap bitmapEnemyTurn = BitmapFactory.decodeResource(renderer.getResources(),R.drawable.t_pb_enemy_turn);
        Bitmap bitmapButtonEnd = BitmapFactory.decodeResource(renderer.getResources(),R.drawable.t_pb_enemy_turn);
        Bitmap bitmap= BitmapFactory.decodeResource(cont.getResources(), R.drawable.t_b_board_background);

        //adding the background
        renderer.addToDraw(new DrawableBitmap(bitmap, 0,0, "background", 100, 100 ));
        //drawHandPreview();
        //Rectangle pos = new Rectangle(0F,0F,100F,100F);
        ready=true;

        final String host = "0.tcp.ngrok.io";//192.168.43.251tcp://2.tcp.ngrok.io:
        final int port = 13562;

        try {
            Socket client = new Socket(host, port);
            coms= new Communication(client);
        } catch (IOException e) {
            Log.e("ERROR SERVER DEAD","srv");
            //TODO display error and don't crash the game;
            System.exit(1);
        }
        renderer.updateFrame();

        //principe de fonctionnement : on attends une comande du server et on l'execute
        //si quelquechose se passe alors c'est le server qu'il l'a dit
        while(ready && !cancelled)
        {
            if(!requestDone && requestZone != -1 && requestCard != null)
            {
                try {//on allonge le delais car sinon on skip l'autorisation serveur
                    coms.setUnlimitedTimeOut();
                } catch (SocketException e) {}
                requestResult = onCardPlayed(requestCard,requestZone);
                requestZone=-1;
                requestCard=null;
                Log.e("request","done");
                requestDone=true;
            }


            try {
                //on limite le temps du readobject pour eviter les blockaeg
                coms.setLimitedTimeOut();
                String serveurCmd = coms.recieve();
                coms.setUnlimitedTimeOut();
                Log.e("command",serveurCmd);
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
                        isYourTurn=true;
                        renderer.addToDraw(new DrawableSelfRemoving(new DrawableBitmap(bitmapYourTurn,0,0,"yourTurn",100F,50F),1));
                        renderer.addToDraw(new DrawableBitmap(bitmapButtonEnd, BUTTON_X_POS, BUTTON_Y_POS,"buttonturn", BUTTON_X_SIZE, BUTTON_Y_SIZE));
                        break;

                    case Command.SETMANA:
                        renderer.removeToDraw("mana");
                        mana = coms.recieveInt();
                        renderer.addToDraw(new DrawableText("Mana : "+mana,90F,90F,"mana",10F,10F,100,800,200));
                        break;

                    case Command.ENEMYTURN:
                        renderer.addToDraw(new DrawableSelfRemoving(new DrawableBitmap(bitmapEnemyTurn,0,0,"enemyTurn",100F,50F),1));
                        break;

                    case Command.POPUP:
                        String recivedMessage = coms.recieve();
                        Toast popupToast = new Toast(gameActivity);
                        popupToast.setText(recivedMessage);
                        popupToast.setDuration(Toast.LENGTH_LONG);
                        popupToast.show();
                        Log.e("popup",recivedMessage);
                        break;

                    case Command.WIN:
                        //on termine la parite
                        cancelled = true;
                        Toast winToast = new Toast(gameActivity);
                        winToast.setText("bravo ! vous avez gagné");
                        winToast.setDuration(Toast.LENGTH_LONG);
                        winToast.show();
                        renderer.updateFrame();
                        break;
                    case Command.LOSE:
                        //on termine la partie
                        cancelled=true;
                        Toast loseToast = new Toast(gameActivity);
                        loseToast.setText("vous avez perdu!");
                        loseToast.setDuration(Toast.LENGTH_LONG);
                        loseToast.show();
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

                        //ajout de la carte au rendu
                        cardPlayed.getDrawableCard().setCoordinates(((DrawableCard.getCardWith()+1)*zone+DrawableCard.getCardWith()),10F);
                        renderer.addToDraw(cardPlayed.getDrawableCard());

                        //ajout la card au terain
                        board.setEnemyCard(zone,cardPlayed);
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

    /**
     * cette methode est UNIQUEMENT a bute de test elle ne sert a rien d'autre ne pas s'en servir
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
     * cette evenement est appelé par le touchHandler et permet d'envoyer au serveur le fait qu'un carte a été jouer
     * CE CODE CONTIEN DE l'ACCES RESEAU QUI N'APPARTIEN QU'A CE THREAD IS DOIT RESTER PRIVE
     * @param card la carte jouer
     * @param zone ou elle a été joué sur le terrain
     * @return retourne si la carte a put être jouer
     */
    private boolean onCardPlayed(DrawableCard card,int zone)
    {
        if(isYourTurn)
        {
            try {
                //on veut jouer une carte
                coms.send(Command.PUT_CARD);


                //on lui dit quelle caret on veut
                int cardId = CardRegistery.indexOf(card.getCard());
                if (cardId == -1) {
                    coms.send("CLIENT REGISTRY ERROR");
                    throw new UnrecognizedCard();
                }
                coms.send(cardId);
                coms.send(zone);

                Log.e("waiting","for ok");
                if (coms.recieve().equals(Command.OK)) {
                    board.setCard(zone, card.getCard());
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
        //on ne peut pas avoir 2 requette simultané
        if(!requestDone)return false;

        this.requestCard=card;
        this.requestZone=zone;
        requestDone=false;

        //on attends la fin de la requette
        while(!requestDone) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return requestResult;
    }

    /**
     * cette fonction est appeller a chaque fin de frame
     * CETTE FONCTION A UN IMPACT DIRECT SUR LES FPS ELLE SE DOIT D'être OPTIMAL
     * TOUT CALCUL REDONDANT CE DOIT D'AVOIR ETE PRE FAIT
     *
     * TOUT APELLE DE CETTE FONCTION SE DOIT D'ÊTRE PROTEGER PAR isReady()
     *
     * /!\ renderer.updateFrame(); va forcer le system a redessiner l'afficher qui ensuite reappelera onFrameDoneRendering() ce qui peut causer si le updateFrame() n'est pas proteger
     * une utilisation de la batterie massive
     */
    //TODO : supprimer cette fonction si on a rien a en faire
    public void onFrameDoneRendering()
    {
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

    public boolean isYourTurn() {
        return isYourTurn;
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

    public void onEndTurnButtonPushed() {
        if(isYourTurn)
        {
            try {
                coms.send(Command.PASS_TURN);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Toast t = new Toast(gameActivity);
            t.setText("IT'S NOT YOUR TURN BE PATIENT");
            t.setDuration(Toast.LENGTH_SHORT);
            t.show();
        }
    }
}
