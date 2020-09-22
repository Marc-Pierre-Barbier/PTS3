package com.iutlaval.myapplication.Game.Decks;

import android.content.Context;

import com.iutlaval.myapplication.Game.Cards.Card;
import com.iutlaval.myapplication.Game.Cards.DemoCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DeckDemo extends Deck{

    Stack<Card> cards;

    public DeckDemo(String id, Context constext)
    {
        cards = new Stack<>();

        //generating cards randomly
        for(int i = 0 ; i < 50 ; i++)
        {
            cards.add(new DemoCard(id +"_card_"+ i,constext));
        }


    }

    @Override
    public Stack<Card> getCards() {
        return cards;
    }

    public String getDeckName() {
        return "demo";
    }

}
