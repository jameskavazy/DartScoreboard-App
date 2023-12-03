package com.example.dartscoreboard;

import java.io.Serializable;
import java.util.ArrayList;

public class GameState implements Serializable {

    private SelectGameActivity.GameType gameType;
    private GameSettings gameSettings;
    private ArrayList<User> playerList;
    private int turnLead;
    private int turnLeadForSets;

    public GameState(SelectGameActivity.GameType gameType, GameSettings gameSettings, ArrayList<User> playerList, int turnLead, int turnLeadForSets) {
        this.gameType = gameType;
        this.gameSettings = gameSettings;
        this.playerList = playerList;
        this.turnLead = turnLead;
        this.turnLeadForSets = turnLeadForSets;
    }

    public SelectGameActivity.GameType getGameType() {
        return gameType;
    }

    public void setGameType(SelectGameActivity.GameType gameType) {
        this.gameType = gameType;
    }

    public ArrayList<User> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(ArrayList<User> playerList) {
        this.playerList = playerList;
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public void setGameSettings(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    public int getTurnLead() {
        return turnLead;
    }

    public void setTurnLead(int turnLead) {
        this.turnLead = turnLead;
    }

    public int getTurnLeadForSets() {
        return turnLeadForSets;
    }

    public void setTurnLeadForSets(int turnLeadForSets) {
        this.turnLeadForSets = turnLeadForSets;
    }


    //todo creates an object of hashmaps. Hashmaps will all store different things, all player scores, all player turns, all player average scores etc...
    // todo this then can be added to arraydeque of gamestates

//    public void loadPreviousGameState(ArrayList<User> playersList){
//        for (User player:playersList
//             ) {

//            player.setPlayerScore(getCurrentScoresMap(player));
//            player.setTurn(getTurnsMap(player));
//            player.setPreviousScoresList(getPreviousScoresListMap(player));
//            player.setPlayerLegs(getLegsMap(player));
//            player.setPlayerSets(getSetsMap(player));
//
//            //todo below code block shouldn't be needed but the setpreviousscore method above doesn't do its job
//            if (player.turn && !player.previousScoresList.isEmpty()){
//                player.previousScoresList.remove(player.previousScoresList.size() - 1);
//                //player.visits--;
//            }
//        }
//    }
}