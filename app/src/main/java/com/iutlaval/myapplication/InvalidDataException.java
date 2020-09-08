package com.iutlaval.myapplication;


import com.iutlaval.myapplication.Video.Rectangle;

public class InvalidDataException extends Throwable {
    private String name;
    private float x;
    private float y;

    public InvalidDataException(String name, Rectangle rectangle)
    {
        x = rectangle.getWidth();
        y = rectangle.getHeight();
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
