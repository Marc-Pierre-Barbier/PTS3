package com.iutlaval.myapplication.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.MotionEvent;

import com.iutlaval.myapplication.Game.Cards.Card;
import com.iutlaval.myapplication.Game.Cards.CardRegistery;
import com.iutlaval.myapplication.Game.Decks.Deck;
import com.iutlaval.myapplication.Game.Decks.NetWorkDeck;
import com.iutlaval.myapplication.GameActivity;
import com.iutlaval.myapplication.R;
import com.iutlaval.myapplication.Video.Drawables.DrawableBitmap;
import com.iutlaval.myapplication.Video.Drawables.DrawableCard;
import com.iutlaval.myapplication.Video.Drawables.DrawableSelfRemoving;
import com.iutlaval.myapplication.Video.Renderer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GameLogicThread extends Thread{

    private Context cont;
    private Renderer renderer;
    private boolean ready;
    private TouchHandler touch;
    private GameActivity gameActivity;

    private Deck localPlayerDeck;
    private Hand localPlayerHand;
    private Board board;



    private boolean isYourTurn;

    public GameLogicThread(GameActivity gameActivity, Renderer renderer)
    {
        new CardRegistery();
        Log.e("RESOLUTION:",""+GameActivity.screenWidth+"x"+GameActivity.screenHeight);
        ready=false;
        this.cont = gameActivity;
        this.renderer = renderer;
        touch = new TouchHandler(renderer,this,gameActivity);
        localPlayerHand = new Hand();
        this.gameActivity=gameActivity;
        board=new Board();
        GameActivity.setGameEngine(this);
        isYourTurn=false;
    }

    @Override
    public void run() {
        //loading textures
        Bitmap bitmapYourTurn = BitmapFactory.decodeResource(renderer.getResources(),R.drawable.t_pb_your_turn);
        Bitmap bitmap= BitmapFactory.decodeResource(cont.getResources(), R.drawable.t_b_board_background);

        //adding the background
        renderer.addToDraw(new DrawableBitmap(bitmap, 0,0, "background", 100, 100 ));
        //drawHandPreview();
        //Rectangle pos = new Rectangle(0F,0F,100F,100F);

        //TODO : choix du deck avant de se co au serv

        final String host = "4.tcp.ngrok.io";//192.168.43.251tcp://2.tcp.ngrok.io:
        final int port = 17531;
        ObjectInputStream clientIn=null;
        ObjectOutputStream clientOut=null;
        try {
            Socket client = new Socket(host, port);
            clientIn = new ObjectInputStream(client.getInputStream());
            clientOut = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException e) {
            Log.e("ERROR SERVER DEAD","srv");
            //TODO display error and don't crash the game;
            System.exit(1);
        }
        renderer.updateFrame();

        //principe de fonctionnement : on attends une comande du server et on l'execute
        //si quelquechose se passe alors c'est le server qu'il l'a dit
        ready=true;
        while(true)
        {
            try {
                String serveurCmd = (String)clientIn.readObject();
                switch (serveurCmd)
                {
                    case COMMAND.GET_DECK:
                        //TODO get deck from player
                        clientOut.writeObject("deckDemo");
                        //TODO assing this deck
                        String deckstr = (String)clientIn.readObject();
                        localPlayerDeck = new NetWorkDeck(deckstr,gameActivity.getBaseContext());
                        Log.e("got deck",deckstr);

                        Log.e("recived","getdeck");
                        break;
                    case COMMAND.DRAW:
                        int nbcard = (Integer)clientIn.readObject();
                        localPlayerHand.pickCardFromDeck(localPlayerDeck,nbcard);
                        Log.e("picked",nbcard+"card");
                        drawHandPreview();
                        break;

                    case COMMAND.YOURTURN:
                        isYourTurn=true;
                        renderer.addToDraw(new DrawableSelfRemoving(new DrawableBitmap(bitmapYourTurn,0,0,"yourTurn",100F,50F),1));
                        break;
                    default:
                        Log.e("UNKOWN COMMAND",serveurCmd);
                }
            } catch (Exception e) {
                Log.e("ERROR DURING COMS","SERVER DIED");
                e.printStackTrace();
            }
        }
    }

    /**
     * cette methode est UNIQUEMENT a bute de test elle ne sert a rien d'autre ne pas s'en servir
     */
    private void drawHandPreview() {
        int i = 0;
        for(Card c : localPlayerHand.getHand())
        {
            renderer.addToDraw(c.getDrawableCard());
            renderer.moveToDraw(i* DrawableCard.getCardWith(),90F,c.getDrawableCard().getName());
            i++;
        }

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
        //if(isReady())touch.onTouchEvent(event);
    }

    public boolean isYourTurn() {
        return isYourTurn;
    }


    public Board getBoard() {
        return board;
    }
}
