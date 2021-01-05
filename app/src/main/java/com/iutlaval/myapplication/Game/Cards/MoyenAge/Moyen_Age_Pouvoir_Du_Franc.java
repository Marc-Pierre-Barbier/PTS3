package com.iutlaval.myapplication.Game.Cards.MoyenAge;

import android.content.Context;

import com.iutlaval.myapplication.Game.Cards.Card;
import com.iutlaval.myapplication.R;

public class Moyen_Age_Pouvoir_Du_Franc extends Card {
    /**
     * cree une carte est lui definit un drawable
     *
     * @param UID
     * @param c
     */
    public Moyen_Age_Pouvoir_Du_Franc(String UID, Context c) {
        super(UID, c);
    }

    /**
     * retourne l'indice de la texture de la photo de la carte
     *
     * @return
     */
    @Override
    public int getCardPicture() {
        return R.drawable.t_c_franc;
    }

    /**
     * retourn le texte de la description de la carte
     *
     * @return la description
     */
    @Override
    public String getDescription() {
        return "vous piochez deux carte";
    }

    /**
     * retourne un entier coresspondant a l'attaque de la carte
     *
     * @return
     */
    @Override
    public int getDefaultAttack() {
        return 0;
    }

    /**
     * retourne la santé de la carte
     *
     * @return
     */
    @Override
    public int getDefaultHealth() {
        return 0;
    }

    /**
     * cette fonction retourne le nom de la carte qui sera afficher
     *
     * @return le nom
     */
    @Override
    public String getName() {
        return "pouvoir du franc";
    }

    /**
     * cette fonction retourne le coût en mana de la carte qui sera afficher
     *
     * @return le cout
     */
    @Override
    public int getCost() {
        return 3;
    }

    /**
     * cette fonction retourne le lien wikipédia de la carte qui sera afficher
     *
     * @return le lien wikipedia
     */
    @Override
    public String getWikipediaLink() {
        return "https://fr.wikipedia.org/wiki/Franc";
    }

    /**
     * cette fonction retourne la catégorie de la carte qui sera afficher
     *
     * @return la categorie
     */
    @Override
    public String getCategorie() {
        return "sort";
    }
}
