package com.iutlaval.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class DeckPickDialogue extends DialogFragment {
    Context context;
    public DeckPickDialogue(@NonNull Context context) {
        this.context=context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.pickdeck)
                .setItems(R.array.decks, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        String[] mTestArray = getResources().getStringArray(R.array.decks);

                        Intent gameActivity = new Intent(context, GameActivity.class);
                        gameActivity.putExtra("DECK", mTestArray[which]);
                        gameActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(gameActivity);
                    }
                });
        return builder.create();
    }

}
