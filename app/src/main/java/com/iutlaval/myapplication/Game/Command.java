package com.iutlaval.myapplication.Game;

public final class Command {

    private Command(){} // pas de contructor public on ne veut pas instancier cette classe
    public static final String YOURTURN="yourturn";
    public static final String DRAW = "draw";
    public static final String GET_DECK = "getDeck";
    public static final String PUT_CARD = "putCard";
    public static final String PUT_ENEMY_CARD = "enemycard";
    public static final String SHUFFLE_DECK = "shuffleDeck";
    public static final String SELECT_CARD = "selectCard";
    public static final String ATTACK = "attack";
    public static final String DEFEND = "defend";
    public static final String PASS_TURN = "passTurn";
    public static final String POPUP = "pupup";
    public static final String ENEMYTURN = "enemyTurn";
    public static final String SETMANA = "setmana";
    public static final String WIN = "win";
    public static final String LOSE = "lose";
    public static final String PING = "ping";
    public static final Object OK = "ok";
}
