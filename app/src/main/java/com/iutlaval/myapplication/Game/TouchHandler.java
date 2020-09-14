package com.iutlaval.myapplication.Game;

import android.view.MotionEvent;

import com.iutlaval.myapplication.GameActivity;
import com.iutlaval.myapplication.Video.Drawables.DrawableCard;
import com.iutlaval.myapplication.Video.Renderer;

public class TouchHandler {

    //TODO mettre sa dans une classe isolé
    private float deltaX = 0;
    private float deltaY = 0;
    private DrawableCard moveEventCard = null;
    private Renderer renderer;

    public TouchHandler(Renderer renderer)
    {
        this.renderer = renderer;
    }


    public void onTouchEvent(MotionEvent event){

        if(event.getPointerCount() <= 1)
        {
            float unscalled_X = event.getX() / GameActivity.screenWidth * 100;
            float unscalled_Y = event.getY() / GameActivity.screenHeight * 100;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                moveEventCard = renderer.getCardOn(unscalled_X, unscalled_Y);
                if(moveEventCard != null) {
                    deltaY = unscalled_Y - moveEventCard.getY();
                    deltaX = unscalled_X - moveEventCard.getX();
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                moveEventCard = null;
                deltaY = 0;
                deltaX = 0;
            } else if (moveEventCard != null) {
                moveEventCard.setCoordinates(unscalled_X - deltaX, unscalled_Y - deltaY);
            }
        }
    }
}
