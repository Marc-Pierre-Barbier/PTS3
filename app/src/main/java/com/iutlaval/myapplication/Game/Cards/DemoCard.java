package com.iutlaval.myapplication.Game.Cards;

import android.graphics.Color;

import com.iutlaval.myapplication.R;

public class DemoCard extends Card{

    @Override
    public int getCardPicture() {
        return R.drawable.guerrier_nul;
    }


    //#AARRGGBB
    @Override
    public String getColor() {
        return "#70000000";
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
}
