package com.iutlaval.myapplication.Video;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.annotation.NonNull;

public class RectangleCanevas extends Canvas {
    public RectangleCanevas(Bitmap bitmap)
    {
        super(bitmap);
    }

    public void drawRect(@NonNull Rectangle r, @NonNull Paint paint) {
        super.drawRect(r.getPositionX(),
                r.getPositionY(),
                r.getPositionX()+r.getWidth(),
                r.getPositionY()+r.getHeight(),
                paint);
    }
}
