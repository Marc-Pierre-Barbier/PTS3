package com.iutlaval.myapplication.Game.Cards;

import java.util.ArrayList;
import java.util.List;

public class CardRegistery {
    public static List<Class> registry;

    public CardRegistery()
    {
        registry = new ArrayList<>();

        registry.add(Mythes_Perséphone.class);
        registry.add(Mythes_Chiron.class);



    }

    public static int get(Class<? extends Card> class1) {
        int a = registry.indexOf(class1);
        return a != -1 ? a : 0;
    }

    public static Class get(int index) {
        return registry.get(index);
    }


}
