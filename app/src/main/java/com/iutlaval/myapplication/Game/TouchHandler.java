package com.iutlaval.myapplication.Game;

import android.view.MotionEvent;

import com.iutlaval.myapplication.GameActivity;
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
        //on ignore le multi touch
        if(event.getPointerCount() <= 1)
        {
            float unscaled_X = event.getX() / GameActivity.screenWidth * 100;
            float unscaled_Y = event.getY() / GameActivity.screenHeight * 100;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if(unscaled_Y <= 90F)
                {
                    moveEventCard = renderer.getCardOn(unscaled_X, unscaled_Y -20F);
                }else{
                    moveEventCard = renderer.getCardOn(unscaled_X, unscaled_Y);
                }

                if (moveEventCard != null) {
                    TouchDeltaY = unscaled_Y - moveEventCard.getY();
                    TouchDeltaX = unscaled_X - moveEventCard.getX();
                    originalPositionX = moveEventCard.getX();
                    originalPositionY = moveEventCard.getY();
                    }

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                TouchDeltaY = 0;
                TouchDeltaX = 0;
                //TODO detect zones
                if(moveEventCard != null)renderer.moveToDraw(originalPositionX,originalPositionY,moveEventCard.getName());
                moveEventCard = null;
            } else if (moveEventCard != null) {
                moveEventCard.setCoordinates(unscaled_X - TouchDeltaX, unscaled_Y - TouchDeltaY);
                renderer.updateFrame();
            }
        }
    }
}
