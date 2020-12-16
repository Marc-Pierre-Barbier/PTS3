package com.iutlaval.myapplication;

//N'oubliez pas de déclarer le bon package dans lequel se trouve le fichier !

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.iutlaval.myapplication.Game.GameLogicThread;
import com.iutlaval.myapplication.Video.FpsTime;
import com.iutlaval.myapplication.Video.Renderer;

public class GameActivity extends Activity {

    private static GameLogicThread gameEngine;

    public static int screenWidth=0;
    public static int screenHeight=0;
    public static boolean bilinearFiltering = true;

    private Renderer renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Display display = getWindowManager().getDefaultDisplay();
        //permet d'attendre la duré de une image affiché a l'écran
        FpsTime.init(display);

        String deck = getIntent().getStringExtra("DECK");

        renderer = new Renderer(getBaseContext(),deck,this);



        setContentView(renderer);
    }

    public static void setGameEngine(GameLogicThread gameEngine) {
        GameActivity.gameEngine = gameEngine;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //kill les threads
        System.exit(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (renderer != null) renderer.terminate();
        gameEngine = null;
        renderer = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(gameEngine != null)gameEngine.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}

