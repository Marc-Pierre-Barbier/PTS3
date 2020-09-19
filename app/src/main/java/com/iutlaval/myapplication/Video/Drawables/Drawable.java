package com.iutlaval.myapplication.Video.Drawables;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.iutlaval.myapplication.ERROR_CODE;
import com.iutlaval.myapplication.GameActivity;
import com.iutlaval.myapplication.InvalidDataException;
import com.iutlaval.myapplication.Video.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Drawable {

    private float x,y;
    private String name;
    private Bitmap bitmap;
    protected static Paint p;

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
     * @param  bitmap a dessiner
     * @param x_pos position en x du texte
     * @param y_pos position en y du texte
     * @param name nom du Drawable
     * @param x_size taille du drawable entre 0 et 100
     * @param y_size taille du drawable entre 0 et 100
     * @throws InvalidDataException
     */
    public Drawable(@NonNull Bitmap bitmap, float x_pos, float y_pos, String name, float x_size, float y_size){
        this(x_pos,y_pos,name);

        float x_scaled_size = x_size* GameActivity.screenWidth/100;
        float y_scaled_size = y_size* GameActivity.screenHeight/100;

        if(x_scaled_size <=0 || y_scaled_size <=0)
        {
            Log.e("TEXTURE :","ERREUR DE MISE A L'ECHELLE");
            System.exit(ERROR_CODE.ERROR_TEXTURE_DIMENSIONS_INVALID.ordinal());
        }

        this.bitmap = Bitmap.createScaledBitmap(bitmap, (int)x_scaled_size, (int)y_scaled_size, GameActivity.bilinearFiltering);
    }

    /**TODO : corriger les deformation due au changement de ratio (x_size,y_size)
     * ce constructeur permet de rendre du texte
     * @param text le texte a rendre
     * @param x_pos position en x du texte
     * @param y_pos position en y du texte
     * @param name nom du Drawable
     * @param x_size taille du drawable entre 0 et 100
     * @param y_size taille du drawable entre 0 et 100
     */
    public Drawable(String text,float x_pos,float y_pos,String name, float x_size,float y_size,float textSize , int x_canvasRatio, int y_canvasRatio)
    {
        this(x_pos,y_pos,name);

        checkPaint();
        p.setColor(Color.BLACK);
        p.setAntiAlias(true);



        List<String> linesOfText = cutText(20,text);
        p.setTextSize(textSize);

        //+10 permet d'e prendre en compte les p,q,j qui dessende plus bas que les autres
        bitmap = Bitmap.createBitmap(x_canvasRatio, y_canvasRatio, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);

        int index = 1;
        for(String line : linesOfText)
        {
            c.drawText(line ,0,index*y_canvasRatio/linesOfText.size(),p);
            index++;
        }

        int scalled_x_size = (int)(GameActivity.screenWidth*x_size/100);
        int scalled_y_size = (int)(GameActivity.screenHeight*y_size/100);

        bitmap = Bitmap.createScaledBitmap(bitmap,scalled_x_size,scalled_y_size,GameActivity.bilinearFiltering);
    }


    /**
     * ce constructeur n'est pas rapide essayer de l'utiliser le moin possible
     * TODO : optimize it
     *
     * ce constructeur dessine un rectangle avec la texture donner en argument
     * @param rectangle recange cooresspondant a la surface a dessiner
     * @param name nom du drawable agit comme un identifiant mais doit être unique
     * @param color couleur du rectangle
     */
    public Drawable(Rectangle rectangle, String name, int color) throws InvalidDataException {
        this(rectangle.getPositionX(),rectangle.getPositionY(),name);
        if(rectangle.getHeight() <= 0 || rectangle.getWidth() <=0)throw new InvalidDataException(name,rectangle);

        rectangle.scaleRectangleToScreen();
        Bitmap bitmap = Bitmap.createBitmap((int)rectangle.getWidth(),(int)rectangle.getHeight(), Bitmap.Config.ARGB_8888);
        rectangle.bitmapRectangleBuilder(bitmap,color);

        this.bitmap=bitmap;
    }


    /**
     * ce constructeur n'est pas tres rapide donc a eviter il utilse pas mal de ram mais sava encore le gc de java s'en ocupe vite
     * ce constructeur dessine un rectangle avec la texture donner en argument
     * @param bitmap
     * @param rectangle recange cooresspondant a la surface a dessiner
     * @param name nom du drawable agit comme un identifiant mais doit être unique
     * @param color couleur du rectangle
     * @throws InvalidDataException
     */
    //TODO verifier l'utiliter de ce constructeur
    @Deprecated
    public Drawable(Bitmap bitmap,Rectangle rectangle, String name, int color) throws InvalidDataException {
        this(rectangle.getPositionX(),rectangle.getPositionY(),name);
        if(rectangle.getHeight() <= 0 || rectangle.getWidth() <=0 || bitmap == null)throw new InvalidDataException(name,rectangle);

        rectangle.scaleRectangleToScreen();
        rectangle.bitmapRectangleBuilder(bitmap,color);
        this.bitmap = bitmap;
    }

    /**
     * NE PAS UTLISER CETTE FONCTION EST RESERVER AU FONCTIONNEMENT DU MOTEUR GRAPHIQUE
     * SI VOUS EN AVEZ BESOIN ENTRE EN CONTACT AVEC MARC POUR AJOUTER AU MOTEUR LA FONCTIONNALITER QUE VOUS AVEZ BESOIN
     * @param c canevas sur le quel dessiner
     * @param p pinceau
     */
    public void drawOn(@NonNull Canvas c,@NonNull Paint p)
    {
        //drawings
        if(c != null)
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

    /**
     * retourne vrai si l'objet passer en argument est un drawble qui a le meme nom
     * @param o l'objet passer en argument
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if(o instanceof Drawable)
        {
            return ((Drawable)o).getName().equals(name);
        }
        return false;
    }

    /**
     * permet de changer les coordonnés sur l'ecran de l'objet
     * TOUT LES OBJET SONT DESSINER DEPUIS LE COIN HAUT GAUCHE LES COODONNE DESIGNE DONC CE COIN
     * ET NON PAS LE CENTRE DE L'OBJET
     * @param x
     * @param y
     * /!\ PENSER A APPELER renderer.updateFrame(); AFFIN D'AFFICHER LE CHANGEMENT /!\
     */
    public void setCoordinates(float x, float y) {
        this.x=x;
        this.y=y;
    }

    /**
     * cette fonction permet d'initialiser le pinceau
     */
    protected void checkPaint() {
        if(p==null)
            p = new Paint();
    }

    /**
     * coupe le texte a une certaine longeur
     * @param charPerLines nombre de caractére par lignes
     * @param text texte a afficher
     * @return liste de lignes de texte
     */
    protected List<String> cutText(int charPerLines,String text)
    {
        List<String> output = new ArrayList<>();
        //TODO cut text in lines
        StringBuilder nextLine = new StringBuilder(charPerLines);
        for (String word : text.split(" ")) {
            if(word.length() < charPerLines)
            {
                if(word.length() + nextLine.toString().length() +1 < charPerLines)
                {
                    nextLine.append(" " + word);
                }else{
                    //fillWithSpace(nextLine,charPerLines);
                    output.add(nextLine.toString());
                    nextLine.delete(0,charPerLines-1);
                }
            }else{
                Log.e("TextRender","ERROR WORD TOO LONG :" + word);
            }
        }
        if(!nextLine.toString().equals(""))
        {
            //fillWithSpace(nextLine,charPerLines);
            output.add(nextLine.toString());
        }
        return output;
    }
}
