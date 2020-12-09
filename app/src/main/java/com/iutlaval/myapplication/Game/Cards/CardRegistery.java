package com.iutlaval.myapplication.Game.Cards;

import java.util.ArrayList;
import java.util.List;

public class CardRegistery {
    public static List<Class> registry;

    public CardRegistery()
    {
        registry = new ArrayList<>();

        //Ce sont les cartes du deck Mythes et légendes grecs
        //Créatures
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
        //Sorts
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

        //Cartes du deck Rennaissance
        //créatures
        registry.add(Renaissance_Soliman.class);
        registry.add(Rennaissance_Francois.class);
        registry.add(Rennaissance_Charles.class);
        registry.add(Rennaissance_Casimir.class);
        registry.add(Rennaissance_Henri.class);
        registry.add(Rennaissance_Henri_8.class);
        registry.add(Rennaissance_Constantin.class);
        registry.add(Renaissance_Mehmet.class);
        registry.add(Renaissance_Copernic.class);
        registry.add(Rennaissance_Leonard.class);
        registry.add(Rennaissance_Michel_Ange.class);
        registry.add(Rennaissance_Rabelais.class);
        registry.add(Rennaissance_Machiavel.class);
        registry.add(Rennaissance_Ivan.class);
        registry.add(Rennaissance_Elisabeth.class);
        registry.add(Rennaissance_Vlad.class);
        registry.add(Rennaissance_Nobunaga.class);
        registry.add(Rennaissance_Chatherine.class);
        registry.add(Rennaissance_Ferdinand.class);
        registry.add(Rennaissance_Isabelle.class);
        registry.add(Rennaissance_Skanderberg.class);
        registry.add(Rennaissance_Shakespeare.class);
        //sorts
        registry.add(Rennaissance_Fin_Guerre_Cent_Ans.class);
        registry.add(Rennaissance_Guerres_Italie.class);
        registry.add(Rennaissance_Chute_Constantinople.class);
        registry.add(Rennaissance_Presse_Imprimer.class);
        registry.add(Rennaissance_Decouverte_Amerique.class);
        registry.add(Rennaissance_Concile_Trente.class);
        registry.add(Rennaissance_Succession_Bourgogne.class);
        registry.add(Rennaissance_Reforme.class);

        //Cartes du deck Moyen-age
        //créatures
        registry.add(Moyen_Age_Clovis.class);
        registry.add(Moyen_Age_Charlemagne.class);
        registry.add(Moyen_Age_Hugue.class);
        registry.add(Moyen_Age_Philippe.class);
        registry.add(Moyen_Age_Louis.class);
        registry.add(Moyen_Age_CharlesV.class);
        registry.add(Moyen_Age_CharlesVII.class);
        registry.add(Moyen_Age_PhilippeIV.class);
        registry.add(Moyen_Age_Jeanne.class);
        registry.add(Moyen_Age_Jean.class);
        registry.add(Moyen_Age_Pierre.class);
        registry.add(Moyen_Age_Etienne.class);
        registry.add(Moyen_Age_Ferré.class);
        registry.add(Moyen_Age_Isabelle.class);
        registry.add(Moyen_Age_Jean_Vienne.class);
        registry.add(Moyen_Age_Sire_Jean.class);
        registry.add(Moyen_Age_Philippe_Cacqueray.class);
        registry.add(Moyen_Age_Guillaume.class);
        registry.add(Moyen_Age_Christine.class);
        registry.add(Moyen_Age_Chretien_De_Troyes.class);
        registry.add(Moyen_Age_Pierre_Ermite.class);
        registry.add(Moyen_Age_Roland.class);
        //sorts
        registry.add(Moyen_Age_Traite_Verdun.class);
        registry.add(Moyen_Age_Peste.class);
        registry.add(Moyen_Age_Croisade.class);
        registry.add(Moyen_Age_Guerre_100.class);
        registry.add(Moyen_Age_Malediction_Templier.class);
        registry.add(Moyen_Age_Prise_Sainte_Terre.class);
        registry.add(Moyen_Age_Heresie_Cathares.class);
        registry.add(Moyen_Age_Pouvoir_Du_Franc.class);
    }

    public static int get(Class<? extends Card> class1) {
        int a = registry.indexOf(class1);
        return a != -1 ? a : 0;
    }

    public static Class get(int index) {
        return registry.get(index);
    }


}