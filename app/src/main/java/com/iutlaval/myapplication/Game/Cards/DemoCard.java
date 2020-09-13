package com.iutlaval.myapplication.Game.Cards;

import android.content.Context;
import android.graphics.Color;

import com.iutlaval.myapplication.R;

public class DemoCard extends Card{

    /**
     * cree une carte est lui definit un drawable
     *
     * @param UID
     * @param c
     */
    public DemoCard(String UID, Context c) {
        super(UID, c);
    }


    @Override
    public int getCardPicture() {
        return R.drawable.t_cp_elfe_fournisseur;
    }


    //#AARRGGBB
    @Override
    public String getColor() {
        return "#00000000";
    }

    @Override
    public String getDescription() {
        return "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec volutpat metus eu augue aliquet, nec accumsan tellus aliquam. Nullam eu consectetur quam.";
    }

    @Override
    public int getAttack() {
        return 10;
    }

    @Override
    public int getHealth() {
        return 10;
    }

    @Override
    public String getName() {
        return "DEMO";
    }
}
