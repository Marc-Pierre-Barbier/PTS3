package com.iutlaval.myapplication.Game;

import android.graphics.Color;

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
    //90F au lieu de 100F car on n'a pas la pioche
    private static final float CARD_SPACING = 90F / Board.MAX_CARD_ON_BOARD;
    private List<Drawable> playableZones;
    private Board board;

    public PlayableZonesHandler(Board board)
    {
        playableZones=new ArrayList<>();
        this.board=board;
    }

    public void displayPlayableZones(Renderer renderer)
    {
        Card[] cardsOnBoard = board.getPlayerCardsOnBoard();
        for(int i=0 ; i < cardsOnBoard.length ;i++)
        {
            if(cardsOnBoard[i] == null)
            {
                displayPlayableZoneOn(i,renderer);
            }
        }
    }

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

    public void HidePlayableZones(Renderer renderer)
    {
        for(Drawable d : playableZones)
        {
            renderer.removeToDraw(d);
        }
        playableZones.clear();
    }
}
