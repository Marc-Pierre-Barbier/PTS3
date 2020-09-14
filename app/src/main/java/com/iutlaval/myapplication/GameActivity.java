package com.iutlaval.myapplication;

//N'oubliez pas de déclarer le bon package dans lequel se trouve le fichier !

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;

import com.iutlaval.myapplication.Game.GameLogicThread;
import com.iutlaval.myapplication.Video.FpsTime;
import com.iutlaval.myapplication.Video.Renderer;

public class GameActivity extends Activity {

    private static GameLogicThread gameEngine;

    public static int screenWidth=0;
    public static int screenHeight=0;
    public static boolean bilinearFiltering = true;

    public static boolean isMultiplayer() {
        return false;//TODO implement it
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Display display = getWindowManager().getDefaultDisplay();
        FpsTime.init(display);

        Renderer renderer = new Renderer(getBaseContext());

        //nous donne la resolution
        Point size = new Point();
        display.getSize(size);

        //vu que l'on verouille en mode portrait en veut juste la resolution en 16:9 et non en 9:16
        //donc on prend celui le plus large
        //en cas de la de la machine les  peuvent s'inverser d'ou l'interet du test
        if(size.y > size.x)
        {
            screenWidth = size.y;
            screenHeight = size.x;
        }else{
            screenWidth = size.x;
            screenHeight = size.y;
        }

        gameEngine = new GameLogicThread(this,renderer);
        gameEngine.start();

        setContentView(renderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(gameEngine != null)gameEngine.onTouchEvent(event);
        System.out.println(event.getX());
        return super.onTouchEvent(event);

    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        System.out.println(event.getX());
        return super.onGenericMotionEvent(event);
    }
}

