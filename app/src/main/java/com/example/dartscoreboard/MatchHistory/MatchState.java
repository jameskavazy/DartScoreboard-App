package com.example.dartscoreboard.MatchHistory;

import com.example.dartscoreboard.User.User;

import java.io.Serializable;
import java.util.List;

public class MatchState implements Serializable {

    private List<User> playerList;
    private int turnIndex;
    private int turnIndexForLegs;
    private int turnIndexForSets;

    public MatchState(List<User> playerList, int turnIndex, int turnIndexForLegs, int turnIndexForSets){
        this.playerList = playerList;
        this.turnIndex = turnIndex;
        this.turnIndexForLegs = turnIndexForLegs;
        this.turnIndexForSets = turnIndexForSets;
    }

    public List<User> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<User> playerList) {
        this.playerList = playerList;
    }

    public int getTurnIndex() {
        return turnIndex;
    }

    public void setTurnIndex(int turnIndex) {
        this.turnIndex = turnIndex;
    }

    public int getTurnIndexForLegs() {
        return turnIndexForLegs;
    }

    public void setTurnIndexForLegs(int turnIndexForLegs) {
        this.turnIndexForLegs = turnIndexForLegs;
    }

    public int getTurnIndexForSets() {
        return turnIndexForSets;
    }

    public void setTurnIndexForSets(int turnIndexForSets) {
        this.turnIndexForSets = turnIndexForSets;
    }



}
