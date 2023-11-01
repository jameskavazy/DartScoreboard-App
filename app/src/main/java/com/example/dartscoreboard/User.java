package com.example.dartscoreboard;

import java.util.ArrayList;

public class User {

    public String username;

    public int playerScore;
    public ArrayList<Integer> previousScoreList;
    public boolean active;
    public boolean turn;
    public int currentLegs;
    public int currentSets;

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
        int previousScore = previousScoreList.get(previousScoreList.size() - 1);
        previousScoreList.remove(previousScoreList.size() - 1);
        return previousScore;
    }

    public void setPreviousScore(int previousScore){
        if (previousScoreList == null) {
            previousScoreList = new ArrayList<>();
        }
        previousScoreList.add(previousScore);
    }

    public int getVisits(){
        //If previous scores list is empty then return 1 to avoid divide by zero
        if (previousScoreList == null){
            return 1;
        }
        else return previousScoreList.size();
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


    public void setCurrentLegs(int currentLegs){
        this.currentLegs = currentLegs;
    }

    public int getCurrentLegs(){
        return currentLegs;
    }

    public void setCurrentSets(int currentSets){
        this.currentSets = currentSets;
    }

    public int getCurrentSets(){
        return currentSets;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public boolean isTurn(){
        return turn;
    }

    public double getAvg(int startingScore){
        double thrown = startingScore - getPlayerScore();
        return thrown/getVisits();
    }

}
