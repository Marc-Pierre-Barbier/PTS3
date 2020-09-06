package com.iutlaval.myapplication;

import android.graphics.Rect;

public class InvalidDataException extends Throwable {
    private String name;
    private float x;
    private float y;

    public InvalidDataException(String name, Rect rectangle)
    {
        x = rectangle.width();
        y = rectangle.height();
        this.name=name;
    }
    public InvalidDataException(String name,float x,float y)
    {
        this.y=y;
        this.x=x;
    }

    public String getDetail() {
        return "name " + name + " x:" + x +" y:"+ y;
    }
}
