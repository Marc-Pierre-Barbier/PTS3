package com.iutlaval.myapplication.Game;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.MotionEvent;

import com.iutlaval.myapplication.GameActivity;
import com.iutlaval.myapplication.R;
import com.iutlaval.myapplication.Video.Drawables.DrawableBitmap;
import com.iutlaval.myapplication.Video.Drawables.DrawableCard;
import com.iutlaval.myapplication.Video.Renderer;

/**
 * NOT A MES COLEGUES : n'optimiser pas cette classe elle est sub optimal c'est évident je le sais MAIS
 * la compréhention du code deviendera trés difficile si on optimise
 */
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
    private float unScalled_X;
    private float unScalled_Y;

    public TouchHandler(Renderer renderer, GameLogicThread gameLogic, Context context)
    {
        playableZonesHandler = new PlayableZonesHandler(gameLogic.getBoard());
        this.renderer = renderer;
        this.gameLogic = gameLogic;
        this.context=context;
    }

    /**
     * cette evenement doit recevoir le onTouchEvent d'une activité
     * cette fonction gére l'integralité du tactile de l'application elle est divisé en de nombreuses sous fonction
     * @param event
     */
    public void onTouchEvent(MotionEvent event)
    {
        unScalled_X = event.getX() / GameActivity.screenWidth * 100;
        unScalled_Y = event.getY() / GameActivity.screenHeight * 100;
        //on ignore le multi touch
        if(event.getPointerCount() <= 1)
        {
            endTurnButtonHandle(event);
            handSelectionHandler(event);
            //afficher grosse carte
            bigCardHandler(event);
            if(gameLogic.isYourMainPhase() && ! gameLogic.isYourBattlePhase())
            {
                //selectionne la carte dans la main
                dragAndDropHandler(event,false);
                //affiche les zones de jeu
                playCard(event);

            }
            if(gameLogic.isYourBattlePhase() && ! gameLogic.isYourMainPhase())
            {
                //selectionne la carte sur le terrain
                dragAndDropHandler(event,true);

                attackHandler(event);
            }
        }
    }

    /**
     * gére le drag and drop de carte
     * @param event
     */
    private void dragAndDropHandler(MotionEvent event,boolean isBattleTurn)
    {
        //on calcul les coordonées en % de l'écran


        //quand on appui on capture une carte et on note sa positon initial
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            dragAndDropCard = renderer.getCardOn(unScalled_X, unScalled_Y);

            if(dragAndDropCard != null && !dragAndDropCard.isDraggable()) dragAndDropCard = null;

            //si la carte est dans la main durant la phase de combat on ne la prend pas
            if(dragAndDropCard != null && isBattleTurn && !dragAndDropCard.isOnBoard())dragAndDropCard = null;
            //si la carte est sur le terrain durant la phase principale on ne la prends pas
            if(dragAndDropCard != null && !isBattleTurn && dragAndDropCard.isOnBoard())dragAndDropCard = null;

            if (dragAndDropCard != null) {
                //on calcul la position par raport au doigt de l'utilisateur
                TouchDeltaY = unScalled_Y - dragAndDropCard.getY();
                TouchDeltaX = unScalled_X - dragAndDropCard.getX();
                originalPositionX = dragAndDropCard.getX();
                originalPositionY = dragAndDropCard.getY();
            }

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            TouchDeltaY = 0;
            TouchDeltaX = 0;

            //dragAndDropCard = null;
        } else if (dragAndDropCard != null) {
            dragAndDropCard.setCoordinates(unScalled_X - TouchDeltaX, unScalled_Y - TouchDeltaY);
            renderer.updateFrame();
        }
    }

    /**
     * affiche une grosse carte carte quand une carte est sélectionner
     * @param event
     */
    private void bigCardHandler(MotionEvent event)
    {
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
                bigCard.setDraggable(false);
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
                if(zone != -1 && gameLogic.setOnCardPlayedRequest(dragAndDropCard,zone))
                {
                    dragAndDropCard.setOnBoard(true);
                    dragAndDropCard.setCoordinates((DrawableCard.getCardWith()+1)*zone,55F);
                }else{
                    if(dragAndDropCard != null)renderer.moveToDraw(originalPositionX,originalPositionY, dragAndDropCard.getName());
                }
                playableZonesHandler.hidePlayableZones(renderer);
            }
        }

    }

    /**
     * gére la selection de carte dans la main
     * @param event
     */
    private void handSelectionHandler(MotionEvent event)
    {
        DrawableCard smallCArd = renderer.getCardOn(unScalled_X,unScalled_Y);
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if(cardSeletedInHand != null && !cardSeletedInHand.isOnBoard()){
                cardSeletedInHand.setCoordinates(cardSeletedInHand.getX(), cardSeletedInHand.getY() + SELECTING_OFFSET);
                cardSeletedInHand=null;
                renderer.updateFrame();
            }

            if(unScalled_Y > 90F && smallCArd !=null  && !smallCArd.isOnBoard() && (cardSeletedInHand == null || !cardSeletedInHand.equals(smallCArd)))
            {
                smallCArd.setCoordinates(smallCArd.getX(),smallCArd.getY() - SELECTING_OFFSET);
                cardSeletedInHand=smallCArd;
                renderer.updateFrame();
            }
        }
    }

    /**
     * gére le button de fin de tours
     * @param event
     */
    private void endTurnButtonHandle(MotionEvent event)
    {
        //si on appuie
        if(event.getAction() == MotionEvent.ACTION_DOWN &&
                //si on est sur le boutton
                unScalled_X > GameLogicThread.BUTTON_X_POS && unScalled_X < GameLogicThread.BUTTON_X_POS + GameLogicThread.BUTTON_X_SIZE &&
                unScalled_Y > GameLogicThread.BUTTON_Y_POS && unScalled_Y < GameLogicThread.BUTTON_Y_POS + GameLogicThread.BUTTON_Y_SIZE)
        {
            gameLogic.requestEndTurn();
        }
    }

    private void attackHandler(MotionEvent event)
    {
        if(dragAndDropCard != null){
            if(event.getAction() == MotionEvent.ACTION_DOWN)
                renderer.addToDraw(new DrawableBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.t_s_fleche),2F,20F,"arrow",14F,25F));

            if(event.getAction() == MotionEvent.ACTION_UP) {
                renderer.removeToDraw("arrow");
                int zone = playableZonesHandler.getEnemyHoveredZone(dragAndDropCard);
                if(zone != -1)
                {
                    gameLogic.setOnCardAttackRequest(dragAndDropCard,zone);
                }
                renderer.moveToDraw(originalPositionX,originalPositionY, dragAndDropCard.getName());
                playableZonesHandler.hidePlayableZones(renderer);
            }
        }
    }
}
