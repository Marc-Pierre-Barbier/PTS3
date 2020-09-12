package com.iutlaval.myapplication.Video.Drawables;

public class DrawableCardTitle extends Drawable{
    private static int TEXT_X_RES = 281;
    private static int TEXT_Y_RES = 60;

    public DrawableCardTitle(String text, float x_pos, float y_pos, String name, float x_size, float y_size, float textSize) {
        super(text,x_pos,y_pos,name,x_size,y_size,textSize,TEXT_X_RES,TEXT_Y_RES);
    }
}
