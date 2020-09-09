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

    /**
     * retourne un string detaillant l'erreur
     * @return
     */
    public String getDetail() {
        return "name " + name + " x:" + x +" y:"+ y;
    }
}
