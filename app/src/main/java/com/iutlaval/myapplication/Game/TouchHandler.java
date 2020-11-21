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
    private DrawableCard smallCardUsedForBig = null;
    private DrawableCard cardSeletedInHand = null;
    private static final float SELECTING_OFFSET = 10F;
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

    /**
     * cette evenement doit recevoir le onTouchEvent d'une activité
     * @param event
     */
    public void onTouchEvent(MotionEvent event)
    {
        //on ignore le multi touch
        if(event.getPointerCount() <= 1)
        {
            handSelectionHandler(event);
            //afficher grosse carte
            bigCardHandler(event);
            if(gameLogic.isYourTurn())
            {
                //selectionne la carte
                dragAndDropHandler(event);
                //affiche les zones de jeu
                playCard(event);

            }
        }
    }

    /**
     * gére le drag and drop de carte
     * @param event
     */
    private void dragAndDropHandler(MotionEvent event)
    {
        //on calcul les coordonées en % de l'écran
        float unscaled_X = event.getX() / GameActivity.screenWidth * 100;
        float unscaled_Y = event.getY() / GameActivity.screenHeight * 100;

        //quand on appui on capture une carte et on note sa positon initial
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            dragAndDropCard = renderer.getCardOn(unscaled_X, unscaled_Y);

            if(dragAndDropCard != null && !dragAndDropCard.isDraggable()) dragAndDropCard = null;

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

            //dragAndDropCard = null;
        } else if (dragAndDropCard != null) {
            dragAndDropCard.setCoordinates(unscaled_X - TouchDeltaX, unscaled_Y - TouchDeltaY);
            renderer.updateFrame();
        }
    }

    /**
     * affiche une grosse carte carte quand une carte est sélectionner
     * @param event
     */
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
                if(smallCardUsedForBig !=null)
                {
                    //on update pas affin d'avoir un rendu plus fluide au moment de mettre l'aure carte
                    renderer.removeToDrawWithoutUpdate(smallCardUsedForBig.getName()+"BIG");
                }
                smallCardUsedForBig = smallCArd;
                DrawableCard bigCard = new DrawableCard(smallCArd,context,2);
                renderer.addToDraw(bigCard);
                bigCard.setCoordinates(72F,10F);
                //on sélectionne si elle est dans notre main(déplacer la care vers le haut de quelque pixel)

            //si on lache la carte
            }
        }else if(event.getAction() == MotionEvent.ACTION_UP) {
            if (smallCardUsedForBig != null) {
                renderer.removeToDraw(smallCardUsedForBig.getName() + "BIG");
                smallCardUsedForBig = null;
            }
        }
    }

    /**
     * gére le placement des cartes sur le terrain
     * @param event
     */
    private void playCard(MotionEvent event)
    {
        if(dragAndDropCard != null){
            //l'affichage se fait par une liste fifo donc on veut que notre carte soit afficher aprés les zones
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                //on enleve l carte
                renderer.removeToDrawWithoutUpdate(dragAndDropCard);
                //on ajoute les zones
                playableZonesHandler.displayPlayableZones(renderer);
                // et enfin on remet la carte
                renderer.addToDraw(dragAndDropCard);

            }else if(event.getAction() == MotionEvent.ACTION_UP) {
                int zone = playableZonesHandler.getHoveredZone(dragAndDropCard);
                if(zone != -1)
                {
                    dragAndDropCard.setOnBoard(true);
                    dragAndDropCard.setCoordinates((DrawableCard.getCardWith()+1)*zone,55F);
                    dragAndDropCard.setDraggable(false);
                    gameLogic.onCardPlayed(dragAndDropCard,zone);
                }else{
                    if(dragAndDropCard != null)renderer.moveToDraw(originalPositionX,originalPositionY, dragAndDropCard.getName());
                }
                playableZonesHandler.hidePlayableZones(renderer);
            }
        }

    }

    private void handSelectionHandler(MotionEvent event)
    {
        float unScalled_Y = event.getY() / GameActivity.screenHeight * 100;
        float unScalled_X = event.getX() / GameActivity.screenWidth * 100;
        DrawableCard smallCArd = renderer.getCardOn(unScalled_X,unScalled_Y);
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if(cardSeletedInHand != null && !cardSeletedInHand.isOnBoard()){
                cardSeletedInHand.setCoordinates(cardSeletedInHand.getX(), cardSeletedInHand.getY() + SELECTING_OFFSET);
                cardSeletedInHand=null;
                renderer.updateFrame();
            }

            if(unScalled_Y > 90F && smallCArd !=null && smallCArd.isDraggable() && !smallCArd.isOnBoard() && (cardSeletedInHand == null || !cardSeletedInHand.equals(smallCArd)))
            {
                smallCArd.setCoordinates(smallCArd.getX(),smallCArd.getY() - SELECTING_OFFSET);
                cardSeletedInHand=smallCArd;
                renderer.updateFrame();
            }
        }
    }
}
