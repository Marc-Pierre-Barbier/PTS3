package com.iutlaval.myapplication.Game;

import com.iutlaval.myapplication.Game.Cards.Card;
import com.iutlaval.myapplication.Game.Player.Player;
import com.iutlaval.myapplication.Game.Player.PlayerBot;
import com.iutlaval.myapplication.Game.Player.PlayerOnlineAdversary;
import com.iutlaval.myapplication.Game.Player.PlayerLocal;
import com.iutlaval.myapplication.GameActivity;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private static final int MAX_CARD_ON_BOARD=10;
    private List<Card> AdvCardsOnBoard;
    private List<Card> PlayerCardsOnBoard;
    private PlayerLocal player1;
    private Player player2;

    public Board()
    {
        AdvCardsOnBoard = new ArrayList<>();
        PlayerCardsOnBoard = new ArrayList<>();
        player1 = new PlayerLocal();

        if(GameActivity.isMultiplayer())
        {
            player2 = new PlayerOnlineAdversary();
        }else{
            player2 = new PlayerBot();
        }

        //TODO fill decks
        if(GameActivity.isHosting())
        {

        }else{

        }
    }
}
