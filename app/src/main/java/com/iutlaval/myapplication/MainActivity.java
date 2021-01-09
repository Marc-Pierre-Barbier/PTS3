package com.iutlaval.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private boolean disabledMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context=this;
        setContentView(R.layout.activity_main);

        final Button playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pickDeckGame();
            }
        });

        final Button cardButton = (Button) findViewById(R.id.cardButton);
        cardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pickDeckList();
            }
        });

        final ImageButton handicapeButton = (ImageButton) findViewById(R.id.handicapeButton);
        handicapeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handicapeButtonHandle(handicapeButton);
            }
        });

        final Button creditbutton = (Button) findViewById(R.id.credit);
        creditbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent creditIntent = new Intent(context,Credit.class);
                context.startActivity(creditIntent);
            }
        });


        disabledMode=true;
        handicapeButtonHandle(handicapeButton);
    }

    private void pickDeckGame() {
        DialogFragment d = new DeckPickDialogueGame(this);
        d.show(getSupportFragmentManager(),"s");
    }

    private void pickDeckList() {
        DialogFragment d = new DeckPickDialogueList(this);
        d.show(getSupportFragmentManager(),"s");
    }

    //permet d'utiliser le bouton handicape et de changer son aparence
    private void handicapeButtonHandle(ImageButton handicapeButton)
    {
        if(!disabledMode)
        {
            handicapeButton.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.t_h_handicape));
        }else{
            handicapeButton.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.t_h_handicape_greyed));
        }
        disabledMode = ! disabledMode;

        SharedPreferences sharedPref = this.getSharedPreferences("handicape",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.handicape), disabledMode);
        editor.apply();
    }


}

