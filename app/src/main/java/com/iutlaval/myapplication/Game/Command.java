package com.iutlaval.myapplication.Game;

/**
 * cette classe sert de registre de command basiquement elle joue le role d'un enum mais en plus simple a faire avec le fait que l'on a besoin de string
 */
public final class Command {


    private Command(){} // pas de contructor on ne veut pas instancier cette classe
    public static final String YOURTURN="yourturn"; // commande de debut de tours
    public static final String DRAW = "draw"; //demande a pioché
    public static final String GET_DECK = "getDeck"; // demande le deck
    public static final String PUT_CARD = "putCard"; // place une carte
    public static final String PUT_ENEMY_CARD = "enemycard"; // place une carte sur le terrain adverse
    public static final String PASS_TURN = "passTurn"; // passe son tours
    public static final String POPUP = "popup"; // affiche un messsage
    public static final String ENEMYTURN = "enemyTurn"; // tours enemy
    public static final String SETMANA = "setmana"; // defini la mana
    public static final String WIN = "win"; // gagne
    public static final String LOSE = "lose"; // perds
    public static final String PING = "ping"; //envoie un ping en l'attente d'un pong
    public static final String PONG = "pong";
    public static final String NOK = "nok"; // refuse une action
    public static final String OK = "ok"; // accepte une actoin
    public static final String MEULE = "meule"; // fait pioché mais n'ajoute pas a la main la carte sera donc perdu
    public static final String UPDATE = "update";//fonction client uniquement a utilisé avec le serveur de débugage permet de mettre a jour l'écrans
    public static final String ATTACK = "attack"; // dit au server qu'une attaque est lancé
    public static final String DESTROY_CARD = "destroyself"; //demande au clien de retirer une carte du terrain
    public static final String DESTROY_ADV_CARD = "destroyadv"; //demande au client de retirer une carte adverse du terrain
    public static final String BATTLE = "battlephase";
    public static final String SET_ENEMY_HP = "setenemhp";
    public static final String SET_HP = "sethp";
    public static final String SET_CARD_HP = "setcardhp";
    public static final String SET_ADV_CARD_HP = "setadvcardhp";
    public static final String SET_CARD_ATK = "setcardatk";
    public static final String SET_ADV_CARD_ATK = "setadvcardatk";


}
