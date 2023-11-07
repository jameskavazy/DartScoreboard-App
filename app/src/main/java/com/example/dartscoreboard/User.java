package com.example.dartscoreboard;

import java.util.ArrayList;
import java.util.Arrays;

public class User {

    public String username;

    public int playerScore;
    public ArrayList<Integer> previousScoreList;

    public ArrayList<Integer> previousLegsList;

    public ArrayList<Integer> previousSetsList;

    public ArrayList<Integer> scoresList;

    //public GameState gameState;

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

    public ArrayList<Integer> getScoresList(){
        if (scoresList == null){
            scoresList = new ArrayList<>();
        }
        return scoresList;
    }

    public void setScoresList(int score){
        if (scoresList == null){
            scoresList = new ArrayList<>();
        }
        scoresList.add(score);

    }

    public void revertScoresList(ArrayList<Integer> pastScoresList){
        this.scoresList = pastScoresList;
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


    public int getPreviousLegs(){
        if (previousLegsList == null){
            previousLegsList = new ArrayList<>();
        }
        int previousLegs = previousLegsList.get(previousLegsList.size() - 1);
        previousLegsList.remove(previousLegsList.size() - 1);
        return previousLegs;
    }

    public void setPreviousLegs(int previousScore){
        if (previousLegsList == null) {
            previousLegsList = new ArrayList<>();
        }
        previousLegsList.add(previousScore);
    }

    public int getPreviousSets(){
        if (previousSetsList == null){
            previousSetsList = new ArrayList<>();
        }
        int previousSets = previousSetsList.get(previousSetsList.size() - 1);
        previousSetsList.remove(previousSetsList.size() - 1);
        return previousSets;
    }

    public void setPreviousSets(int previousSets){
        if (previousSetsList == null) {
            previousSetsList = new ArrayList<>();
        }
        previousSetsList.add(previousSets);
    }



    public int getVisits(){
        //If previous scores list is empty then return 1 to avoid divide by zero
        if (scoresList == null){
            return 1;
        }
        else return scoresList.size();
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
        for (int i = 0; i < getScoresList().size(); i++) {
            if (!scoresList.isEmpty()) {
                //todo this doesn't calcualte the average
                totalScores += getScoresList().get(i);
            } else {
                return 0;
            }
        }
        return totalScores / getVisits();
    }

    public void setPlayerLegs(int currentLegs) {
        this.currentLegs = currentLegs;
    }
    public void setPlayerSets (int currentSets){
        this.currentSets = currentSets;
    }
}
