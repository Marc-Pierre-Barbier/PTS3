package com.iutlaval.myapplication.Game.Decks;

import android.content.Context;

import com.iutlaval.myapplication.Game.HandActivity;

import java.util.Objects;

public class DeckRegister {
    public static Deck getClassFromName(String name, Context context)
    {
        //switch case non utilisable MAIS c'est pas important car c'ette foction n'est que pour
        //HandActivity

        if(name.equals("demo"))
        {
            return new DeckDemo(name,context);
        }
        return null;
    }
}
