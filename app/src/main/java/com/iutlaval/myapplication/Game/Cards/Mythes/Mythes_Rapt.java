package com.iutlaval.myapplication.Game.Cards.Mythes;

import android.content.Context;

import com.iutlaval.myapplication.Game.Cards.Card;
import com.iutlaval.myapplication.R;

public class Mythes_Rapt extends Card {
    /**
     * cree une carte est lui definit un drawable
     *
     * @param UID
     * @param c
     */
    public Mythes_Rapt(String UID, Context c) {
        super(UID, c);
    }

    /**
     * retourne l'indice de la texture de la photo de la carte
     *
     * @return
     */
    @Override
    public int getCardPicture() {
        return R.drawable.t_c_rapt;
    }

    /**
     * retourn le texte de la description de la carte
     *
     * @return la description
     */
    @Override
    public String getDescription() {
        return "-5 pv à une creature";
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
        return "Le rapt de Perséphone";
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
        return "https://fr.wikipedia.org/wiki/Rapt_de_Pers%C3%A9phone#:~:text=Le%20rapt%20de%20Pers%C3%A9phone%2C%20dans,ne%20reste%20dans%20le%20monde";
    }

    /**
     * cette fonction retourne la catégorie de la carte qui sera afficher
     *
     * @return la categorie
     */
    @Override
    public String getCategorie() {
        return null;
    }
}
