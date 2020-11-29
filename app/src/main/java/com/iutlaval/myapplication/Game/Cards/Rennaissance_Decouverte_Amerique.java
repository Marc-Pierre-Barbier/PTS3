package com.iutlaval.myapplication.Game.Cards;

import android.content.Context;

import com.iutlaval.myapplication.R;

public class Rennaissance_Decouverte_Amerique extends Card {
    /**
     * cree une carte est lui definit un drawable
     *
     * @param UID
     * @param c
     */
    public Rennaissance_Decouverte_Amerique(String UID, Context c) {
        super(UID, c);
    }

    /**
     * retourne l'indice de la texture de la photo de la carte
     *
     * @return
     */
    @Override
    public int getCardPicture() {
        return R.drawable.t_c_decouverte_amerique;
    }

    /**
     * retourn le texte de la description de la carte
     *
     * @return la description
     */
    @Override
    public String getDescription() {
        return "Regardez les 4 premières cartes de votre deck, choississez en une, mettez la dans votre main";
    }

    /**
     * retourne un entier coresspondant a l'attaque de la carte
     *
     * @return
     */
    @Override
    public int getAttack() {
        return 0;
    }

    /**
     * retourne la santé de la carte
     *
     * @return
     */
    @Override
    public int getHealth() {
        return 0;
    }

    /**
     * cette fonction retourne le nom de la carte qui sera afficher
     *
     * @return le nom
     */
    @Override
    public String getName() {
        return "Découverte de l'Amérique";
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
        return "https://fr.wikipedia.org/wiki/D%C3%A9couverte_et_exploration_de_l%27Am%C3%A9rique";
    }

    /**
     * cette fonction retourne la catégorie de la carte qui sera afficher
     *
     * @return la categorie
     */
    @Override
    public String getCategorie() {
        return "Evénements";
    }
}
