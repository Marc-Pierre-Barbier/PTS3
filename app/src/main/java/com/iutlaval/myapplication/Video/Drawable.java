package com.iutlaval.myapplication.Video;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.iutlaval.myapplication.GameActivity;
import com.iutlaval.myapplication.InvalidDataException;

import java.util.ArrayList;
import java.util.List;

public class Drawable {

    private float x,y;
    private String name;
    private Bitmap bitmap;
    private static Paint p;

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
    }

    /**TODO : limiter la longeur des lignes et implementer le retour a la ligne
     * ce constructeur permet de rendre du texte
     * @param text le texte a rendre
     * @param x_pos position en x du texte
     * @param y_pos position en y du texte
     * @param name nom du Drawable
     * @param x_size taille du drawable entre 0 et 100
     * @param y_size taille du drawable entre 0 et 100
     */
    public Drawable(String text,float x_pos,float y_pos,String name, float x_size,float y_size,float textSize)
    {
        this(x_pos,y_pos,name);
        //this ratio is used to dial the quality of the text;

        List<String> linesOfText = cutText(20,text);

        Log.i("bitmap"," : h:" + 40*linesOfText.size() + "  w: "+text.length()*textSize / linesOfText.size());

        bitmap = Bitmap.createBitmap((int)(text.length()*textSize / linesOfText.size()), (int) 40*linesOfText.size(), Bitmap.Config.ARGB_8888);

        checkPaint();
        p.setTextSize(textSize*2);

        Canvas c = new Canvas(bitmap);

        int index = 1;
        for(String line : linesOfText)
        {
            c.drawText(line ,0,textSize*2*index,p);
            index++;
        }

        float x_scaled_size = x_size* GameActivity.screenWidth/100;
        float y_scaled_size = y_size* GameActivity.screenHeight/100;
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)x_scaled_size, (int)y_scaled_size, GameActivity.bilinearFiltering);
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
        this(null,rectangle.getPositionX(),rectangle.getPositionY(),name);
        if(rectangle.getHeight() <= 0 || rectangle.getWidth() <=0)throw new InvalidDataException(name,rectangle);

        rectangle.scaleRectangleToScreen();
        Bitmap bitmap = Bitmap.createBitmap((int)rectangle.getWidth(),(int)rectangle.getHeight(), Bitmap.Config.ARGB_8888);
        rectangle.bitmapRectangleBuilder(bitmap,color,p);

        this.bitmap=bitmap;
    }

    /**
     * ce constructeur n'est pas rapide essayer de l'utiliser le moin possible
     * TODO : optimize it and remove the deprecated flag
     *TODO update javadoc
     *
     * this contructor create a Drawable object from a rectangle and a color
     * @param rectangle recange cooresspondant a la surface a dessiner
     * @param name nom du drawable agit comme un identifiant mais doit être unique
     * @param color couleur du rectangle
     */
    @Deprecated
    public Drawable(Bitmap bitmap,Rectangle rectangle, String name, int color) throws InvalidDataException {
        this(bitmap,rectangle.getPositionX(),rectangle.getPositionY(),name);
        if(rectangle.getHeight() <= 0 || rectangle.getWidth() <=0 || bitmap == null)throw new InvalidDataException(name,rectangle);

        rectangle.scaleRectangleToScreen();
        rectangle.bitmapRectangleBuilder(bitmap,color,p);
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
     * cette fonction permet d'initialiser le pinceau
     */
    private void checkPaint() {
        if(p==null)
            p = new Paint();
    }

    /**
     * cette fonction est appeler quand le drawable est retirer de la liste toDraw
     */
    public void onDeletion(Renderer r){}

    private List<String> cutText(int charPerLines,String text)
    {
        List<String> output = new ArrayList<>();
        //TODO cut text in lines
        String nextLine = "";
        for (String word : text.split(" ")) {
            if(word.length() < charPerLines)
            {
                if(word.length() + nextLine.length() +1 < charPerLines)
                {
                    nextLine += " " + word;
                }else{
                    output.add(nextLine);
                    nextLine="";
                }
            }else{
                Log.e("TextRender","ERROR WORD TOO LONG :" + word);
            }
        }
        if(!nextLine.equals(""))output.add(nextLine);
        return output;
    }
}
