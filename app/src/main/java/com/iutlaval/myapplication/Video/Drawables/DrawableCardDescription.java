package com.iutlaval.myapplication.Video.Drawables;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import androidx.annotation.MainThread;

import com.iutlaval.myapplication.GameActivity;

import java.util.List;

public class DrawableCardDescription extends Drawable {
    private static int TEXT_X_RES = 281;
    private static int TEXT_Y_RES = 195;

    private Bitmap bitmap;

    public DrawableCardDescription(String text, float x_pos, float y_pos, String name, float x_size, float y_size, float textSize) {
        super(x_pos,y_pos,name);
        int x_scaled_size = (int)x_size* GameActivity.screenWidth/100;
        int y_scaled_size = (int)y_size* GameActivity.screenHeight/100;

        checkPaint();
        super.p.setColor(Color.BLACK);



        List<String> linesOfText = cutText(20,text);
        p.setTextSize(textSize);

        //+10 permet d'e prendre en compte les p,q,j qui dessende plus bas que les autres
        Bitmap bitmap = Bitmap.createBitmap(TEXT_X_RES, TEXT_Y_RES, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);

        int index = 1;
        for(String line : linesOfText)
        {
            c.drawText(line ,0,index*y_scaled_size/linesOfText.size(),p);
            index++;
        }

        this.bitmap = Bitmap.createScaledBitmap(bitmap,x_scaled_size,y_scaled_size, GameActivity.bilinearFiltering);
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }
}
