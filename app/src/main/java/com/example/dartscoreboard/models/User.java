package com.example.dartscoreboard.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "user_table")
public class User implements Serializable { //todo guy easter egg
    @PrimaryKey(autoGenerate = true)
    protected int userID;
    protected String username;
    @Ignore
    protected int playerScore;
    @Ignore
    protected ArrayList<Integer> previousScoresList;
    protected boolean active;
    @Ignore
    protected int currentLegs;
    @Ignore
    protected int currentSets;

    public User(String username, boolean active) {
        this.username = username;
        this.active = active;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Integer> getPreviousScoresList() {
        if (previousScoresList == null) {
            previousScoresList = new ArrayList<>();
        }
        return previousScoresList;
    }

    public void addToPreviousScoresList(int score) {
        if (previousScoresList == null) {
            previousScoresList = new ArrayList<>();
        }
        previousScoresList.add(score);

    }

    public void setPreviousScoresList(ArrayList<Integer> pastScoresList) {
        this.previousScoresList = pastScoresList;
    }


    public int getVisits() {
        //If previous scores list is empty then return 1 to avoid divide by zero
        if (previousScoresList == null) {
            return 1;
        } else return previousScoresList.size();
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getActive() {
        return active;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public int getPlayerScore() {
        return playerScore;
    }


    public void setCurrentLegs(int currentLegs) {
        this.currentLegs = currentLegs;
    }

    public int getCurrentLegs() {
        return currentLegs;
    }

    public void setCurrentSets(int currentSets) {
        this.currentSets = currentSets;
    }

    public int getCurrentSets() {
        return currentSets;
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

    public void setPlayerSets(int currentSets) {
        this.currentSets = currentSets;
    }


    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
