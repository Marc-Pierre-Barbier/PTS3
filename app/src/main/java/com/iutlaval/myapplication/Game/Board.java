package com.iutlaval.myapplication.Game;

import com.iutlaval.myapplication.Game.Cards.Card;

/**
 * cette classe contrain toutes les cartes sur le terrain cela permet de garder une copie qui sera utilisé pour l'affichage
 */
public class Board {
    public static final int MAX_CARD_ON_BOARD=5;
    private Card[] advCardsOnBoard;
    private Card[] playerCardsOnBoard;

    public Board()
    {
        advCardsOnBoard = new Card[MAX_CARD_ON_BOARD];
        playerCardsOnBoard = new Card[MAX_CARD_ON_BOARD];
    }

    public Card[] getPlayerCardsOnBoard() {
        return playerCardsOnBoard;
    }

    public Card[] getAdvCardsOnBoard() {
        return advCardsOnBoard;
    }
}
