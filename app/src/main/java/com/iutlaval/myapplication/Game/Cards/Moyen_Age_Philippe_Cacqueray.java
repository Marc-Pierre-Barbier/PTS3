package com.iutlaval.myapplication.Game.Cards;

import android.content.Context;

import com.iutlaval.myapplication.R;

public class Moyen_Age_Philippe_Cacqueray extends Card{
    /**
     * cree une carte est lui definit un drawable
     *
     * @param UID
     * @param c
     */
    public Moyen_Age_Philippe_Cacqueray(String UID, Context c) {
        super(UID, c);
    }

    /**
     * retourne l'indice de la texture de la photo de la carte
     *
     * @return
     */
    @Override
    public int getCardPicture() {
        return R.drawable.t_c_cacqueray;
    }

    /**
     * retourn le texte de la description de la carte
     *
     * @return la description
     */
    @Override
    public String getDescription() {
        return "";
    }

    /**
     * retourne un entier coresspondant a l'attaque de la carte
     *
     * @return
     */
    @Override
    public int getAttack() {
        return 3;
    }

    /**
     * retourne la santé de la carte
     *
     * @return
     */
    @Override
    public int getHealth() {
        return 1;
    }

    /**
     * cette fonction retourne le nom de la carte qui sera afficher
     *
     * @return le nom
     */
    @Override
    public String getName() {
        return "Philippe Cacqueray, inventeur du plat en verre";
    }

    /**
     * cette fonction retourne le coût en mana de la carte qui sera afficher
     *
     * @return le cout
     */
    @Override
    public int getCost() {
        return 2;
    }

    /**
     * cette fonction retourne le lien wikipédia de la carte qui sera afficher
     *
     * @return le lien wikipedia
     */
    @Override
    public String getWikipediaLink() {
        return "https://fr.wikipedia.org/wiki/Famille_de_Cacqueray";
    }

    /**
     * cette fonction retourne la catégorie de la carte qui sera afficher
     *
     * @return la categorie
     */
    @Override
    public String getCategorie() {
        return "scientifique";
    }
}