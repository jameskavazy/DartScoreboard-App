package com.example.dartscoreboard.match.data.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.OffsetDateTime;


@Entity (tableName = "game")
public class Game implements Serializable {

    @PrimaryKey
    @NonNull
    public String gameId;
    public String setId;

    public String matchId;

    @ColumnInfo(name = "turn_index")
    public int turnIndex;
    @ColumnInfo(name = "leg_index")
    public int legIndex;
    @ColumnInfo(name = "set_index")
    public int setIndex;
    public int winnerId;
    @ColumnInfo(name = "set_number")
    public int setNumber;

    @ColumnInfo(name = "created_at")
    public OffsetDateTime createdAt = OffsetDateTime.now();

    public Game(@NonNull String gameId, String setId, String matchId, int turnIndex, int legIndex, int setIndex) {
        this.gameId = gameId;
        this.setId = setId;
        this.matchId = matchId;
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
