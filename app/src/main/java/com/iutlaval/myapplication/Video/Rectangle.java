package com.iutlaval.myapplication.Video;

import android.graphics.Bitmap;
import android.graphics.Paint;

import com.iutlaval.myapplication.GameActivity;

public class Rectangle {

    float positionX,positionY,width,height;
    public Rectangle(float positionX,float positionY,float width,float height)
    {
        set(positionX,positionY,width,height);
    }

    public void set(float positionX,float positionY,float width,float height)
    {
        this.positionX=positionX;
        this.positionY=positionY;
        this.width=width;
        this.height=height;
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    protected void scaleRectangleToScreen()
    {
        set(getPositionX()* GameActivity.screenWidth/100,
                getPositionY()*GameActivity.screenHeight/100,
                getWidth()*GameActivity.screenWidth/100,
                getHeight()*GameActivity.screenHeight/100
        );
    }

    /**
     * retourne la bitmap entre mais avec un rectangle dessiner dedans
     * @param bitmap la bitmap sur la quelle dessiner
     * @param color la couleur du carre
     * @param p le pinceau utiliser pour le canevas
     */
    public void bitmapRectangleBuilder(Bitmap bitmap, int color,Paint p)
    {
        RectangleCanevas c = new RectangleCanevas(bitmap);
        if(p==null)p = new Paint();

        p.setColor(color);
        p.setStyle(Paint.Style.FILL_AND_STROKE);

        c.drawRect(this,p);
    }
}
