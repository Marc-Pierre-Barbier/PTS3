package com.iutlaval.myapplication.Video;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.iutlaval.myapplication.GameActivity;
import com.iutlaval.myapplication.InvalidDataException;

public class Drawable {

    private float x,y;
    private String name;
    private Bitmap bitmap;

    /**
     * cree un drawable a partir de coordonné est d'un nom
     * @param x coordoné x compris entre 0 et 100
     * @param y coordoné y comrpis entre 0 et 100
     * @param name nom du drawable agit comme un identifiant mais doit être unique
     */
    protected Drawable(float x,float y,String name)
    {
        this.x=x;
        this.y=y;
        this.name=name;
    }

    /**
     * cree un drawable depuis une bitmap est des coordonnés
     * @param bitmap bitmap
     * @param x coordoné x compris entre 0 et 100
     * @param y coordoné y comrpis entre 0 et 100
     * @param name nom du drawable agit comme un identifiant mais doit être unique
     *
     * deprecier car non adapter a la resolution de l'ecran
     */
    @Deprecated
    public Drawable(Bitmap bitmap,float x,float y,String name){
        this(x,y,name);
        this.bitmap=bitmap;
    }

    /**
     * cree un drawable depuis une bitmap est des coordonnés
     * @param bitmap bitmap
     * @param x_pos coordoné x compris entre 0 et 100
     * @param y_pos coordoné y comrpis entre 0 et 100
     * @param name nom du drawable agit comme un identifiant mais doit être unique
     * deprecier car non adapter a la resolution de l'ecran
     *
     *  TODO : echelle dynamique avec l'ecran
     */
    public Drawable(@NonNull Bitmap bitmap, float x_pos, float y_pos, String name, float x_size, float y_size) throws InvalidDataException {
        this(x_pos,y_pos,name);

        float x_scaled_size = x_size* GameActivity.screenWidth/100;
        float y_scaled_size = y_size* GameActivity.screenHeight/100;

        System.out.println(x_scaled_size + " y: " + y_scaled_size);
        System.out.println(y_size);

        if(x_scaled_size <=0 || y_scaled_size <=0)
        {
            throw new InvalidDataException(name,x_scaled_size,y_scaled_size);
        }

        this.bitmap = Bitmap.createScaledBitmap(bitmap, (int)x_scaled_size, (int)y_scaled_size, GameActivity.bilinearFiltering);
        Log.i("loul",""+this.bitmap.getWidth());
    }

    /**
     * ce constructeur n'est pas rapide essayer de l'utiliser le moin possible
     * TODO : optimize it and remove the deprecated flag
     *
     * this contructor create a Drawable object from a rectangle and a color
     * @param rectangle recange cooresspondant a la surface a dessiner
     * @param name nom du drawable agit comme un identifiant mais doit être unique
     * @param color couleur du rectangle
     */
    @Deprecated
    public Drawable(Rectangle rectangle, String name, int color) throws InvalidDataException {
        if(rectangle.getHeight() <= 0 || rectangle.getWidth() <=0)throw new InvalidDataException(name,rectangle);

        rectangle.scaleRectangleToScreen();

        Bitmap bitmap = Bitmap.createBitmap((int)rectangle.getWidth(),(int)rectangle.getHeight(), Bitmap.Config.ARGB_8888);

        //on crée un canevas pour interagir avec la bitmap
        RectangleCanevas c = new RectangleCanevas(bitmap);
        Paint p = new Paint();

        p.setColor(color);
        p.setStyle(Paint.Style.FILL_AND_STROKE);

        c.drawRect(rectangle,p);

        this.bitmap=bitmap;
        this.name=name;
        this.x=rectangle.getPositionX();
        this.y=rectangle.getPositionY();
    }

    public void drawOn(Canvas c, Paint p)
    {
        //drawings
        c.drawBitmap(getBitmap(),x* GameActivity.screenWidth/100,y* GameActivity.screenHeight/100,p);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Drawable)
        {
            return ((Drawable)o).getName().equals(name);
        }
        return false;
    }

    public void setCoordinates(float x, float y) {
        this.x=x;
        this.y=y;
    }

    /**
     * cette fonction est appeler quand le drawable est retirer de la liste toDraw
     */
    public void onDeletion(Renderer r){}
}
