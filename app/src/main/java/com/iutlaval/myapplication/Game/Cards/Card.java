package com.iutlaval.myapplication.Game.Cards;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.iutlaval.myapplication.Game.Board;
import com.iutlaval.myapplication.R;
import com.iutlaval.myapplication.Video.Drawables.DrawableCard;

public abstract class Card {
    private static Bitmap frameBitmap = null;
    private DrawableCard drawableCard = null;


    private int attack;
    private int health;
    /**
     * cree une carte est lui definit un drawable
     * @param UID
     * @param c
     */
    public Card(String UID,Context c) {
        this.attack=getDefaultAttack();
        this.health=getDefaultHealth();
        if(!(UID == null || c == null)) {
            drawableCard = new DrawableCard(this, 0, 0, UID, c);
        }
    }

    public int getAttack() {
        return attack;
    }

    public int getHealth() {
        return health;
    }

    public void setAttack(int attack) {
        this.attack = attack;
        //on met a jours l'affichage
        drawableCard.updateHpAndAtk(attack,null);
    }

    public void setHealth(int health) {
        this.health = health;
        //on met a jours l'affichage
        drawableCard.updateHpAndAtk(null,health);
    }

    /**
     * SURTOUT NE PAS @OVERRIDE
     * si vous voulez modifier le cadre changer getFrameTexture()
     * @param c context
     * @return bitmap
     */
    public Bitmap getFrameBitmap(Context c)
    {
        if(frameBitmap == null) {
            frameBitmap=BitmapFactory.decodeResource(c.getResources(), R.drawable.t_cc_defaut);
        }

        if(getFrameTexture() == R.drawable.t_cc_defaut)
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
        return R.drawable.t_cc_defaut;
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
     * #AARRGGB    //TODOB
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


    /**
     * retourne un entier coresspondant a l'attaque de la carte
     * @return
     */
    public abstract int getDefaultAttack();

    /**
     * retourne la santé de la carte
     * @return
     */
    public abstract int getDefaultHealth();

    /**
     * NE PAS TOUCHER CETTE FONCTION SI VOUS N'ÊTES PAS SUR DE CE QUE VOUS FAITES
     * cette fonction permet de redefinir le drawable de la carte
     * @param drawableCard
     */
    public void setDrawableCard(DrawableCard drawableCard) {
        this.drawableCard = drawableCard;
    }

    public final DrawableCard getDrawableCard() {
        return drawableCard;
    }

    /**
     * cette fonction retourne le nom de la carte qui sera afficher
     * @return le nom
     */
    public abstract String getName();

    /**
     * cette fonction retourne le coût en mana de la carte qui sera afficher
     * @return le cout
     */
    public abstract int getCost();

    /**
     * cette fonction retourne le lien wikipédia de la carte qui sera afficher
     * @return le lien wikipedia
     */
    public abstract String getWikipediaLink();

    /**
     * cette fonction retourne la catégorie de la carte qui sera afficher
     * @return la categorie
     */
    public abstract String getCategorie();
}

