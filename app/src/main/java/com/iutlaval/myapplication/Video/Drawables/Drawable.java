package com.iutlaval.myapplication.Video.Drawables;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public abstract class Drawable {

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
     * retourne si le drawable peut être deplacer en drag and drop
     * @return
     */
    public boolean isDraggable() {
        return false;
    }

    protected void setBitmap(Bitmap bitmap)
    {
        this.bitmap=bitmap;
    }
}
