package com.iutlaval.myapplication.Game.Players;

import com.iutlaval.myapplication.Game.Cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Player {
    private static final int MAX_HAND_SIZE = 10;

    private List<Card> deck;
    private List<Card> hand;
    private int hp = 100;


    public Player(){
        deck=new ArrayList<>();
        hand=new ArrayList<>();
    }

    public void shuffle()
    {
        Collections.shuffle(deck);
    }

    /**
     * cette fonction ajoute un carte dans la main de l'utilisateur
     */
    public void pickCard()
    {
        if(!deck.isEmpty()) {
            if (hand.size() >= MAX_HAND_SIZE) {
                deck.remove(0);
            } else {
                hand.add(deck.remove(0));
            }
        }else{
            getHurt(2);
        }
    }

    public void getHurt(int dmg)
    {
        hp -= dmg;
        //TODO death trigger
    }
}
