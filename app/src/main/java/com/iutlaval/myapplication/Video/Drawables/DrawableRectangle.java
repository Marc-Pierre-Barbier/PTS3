package com.iutlaval.myapplication.Video.Drawables;

import android.graphics.Bitmap;
import android.util.Log;

import com.iutlaval.myapplication.InvalidDataException;
import com.iutlaval.myapplication.Video.Rectangle;

public class DrawableRectangle extends Drawable{
    /**
     * ce constructeur n'est pas rapide essayer de l'utiliser le moin possible
     *
     * ce constructeur dessine un rectangle avec la couleur donné en argument
     * @param rectangle recange cooresspondant a la surface a dessiner
     * @param name nom du drawable agit comme un identifiant mais doit être unique
     * @param color couleur du rectangle
     */
    public DrawableRectangle(Rectangle rectangle, String name, int color) throws InvalidDataException {
        super(rectangle.getPositionX(),rectangle.getPositionY(),name);
        if(rectangle.getHeight() <= 0 || rectangle.getWidth() <=0)throw new InvalidDataException(name,rectangle);

        rectangle.scaleRectangleToScreen();
        Bitmap bitmap = Bitmap.createBitmap((int)rectangle.getWidth(),(int)rectangle.getHeight(), Bitmap.Config.ARGB_8888);
        rectangle.bitmapRectangleBuilderNoCoordinates(bitmap,color);

        setBitmap(bitmap);
    }
}
