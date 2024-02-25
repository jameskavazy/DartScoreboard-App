package com.example.dartscoreboard.MatchHistory;

import com.example.dartscoreboard.User.User;

import java.io.Serializable;
import java.util.ArrayList;

public class MatchState implements Serializable {

    private ArrayList<User> playerList;
    private int turnIndex;
    private int turnIndexForLegs;
    private int turnIndexForSets;

    public MatchState(ArrayList<User> playerList, int turnIndex, int turnIndexForLegs, int turnIndexForSets){
        this.playerList = playerList;
        this.turnIndex = turnIndex;
        this.turnIndexForLegs = turnIndexForLegs;
        this.turnIndexForSets = turnIndexForSets;
    }

    public ArrayList<User> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(ArrayList<User> playerList) {
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
