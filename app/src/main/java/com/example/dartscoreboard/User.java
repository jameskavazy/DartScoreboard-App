package com.example.dartscoreboard;

import java.util.ArrayList;

public class User {

    public String username;

    public int playerScore;

    public ArrayList<Integer> previousScoreList;

    public boolean active;

    public boolean turn;

    public User(String username, boolean active) {
        this.username = username;
        this.active = active;
    }

    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public int getPreviousScore(){
        if (previousScoreList == null){
            previousScoreList = new ArrayList<>();
        }
        return previousScoreList.get(previousScoreList.size() - 1);
    }

    public void setPreviousScore(int previousScore){
        if (previousScoreList == null) {
            previousScoreList = new ArrayList<>();
        }
        previousScoreList.add(previousScore);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getActive() {
        return active;
    }

    public void setPlayerScore(int playerScore){
        this.playerScore = playerScore;
    }

    public int getPlayerScore(){
        return playerScore;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public boolean isTurn(){
        return turn;
    }

}
