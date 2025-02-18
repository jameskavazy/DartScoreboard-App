package com.example.dartscoreboard.MatchHistory;

import com.example.dartscoreboard.User.User;

import java.io.Serializable;
import java.util.List;


// TODO: 17/02/2025 This will probably not be needed as we won't be using stack data structure to store match history
public class MatchState implements Serializable {

    private final List<User> playerList;
    private final int turnIndex;
    private final int turnIndexForLegs;
    private final int turnIndexForSets;

    public MatchState(List<User> playerList, int turnIndex, int turnIndexForLegs, int turnIndexForSets){
        this.playerList = playerList;
        this.turnIndex = turnIndex;
        this.turnIndexForLegs = turnIndexForLegs;
        this.turnIndexForSets = turnIndexForSets;
    }

    public List<User> getPlayerList() {
        return playerList;
    }

    public int getTurnIndex() {
        return turnIndex;
    }

    public int getTurnIndexForLegs() {
        return turnIndexForLegs;
    }

    public int getTurnIndexForSets() {
        return turnIndexForSets;
    }

}
