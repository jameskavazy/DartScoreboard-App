package com.example.dartscoreboard.Game;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.dartscoreboard.User.User;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;



@Entity (tableName = "match")
public class Game implements Serializable {

    @PrimaryKey
    @NonNull
    public String gameId;

    @ColumnInfo(name = "datetime")
    public OffsetDateTime offsetDateTime = OffsetDateTime.now();

    @ColumnInfo(name = "game_type")
    public GameType gameType;

    @ColumnInfo(name = "settings")
    private GameSettings gameSettings;

    @ColumnInfo(name = "turn_index")
    public int turnIndex;
    @ColumnInfo(name = "leg_index")
    public int legIndex;
    @ColumnInfo(name = "set_index")
    public int setIndex;
    public int winnerId;
    public String playersCSVString;


    public Game(GameType gameType, GameSettings gameSettings,
                int turnIndex, int legIndex, int setIndex, @NonNull String gameId) {
        this.gameType = gameType;
        this.gameSettings = gameSettings;
        this.turnIndex = turnIndex;
        this.legIndex = legIndex;
        this.setIndex = setIndex;
        this.gameId = gameId;
    }

    public OffsetDateTime getCreatedDate() {
        return offsetDateTime;
    }

    public void setCreatedDate(OffsetDateTime offsetDateTime) {
        this.offsetDateTime = offsetDateTime;
    }



    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
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

    public int getLegIndex() {
        return legIndex;
    }

    public void setLegIndex(int legIndex) {
        this.legIndex = legIndex;
    }

    public int getSetIndex() {
        return setIndex;
    }

    public void setSetIndex(int setIndex) {
        this.setIndex = setIndex;
    }

    public OffsetDateTime getOffsetDateTime() {
        return offsetDateTime;
    }

    public void setPlayersCSV(List<User> players) {

        if (players == null) {
           this.playersCSVString = "";
           return;
        }
        String[] namesOfGame = new String[players.size()];
        for (int i = 0; i < players.size(); i++) {
            namesOfGame[i] = players.get(i).getUsername();
        }
        this.playersCSVString = String.join(", ", namesOfGame);
    }

//    public void set(GameType gameType, GameSettings gameSettings,
//                int turnIndex, int legIndex, int setIndex, String gameId) {
//        this.gameType = gameType;
//        this.gameSettings = gameSettings;
//        this.turnIndex = turnIndex;
//        this.legIndex = legIndex;
//        this.setIndex = setIndex;
//        this.gameId = gameId;
//    }

}
