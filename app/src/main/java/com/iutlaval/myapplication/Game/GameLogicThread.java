package com.iutlaval.myapplication.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

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

    Context cont;
    Renderer renderer;

    public GameLogicThread(Context cont, Renderer renderer)
    {
        this.cont = cont;
        this.renderer = renderer;
    }

    @Override
    public void run() {

        Bitmap bm = BitmapFactory.decodeResource(cont.getResources(), R.drawable.debug_card);

        try {
            renderer.addToDraw(new Drawable(new Rectangle(0,0,100,100),"background", Color.BLUE));
            //renderer.addToDraw(new Drawable(bm,0.0F,0.0F,"running",8F,16F));
            //renderer.addToDraw(new DrawableCard(new DemoCard(),0.0F,0.0F,"card2",cont));
            Card card2 = new DemoCard("card3",cont);
            if(!renderer.addToDraw(card2.getDrawableCard())) Log.e("F","WTF");
            Card card1 = new DemoCard("card1",cont);
            if(!renderer.addToDraw(card1.getDrawableCard())) Log.e("F","WTF");
        } catch (InvalidDataException e) {
            //cette erreur est lance si un carre est invalid
            System.err.println(e.getDetail());
            e.printStackTrace();
        }


        int i=0;
        int increm = 1;
        while(true)
        {
             if(i < 100)
            {
                i+=increm;
            }else{
                increm=-increm;
                i+=increm;
             }
            if(i==0)increm=-increm;

            //TODO get x,y coordinate form name
            //r.moveToDraw(i,i,"card2");
            renderer.moveToDraw(1,1,"card3");
            renderer.moveToDraw(20,1,"card1");
            FpsTime.waitFrameTime();
        }
    }

    //placeOnBoard()
}
