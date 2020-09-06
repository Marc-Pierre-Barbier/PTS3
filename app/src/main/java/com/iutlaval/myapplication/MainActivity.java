package com.iutlaval.myapplication;

//N'oubliez pas de déclarer le bon package dans lequel se trouve le fichier !

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends Activity {

    public static int screenWidth=0;
    public static int screenHeight=0;
    public static boolean bilinearFiltering = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Display display = getWindowManager().getDefaultDisplay();


        CanevasView v = new CanevasView(getBaseContext());

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
            v.addToDraw(new Drawable(new Rect(0,0,screenWidth,screenHeight),"background",Color.BLUE));
            v.addToDraw(new Drawable(bm,0.0F,00.0F,"Card1",50F,50F));
        } catch (InvalidDataException e) {
            //cette erreur est lance si un carre est invalid
            System.err.println(e.getDetail());
            e.printStackTrace();
        }

        setContentView(v);
    }
}