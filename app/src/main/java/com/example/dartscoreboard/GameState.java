package com.example.dartscoreboard;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;


@Entity (tableName = "match_history")
public class GameState implements Serializable {

    @PrimaryKey (autoGenerate = true)
    public int gameID;

   @ColumnInfo
   public OffsetDateTime offsetDateTime = OffsetDateTime.now();


    //todo add Stack of gameStates?
    public SelectGameActivity.GameType gameType;

    public ArrayList<User> playerList;

    private GameSettings gameSettings;

    public int turnIndex;

    public int turnLeadForLegs;

    public int turnLeadForSets;

    public GameState(SelectGameActivity.GameType gameType, GameSettings gameSettings, ArrayList<User> playerList,
                     int turnIndex, int turnLeadForLegs, int turnLeadForSets) {
        this.gameType = gameType;
        this.gameSettings = gameSettings;
        this.playerList = playerList;
        this.turnIndex = turnIndex;
        this.turnLeadForLegs = turnLeadForLegs;
        this.turnLeadForSets = turnLeadForSets;
    }

    public OffsetDateTime getCreatedDate() {
        return offsetDateTime;
    }

    public void setCreatedDate(OffsetDateTime offsetDateTime) {

        this.offsetDateTime = offsetDateTime;
    }



    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
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

    public int getTurnIndex() {
        return turnIndex;
    }

    public void setTurnIndex(int turnIndex) {
        this.turnIndex = turnIndex;
    }

    public int getTurnLeadForLegs() {
        return turnLeadForLegs;
    }

    public void setTurnLeadForLegs(int turnLeadForLegs) {
        this.turnLeadForLegs = turnLeadForLegs;
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