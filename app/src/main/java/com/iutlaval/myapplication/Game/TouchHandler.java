package com.iutlaval.myapplication.Game;

import android.content.Intent;
import android.view.MotionEvent;

import com.iutlaval.myapplication.GameActivity;
import com.iutlaval.myapplication.MainActivity;
import com.iutlaval.myapplication.Video.Drawables.DrawableCard;
import com.iutlaval.myapplication.Video.Renderer;

public class TouchHandler {

    private float TouchDeltaX = 0;
    private float TouchDeltaY = 0;
    private float originalPositionX = 0;
    private float originalPositionY = 0;
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
                if(unscalled_Y <= 90F) {
                    moveEventCard = renderer.getCardOn(unscalled_X, unscalled_Y);
                    if (moveEventCard != null) {
                        TouchDeltaY = unscalled_Y - moveEventCard.getY();
                        TouchDeltaX = unscalled_X - moveEventCard.getX();
                        originalPositionX = moveEventCard.getX();
                        originalPositionY = moveEventCard.getY();
                    }
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                TouchDeltaY = 0;
                TouchDeltaX = 0;
                //TODO detect zones
                if(moveEventCard != null)renderer.moveToDraw(originalPositionX,originalPositionY,moveEventCard.getName());
                moveEventCard = null;
            } else if (moveEventCard != null) {
                moveEventCard.setCoordinates(unscalled_X - TouchDeltaX, unscalled_Y - TouchDeltaY);
                renderer.updateFrame();
            }
        }
    }
}
