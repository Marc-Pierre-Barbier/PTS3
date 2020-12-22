package com.iutlaval.myapplication;

import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iutlaval.myapplication.Game.Cards.Card;
import com.iutlaval.myapplication.Game.Cards.CardRegistery;

import java.lang.reflect.Constructor;

public class CardListActivity extends AppCompatActivity {

    private TextView deck;
    private LinearLayout linearLayout;
    private int inf;
    private int sup;
    private ImageView cardPicture;
    private Button buttonHome;
    private TextView cardName;
    private TextView cardStats;
    private TextView cardLink;


    private int screenHeight;
    private int screenWidth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        deck = findViewById(R.id.textViewDeck);
        linearLayout = findViewById(R.id.linearLayout);
        buttonHome = findViewById(R.id.buttonHome);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnHome();
            }
        });

        //récupération de la taille de l'écran
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        SharedPreferences chosenOne = PreferenceManager.getDefaultSharedPreferences(this);
        int deckIndex = chosenOne.getInt("deckIndex", 0);

        String[] mTestArray = getResources().getStringArray(R.array.decks);

        deck.setText("Vous êtes sur le deck : "+mTestArray[deckIndex]);

        switch (deckIndex) {
            case 0 :
                inf = 0;
                sup = 30;
                break;
            case 1 :
                inf = 30;
                sup = 60;
                break;
            case 2 :
                inf = 60;
                sup = 90;
                break;
        }
        showDeckCards(inf, sup);
    }

    public void showDeckCards(int inf, int sup) {
        for(int i = inf; i < sup; i++) {
            try {
                Class c = CardRegistery.get(i);
                Constructor con = c.getConstructor(String.class, Context.class);
                Card card = (Card) con.newInstance(null, null);
                cardPicture = new ImageView(this);
                cardPicture.setBackgroundResource(card.getCardPicture());
                cardPicture.setLayoutParams(new ViewGroup.LayoutParams((int) (screenWidth / 1.2), (int) (screenHeight / 1.7)));
                linearLayout.addView(cardPicture);

                cardName = new TextView(this);
                cardName.setText(card.getName()+"\n");
                linearLayout.addView(cardName);

                cardStats = new TextView(this);
                cardStats.setText("Attaque : "+card.getAttack()+"       Vie : "+card.getHealth()+"\n");
                linearLayout.addView(cardStats);


                cardLink = new TextView(this);
                cardLink.setText("Lien : "+card.getWikipediaLink()+"\n\n\n");
                Linkify.addLinks(cardLink, Linkify.ALL);
                linearLayout.addView(cardLink);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void returnHome() {
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
    }

}
