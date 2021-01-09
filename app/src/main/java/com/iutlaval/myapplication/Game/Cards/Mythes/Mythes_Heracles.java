package com.iutlaval.myapplication.Game.Cards.Mythes;

import android.content.Context;

import com.iutlaval.myapplication.Game.Cards.Card;
import com.iutlaval.myapplication.R;

public class Mythes_Heracles extends Card {
    /**
     * cree une carte est lui definit un drawable
     *
     * @param UID
     * @param c
     */
    public Mythes_Heracles(String UID, Context c) {
        super(UID, c);
    }

    /**
     * retourne l'indice de la texture de la photo de la carte
     *
     * @return
     */
    @Override
    public int getCardPicture() {
        return R.drawable.t_c_heracles;
    }

    /**
     * retourn le texte de la description de la carte
     *
     * @return la description
     */
    @Override
    public String getDescription() {
        return "si il est seul sur notre cote du champ de bataille alors il gagne +1/+1";
    }

    /**
     * retourne un entier coresspondant a l'attaque de la carte
     *
     * @return
     */
    @Override
    public int getDefaultAttack() {
        return 3;
    }

    /**
     * retourne la santé de la carte
     *
     * @return
     */
    @Override
    public int getDefaultHealth() {
        return 3;
    }

    /**
     * cette fonction retourne le nom de la carte qui sera afficher
     *
     * @return le nom
     */
    @Override
    public String getName() {
        return "Héraclès";
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
        return "https://fr.wikipedia.org/wiki/H%C3%A9racl%C3%A8s#:~:text=H%C3%A9racl%C3%A8s%20(en%20grec%20ancien%20%E1%BC%A9%CF%81%CE%B1%CE%BA%CE%BB%E1%BF%86%CF%82,v%C3%A9n%C3%A9r%C3%A9s%20de%20la%20Gr%C3%A8ce%20antique.";
    }

    /**
     * cette fonction retourne la catégorie de la carte qui sera afficher
     *
     * @return la categorie
     */
    @Override
    public String getCategorie() {
        return "Dieu";
    }
}
