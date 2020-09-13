package com.iutlaval.myapplication.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.iutlaval.myapplication.Game.Cards.Card;
import com.iutlaval.myapplication.Game.Cards.DemoCard;
import com.iutlaval.myapplication.R;
import com.iutlaval.myapplication.Video.Drawables.Drawable;
import com.iutlaval.myapplication.Video.Drawables.DrawableCard;
import com.iutlaval.myapplication.Video.Renderer;

public class GameLogicThread extends Thread{

    private Context cont;
    private Renderer renderer;
    private boolean ready;

    public GameLogicThread(Context cont, Renderer renderer)
    {
        ready=false;
        this.cont = cont;
        this.renderer = renderer;
        renderer.setEngine(this);
    }

    private float i=0;
    private float increm = 1F;


    @Override
    public void run() {

        //renderer.addToDraw(new Drawable(new Rectangle(0,0,100,100),"background", Color.BLUE));
        Bitmap bitmap= BitmapFactory.decodeResource(cont.getResources(), R.drawable.t_b_board_background);
        renderer.addToDraw(new Drawable(bitmap, 0,0, "background", 100, 100 ));
        //renderer.addToDraw(new Drawable(bm,0.0F,0.0F,"running",8F,16F));
        //renderer.addToDraw(new DrawableCard(new DemoCard(),0.0F,0.0F,"card2",cont));
        Card card2 = new DemoCard("card3",cont);
        renderer.addToDraw(card2.getDrawableCard());
        Card card1 = new DemoCard("card1",cont);
        renderer.addToDraw(card1.getDrawableCard());



        ready=true;
        while(true)
        {
            //Do Stuff
            renderer.moveToDraw(20,20,"card1");
            renderer.moveToDraw(40,20,"card3");
            break;
        }
    }

    /**
     * cette fonction est appeller a chaque fin de frame
     * CETTE FONCTION A UN IMPACT DIRRECT SUR LES FPS ELLE SE DOIT D'être OPTIMAL
     * TOUT CALCUL REDONDANT CE DOIT D'AVOIR ETE PRE FAIT
     *
     * TOUT APELLE DE CETTE FONCTION SE DOIT D'ÊTRE PROTEGER PAR isReady()
     */
    public void onFrameDoneRendering()
    {
        if(i < 100)
        {
            i+=increm;
            //((DrawableCard)renderer.getDrawAble("card3")).setOnBoard(increm <= 0);
        }else{
            increm=-increm;
            i+=increm;
            //((DrawableCard)renderer.getDrawAble("card3")).setOnBoard(increm > 0);
        }
        if(i==0)increm=-increm;

        ((DrawableCard)renderer.getDrawAble("card3")).setOnBoard(false);
        //TODO get x,y coordinate form name
        //r.moveToDraw(i,i,"card2");
        //renderer.moveToDraw(i,i,"card3");

    }

    /**
     * retourne vrai sir le moteur est pret a avoir sont evenemnt onFrameDoneRendering d'appeler
     * @return
     */
    public boolean isReady() {
        return ready;
    }

    //placeOnBoard()
}
