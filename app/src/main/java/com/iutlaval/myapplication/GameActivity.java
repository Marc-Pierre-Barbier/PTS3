package com.iutlaval.myapplication;

//N'oubliez pas de déclarer le bon package dans lequel se trouve le fichier !

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

import com.iutlaval.myapplication.Game.Cards.DemoCard;
import com.iutlaval.myapplication.Video.Drawable;
import com.iutlaval.myapplication.Video.DrawableCard;
import com.iutlaval.myapplication.Video.FpsTime;
import com.iutlaval.myapplication.Video.Rectangle;
import com.iutlaval.myapplication.Video.Renderer;

public class GameActivity extends Activity {

    public static int screenWidth=0;
    public static int screenHeight=0;
    public static boolean bilinearFiltering = true;

    public static boolean isMultiplayer() {
        return false;//TODO implement it
    }

    public static boolean isHosting() {
        return false;//TODO implement it
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Display display = getWindowManager().getDefaultDisplay();
        FpsTime.init(display);

        Renderer v = new Renderer(getBaseContext());

        //return portrait mode resolution so we need to flip them
        Point size = new Point();
        display.getSize(size);
        if(size.y > size.x)
        {
            screenWidth = size.y;
            screenHeight = size.x;
        }else{
            screenWidth = size.x;
            screenHeight = size.y;
        }


        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.debug_card);

        try {
            v.addToDraw(new Drawable(new Rectangle(0,0,100,100),"background",Color.BLUE));
            v.addToDraw(new Drawable(bm,0.0F,0.0F,"running",8F,16F));
            v.addToDraw(new DrawableCard(new DemoCard(),0.0F,0.0F,"card2",this));
        } catch (InvalidDataException e) {
            //cette erreur est lance si un carre est invalid
            System.err.println(e.getDetail());
            e.printStackTrace();
        }

        Thread t = new MainThread(v);
        t.start();

        setContentView(v);
    }

    //TODO a supprime quand le moteur de jeu pour être utliser a des fin de teste
    private class MainThread extends Thread
    {
        Renderer view;
        public MainThread(Renderer view)
        {
            super();
            this.view=view;
        }

        @Override
        public void run() {
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
                view.moveToDraw(i,i,"card2");
                //view.moveToDraw(1,1,"card2");
                FpsTime.waitFrameTime();
            }
        }
    }

}

