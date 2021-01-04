package com.iutlaval.myapplication.Video.Drawables;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.Log;

import com.iutlaval.myapplication.GameActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * ce code a éte particuliérement dur a implementé de maniére correct
 * tout modification de ce code doit passer par marc car seul moi sait a quel point il est complex
 */
public class DrawableText extends Drawable{


    public DrawableText(String text,float x_pos,float y_pos,String name,float textSize , float x_size,int color)
    {
        this(text,x_pos,y_pos,name,textSize,x_size,color,20);
    }

    /**
     * ce constructeur permet de créer un text un une ligne avec moin d'argument
     * @param text
     * @param x_pos
     * @param y_pos
     * @param name
     * @param textSize
     */
    public DrawableText(String text,float x_pos,float y_pos,String name,float textSize , float x_size,int color,int nbcharPerlines)
    {
        super(x_pos,y_pos,name);


        TextPaint p = new TextPaint();
        p.setAntiAlias(true);
        p.setTextSize(textSize);
        p.setColor(color);

        int canvas_width = (int) p.measureText(text);
        int canvas_height = (int) textSize;


        Bitmap bitmap = Bitmap.createBitmap(canvas_width, canvas_height, Bitmap.Config.ARGB_8888);
        setBitmap(bitmap);
        Canvas c = new Canvas(bitmap);

        //si on a une description
        int index = 1;
        List<String> linesOfText = cutText(nbcharPerlines,text);
        for(String line : linesOfText)
        {
            //y_canvasRatio/linesOfText.size()
            c.drawText(line ,0,index*textSize,p);
            index++;
        }

        int scalled_x_size = (int) (GameActivity.screenWidth*x_size/100);

        int y_size = (int) (x_size/(canvas_width * 1.0) * 200 * linesOfText.size());

        int scalled_y_size = GameActivity.screenHeight*y_size/100;

        setBitmap(Bitmap.createScaledBitmap(bitmap,scalled_x_size,scalled_y_size,GameActivity.bilinearFiltering));
    }

    /**
     * ce constructeur permet de rendre du texte
     * @param text le texte a rendre
     * @param x_pos position en x du texte
     * @param y_pos position en y du texte
     * @param name nom du Drawable
     * @param x_size taille du drawable entre 0 et 100
     * @param y_size taille du drawable entre 0 et 100
     * @param y_canvasRatio taille du canevas en pixel sur le quel le texte va être dessiné /!\ doit être proportionnel a la police
     * @param x_canvasRatio voire y_canvasRatio
     */
    public DrawableText(String text,float x_pos,float y_pos,String name, float x_size,float y_size,float textSize , int x_canvasRatio, int y_canvasRatio,int color)
    {
        this(text,x_pos,y_pos,name,x_size,y_size,textSize,x_canvasRatio,y_canvasRatio,color,20);
    }

    /**
     * ce constructeur permet de rendre du texte
     * @param text le texte a rendre
     * @param x_pos position en x du texte
     * @param y_pos position en y du texte
     * @param name nom du Drawable
     * @param x_size taille du drawable entre 0 et 100
     * @param y_size taille du drawable entre 0 et 100
     * @param y_canvasRatio taille du canevas en pixel sur le quel le texte va être dessiné /!\ doit être proportionnel a la police
     * @param x_canvasRatio voire y_canvasRatio
     */
    public DrawableText(String text,float x_pos,float y_pos,String name, float x_size,float y_size,float textSize , int x_canvasRatio, int y_canvasRatio,int color,int nbcharPerlines)
    {
        super(x_pos,y_pos,name);

        checkPaint();
        p.setColor(color);
        p.setAntiAlias(true);
        p.setTextSize(textSize);

        Bitmap bitmap = Bitmap.createBitmap(x_canvasRatio, y_canvasRatio, Bitmap.Config.ARGB_8888);
        setBitmap(bitmap);
        Canvas c = new Canvas(bitmap);

        //si on a une description
        int index = 1;
        List<String> linesOfText = cutText(nbcharPerlines,text);
        for(String line : linesOfText)
        {
            //y_canvasRatio/linesOfText.size()
            c.drawText(line ,0,index*textSize,p);
            index++;
        }


        int scalled_x_size = (int)(GameActivity.screenWidth*x_size/100);
        int scalled_y_size = (int)(GameActivity.screenHeight*y_size/100);

        setBitmap(Bitmap.createScaledBitmap(bitmap,scalled_x_size,scalled_y_size,GameActivity.bilinearFiltering));
    }

    /**
     * coupe le texte a une certaine longeur
     * @param charPerLines nombre de caractére par lignes
     * @param text texte a afficher
     * @return liste de lignes de texte
     */
    private static List<String> cutText(int charPerLines,String text)
    {
        List<String> output = new ArrayList<>();

        //cut text in lines
        StringBuilder nextLine = new StringBuilder(charPerLines);
        for (String word : text.split(" ")) {
            if(word.length() < charPerLines)
            {
                if(word.length() + nextLine.toString().length() +1 < charPerLines)
                {
                    nextLine.append(" " + word);
                }else{
                    //fillWithSpace(nextLine,charPerLines);
                    output.add(nextLine.toString());
                    nextLine.delete(0,charPerLines-1);
                    nextLine.append(" " + word);
                }
            }else{

                if(!nextLine.toString().isEmpty())output.add(nextLine.toString());
                nextLine.delete(0,charPerLines-1);
                output.add(word);
            }
        }
        if(!nextLine.toString().equals(""))
        {
            //fillWithSpace(nextLine,charPerLines);
            output.add(nextLine.toString());
        }
        return output;
    }
}
