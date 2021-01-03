package com.iutlaval.myapplication.Game;

import com.iutlaval.myapplication.Game.Cards.Card;
import com.iutlaval.myapplication.Video.Drawables.DrawableCard;

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

    public int getCardZone(DrawableCard drawableCard)
    {
        return getCardZone(drawableCard.getCard());
    }

    public int getCardZone(Card card)
    {
        int index=0;
        for(Card c : playerCardsOnBoard)
        {
            //si c'est la meme instance de la carte
            // /!\ ne pas mettre un equals ce n'est pas une erreur je test l'instance et non la similarité
            if(c == card)
            {
                return index;
            }
            index++;
        }
        return -1;
    }

    public Card removeCardOnPlayerBoard(int zone)
    {
        Card c = playerCardsOnBoard[zone];
        playerCardsOnBoard[zone] = null;
        return c;
    }

    public Card removeCardOnEnemyPlayerBoard(int zone) {
        Card c = advCardsOnBoard[zone];
        advCardsOnBoard[zone] = null;
        return c;
    }

    public void setCard(int index, Card c)
    {
        playerCardsOnBoard[index]=c;
    }

    public void setEnemyCard(int index, Card c)
    {
        advCardsOnBoard[index]=c;
    }

    public Card[] getAdvCardsOnBoard() {
        return advCardsOnBoard;
    }
}
