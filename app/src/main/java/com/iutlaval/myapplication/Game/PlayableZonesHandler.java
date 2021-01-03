package com.iutlaval.myapplication.Game;

import android.graphics.Color;
import android.util.Log;

import com.iutlaval.myapplication.Game.Cards.Card;
import com.iutlaval.myapplication.InvalidDataException;
import com.iutlaval.myapplication.Video.Drawables.Drawable;
import com.iutlaval.myapplication.Video.Drawables.DrawableCard;
import com.iutlaval.myapplication.Video.Drawables.DrawableRectangle;
import com.iutlaval.myapplication.Video.Rectangle;
import com.iutlaval.myapplication.Video.Renderer;

import java.util.ArrayList;
import java.util.List;


public class PlayableZonesHandler {
    private List<Drawable> playableZones;
    private boolean isDisplayed;
    private Board board;

    public PlayableZonesHandler(Board board)
    {
        playableZones=new ArrayList<>();
        this.board=board;
        isDisplayed=false;
    }

    /**
     * affiche les zones de jeu libre
     * @param renderer
     */
    public void displayPlayableZones(Renderer renderer)
    {
        //on affiche pas si elle se sont deja
        if(isDisplayed)return;
        isDisplayed=true;

        Card[] cardsOnBoard = board.getPlayerCardsOnBoard();
        for(int i=0 ; i < cardsOnBoard.length ;i++)
        {
            if(cardsOnBoard[i] == null)
            {
                displayPlayableZoneOn(i,renderer);
            }
        }

    }

    /**
     * affiche un rectangle gris représentant une zone jouable en la case i
     * @param i car sur la quelle afficher la zone
     * @param renderer
     */
    private void displayPlayableZoneOn(int i,Renderer renderer) {
        Rectangle pos = new Rectangle((DrawableCard.getCardWith()+1)*i,50F,DrawableCard.getCardWith(),DrawableCard.getCardHeight());

        try {
            Drawable zone = new DrawableRectangle(pos,"greySpace"+i, Color.LTGRAY);
            playableZones.add(zone);
            renderer.addToDrawWithoutUpdate(zone);
        } catch (InvalidDataException e) {
            e.printStackTrace();
        }

    }

    /**
     * cache les zones de jeu
     * @param renderer
     */
    public void hidePlayableZones(Renderer renderer)
    {
        if(!isDisplayed)return;
        for(Drawable d : playableZones)
        {
            renderer.removeToDraw(d);
        }
        playableZones.clear();
        isDisplayed=false;
    }

    /**
     * retourne le numero de la zone survolé par le drawable si la zone est vite
     * sinon retourne -1
     * @param card
     * @return
     */
    public int getHoveredZone(Drawable card) {
        for(int i=0 ; i< board.MAX_CARD_ON_BOARD;i++)
        {
            int cardX =(DrawableCard.getCardWith()+1)*i+1;
            //es que on survole sur l'axe x
            if(cardX > card.getX() && card.getY() < 75F/*90F orrigine */ && card.getY() > 50F)
            {
                //on a trouvé l'emplacement de la carte
                //si il y a une carte a l'emplacement on retourne le numero
                if(board.getPlayerCardsOnBoard()[i] == null)return i;
                else break;
            }
        }
        return -1;
    }

    /**
     * retourne le numero de la zone survolé par le drawable si la zone est vite
     * sinon retourne -1
     * @param card
     * @return
     */
    public int getEnemyHoveredZone(Drawable card) {
        for(int i=0 ; i< board.MAX_CARD_ON_BOARD;i++)
        {
            int cardX =(DrawableCard.getCardWith()+1)*i + 20;
            //es que on survole sur l'axe x
            if(cardX > card.getX() && card.getY() < 50F)
            {
                //on a trouvé l'emplacement de la carte
                //si il y a une carte a l'emplacement on retourne le numero
                if(board.getPlayerCardsOnBoard()[i] == null)return i;
                else break;
            }
        }
        return -1;
    }

    //cardPlayed.getDrawableCard().setCoordinates(((DrawableCard.getCardWith()+1)*zone+DrawableCard.getCardWith()),10F);
}
