package com.iutlaval.myapplication.Game.Cards;

import android.content.Context;

import com.iutlaval.myapplication.R;

public class Mythes_Perséphone extends Card{
    /**
     * cree une carte est lui definit un drawable
     *
     * @param UID
     * @param c
     */
    public Mythes_Perséphone(String UID, Context c) {
        super(UID, c);
    }

    @Override
    public int getCardPicture() {
        return R.drawable.t_c_mythe_persephone;
    }

    @Override
    public String getDescription() {
        return "Redonne 5 points de vie au joueur";
    }

    @Override
    public int getAttack() {
        return 4;
    }

    @Override
    public int getHealth() {
        return 4;
    }

    @Override
    public String getName() {
        return "Perséphone, Reine des Enfers";
    }

    @Override
    public int getCost() {
        return 7;
    }

    @Override
    public String getWikipediaLink() {
        return "https://fr.wikipedia.org/wiki/Pers%C3%A9phone#";
    }

    /**
     * cette fonction retourne la catégorie de la carte qui sera afficher
     *
     * @return la categorie
     */
    @Override
    public String getCategorie() {
        return "Dieu" ;
    }
}
