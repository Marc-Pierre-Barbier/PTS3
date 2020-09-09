package com.iutlaval.myapplication.Video;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.iutlaval.myapplication.Game.Cards.Card;
import com.iutlaval.myapplication.InvalidDataException;

public class DrawableCard extends Drawable{

    private static final int CARD_WITH = 14;
    private static final int CARD_HEIGHT = 32;
    private static final float HP_ATK_FONT_SIZE =10F;

    private String color;

    private Drawable OpacityRectangleDrawable;
    private Drawable PictureDrawable;
    private Boolean onBoard;

    private Drawable cardOnBardDrawable;
    private Drawable cardDescription;

    private Drawable cardHpDrawable;
    private Drawable cardAtkDrawable;


    public DrawableCard(Card c, float x, float y, String name, Context context) throws InvalidDataException {
        super(c.getFrameBitmap(context),x,y,name,CARD_WITH,CARD_HEIGHT);

        cardDescription = new Drawable(c.getDescription(),x+1F,y+CARD_HEIGHT/2,toString()+"description",CARD_WITH,CARD_HEIGHT/2,10F);
        updateHpAndAtk(c.getAttack(),c.getHealth());

        color = c.getColor();//CARD_WITH+x
        Rectangle cardRect = new Rectangle(x,y,(float)(CARD_WITH+x),(float)(CARD_HEIGHT+x));
        OpacityRectangleDrawable = new Drawable(cardRect,toString()+"Opacity", Color.parseColor(c.getColor()));

        Bitmap pictureBitmap = BitmapFactory.decodeResource(context.getResources(),c.getCardPicture());
        PictureDrawable = new Drawable(pictureBitmap,getX()+1.1F,getY()+2.5F,toString()+"Picture",CARD_WITH-2.4F,CARD_HEIGHT/2 - 4);


        Paint p = new Paint();


        //crée les rectangle pour la carte quand elle est sur le terrain
        Rectangle cardOnBoardFrame = new Rectangle(x,y,CARD_WITH,CARD_HEIGHT/2);
        cardOnBoardFrame.scaleRectangleToScreen();

        int offset = CARD_HEIGHT/2 - CARD_HEIGHT/8;
        Rectangle cardOnBoardHp = new Rectangle(x,offset,CARD_WITH/2,CARD_HEIGHT/8);
        cardOnBoardHp.scaleRectangleToScreen();
        Rectangle cardOnBoardAtk = new Rectangle(CARD_WITH/2,offset,CARD_WITH/2,CARD_HEIGHT/8);
        cardOnBoardAtk.scaleRectangleToScreen();

        //creation de la bitmap qui va être rendu
        Bitmap cardOnBoardBitmap= Bitmap.createBitmap((int)cardOnBoardFrame.getWidth(),(int)cardOnBoardFrame.getHeight(), Bitmap.Config.ARGB_8888);

        //on dessine nos rectangle sur la bitmap
        cardOnBoardFrame.bitmapRectangleBuilder(cardOnBoardBitmap,Color.parseColor(removeAlpha(c.getColor())),p);
        cardOnBoardHp.bitmapRectangleBuilder(cardOnBoardBitmap,Color.parseColor("#FF00FF00"),p);
        cardOnBoardAtk.bitmapRectangleBuilder(cardOnBoardBitmap,Color.parseColor("#FFFF0000"),p);

        cardOnBardDrawable = new Drawable(cardOnBoardBitmap,x,y,toString()+"BOARD",CARD_WITH,CARD_HEIGHT*2/3);

        onBoard=false;
    }

    /**
     * cette fonction permet de mettre a jour le nombre de hp restant
     * si vous ne voulez pas changer une des donnes metter la null ce n'est pas des int mais des Integer donc c'est possible
     * @param atk l'attaque de la carte
     * @param hp les point de vie de la carte
     */
    public void updateHpAndAtk(Integer atk,Integer hp)
    {
        if(atk != null)cardAtkDrawable = new Drawable(atk+"    ",getX()+0.6F,getY()+CARD_HEIGHT-5F,toString()+"atk",CARD_WITH/2,CARD_HEIGHT/8,HP_ATK_FONT_SIZE);
        if(hp != null)cardHpDrawable = new Drawable(hp+"    ",getX()+2.4F+CARD_WITH/2,getY()+CARD_HEIGHT-5F,toString()+"hp",CARD_WITH/2,CARD_HEIGHT/8,HP_ATK_FONT_SIZE);
    }

    private String removeAlpha(String color) {
        char[] colorArray = color.toCharArray();
        if(colorArray[1]=='0' && colorArray[2]=='0')return "#FFFFFFFF";
        colorArray[1]='F';
        colorArray[2]='F';
        System.out.println(String.valueOf(colorArray));
        return String.valueOf(colorArray);
    }

    @Override
    public void drawOn(Canvas c, Paint p) {
        //draw the frame
        if(onBoard)
        {
            cardOnBardDrawable.drawOn(c,p);
            PictureDrawable.drawOn(c,p);


            cardHpDrawable.drawOn(c,p);
            cardAtkDrawable.drawOn(c,p);
        }else{
            super.drawOn(c, p);
            OpacityRectangleDrawable.drawOn(c,p);
            PictureDrawable.drawOn(c,p);

            //dessine le texte
            cardDescription.drawOn(c,p);
            cardHpDrawable.drawOn(c,p);
            cardAtkDrawable.drawOn(c,p);
        }

    }

    @Override
    public void setCoordinates(float x, float y) {
        if (onBoard) {
            cardOnBardDrawable.setCoordinates(x,y);
            PictureDrawable.setCoordinates(x +1, y +2);
            cardAtkDrawable.setCoordinates(getX()+0.6F,getY()+CARD_HEIGHT-5F);
            cardHpDrawable.setCoordinates(getX()+2.4F+CARD_WITH/2,getY()+CARD_HEIGHT-5F);
        }else{
            super.setCoordinates(x, y);
            OpacityRectangleDrawable.setCoordinates(x, y);
            PictureDrawable.setCoordinates(x + 1.1F, y + 2.5F);
            cardDescription.setCoordinates(x+CARD_WITH*0.1F,y+CARD_HEIGHT/2);
            cardAtkDrawable.setCoordinates(x+1F,getY()+CARD_HEIGHT-5F);
            cardHpDrawable.setCoordinates(x+1F+CARD_WITH/2,getY()+CARD_HEIGHT-5F);
        }
    }

    public void setOnBoard(Boolean onBoard)
    {
        this.onBoard = onBoard;
        //force recalculate coodinates
        setCoordinates(getX(), getY());
    }
}
