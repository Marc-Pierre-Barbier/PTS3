package com.iutlaval.myapplication.Video.Drawables;

import android.graphics.Bitmap;
import android.util.Log;

import com.iutlaval.myapplication.InvalidDataException;
import com.iutlaval.myapplication.Video.Rectangle;

public class DrawableRectangle extends Drawable{
    /**
     * ce constructeur n'est pas rapide essayer de l'utiliser le moin possible
     * TODO : optimize it
     *
     * ce constructeur dessine un rectangle avec la couleur donné en argument
     * @param rectangle recange cooresspondant a la surface a dessiner
     * @param name nom du drawable agit comme un identifiant mais doit être unique
     * @param color couleur du rectangle
     */
    public DrawableRectangle(Rectangle rectangle, String name, int color) throws InvalidDataException {
        super(rectangle.getPositionX(),rectangle.getPositionY(),name);
        if(rectangle.getHeight() <= 0 || rectangle.getWidth() <=0)throw new InvalidDataException(name,rectangle);

        Log.e("rect1",rectangle.getWidth()+"");
        rectangle.scaleRectangleToScreen();
        Log.e("rect2",rectangle.getWidth()+"");
        Bitmap bitmap = Bitmap.createBitmap((int)rectangle.getWidth(),(int)rectangle.getHeight(), Bitmap.Config.ARGB_8888);
        rectangle.bitmapRectangleBuilder(bitmap,color);

        setBitmap(bitmap);
    }


    /**
     * ce constructeur n'est pas tres rapide donc a eviter il utilse pas mal de ram mais sava encore le gc de java s'en ocupe vite
     * ce constructeur dessine un rectangle avec la couleur donné en argument
     * @param bitmap bitmap sur le quel dessiner
     * @param rectangle recange cooresspondant a la surface a dessiner
     * @param name nom du drawable agit comme un identifiant mais doit être unique
     * @param color couleur du rectangle
     * @throws InvalidDataException
     */
    //TODO verifier l'utiliter de ce constructeur
    @Deprecated
    public DrawableRectangle(Bitmap bitmap,Rectangle rectangle, String name, int color) throws InvalidDataException {
        super(rectangle.getPositionX(),rectangle.getPositionY(),name);
        if(rectangle.isDrawable() || bitmap == null)throw new InvalidDataException(name,rectangle);

        rectangle.scaleRectangleToScreen();
        rectangle.bitmapRectangleBuilder(bitmap,color);
        setBitmap(bitmap);
    }
}
