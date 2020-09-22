package com.iutlaval.myapplication.Game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;

import com.iutlaval.myapplication.Game.Cards.Card;
import com.iutlaval.myapplication.Game.Cards.DemoCard;
import com.iutlaval.myapplication.Game.Decks.Deck;
import com.iutlaval.myapplication.Game.Decks.DeckDemo;
import com.iutlaval.myapplication.GameActivity;
import com.iutlaval.myapplication.R;
import com.iutlaval.myapplication.Video.Drawables.Drawable;
import com.iutlaval.myapplication.Video.Drawables.DrawableCard;
import com.iutlaval.myapplication.Video.Renderer;

public class GameLogicThread extends Thread{

    private Context cont;
    private Renderer renderer;
    private boolean ready;
    private TouchHandler touch;
    private GameActivity gameActivity;

    private Deck localPlayerDeck;
    private Hand localPlayerHand;


    public GameLogicThread(GameActivity gameActivity, Renderer renderer)
    {
        ready=false;
        this.cont = gameActivity;
        this.renderer = renderer;
        renderer.setEngine(this);
        touch = new TouchHandler(renderer);
        localPlayerDeck = new DeckDemo("local",cont);
        localPlayerDeck.suffle();
        localPlayerHand = new Hand();
        localPlayerHand.fillHand(localPlayerDeck);
        this.gameActivity=gameActivity;
    }

    private float i=0;
    private float increm = 1F;


    @Override
    public void run() {
        //affichage de l'arriere plan
        Bitmap bitmap= BitmapFactory.decodeResource(cont.getResources(), R.drawable.t_b_board_background);
        renderer.addToDraw(new Drawable(bitmap, 0,0, "background", 100, 100 ));

        //renderer.addToDraw(new Drawable(bm,0.0F,0.0F,"running",8F,16F));
        //renderer.addToDraw(new DrawableCard(new DemoCard(),0.0F,0.0F,"card2",cont));
       /*Card card2 = new DemoCard("card3",cont);
        renderer.addToDraw(card2.getDrawableCard());
        Card card1 = new DemoCard("card1",cont);
        renderer.addToDraw(card1.getDrawableCard());*/

        drawHandPreview();

        ready=true;
        while(true)
        {
            //Do Stuff
            break;
        }
        renderer.updateFrame();
    }

    private void drawHandPreview() {
        int i = 0;
        for(Card c : localPlayerHand.getHand())
        {
            renderer.addToDraw(c.getDrawableCard());
            renderer.moveToDraw(i*DrawableCard.CARD_WITH,90F,c.getDrawableCard().getName());
            i++;
        }

    }

    /**
     * cette fonction est appeller a chaque fin de frame
     * CETTE FONCTION A UN IMPACT DIRRECT SUR LES FPS ELLE SE DOIT D'être OPTIMAL
     * TOUT CALCUL REDONDANT CE DOIT D'AVOIR ETE PRE FAIT
     *
     * TOUT APELLE DE CETTE FONCTION SE DOIT D'ÊTRE PROTEGER PAR isReady()
     *
     * /!\ renderer.updateFrame(); va forcer le system a redessiner l'afficher qui ensuite reappelera onFrameDoneRendering() ce qui peut causer si le updateFrame() n'est pas proteger
     * une utilisation de la batterie massive
     */
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
     * relais l'evenement directement au touch Handler
     * @param event
     */
    public void onTouchEvent(MotionEvent event) {
        if(isReady()){
            float unscalled_Y = event.getY() / GameActivity.screenHeight * 100;
            if(unscalled_Y >= 90F) {
                float unscalled_X = event.getX() / GameActivity.screenWidth * 100;
                //TODO : display fullscreen cards
                Intent HandActivity = new Intent(gameActivity, HandActivity.class);
                String[] serializedHand = new String[localPlayerHand.getHand().size() +1];
                int i = 0;
                serializedHand[i] = localPlayerDeck.getDeckName();
                for(Card c : localPlayerHand.getHand())
                {
                    i++;
                    serializedHand[i] = c.getName();

                }
                HandActivity.putExtra("hand",serializedHand);
                Bundle cardSelectedBundle = new Bundle();
                gameActivity.startActivity(HandActivity,cardSelectedBundle);


            }else{
                touch.onTouchEvent(event);
            }
        }

    }
}
