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
}
