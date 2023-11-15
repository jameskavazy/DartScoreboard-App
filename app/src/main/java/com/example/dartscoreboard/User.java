package com.example.dartscoreboard;

import android.util.Log;

import java.util.ArrayList;

public class User {

    public String username;
    public int playerScore;
    public ArrayList<Integer> previousScoresList;
    public boolean active;
    public boolean turn;
    public int currentLegs;
    public int currentSets;
    public int visits = getPreviousScoresList().size(); //todo make sure visits is set properly by getVisits?//setvisits.

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

    public ArrayList<Integer> getPreviousScoresList(){
        if (previousScoresList == null){
            previousScoresList = new ArrayList<>();
        }
        return previousScoresList;
    }

    public void addToPreviousScoresList(int score){
        if (previousScoresList == null){
            previousScoresList = new ArrayList<>();
        }
        previousScoresList.add(score);

    }

    public void setPreviousScoresList(ArrayList<Integer> pastScoresList){
        this.previousScoresList = pastScoresList;
    }


    public int getVisits(){
        //If previous scores list is empty then return 1 to avoid divide by zero
        if (previousScoresList == null){
            return 1;
        }
        else return previousScoresList.size();
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

    public double getAvg() {
        double totalScores = 0;
        for (int i = 0; i < getPreviousScoresList().size(); i++) {
            if (!previousScoresList.isEmpty()) {
                totalScores += getPreviousScoresList().get(i);
            } else {
                return 0;
            }
        }
        double average = totalScores / getVisits();
        return Math.round(average * 10.0) / 10.0;
    }

    public void setPlayerLegs(int currentLegs) {
        this.currentLegs = currentLegs;
    }
    public void setPlayerSets (int currentSets){
        this.currentSets = currentSets;
    }
}
