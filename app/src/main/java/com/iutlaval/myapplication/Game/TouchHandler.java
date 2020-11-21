package com.iutlaval.myapplication.Game;

import android.content.Context;
import android.view.MotionEvent;

import com.iutlaval.myapplication.GameActivity;
import com.iutlaval.myapplication.Video.Drawables.DrawableCard;
import com.iutlaval.myapplication.Video.Renderer;

public class TouchHandler {

    private float TouchDeltaX = 0;
    private float TouchDeltaY = 0;
    private float originalPositionX = 0;
    private float originalPositionY = 0;
    private DrawableCard dragAndDropCard = null;
    private DrawableCard smallCardUserForBig = null;
    private static final float SELECTING_OFFSET = 20F;
    private PlayableZonesHandler playableZonesHandler;

    private Renderer renderer;
    private GameLogicThread gameLogic;
    private Context context;

    public TouchHandler(Renderer renderer, GameLogicThread gameLogic, Context context)
    {
        playableZonesHandler = new PlayableZonesHandler(gameLogic.getBoard());
        this.renderer = renderer;
        this.gameLogic = gameLogic;
        this.context=context;
    }


    public void onTouchEvent(MotionEvent event)
    {
        //on ignore le multi touch
        if(event.getPointerCount() <= 1)
        {
            if(gameLogic.isYourTurn())
            {
                //l'ordre importe on a besoin d'avoir une carte sélectionner pour la jouer
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    //selectionne la carte
                    dragAndDropHandler(event);
                    //affiche les zones de jeu
                    playCard(event);
                }else{
                    //joue la carte si on a ACTION_UP
                    playCard(event);
                    //déplace la carte(non ACTION_UP) et la désélection (ACTION_UP)
                    dragAndDropHandler(event);
                }

            }
            //afficher grosse carte
            bigCardHandler(event);

        }
    }


    private void dragAndDropHandler(MotionEvent event)
    {
        //on calcul les coordonées en % de l'écran
        float unscaled_X = event.getX() / GameActivity.screenWidth * 100;
        float unscaled_Y = event.getY() / GameActivity.screenHeight * 100;

        //quand on appui on capture une carte et on note sa positon initial
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            dragAndDropCard = renderer.getCardOn(unscaled_X, unscaled_Y);

            if(!dragAndDropCard.isDraggable()) dragAndDropCard = null;

            if (dragAndDropCard != null) {
                //on calcul la position par raport au doigt de l'utilisateur
                TouchDeltaY = unscaled_Y - dragAndDropCard.getY();
                TouchDeltaX = unscaled_X - dragAndDropCard.getX();
                originalPositionX = dragAndDropCard.getX();
                originalPositionY = dragAndDropCard.getY();
            }

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            TouchDeltaY = 0;
            TouchDeltaX = 0;
            //TODO detect zones
            if(dragAndDropCard != null)renderer.moveToDraw(originalPositionX,originalPositionY, dragAndDropCard.getName());
            dragAndDropCard = null;
        } else if (dragAndDropCard != null) {
            dragAndDropCard.setCoordinates(unscaled_X - TouchDeltaX, unscaled_Y - TouchDeltaY);
            renderer.updateFrame();
        }
    }

    private void bigCardHandler(MotionEvent event)
    {
        //calcul y,x sur une echelle de 0 a 100
        float unScalled_Y = event.getY() / GameActivity.screenHeight * 100;
        float unScalled_X = event.getX() / GameActivity.screenWidth * 100;

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            //on recupére la carte a afficher en grand
            DrawableCard smallCArd = renderer.getCardOn(unScalled_X,unScalled_Y);
            if(smallCArd !=null)
            {
                if(smallCardUserForBig !=null)
                {
                    smallCardUserForBig.setCoordinates(smallCardUserForBig.getX(), smallCardUserForBig.getY() + SELECTING_OFFSET);
                    //on update pas affin d'avoir un rendu plus fluide au moment de mettre l'aure carte
                    renderer.removeToDrawWithoutUpdate(smallCardUserForBig.getName()+"BIG");
                }
                smallCardUserForBig = smallCArd;
                DrawableCard bigCard = new DrawableCard(smallCArd,context,2);
                renderer.addToDraw(bigCard);
                bigCard.setCoordinates(72F,10F);
                //on ne décale pas la carte sélectionner si ce n'est pas notre tours
                if(gameLogic.isYourTurn())smallCArd.setCoordinates(smallCArd.getX(),smallCArd.getY() - SELECTING_OFFSET);
            //si on lache la carte
            }else if(event.getAction() == MotionEvent.ACTION_UP) {
                if (smallCardUserForBig != null) {
                    renderer.removeToDraw(smallCardUserForBig.getName() + "BIG");
                    smallCardUserForBig = null;
                }
            }
        }
    }

    private void playCard(MotionEvent event)
    {
        //l'affichage se fait par une liste fifo donc on veut que notre carte soit afficher aprés les zones
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            //on enleve l carte
            renderer.removeToDrawWithoutUpdate(dragAndDropCard);
            //on ajoute les zones
            playableZonesHandler.displayPlayableZones(renderer);
            // et enfin on remet la carte
            renderer.addToDraw(dragAndDropCard);

        }else if(event.getAction() == MotionEvent.ACTION_UP) {
            int zone = playableZonesHandler.getHoveredZone(smallCardUserForBig);
            if(zone != -1)
            {
                dragAndDropCard.setOnBoard(true);
                dragAndDropCard.setCoordinates((DrawableCard.getCardWith()+1)*zone,55F);
                dragAndDropCard.setDraggable(false);
            }else{
                dragAndDropCard.setCoordinates(dragAndDropCard.getX(),dragAndDropCard.getY() + SELECTING_OFFSET);
            }
        }
    }

}
