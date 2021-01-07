package com.iutlaval.myapplication.Game.Cards.Renaissance;

import android.content.Context;

import com.iutlaval.myapplication.Game.Cards.Card;
import com.iutlaval.myapplication.R;

public class Renaissance_Succession_Bourgogne extends Card {
    /**
     * cree une carte est lui definit un drawable
     *
     * @param UID
     * @param c
     */
    public Renaissance_Succession_Bourgogne(String UID, Context c) {
        super(UID, c);
    }

    /**
     * retourne l'indice de la texture de la photo de la carte
     *
     * @return
     */
    @Override
    public int getCardPicture() {
        return R.drawable.t_c_succession_bourgogne;
    }

    /**
     * retourn le texte de la description de la carte
     *
     * @return la description
     */
    @Override
    public String getDescription() {
        return "mutilation bénéfique";
    }

    /**
     * retourne un entier coresspondant a l'attaque de la carte
     *
     * @return
     */
    @Override
    public int getDefaultAttack() {
        return 1;
    }

    /**
     * retourne la santé de la carte
     *
     * @return
     */
    @Override
    public int getDefaultHealth() {
        return 6;
    }

    /**
     * cette fonction retourne le nom de la carte qui sera afficher
     *
     * @return le nom
     */
    @Override
    public String getName() {
        return "Guerre de succession de Bourgogne";
    }

    /**
     * cette fonction retourne le coût en mana de la carte qui sera afficher
     *
     * @return le cout
     */
    @Override
    public int getCost() {
        return 5;
    }

    /**
     * cette fonction retourne le lien wikipédia de la carte qui sera afficher
     *
     * @return le lien wikipedia
     */
    @Override
    public String getWikipediaLink() {
        return "https://fr.wikipedia.org/wiki/Guerre_de_Succession_de_Bourgogne";
    }

    /**
     * cette fonction retourne la catégorie de la carte qui sera afficher
     *
     * @return la categorie
     */
    @Override
    public String getCategorie() {
        return "Evénement";
    }
}
