package com.iutlaval.myapplication.Game;

import com.iutlaval.myapplication.Game.Cards.Card;
import com.iutlaval.myapplication.Game.Decks.Deck;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    private static final int NUMBER_OF_CARDS_IN_STARTING_HAND = 5;

    private List<Card> hand;

    public Hand()
    {
        hand = new ArrayList<>();
    }

    public void pickCardFromDeck(Deck deck,int amount)
    {
        for(int i = 0 ; i < amount;i++)
        {
            hand.add(deck.getCards().pop());
        }
    }

    //TODO check number of card in hand
    public void fillHand(Deck deck)
    {
        pickCardFromDeck(deck,NUMBER_OF_CARDS_IN_STARTING_HAND);
    }

    public List<Card> getHand() {
        return hand;
    }
}
