package com.iutlaval.myapplication.Game.Cards;

import android.content.Context;

import com.iutlaval.myapplication.R;

public class DemoCard2 extends Card{
    /**
     * cree une carte est lui definit un drawable
     *
     * @param UID
     * @param c
     */
    public DemoCard2(String UID, Context c) {
        super(UID, c);
    }

    @Override
    public int getCardPicture() {
        return R.drawable.t_b_batiut;
    }

    @Override
    public String getDescription() {
        return "usless demo";
    }

    @Override
    public int getAttack() {
        return 0;
    }

    @Override
    public int getHealth() {
        return 0;
    }

    @Override
    public String getName() {
        return "DEMO2";
    }
}
