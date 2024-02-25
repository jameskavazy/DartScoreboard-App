package com.example.dartscoreboard;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.dartscoreboard.models.User;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Stack;


@Entity (tableName = "match_history")
public class GameState implements Serializable {

    @PrimaryKey (autoGenerate = true)
    public long gameID;

    @ColumnInfo
   public OffsetDateTime offsetDateTime = OffsetDateTime.now();


    //todo add Stack of gameStates?
    public GameType gameType;

    public List<User> playerList;

    private GameSettings gameSettings;

    public int turnIndex;

    public int turnLeadForLegs;

    public int turnLeadForSets;

    public Stack<MatchState> matchStateStack;

    public Stack<MatchState> getMatchStateStack() {
        return matchStateStack;
    }

    public void setMatchStateStack(Stack<MatchState> matchStateStack) {
        this.matchStateStack = matchStateStack;
    }

    public GameState(GameType gameType, GameSettings gameSettings, List<User> playerList,
                     int turnIndex, int turnLeadForLegs, int turnLeadForSets, Stack<MatchState> matchStateStack) {
        this.gameType = gameType;
        this.gameSettings = gameSettings;
        this.playerList = playerList;
        this.turnIndex = turnIndex;
        this.turnLeadForLegs = turnLeadForLegs;
        this.turnLeadForSets = turnLeadForSets;
        this.matchStateStack = matchStateStack;
    }

    public OffsetDateTime getCreatedDate() {
        return offsetDateTime;
    }

    public void setCreatedDate(OffsetDateTime offsetDateTime) {

        this.offsetDateTime = offsetDateTime;
    }



    public void setGameID(long gameID) {
        this.gameID = gameID;
    }

    public long getGameID() {
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

    public OffsetDateTime getOffsetDateTime() {
        return offsetDateTime;
    }

}