package com.iutlaval.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

public class DeckPickDialogueList extends DeckPickDialogueGame {
    public DeckPickDialogueList(@NonNull Context context) {
        super(context);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void startActivity(String[] mTestArray, int which) {
        Intent cardListActivity = new Intent(context, CardListActivity.class);
        cardListActivity.putExtra("DECK", mTestArray[which]);
        cardListActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        SharedPreferences chosenOne = PreferenceManager.getDefaultSharedPreferences(context);;
        SharedPreferences.Editor editor = chosenOne.edit();
        editor.putInt("deckIndex", which);
        editor.apply();

        startActivity(cardListActivity);
    }
}
