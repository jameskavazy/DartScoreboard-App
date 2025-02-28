package com.example.dartscoreboard.Game;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.dartscoreboard.User.User;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;



@Entity (tableName = "match")
public class Game implements Serializable {

    @PrimaryKey
    @NonNull
    public String gameID;

    @ColumnInfo(name = "datetime")
    public OffsetDateTime offsetDateTime = OffsetDateTime.now();

    @ColumnInfo(name = "game_type")
    public GameType gameType;

    public List<User> playerList;

    @ColumnInfo(name = "settings")
    private GameSettings gameSettings;

    @ColumnInfo(name = "turn_index")
    public int turnIndex;
    @ColumnInfo(name = "leg_index")
    public int legIndex;
    @ColumnInfo(name = "set_index")
    public int setIndex;


    public boolean finished;


    public Game(GameType gameType, GameSettings gameSettings, List<User> playerList,
                int turnIndex, int legIndex, int setIndex, boolean finished, @NonNull String gameID) {
        this.gameType = gameType;
        this.gameSettings = gameSettings;
        this.playerList = playerList;
        this.turnIndex = turnIndex;
        this.legIndex = legIndex;
        this.setIndex = setIndex;
        this.finished = finished;
        this.gameID = gameID;
    }

    public OffsetDateTime getCreatedDate() {
        return offsetDateTime;
    }

    public void setCreatedDate(OffsetDateTime offsetDateTime) {
        this.offsetDateTime = offsetDateTime;
    }



    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getGameID() {
        return gameID;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public List<User> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<User> playerList) {
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
    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

}
