package com.iutlaval.myapplication.Video;

import android.graphics.Bitmap;
import android.graphics.Paint;

import com.iutlaval.myapplication.GameActivity;

public class Rectangle {

    float positionX,positionY,width,height;

    /**
     * initialiser un rectangle avec
     * @param positionX une postion en x
     * @param positionY une position en y
     * @param width une largeur
     * @param height une hauteur
     */
    public Rectangle(float positionX,float positionY,float width,float height)
    {
        set(positionX,positionY,width,height);
    }

    /**
     * permet de redefinir toutes le information du rectange
     *
     * /!\ si vous modifier un rectange du moteur de rendu il faut prendre en compte la resolution/!\
     * @param positionX une postion en x
     * @param positionY une position en y
     * @param width une largeur
     * @param height une hauteur
     */
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

    /**
     * met automatiquement le rectangle a la taille de l'ecran
     * /!\ ne pas le lancer plus d'une fois sinon les mesures devienderons n'importequoi /!\
     * /!\ toutes les dimention du rectangle doivent être comprise entre 0 et 100 sinon elle sortiron de l'ecran/!\
     */
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
