package com.iutlaval.myapplication.Game.Cards;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.iutlaval.myapplication.Game.Board;
import com.iutlaval.myapplication.R;

public abstract class Card {
    private int health;
    private int attack;

    private static Bitmap frameBitmap = null;

    public void onCardAttack(Board board){}

    public void onCardPlayed(Board board){}

    public void onCardDeath(Board board){}

    public void onTurnBegin(Board board){}

    /**
     * SURTOUT NE PAS @OVERRIDE
     * si vous voulez modifier le cadre changer getFrameTexture()
     * @param c context
     * @return bitmap
     */
    public Bitmap getFrameBitmap(Context c)
    {
        //TODO add a frame
        if(frameBitmap == null) {
            frameBitmap=BitmapFactory.decodeResource(c.getResources(), R.drawable.cadre_carte);
        }

        if(getFrameTexture() == R.drawable.cadre_carte)
        {
            return frameBitmap;
        }else{
            return BitmapFactory.decodeResource(c.getResources(),getFrameTexture());
        }

    }

    public int getFrameTexture()
    {
        return R.drawable.cadre_carte;
    }


    public abstract int getCardPicture();

    /**
     * retourne la culeur de la carte
     * la couleur s'affiche comme un filtre sur la carte
     * /!\ attention a l'opacite /!\ le format n'est PAS rgba MAIS argb
     * #AARRGGBB
     * je recomander une opaciter en 44 et 70
     * @return
     */
    public String getColor()
    {
        return "#00000000";
    }

    public abstract String getDescription();
    //TODO
}
