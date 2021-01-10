package com.iutlaval.myapplication.Video.Drawables;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.iutlaval.myapplication.Game.Cards.Card;
import com.iutlaval.myapplication.InvalidDataException;
import com.iutlaval.myapplication.Video.Rectangle;

/**
 * TOUT LES NUMEROS QUI NE SONT PAS DANS DES FINAL SONT DES RATIO PERMETTANT DE METTRE TOUT A L ECHCELLE
 */
public class DrawableCard extends Drawable{
    //propriéte de base utilisé pour calculer toutes les tailles est positoins
    //ces données ne sont pas dans les digrames car sa renderais la lecture impossible
    private static final int CARD_WITH = 14;
    private static final int CARD_HEIGHT = 40;
    private static final float HP_ATK_FONT_SIZE =60F;
    private static final float DESCRIPTION_FONT_SIZE =26F;
    private static final float TITLE_FONT_SIZE =30F;
    private static int DESCRIPTION_TEXT_X_RES = 281;
    private static int DESCRIPTION_TEXT_Y_RES = 195;
    private static int TEXT_TITLE_X_RES = 281;
    private static int TEXT_TITLE_Y_RES = 60;
    private static int TEXT_ATK_HP_X_RES = 110;
    private static int TEXT_ATK_HP_Y_RES = 60;//60

    //des floats sont utiliser car ils prenne moin de ram que des doubles et que l'on a pas besoin de la precision supplementaire
    //dimentions
    private static final float ONBOARD_RECTANGLE_ATK_HP_WIDTH = CARD_WITH * 0.5F;
    private static final float ONBOARD_RECTANGLE_ATK_HP_HEIGHT = CARD_HEIGHT * 0.175F;
    private static final float ONBOARD_FRAME_X_SIZE = CARD_WITH;
    private static final float ONBOARD_FRAME_Y_SIZE = CARD_HEIGHT*0.5F;
    private static final float ONBOARD_ATK_HP_WIDTH = 0.45F * CARD_WITH;
    private static final float ONBOARD_ATK_HP_HEIGHT = 0.170F  * CARD_HEIGHT;

    private static final float OFFBOARD_ATK_HP_WIDTH = 0.25F * CARD_WITH;
    private static final float OFFBOARD_ATK_HP_HEIGHT = 0.08F  * CARD_HEIGHT;

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
    private final float ONBOARD_TEXT_ATK_X = 0.05F * CARD_WITH;
    private final float ONBOARD_TEXT_ATK_HP_Y = 0.48F * CARD_HEIGHT;
    private final float ONBOARD_TEXT_HP_X = 0.55F * CARD_WITH;
    private final float ONBOARD_TITLE_X = 0.1F * CARD_WITH;
    private final float ONBOARD_TITLE_Y = 0.00F * CARD_HEIGHT;

    private static final float ONBOARD_RECTANGLE_ATK_HP_Y = CARD_HEIGHT*0.375F;
    private static final float ONBOARD_RECTANGLE_ATK_X = CARD_WITH * 0F;
    private static final float ONBOARD_RECTANGLE_HP_X = CARD_WITH * 0.5F;

    private static final float OFFBOARD_PICTURE_X = 0.0787F * CARD_WITH;
    private static final float OFFBOARD_PICTURE_Y = 0.0834F * CARD_HEIGHT;
    private static final float OFFBOARD_DESCRIPTION_X = 0.07F * CARD_WITH;
    private static final float OFFBOARD_DESCRIPTION_Y = 0.45F * CARD_HEIGHT;
    private static final float OFFBOARD_ATK_X = 0.08F * CARD_WITH;
    private static final float OFFBOARD_HP_X = 0.71F * CARD_WITH;
    private static final float OFFBOARD_ATK_HP_Y = 0.85F * CARD_HEIGHT;//735F pour un ration de 2
    private static final float OFFBOARD_TITLE_X =0.1F * CARD_WITH;
    private static final float OFFBOARD_TITLE_Y = -0.025F * CARD_HEIGHT;

    private static final float CARD_ON_BOARD_DRAWABLE_WIDTH = CARD_WITH;
    private static final float CARD_ON_BOARD_DRAWABLE_HEIGHT = CARD_HEIGHT*0.667F;

    /**
     * ratio définissant la taille de l'objet
     */
    private int ratio;

    private Boolean onBoard;
    private Boolean draggable;

    private Drawable frameDrawable;
    private Drawable opacityRectangleDrawable;
    private Drawable pictureDrawable;

    //TODO : optimiser si la couleur est déja en mémoire
    private Drawable cardOnBoardDrawable;

    private Drawable cardDescription;
    private Drawable cardTitle;
    private Drawable cardHpDrawable;
    private Drawable cardAtkDrawable;
    private Drawable cardCostDrawable;

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
        this(c,x,y,name,context,1);
    }

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
     * @param ratio le ratio dirigeant la taille de la carte
     * @throws InvalidDataException
     * toutes les coodonés sont a zero pour faciliter la maintenance car la fonction setCoordinates() est appler pour corriger toutes les coordonées
     * donc les coodonées ne se trouve que dans cette fonction
     */
    public DrawableCard(Card c, float x, float y, String name, Context context,int ratio){
        super(x,y,name);
        this.draggable =true;
        this.onBoard=false;
        this.ratio=ratio;
        this.card=c;

        frameDrawable = new DrawableBitmap(c.getFrameBitmap(context),0,0,name+"frame",CARD_WITH*ratio,CARD_HEIGHT*ratio);
        //to string retourne le hash code de l'objet qui est donc unique ce qui fait de lui un id parfait
        if(c.getDescription() != null)
            cardDescription = new DrawableText(c.getDescription(),0,0,toString()+"description",DESCRIPTION_WIDTH*ratio,DESCRIPTION_HEIGHT*ratio,DESCRIPTION_FONT_SIZE,DESCRIPTION_TEXT_X_RES,DESCRIPTION_TEXT_Y_RES,Color.BLACK);

        cardTitle = new DrawableText(c.getName(),0,0,toString()+"name",TITLE_WIDTH*ratio,TITLE_HEIGHT*ratio,TITLE_FONT_SIZE*0.75F,TEXT_TITLE_X_RES,TEXT_TITLE_Y_RES,Color.BLACK,26);


        cardCostDrawable = new DrawableText(c.getCost()  + "", 0, 0, toString() + "cost", OFFBOARD_ATK_HP_WIDTH*ratio, OFFBOARD_ATK_HP_HEIGHT*ratio, HP_ATK_FONT_SIZE, TEXT_ATK_HP_X_RES, TEXT_ATK_HP_Y_RES,Color.BLACK);


        Rectangle opacityRect = new Rectangle(x,y,OPACITY_RECT_WIDTH*ratio+x,OPACITY_RECT_HEIGHT*ratio+x);
        Bitmap pictureBitmap = BitmapFactory.decodeResource(context.getResources(),c.getCardPicture());

        try {
            opacityRectangleDrawable = new DrawableRectangle(opacityRect,toString()+"Opacity", Color.parseColor(c.getColor()));
            pictureDrawable = new DrawableBitmap(pictureBitmap,0,0,toString()+"Picture",PICTURE_WIDTH*ratio,PICTURE_HEIGHT*ratio);
        } catch (InvalidDataException e) {
            e.printStackTrace();
            Log.e("CARTE :","forme de la carte invalide !");
            System.exit(100);
        }


        //TODO optimize rectangle by making it only one time
        //crée les rectangle pour la carte quand elle est sur le terrain
        Rectangle cardOnBoardFrame = new Rectangle(x,y,ONBOARD_FRAME_X_SIZE*ratio,ONBOARD_FRAME_Y_SIZE*ratio);
        cardOnBoardFrame.scaleRectangleToScreen();

        Rectangle cardOnBoardAtk = new Rectangle(x + ONBOARD_RECTANGLE_ATK_X, ONBOARD_RECTANGLE_ATK_HP_Y*ratio,ONBOARD_RECTANGLE_ATK_HP_WIDTH*ratio,ONBOARD_RECTANGLE_ATK_HP_HEIGHT*ratio);
        cardOnBoardAtk.scaleRectangleToScreen();
        Rectangle cardOnBoardHp = new Rectangle(x + ONBOARD_RECTANGLE_HP_X, ONBOARD_RECTANGLE_ATK_HP_Y*ratio,ONBOARD_RECTANGLE_ATK_HP_WIDTH*ratio,ONBOARD_RECTANGLE_ATK_HP_HEIGHT*ratio);
        cardOnBoardHp.scaleRectangleToScreen();

        //creation de la bitmap qui va être rendu
        Bitmap cardOnBoardBitmap= Bitmap.createBitmap((int)cardOnBoardFrame.getWidth()*ratio,(int)cardOnBoardFrame.getHeight(), Bitmap.Config.ARGB_8888);

        //on dessine nos rectangle sur la bitmap
        cardOnBoardFrame.bitmapRectangleBuilder(cardOnBoardBitmap,Color.parseColor(removeAlpha(c.getColor())));
        cardOnBoardAtk.bitmapRectangleBuilder(cardOnBoardBitmap,Color.parseColor("#FFFF0000"));
        cardOnBoardHp.bitmapRectangleBuilder(cardOnBoardBitmap,Color.parseColor("#FF00FF00"));

        cardOnBoardDrawable = new DrawableBitmap(cardOnBoardBitmap,x,y,toString()+"BOARD",CARD_ON_BOARD_DRAWABLE_WIDTH*ratio,CARD_ON_BOARD_DRAWABLE_HEIGHT*ratio);

        if(ratio == 1)c.setDrawableCard(this);

        updateHpAndAtk(c.getAttack(),c.getHealth() );
        setCoordinates(getX(), getY());
    }

    /**
     * permet de crée une grande carte a partir d'une carte plus petite
     * @param smallCArd car parante
     * @param cont context
     * @param ratio ratio de taille
     * /!\ ne pas s'en servir pour juste changer la taille d'une carte/!\
     */
    public DrawableCard(DrawableCard smallCArd, Context cont,int ratio) {
        this(smallCArd.card,smallCArd.getX(),smallCArd.getY(),smallCArd.getName()+"BIG",cont,ratio);
        draggable =false;
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
                cardAtkDrawable = new DrawableText(atk+"", 0, 0, toString() + "atk", ONBOARD_ATK_HP_WIDTH*ratio, ONBOARD_ATK_HP_HEIGHT*ratio, HP_ATK_FONT_SIZE,TEXT_ATK_HP_X_RES,TEXT_ATK_HP_Y_RES,Color.BLACK);
            if (hp != null)
                cardHpDrawable = new DrawableText(hp+"", 0, 0, toString() + "hp", ONBOARD_ATK_HP_WIDTH*ratio, ONBOARD_ATK_HP_HEIGHT*ratio, HP_ATK_FONT_SIZE,TEXT_ATK_HP_X_RES,TEXT_ATK_HP_Y_RES,Color.BLACK);
        }else{
            if (atk != null)
                cardAtkDrawable = new DrawableText(atk + "", 0, 0, toString() + "atk", OFFBOARD_ATK_HP_WIDTH*ratio, OFFBOARD_ATK_HP_HEIGHT*ratio, HP_ATK_FONT_SIZE, TEXT_ATK_HP_X_RES, TEXT_ATK_HP_Y_RES,Color.BLACK);
            if (hp != null)
                cardHpDrawable = new DrawableText(hp + "", 0, 0, toString() + "hp", OFFBOARD_ATK_HP_WIDTH*ratio, OFFBOARD_ATK_HP_HEIGHT*ratio, HP_ATK_FONT_SIZE, TEXT_ATK_HP_X_RES, TEXT_ATK_HP_Y_RES,Color.BLACK);
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
            pictureDrawable.drawOn(c,p);

            cardTitle.drawOn(c,p);
            cardHpDrawable.drawOn(c,p);
            cardAtkDrawable.drawOn(c,p);
        }else{
            frameDrawable.drawOn(c,p);
            opacityRectangleDrawable.drawOn(c,p);
            pictureDrawable.drawOn(c,p);

            //dessine le texte
            cardTitle.drawOn(c,p);
            if(cardDescription != null && !cardDescription.equals(""))cardDescription.drawOn(c,p);
            cardHpDrawable.drawOn(c,p);
            cardAtkDrawable.drawOn(c,p);
            cardCostDrawable.drawOn(c,p);
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
            pictureDrawable.setCoordinates(x + ONBOARD_PICTURE_X*ratio, y +ONBOARD_PICTURE_Y*ratio);
            cardAtkDrawable.setCoordinates(x+ ONBOARD_TEXT_ATK_X *ratio,y + ONBOARD_TEXT_ATK_HP_Y *ratio);
            cardHpDrawable.setCoordinates(x+ ONBOARD_TEXT_HP_X *ratio,y + ONBOARD_TEXT_ATK_HP_Y *ratio);
            cardTitle.setCoordinates(x+ONBOARD_TITLE_X*ratio,y - ONBOARD_TITLE_Y*ratio);
        }else{
            frameDrawable.setCoordinates(x,y);
            opacityRectangleDrawable.setCoordinates(x, y);
            pictureDrawable.setCoordinates(x + OFFBOARD_PICTURE_X*ratio, y + OFFBOARD_PICTURE_Y*ratio);

            //les textes ce dessines depuis leur base et non depuis le coin haut gauche
            if(cardDescription != null)cardDescription.setCoordinates(x+OFFBOARD_DESCRIPTION_X*ratio,y+OFFBOARD_DESCRIPTION_Y*ratio);
            cardAtkDrawable.setCoordinates(x + OFFBOARD_ATK_X*ratio,y + OFFBOARD_ATK_HP_Y*ratio);
            cardHpDrawable.setCoordinates(x + OFFBOARD_HP_X*ratio,y +OFFBOARD_ATK_HP_Y*ratio);
            cardTitle.setCoordinates(x+OFFBOARD_TITLE_X*ratio,y - OFFBOARD_TITLE_Y*ratio);
            cardCostDrawable.setCoordinates(x+CARD_WITH/2*ratio,y + OFFBOARD_ATK_HP_Y *ratio);

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

    @Override
    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean b) {
        this.draggable=b;
    }

    public boolean isOnBoard()
    {
        return onBoard;
    }

    public int getRatio() {
        return ratio;
    }

    public Card getCard() {
        return card;
    }
}
