package com.iutlaval.myapplication.Game;

import com.iutlaval.myapplication.Game.Cards.Card;
import com.iutlaval.myapplication.Game.Players.Player;
import com.iutlaval.myapplication.Game.Players.PlayerLocal;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int MAX_CARD_ON_BOARD=5;
    private Card[] advCardsOnBoard;
    private Card[] playerCardsOnBoard;

    public Board()
    {
        advCardsOnBoard = new Card[MAX_CARD_ON_BOARD];
        playerCardsOnBoard = new Card[MAX_CARD_ON_BOARD];
    }

    /**
     * joue une carte et l'ajoute au terrain du joueur correspondant
     * si retourne faux alors la carte n'a pas éte joué
     * @param card
     * @param player
     * @return success/failure
     */
    public boolean playCard(Card card,Player player)
    {
        boolean returnvalue = false;
        if(player instanceof PlayerLocal)
        {
            for(Card c : playerCardsOnBoard)
            {
                if(c == null)
                {
                    c=card;
                    returnvalue=true;
                    break;
                }
            }
        }else{
            for(Card c : advCardsOnBoard)
            {
                if(c == null)
                {
                    c=card;
                    returnvalue=true;
                    break;
                }
            }
        }
        card.onCardPlayed(this,player);
        return returnvalue;
    }

    public Card[] getPlayerCardsOnBoard() {
        return playerCardsOnBoard;
    }
}
