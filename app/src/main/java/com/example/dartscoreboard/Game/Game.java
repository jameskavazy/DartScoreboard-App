package com.example.dartscoreboard.Game;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.dartscoreboard.User.User;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;



@Entity (tableName = "game")
public class Game implements Serializable {

    @PrimaryKey
    @NonNull
    public String gameId;

    public String matchId;

    @ColumnInfo(name = "turn_index")
    public int turnIndex;
    @ColumnInfo(name = "leg_index")
    public int legIndex;
    @ColumnInfo(name = "set_index")
    public int setIndex;
    public int winnerId;


    public Game(@NonNull String gameId, int turnIndex, int legIndex, int setIndex) {
        this.gameId = gameId;
        this.turnIndex = turnIndex;
        this.legIndex = legIndex;
        this.setIndex = setIndex;
    }

    public void setGameId(@NonNull String gameId) {
        this.gameId = gameId;
    }

    @NonNull
    public String getGameId() {
        return gameId;
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




}
