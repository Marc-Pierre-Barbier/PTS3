package com.iutlaval.myapplication.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.iutlaval.myapplication.Game.Cards.Card;
import com.iutlaval.myapplication.Game.Cards.DemoCard;
import com.iutlaval.myapplication.InvalidDataException;
import com.iutlaval.myapplication.R;
import com.iutlaval.myapplication.Video.Drawable;
import com.iutlaval.myapplication.Video.DrawableCard;
import com.iutlaval.myapplication.Video.FpsTime;
import com.iutlaval.myapplication.Video.Rectangle;
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

        try {
            renderer.addToDraw(new Drawable(new Rectangle(0,0,100,100),"background", Color.BLUE));
            //renderer.addToDraw(new Drawable(bm,0.0F,0.0F,"running",8F,16F));
            //renderer.addToDraw(new DrawableCard(new DemoCard(),0.0F,0.0F,"card2",cont));
            Card card2 = new DemoCard("card3",cont);
            renderer.addToDraw(card2.getDrawableCard());
            Card card1 = new DemoCard("card1",cont);
            renderer.addToDraw(card1.getDrawableCard());
        } catch (InvalidDataException e) {
            //cette erreur est lance si un carre est invalid
            System.err.println(e.getDetail());
            e.printStackTrace();
        }


        ready=true;
        while(true)
        {
            //Do Stuff
            renderer.moveToDraw(20,1,"card1");
            break;
        }
    }

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

    public boolean isReady() {
        return ready;
    }

    //placeOnBoard()
}
