package com.iutlaval.myapplication.Game;

import android.content.Context;

import com.iutlaval.myapplication.Game.Cards.Card;
import com.iutlaval.myapplication.Game.Cards.CardRegistery;
import com.iutlaval.myapplication.Game.Cards.Mythes.Mythes_Perséphone;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * c'est un deck qui déserialise les info recut du serveur ce qui donne un deck de carte a terme
 */
public class NetworkDeck{
    private Stack<Card> cards;
    public NetworkDeck(String deckstring, Context context)
    {
        cards = new Stack<>();
        int nbcarte=0;
        for(String s : deckstring.split(","))
        {
            if(s.equals(""))break;
            Class c = CardRegistery.get(Integer.parseInt(s));
            try {
                //on utilise le bon constructeur
                Constructor con = c.getConstructor(String.class, Context.class);
                cards.add((Card) con.newInstance("nas"+nbcarte,context));
            } catch (Exception e) {
                e.printStackTrace();
                cards.add(new Mythes_Perséphone("nas"+nbcarte,context));
            }
            nbcarte++;
        }
    }

    /**
     * retire la premiere carte et la retourne
     * @return
     */
    public Card draw()
    {
        return cards.pop();
    }

    /**
     * retire un nombre donné de carte et les retourne
     * @param amount
     * @return
     */
    public List<Card> draw(int amount)
    {
        List<Card> drawn = new ArrayList<>();
        for(int i = 0 ; i < amount ; i++)
        {
            drawn.add(cards.pop());
        }
        return drawn;
    }
}
