package com.iutlaval.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class MainActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context=this;
        setContentView(R.layout.activity_main);

        final Button playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "click sur button", Toast.LENGTH_LONG).show();
                pickDeckGame();
            }
        });

        final Button cardButton = (Button) findViewById(R.id.cardButton);
        cardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "click sur button", Toast.LENGTH_LONG).show();
                pickDeckList();
            }
        });

    }

    private String pickDeckGame() {
        DialogFragment d = new DeckPickDialogueGame(this);
        d.show(getSupportFragmentManager(),"s");

        return "demodeck";
    }

    private String pickDeckList() {
        DialogFragment d = new DeckPickDialogueList(this);
        d.show(getSupportFragmentManager(),"s");

        return "demodeck";
    }


}

