package com.iutlaval.myapplication.Game.Cards.Renaissance;

import android.content.Context;

import com.iutlaval.myapplication.Game.Cards.Card;
import com.iutlaval.myapplication.R;

public class Renaissance_Soliman extends Card {

    /**
     * cree une carte est lui definit un drawable
     *
     * @param UID
     * @param c
     */
    public Renaissance_Soliman(String UID, Context c) {
        super(UID, c);
    }

    @Override
    public int getCardPicture() {
        return R.drawable.t_c_soliman;
    }

    @Override
    public String getDescription() {
        return "RA: Piocher 4 carte, infliger 4 dégats au joueur adverse";
    }

    @Override
    public int getAttack() {
        return 6;
    }

    @Override
    public int getHealth() {
        return 4;
    }

    @Override
    public String getName() {
        return "Soliman I, Le Magnifique ";
    }

    @Override
    public int getCost() {
        return 9;
    }

    @Override
    public String getWikipediaLink() {
        return "https://fr.wikipedia.org/wiki/Soliman_le_Magnifique";
    }

    @Override
    public String getCategorie() {
        return "Militaire, Politicien";
    }
}
