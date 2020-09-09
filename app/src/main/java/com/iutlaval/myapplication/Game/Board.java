package com.iutlaval.myapplication.Game;

import com.iutlaval.myapplication.Game.Cards.Card;
import com.iutlaval.myapplication.Game.Player.Player;
import com.iutlaval.myapplication.Game.Player.PlayerLocal;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private static final int MAX_CARD_ON_BOARD=10;
    private List<Card> advCardsOnBoard;
    private List<Card> playerCardsOnBoard;

    public Board()
    {
        advCardsOnBoard = new ArrayList<>();
        playerCardsOnBoard = new ArrayList<>();
    }

    /**
     * joue une carte et l'ajoute au terrain du joueur correspondant
     * @param card
     * @param player
     */
    public void playCard(Card card,Player player)
    {
        if(player instanceof PlayerLocal)
        {
            playerCardsOnBoard.add(card);
        }else{
            advCardsOnBoard.add(card);
        }
        card.onCardPlayed(this,player);
    }
}
