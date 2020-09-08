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

    private String color;

    private Drawable OpacityRectangleDrawable;
    private Drawable PictureDrawable;

    public DrawableCard(Card c, float x, float y, String name, Context context) throws InvalidDataException {
        super(c.getFrameBitmap(context),x,y,name,CARD_WITH,CARD_HEIGHT);

        color = c.getColor();//CARD_WITH+x
        Rectangle cardRect = new Rectangle(x,y,(float)(CARD_WITH+x),(float)(CARD_HEIGHT+x));
        OpacityRectangleDrawable = new Drawable(cardRect,toString()+"Opacity", Color.parseColor(c.getColor()));

        Bitmap pictureBitmap = BitmapFactory.decodeResource(context.getResources(),c.getCardPicture());
        PictureDrawable = new Drawable(pictureBitmap,getX()+1.1F,getY()+2.5F,toString()+"Picture",CARD_WITH-2.4F,CARD_HEIGHT/2 - 4);
    }

    @Override
    public void drawOn(Canvas c, Paint p) {
        //draw the frame
        super.drawOn(c, p);
        OpacityRectangleDrawable.drawOn(c,p);
        PictureDrawable.drawOn(c,p);
    }
}
