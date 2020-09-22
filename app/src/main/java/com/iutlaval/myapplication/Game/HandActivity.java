package com.iutlaval.myapplication.Game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.iutlaval.myapplication.Game.Decks.Deck;
import com.iutlaval.myapplication.Game.Decks.DeckRegister;
import com.iutlaval.myapplication.R;

public class HandActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand);

        String[] serializedHand = getIntent().getStringArrayExtra("hand");
        Deck d = DeckRegister.getClassFromName(serializedHand[0],this);


        Intent data = new Intent();
        String text = "Result to be returned....";
        //---set the data to pass back---
        data.setData(Uri.parse(text));
        setResult(RESULT_OK, data);
    }
}