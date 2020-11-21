package com.iutlaval.myapplication.Video.Drawables;

import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import java.time.Instant;

public class DrawableSelfRemoving extends Drawable{

    private Drawable drawable;
    private long lifetime;

    public DrawableSelfRemoving(Drawable drawable,int lifetime) {
        super(drawable.getX(), drawable.getY(), drawable.getName());
        this.drawable = drawable;
        this.lifetime = System.currentTimeMillis()+lifetime*1000;
    }

    @Override
    public void drawOn(@NonNull Canvas c, @NonNull Paint p) {
        if(lifetime > System.currentTimeMillis())
        {
            drawable.drawOn(c,p);
        }
    }

    public boolean isDone()
    {
        return lifetime < System.currentTimeMillis();
    }
}
