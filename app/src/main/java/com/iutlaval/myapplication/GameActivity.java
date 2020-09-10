package com.iutlaval.myapplication;

//N'oubliez pas de déclarer le bon package dans lequel se trouve le fichier !

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

import com.iutlaval.myapplication.Game.Cards.DemoCard;
import com.iutlaval.myapplication.Game.GameLogicThread;
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

    Renderer renderer;
    GameLogicThread t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Display display = getWindowManager().getDefaultDisplay();
        FpsTime.init(display);

        Renderer renderer = new Renderer(getBaseContext());

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

        t = new GameLogicThread(this,renderer);
        t.start();

        setContentView(renderer);
    }
}

