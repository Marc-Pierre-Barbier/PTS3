package com.iutlaval.myapplication.Video.Drawables;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.iutlaval.myapplication.ERROR_CODE;
import com.iutlaval.myapplication.Game.Cards.Card;
import com.iutlaval.myapplication.InvalidDataException;
import com.iutlaval.myapplication.Video.Rectangle;

public class DrawableCard extends Drawable{

    private static final int CARD_WITH = 14;
    private static final int CARD_HEIGHT = 32;
    private static final float HP_ATK_FONT_SIZE_BIG_CARD =30F;
    private static final float HP_ATK_FONT_SIZE_BOARD =70F;
    private static final float DESCRIPTION_FONT_SIZE =30F;
    private static final float TITLE_FONT_SIZE =30F;

    private Boolean onBoard;

    private Drawable OpacityRectangleDrawable;
    private Drawable PictureDrawable;

    private Drawable cardOnBoardDrawable;

    private Drawable cardDescription;
    private Drawable cardTitle;
    private Drawable cardHpDrawable;
    private Drawable cardAtkDrawable;

    //DEPENDANCE CIRCULAIRE NE PAS CHANGER DE GC NON Tracing
    //https://www.baeldung.com/java-gc-cyclic-references
    private Card card;

    /**
     * ce constructeur permet de crée un drawable simplifier pour les carte qui integre :
     *  _le supoort de la description
     *  _le changement d'etat entre grand et terrain
     *  _les point de vie
     *  _l'image
     *  _les deplacement du tout
     *
     *
     * @param c carte a dessiner
     * @param x position de la carte sur l'axe x
     * @param y position de la carte sur l'axe y
     * @param name nom du drawable
     * @param context le contexte est simplement l'instance de GameActivity qui est utiliser pas le moteur de jeu
     * @throws InvalidDataException
     * toutes les coodonés sont a zero pour faciliter la maintenance car la fonction setCoordinates() est appler pour corriger toutes les coordonées
     * donc les coodonées ne se trouve que dans cette fonction
     */
    public DrawableCard(Card c, float x, float y, String name, Context context){
        super(c.getFrameBitmap(context),x,y,name,CARD_WITH,CARD_HEIGHT);

        card=c;

        onBoard=true;

        cardDescription = new DrawableCardDescription(c.getDescription(),x+CARD_WITH*0.07F,y+CARD_HEIGHT/2.2F,toString()+"description",CARD_WITH*0.9F,CARD_HEIGHT*0.35F,DESCRIPTION_FONT_SIZE);
        cardTitle = new DrawableCardTitle(c.getName(),0,0,toString()+"name",CARD_WITH,CARD_HEIGHT*0.1F,TITLE_FONT_SIZE);

        Rectangle cardRect = new Rectangle(x,y,CARD_WITH+x,CARD_HEIGHT+x);
        Bitmap pictureBitmap = BitmapFactory.decodeResource(context.getResources(),c.getCardPicture());

        try {
            OpacityRectangleDrawable = new Drawable(cardRect,toString()+"Opacity", Color.parseColor(c.getColor()));
            PictureDrawable = new Drawable(pictureBitmap,0,0,toString()+"Picture",CARD_WITH/1.2F,CARD_HEIGHT/2.7F);
        } catch (InvalidDataException e) {
            e.printStackTrace();
            Log.e("CARTE :","forme de la carte invalide !");
            System.exit(ERROR_CODE.ERROR_INVALID_CARD_DIMENSIONS.ordinal());
        }


        Paint p = new Paint();

        //TODO optimize rectangle by making it only one time
        //crée les rectangle pour la carte quand elle est sur le terrain
        Rectangle cardOnBoardFrame = new Rectangle(x,y,CARD_WITH,CARD_HEIGHT/2);
        cardOnBoardFrame.scaleRectangleToScreen();

        float offset = CARD_HEIGHT/2.7F;
        Rectangle cardOnBoardAtk = new Rectangle(x,offset,CARD_WITH/2,CARD_HEIGHT/8);
        cardOnBoardAtk.scaleRectangleToScreen();
        Rectangle cardOnBoardHp = new Rectangle(CARD_WITH/2,offset,CARD_WITH/2,CARD_HEIGHT/8);
        cardOnBoardHp.scaleRectangleToScreen();

        //creation de la bitmap qui va être rendu
        Bitmap cardOnBoardBitmap= Bitmap.createBitmap((int)cardOnBoardFrame.getWidth(),(int)cardOnBoardFrame.getHeight(), Bitmap.Config.ARGB_8888);

        //on dessine nos rectangle sur la bitmap
        cardOnBoardFrame.bitmapRectangleBuilder(cardOnBoardBitmap,Color.parseColor(removeAlpha(c.getColor())));
        cardOnBoardAtk.bitmapRectangleBuilder(cardOnBoardBitmap,Color.parseColor("#FFFF0000"));
        cardOnBoardHp.bitmapRectangleBuilder(cardOnBoardBitmap,Color.parseColor("#FF00FF00"));

        cardOnBoardDrawable = new Drawable(cardOnBoardBitmap,x,y,toString()+"BOARD",CARD_WITH,CARD_HEIGHT*2/3);

        c.setDrawableCard(this);

        updateHpAndAtk(c.getAttack(),c.getHealth());
        setCoordinates(getX(), getY());
    }

    /**
     * cette fonction permet de mettre a jour le nombre de hp restant
     * si vous ne voulez pas changer une des donnes metter la null ce n'est pas des int mais des Integer donc c'est possible
     *
     * toutes les coodonés sont a zero pour faciliter la maintenance car la fonction setCoordinates() est appler pour corriger toutes les coordonées
     * donc les coodonées ne se trouve que dans cette fonction
     * @param atk l'attaque de la carte
     * @param hp les point de vie de la carte
     */
    public void updateHpAndAtk(Integer atk,Integer hp)
    {
        if(onBoard) {
            if (atk != null)
                cardAtkDrawable = new DrawableHpAtk(atk+"", 0, 0, toString() + "atk", CARD_WITH * 0.4F, CARD_HEIGHT / 8, HP_ATK_FONT_SIZE_BOARD);
            if (hp != null)
                cardHpDrawable = new DrawableHpAtk(hp+"", 0, 0, toString() + "hp", CARD_WITH * 0.4F, CARD_HEIGHT / 8, HP_ATK_FONT_SIZE_BOARD);
        }else{
            if (atk != null)
                cardAtkDrawable = new DrawableHpAtk(atk + "", 0, 0, toString() + "atk", CARD_WITH / 2, CARD_HEIGHT / 4, HP_ATK_FONT_SIZE_BIG_CARD);
            if (hp != null)
                cardHpDrawable = new DrawableHpAtk(hp + "", 0, 0, toString() + "hp", CARD_WITH / 2, CARD_HEIGHT / 4, HP_ATK_FONT_SIZE_BIG_CARD);
        }
        setCoordinates(getX(), getY());
    }

    /**
     * supprime la couche alpha numerique d'une carte
     * MAIS si la carte est completement transparente ALORS la texture devien blanche
     * @param color le code #AARRGGBB de la carte
     * @return la texture modifier
     */
    private String removeAlpha(String color) {
        char[] colorArray = color.toCharArray();
        if(colorArray[1]=='0' && colorArray[2]=='0')return "#FFFFFFFF";
        colorArray[1]='F';
        colorArray[2]='F';
        System.out.println(String.valueOf(colorArray));
        return String.valueOf(colorArray);
    }

    /**
     * cette fonction permet de dessiner tout les elements de la carte
     * @param c canevas sur le quel dessiner
     * @param p pinceau
     */
    @Override
    public void drawOn(Canvas c, Paint p) {
        //draw the frame
        if(onBoard)
        {
            cardOnBoardDrawable.drawOn(c,p);
            PictureDrawable.drawOn(c,p);

            cardTitle.drawOn(c,p);
            cardHpDrawable.drawOn(c,p);
            cardAtkDrawable.drawOn(c,p);
        }else{
            super.drawOn(c, p);
            OpacityRectangleDrawable.drawOn(c,p);
            PictureDrawable.drawOn(c,p);

            //dessine le texte
            cardTitle.drawOn(c,p);
            cardDescription.drawOn(c,p);
            cardHpDrawable.drawOn(c,p);
            cardAtkDrawable.drawOn(c,p);
        }

    }

    /**
     * cette fonction permet de deplacer la carte et tout ces sous element
     * tout les valeur on ete determiner empiriquement
     * @param x postion x
     * @param y postion y
     */
    @Override
    public void setCoordinates(float x, float y) {
        super.setCoordinates(x, y);
        if (onBoard) {
            cardOnBoardDrawable.setCoordinates(x,y);
            PictureDrawable.setCoordinates(x + CARD_WITH/14F, y +CARD_HEIGHT/16F);
            cardAtkDrawable.setCoordinates(x+CARD_WITH*0.05F,y + CARD_HEIGHT*0.48F );
            cardHpDrawable.setCoordinates(x+CARD_WITH*0.55F,y + CARD_HEIGHT*0.48F );
            cardTitle.setCoordinates(x+CARD_WITH*0.1F,y - CARD_WITH * 0.1F);
        }else{
            OpacityRectangleDrawable.setCoordinates(x, y);
            PictureDrawable.setCoordinates(x + CARD_WITH/12.7F, y + CARD_HEIGHT/12.0F);

            //les textes ce dessines depuis leur base et non depuis le coin haut gauche
            cardDescription.setCoordinates(x+CARD_WITH*0.07F,y+CARD_HEIGHT/2.2F);
            cardAtkDrawable.setCoordinates(x + CARD_WITH*0.08F,y +CARD_HEIGHT*0.70F);
            cardHpDrawable.setCoordinates(x + CARD_WITH*0.7F,y +CARD_HEIGHT*0.70F);
            cardTitle.setCoordinates(x+CARD_WITH*0.1F,y - CARD_WITH * 0.055F);
        }
    }

    /**
     * defini si la carte doit s'afficher en format plein ecran ou en format terrain (avec ou sans description)
     * @param onBoard
     */
    public void setOnBoard(Boolean onBoard)
    {
        this.onBoard = onBoard;
        //force recalculate coodinates
        setCoordinates(getX(), getY());
        updateHpAndAtk(card.getAttack(),card.getHealth());
    }
}
