package com.example.dartscoreboard;

import java.util.ArrayList;

public final class GameController {


    public static GameController gameController;

    private ArrayList<User> playersList;
    private SelectGameActivity.GameType gameType;
    private int totalLegs;
    private int totalSets;
    private int turnLead;
    private int turnLeadForSets;

    private GameSettings gameSettings;


    private GameController(){
    }

    public static GameController getInstance() {
        if (gameController == null){
            gameController = new GameController();
        }
        return gameController;
    }

    //todo game logic here



}
