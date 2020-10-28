package com.iutlaval.myapplication.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import com.iutlaval.myapplication.Game.Cards.Card;
import com.iutlaval.myapplication.Game.Decks.Deck;
import com.iutlaval.myapplication.Game.Decks.DeckDemo;
import com.iutlaval.myapplication.GameActivity;
import com.iutlaval.myapplication.InvalidDataException;
import com.iutlaval.myapplication.R;
import com.iutlaval.myapplication.Video.Drawables.Drawable;
import com.iutlaval.myapplication.Video.Drawables.DrawableBitmap;
import com.iutlaval.myapplication.Video.Drawables.DrawableCard;
import com.iutlaval.myapplication.Video.Drawables.DrawableRectangle;
import com.iutlaval.myapplication.Video.Rectangle;
import com.iutlaval.myapplication.Video.Renderer;

import java.util.List;

public class GameLogicThread extends Thread{

    private Context cont;
    private Renderer renderer;
    private boolean ready;
    private TouchHandler touch;
    private GameActivity gameActivity;

    private Deck localPlayerDeck;
    private Hand localPlayerHand;
    private Board board;
    private PlayableZonesHandler playableZonesHandler;

    public GameLogicThread(GameActivity gameActivity, Renderer renderer)
    {
        Log.e("RESOLUTION:",""+GameActivity.screenWidth+"x"+GameActivity.screenHeight);
        ready=false;
        this.cont = gameActivity;
        this.renderer = renderer;
        touch = new TouchHandler(renderer);
        localPlayerDeck = new DeckDemo("local",cont);
        localPlayerDeck.suffle();
        localPlayerHand = new Hand();
        localPlayerHand.fillHand(localPlayerDeck);
        this.gameActivity=gameActivity;
        board=new Board();
        playableZonesHandler = new PlayableZonesHandler(board);
        GameActivity.setGameEngine(this);
    }

    private float i=0;
    private float increm = 1F;


    @Override
    public void run() {
        //affichage de l'arriere plan
        Bitmap bitmap= BitmapFactory.decodeResource(cont.getResources(), R.drawable.t_b_board_background);
        renderer.addToDraw(new DrawableBitmap(bitmap, 0,0, "background", 100, 100 ));
        drawHandPreview();
        //Rectangle pos = new Rectangle(0F,0F,100F,100F);

        ready=true;
        while(true)
        {
            //Do Stuff
            break;
        }
        renderer.updateFrame();
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




    private DrawableCard currentlySelected = null;
    private static final float SELECTING_OFFSET = 20F;
    /**
     * relais l'evenement directement au touch Handler
     * @param event
     */
    public void onTouchEvent(MotionEvent event) {
        if(isReady()){
            //calcul y sur une echelle de 0 a 100
            float scalled_Y = event.getY() / GameActivity.screenHeight * 100;
            //si on clique sur une carte dans la main
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                float unscalled_X = event.getX() / GameActivity.screenWidth * 100;
                DrawableCard smallCArd = renderer.getCardOn(unscalled_X,scalled_Y);
                if(smallCArd !=null)
                {
                    renderer.removeToDrawWithoutUpdate(smallCArd);
                    playableZonesHandler.displayPlayableZones(renderer);
                    renderer.addToDraw(smallCArd);
                    if(currentlySelected !=null)
                    {
                        currentlySelected.setCoordinates(currentlySelected.getX(),currentlySelected.getY() + SELECTING_OFFSET);
                        //on update pas affin d'avoir un rendu plus fluide au moment de mettre l'aure carte
                        renderer.removeToDrawWithoutUpdate(currentlySelected.getName()+"BIG");
                    }
                    currentlySelected = smallCArd;
                    DrawableCard bigCard = new DrawableCard(smallCArd,cont,2);
                    renderer.addToDraw(bigCard);
                    bigCard.setCoordinates(72F,10F);
                    smallCArd.setCoordinates(smallCArd.getX(),smallCArd.getY() - SELECTING_OFFSET);

                }
                touch.onTouchEvent(event);
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                //currentlySelected.setCoordinates(currentlySelected.getX(),currentlySelected.getY() + SELECTING_OFFSET);
                touch.onTouchEvent(event);
                if(currentlySelected !=null)
                {
                    renderer.removeToDraw(currentlySelected.getName()+"BIG");
                    currentlySelected.setCoordinates(currentlySelected.getX(),currentlySelected.getY() + SELECTING_OFFSET);
                    currentlySelected=null;
                }
                playableZonesHandler.HidePlayableZones(renderer);
            }else{
                touch.onTouchEvent(event);
            }
        }

    }


}
