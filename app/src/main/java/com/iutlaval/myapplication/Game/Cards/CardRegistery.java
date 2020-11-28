package com.iutlaval.myapplication.Game.Cards;

import java.util.ArrayList;
import java.util.List;

public class CardRegistery {
    public static List<Class> registry;

    public CardRegistery()
    {
        registry = new ArrayList<>();

        //Ce sont les cartes du deck Mythes et légendes grecs
        registry.add(Mythes_Perséphone.class);
        registry.add(Mythes_Chiron.class);
        registry.add(Mythes_Dédale.class);
        registry.add(Mythes_Erinyes.class);
        registry.add(Mythes_Oedipie.class);
        registry.add(Mythes_Thanatos.class);
        registry.add(Mythes_Zeus.class);
        registry.add(Mythes_Hades.class);
        registry.add(Mythes_Poseidon.class);
        registry.add(Mythes_Chaos.class);
        registry.add(Mythes_Heracles.class);
        registry.add(Mythes_Achille.class);
        registry.add(Mythes_Minos.class);
        registry.add(Mythes_Psyché.class);
        registry.add(Mythes_Castor_Pollux.class);
        registry.add(Mythes_Sémélé.class);
        registry.add(Mythes_Hermione.class);
        registry.add(Mythes_Amazones.class);
        registry.add(Mythes_Eole.class);

        registry.add(Mythes_Titanomachie.class);
        registry.add(Mythes_12_Travaux.class);
        registry.add(Mythes_Icar.class);
        registry.add(Mythes_Chant_Sirene.class);
        registry.add(Mythes_Rapt.class);
        registry.add(Mythes_Toison.class);
        registry.add(Mythes_Zeus_VS_Typhon.class);
        registry.add(Mythes_Enigme_Sphinx.class);
        registry.add(Mythes_Cheval_Troie.class);
        registry.add(Mythes_Kunée.class);
        registry.add(Mythes_Guerre_Sept_Chefs.class);






    }

    public static int get(Class<? extends Card> class1) {
        int a = registry.indexOf(class1);
        return a != -1 ? a : 0;
    }

    public static Class get(int index) {
        return registry.get(index);
    }


}
