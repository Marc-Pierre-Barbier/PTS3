package com.iutlaval.myapplication.Game;

import com.iutlaval.myapplication.Game.Cards.Card;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<Card> hand;

    public Hand()
    {
        hand = new ArrayList<>();
    }

    /**
     * recupére la quantité indiqué de carte depuis le deck
     * @param deck
     * @param amount
     */
    public void pickCardFromDeck(NetworkDeck deck,int amount)
    {
        for(int i = 0 ; i < amount;i++)
        {
            hand.add(deck.draw());
        }
    }

    public List<Card> getHand() {
        return hand;
    }
}
