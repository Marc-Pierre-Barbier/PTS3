package com.iutlaval.myapplication.Game.Cards;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.iutlaval.myapplication.Game.Board;
import com.iutlaval.myapplication.Game.Player.Player;
import com.iutlaval.myapplication.R;

public abstract class Card {
    private static Bitmap frameBitmap = null;

    /**
     * *cette methode est appeler a chaque fois que la carte attaque
     * @param board
     */
    public void onCardAttack(Board board,Card enemy){}

    /**
     * cette methode est appeler quand la carte est jouer
     * elle se fait appler par board.java
     * @param board
     */
    public void onCardPlayed(Board board, Player p){}

    /**
     * cette methode est appeler quand la carte est detruite
     * @param board
     */
    public void onCardDeath(Board board){}

    /**
     * cette methode est appeler quand le tour commance
     * @param board
     */
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

    /**
     * retourne l'indice de la texture du cadre
     * par defaut cette methode possede un cadre donc vous n'avez pas a l'utilser
     * DE PLUS ne pas l'importer ameliore les performance car la ram n'est solicier qu'une fois pour l'integralite des cartes
     * vous pouvez la trouver a R.drawable.NOMDELATEXTURE
     * @return entier id texture
     */
    public int getFrameTexture()
    {
        return R.drawable.cadre_carte;
    }


    /**
     * retourne l'indice de la texture de la photo de la carte
     * @return
     */
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

    /**
     * retourn le texte de la description de la carte
     * @return la description
     */
    public abstract String getDescription();
    //TODO


    /**
     * retourne un entier coresspondant a l'attaque de la carte
     * @return
     */
    public abstract int getAttack();

    /**
     * retourne la santé de la carte
     * @return
     */
    public abstract int getHealth();
}

