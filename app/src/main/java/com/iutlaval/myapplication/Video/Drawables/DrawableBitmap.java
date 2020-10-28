package com.iutlaval.myapplication.Video.Drawables;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.iutlaval.myapplication.ERROR_CODE;
import com.iutlaval.myapplication.GameActivity;
import com.iutlaval.myapplication.InvalidDataException;

public class DrawableBitmap extends Drawable{
    /**
     * @param  bitmap a dessiner
     * @param x_pos position en x du texte
     * @param y_pos position en y du texte
     * @param name nom du Drawable
     * @param x_size taille du drawable entre 0 et 100
     * @param y_size taille du drawable entre 0 et 100
     * @throws InvalidDataException
     */
    public DrawableBitmap(@NonNull Bitmap bitmap, float x_pos, float y_pos, String name, float x_size, float y_size){
        super(x_pos,y_pos,name);

        float x_scaled_size = x_size* GameActivity.screenWidth/100;
        float y_scaled_size = y_size* GameActivity.screenHeight/100;

        if(x_scaled_size <=0 || y_scaled_size <=0)
        {
            Log.e("DRAWABLE_TEXTURE :","ERREUR DE MISE A L'ECHELLE");
            System.exit(ERROR_CODE.ERROR_TEXTURE_DIMENSIONS_INVALID.ordinal());
        }

        setBitmap(Bitmap.createScaledBitmap(bitmap, (int)x_scaled_size, (int)y_scaled_size, GameActivity.bilinearFiltering));
    }
}
