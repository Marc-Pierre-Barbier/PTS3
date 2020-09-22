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

/**
 * TOUT LES NUMEROS QUI NE SONT PAS DANS DES FINAL SONT DES RATIO PERMETTANT DE METTRE TOUT A L ECHCELLE
 * TODO : tout mettre dans des finals
 */
public class DrawableCard extends Drawable{

    public static final int CARD_WITH = 14;
    private static final int CARD_HEIGHT = 40;
    private static final float HP_ATK_FONT_SIZE_BIG_CARD =30F;
    private static final float HP_ATK_FONT_SIZE_BOARD =70F;
    private static final float DESCRIPTION_FONT_SIZE =30F;
    private static final float TITLE_FONT_SIZE =30F;
    private static int DESCRIPTION_TEXT_X_RES = 281;
    private static int DESCRIPTION_TEXT_Y_RES = 195;
    private static int TEXT_TITLE_X_RES = 281;
    private static int TEXT_TITLE_Y_RES = 60;
    private static int TEXT_ATK_HP_X_RES = 110;
    private static int TEXT_ATK_HP_Y_RES = 60;

    //des floats sont utiliser card ils prenne moin de ram que des doubles et que l'on a pas besoin de la precision supplementaire
    //dimentions
    private static final float ONBOARD_RECTANGLE_ATK_HP_WIDTH = CARD_WITH * 0.5F;
    private static final float ONBOARD_RECTANGLE_ATK_HP_HEIGHT = CARD_HEIGHT * 0.2F;
    private static final float ONBOARD_ATK_HP_WIDTH = 0.4F * CARD_WITH;
    private static final float ONBOARD_ATK_HP_HEIGHT = 0.125F  * CARD_HEIGHT;
    private static final float OFFBOARD_ATK_HP_WIDTH = 0.5F * CARD_WITH;
    private static final float OFFBOARD_ATK_HP_HEIGHT = 0.2F  * CARD_HEIGHT;

    private static final float DESCRIPTION_WIDTH = CARD_WITH*0.9F;
    private static final float DESCRIPTION_HEIGHT = CARD_HEIGHT*0.35F;
    private static final float TITLE_HEIGHT = CARD_HEIGHT*0.1F;
    private static final float TITLE_WIDTH = CARD_WITH;
    private static final float OPACITY_RECT_WIDTH=CARD_WITH;
    private static final float OPACITY_RECT_HEIGHT=CARD_HEIGHT;
    private static final float PICTURE_WIDTH = CARD_WITH * 0.834F;
    private static final float PICTURE_HEIGHT = CARD_HEIGHT * 0.37F;

    //variables de posisions
    private final float ONBOARD_PICTURE_X = 0.0714F * CARD_WITH;
    private final float ONBOARD_PICTURE_Y = 0.0625F * CARD_HEIGHT;
    private final float ONBOARD_ATK_X = 0.05F * CARD_WITH;
    private final float ONBOARD_ATK_HP_Y = 0.52F * CARD_HEIGHT;
    private final float ONBOARD_HP_X = 0.55F * CARD_WITH;
    private final float ONBOARD_TITLE_X = 0.1F * CARD_WITH;
    private final float ONBOARD_TITLE_Y = 0.05F * CARD_HEIGHT;

    private static final float ONBOARD_FRAME_X_SIZE = CARD_WITH;
    private static final float ONBOARD_FRAME_Y_SIZE = CARD_HEIGHT*0.5F;
    private static final float ONBOARD_RECTANGLE_ATK_HP_Y = CARD_HEIGHT * 0.385F;
    private static final float ONBOARD_RECTANGLE_ATK_X = CARD_HEIGHT * 0F;
    private static final float ONBOARD_RECTANGLE_HP_X = CARD_WITH * 0.5F;

    private final float OFFBOARD_PICTURE_X = 0.0787F * CARD_WITH;
    private final float OFFBOARD_PICTURE_Y = 0.0834F * CARD_HEIGHT;
    private final float OFFBOARD_DESCRIPTION_X = 0.07F * CARD_WITH;
    private final float OFFBOARD_DESCRIPTION_Y = 0.455F * CARD_HEIGHT;
    private final float OFFBOARD_ATK_X = 0.08F * CARD_WITH;
    private final float OFFBOARD_ATK_Y = 0.735F * CARD_HEIGHT;
    private final float OFFBOARD_HP_X = 0.7F * CARD_WITH;
    private final float OFFBOARD_HP_Y = 0.735F * CARD_HEIGHT;
    private final float OFFBOARD_TITLE_X =0.1F * CARD_WITH;
    private final float OFFBOARD_TITLE_Y = 0.025F * CARD_HEIGHT;

    private static final float CARD_ON_BOARD_DRAWABLE_WIDTH = CARD_WITH;
    private static final float CARD_ON_BOARD_DRAWABLE_HEIGHT = CARD_HEIGHT*0.667F;

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
     *  _le support du titre
     *  _le changement d'etat entre grand et terrain
     *  _les point de vie
     *  _les point attaque
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

        //to string retourne le hash code de l'objet qui est donc unique ce qui fait de lui un id parfait
        cardDescription = new Drawable(c.getDescription(),0,0,toString()+"description",DESCRIPTION_WIDTH,DESCRIPTION_HEIGHT,DESCRIPTION_FONT_SIZE,DESCRIPTION_TEXT_X_RES,DESCRIPTION_TEXT_Y_RES);
        cardTitle = new Drawable(c.getName(),0,0,toString()+"name",TITLE_WIDTH,TITLE_HEIGHT,TITLE_FONT_SIZE,TEXT_TITLE_X_RES,TEXT_TITLE_Y_RES);

        Rectangle opacityRect = new Rectangle(x,y,OPACITY_RECT_WIDTH+x,OPACITY_RECT_HEIGHT+x);
        Bitmap pictureBitmap = BitmapFactory.decodeResource(context.getResources(),c.getCardPicture());

        try {
            OpacityRectangleDrawable = new Drawable(opacityRect,toString()+"Opacity", Color.parseColor(c.getColor()));
            PictureDrawable = new Drawable(pictureBitmap,0,0,toString()+"Picture",PICTURE_WIDTH,PICTURE_HEIGHT);
        } catch (InvalidDataException e) {
            e.printStackTrace();
            Log.e("CARTE :","forme de la carte invalide !");
            System.exit(ERROR_CODE.ERROR_INVALID_CARD_DIMENSIONS.ordinal());
        }



        //TODO optimize rectangle by making it only one time
        //crée les rectangle pour la carte quand elle est sur le terrain
        Rectangle cardOnBoardFrame = new Rectangle(x,y,ONBOARD_FRAME_X_SIZE,ONBOARD_FRAME_Y_SIZE);
        cardOnBoardFrame.scaleRectangleToScreen();

        Rectangle cardOnBoardAtk = new Rectangle(x + ONBOARD_RECTANGLE_ATK_X, ONBOARD_RECTANGLE_ATK_HP_Y,ONBOARD_RECTANGLE_ATK_HP_WIDTH,ONBOARD_RECTANGLE_ATK_HP_HEIGHT);
        cardOnBoardAtk.scaleRectangleToScreen();
        Rectangle cardOnBoardHp = new Rectangle(x + ONBOARD_RECTANGLE_HP_X, ONBOARD_RECTANGLE_ATK_HP_Y,ONBOARD_RECTANGLE_ATK_HP_WIDTH,ONBOARD_RECTANGLE_ATK_HP_HEIGHT);
        cardOnBoardHp.scaleRectangleToScreen();

        //creation de la bitmap qui va être rendu
        Bitmap cardOnBoardBitmap= Bitmap.createBitmap((int)cardOnBoardFrame.getWidth(),(int)cardOnBoardFrame.getHeight(), Bitmap.Config.ARGB_8888);

        //on dessine nos rectangle sur la bitmap
        cardOnBoardFrame.bitmapRectangleBuilder(cardOnBoardBitmap,Color.parseColor(removeAlpha(c.getColor())));
        cardOnBoardAtk.bitmapRectangleBuilder(cardOnBoardBitmap,Color.parseColor("#FFFF0000"));
        cardOnBoardHp.bitmapRectangleBuilder(cardOnBoardBitmap,Color.parseColor("#FF00FF00"));

        cardOnBoardDrawable = new Drawable(cardOnBoardBitmap,x,y,toString()+"BOARD",CARD_ON_BOARD_DRAWABLE_WIDTH,CARD_ON_BOARD_DRAWABLE_HEIGHT);

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
                cardAtkDrawable = new Drawable(atk+"", 0, 0, toString() + "atk", ONBOARD_ATK_HP_WIDTH, ONBOARD_ATK_HP_HEIGHT, HP_ATK_FONT_SIZE_BOARD,TEXT_ATK_HP_X_RES,TEXT_ATK_HP_Y_RES);
            if (hp != null)
                cardHpDrawable = new Drawable(hp+"", 0, 0, toString() + "hp", ONBOARD_ATK_HP_WIDTH, ONBOARD_ATK_HP_HEIGHT, HP_ATK_FONT_SIZE_BOARD,TEXT_ATK_HP_X_RES,TEXT_ATK_HP_Y_RES);
        }else{
            if (atk != null)
                cardAtkDrawable = new Drawable(atk + "", 0, 0, toString() + "atk", OFFBOARD_ATK_HP_WIDTH, OFFBOARD_ATK_HP_HEIGHT, HP_ATK_FONT_SIZE_BIG_CARD,TEXT_ATK_HP_X_RES,TEXT_ATK_HP_Y_RES);
            if (hp != null)
                cardHpDrawable = new Drawable(hp + "", 0, 0, toString() + "hp", OFFBOARD_ATK_HP_WIDTH, OFFBOARD_ATK_HP_HEIGHT, HP_ATK_FONT_SIZE_BIG_CARD,TEXT_ATK_HP_X_RES,TEXT_ATK_HP_Y_RES);
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
     * @param x postion x
     * @param y postion y
     *          /!\ PENSER A APPELER renderer.updateFrame(); AFFIN D'AFFICHER LE CHANGEMENT /!\
     */
    @Override
    public void setCoordinates(float x, float y) {
        super.setCoordinates(x, y);
        if (onBoard) {
            cardOnBoardDrawable.setCoordinates(x,y);
            PictureDrawable.setCoordinates(x + ONBOARD_PICTURE_X, y +ONBOARD_PICTURE_Y);
            cardAtkDrawable.setCoordinates(x+ONBOARD_ATK_X,y + ONBOARD_ATK_HP_Y);
            cardHpDrawable.setCoordinates(x+ONBOARD_HP_X,y + ONBOARD_ATK_HP_Y);
            cardTitle.setCoordinates(x+ONBOARD_TITLE_X,y - ONBOARD_TITLE_Y);
        }else{
            OpacityRectangleDrawable.setCoordinates(x, y);
            PictureDrawable.setCoordinates(x + OFFBOARD_PICTURE_X, y + OFFBOARD_PICTURE_Y);

            //les textes ce dessines depuis leur base et non depuis le coin haut gauche
            cardDescription.setCoordinates(x+OFFBOARD_DESCRIPTION_X,y+OFFBOARD_DESCRIPTION_Y);
            cardAtkDrawable.setCoordinates(x + OFFBOARD_ATK_X,y + OFFBOARD_ATK_Y);
            cardHpDrawable.setCoordinates(x + OFFBOARD_HP_X,y +OFFBOARD_HP_Y);
            cardTitle.setCoordinates(x+OFFBOARD_TITLE_X,y - OFFBOARD_TITLE_Y);
        }
    }




    /**
     * defini si la carte doit s'afficher en format plein ecran ou en format terrain (avec ou sans description)
     * @param onBoard
     * /!\ PENSER A APPELER renderer.updateFrame(); AFFIN D'AFFICHER LE CHANGEMENT /!\
     */
    public void setOnBoard(Boolean onBoard)
    {
        this.onBoard = onBoard;
        //force recalculate coodinates
        setCoordinates(getX(), getY());
        updateHpAndAtk(card.getAttack(),card.getHealth());
    }

    public static int getCardWith() {
        return CARD_WITH;
    }

    public static int getCardHeight() {
        return CARD_HEIGHT;
    }
}
