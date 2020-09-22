package com.iutlaval.myapplication.Game.Decks;

import com.iutlaval.myapplication.Game.Cards.Card;

import java.util.Collections;
import java.util.Stack;

/**
 * TOUT DECK CREE DOIT ETRE AJOUTER A DECKREGISTER
 */
public abstract class Deck {
    public abstract Stack<Card> getCards();

    public void suffle()
    {
        Collections.shuffle(getCards());
    }

    /**
     * retourne le nom du deck ex: ww2 , science
     * ce nom doit être unique
     * @return
     */
    public abstract String getDeckName();
}
