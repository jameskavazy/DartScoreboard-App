package com.example.dartscoreboard.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "user_table")
public class User implements Serializable, Cloneable { //todo guy easter egg
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

    public boolean isGuy;

    public User(String username, boolean active) {
        this.username = username;
        this.active = active;

        if (this.username.equalsIgnoreCase("guy")) {
            isGuy = true;
        }
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

    public void setPlayerScore(int playerScore, boolean isStarting) {
//        if (isGuy) {
//            if (!isStarting
//                    && this.playerScore > 100
//                    && playerScore > 10 // todo playerscore >
//                    && playerScore % 5 != 0)
//
//                playerScore = playerScore + 3;
//        }
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

    private int wins;

    private int losses;


    public void setTotalMatches(int totalMatches) {
        this.totalMatches = totalMatches;
    }

    public void setNumberOf180s(int numberOf180s) {
        this.numberOf180s = numberOf180s;
    }

    public void setNumberOfDartsThrown(int numberOfDartsThrown) {
        this.numberOfDartsThrown = numberOfDartsThrown;
    }

    private int totalMatches;

    public int getNumberOf180s() {
        return numberOf180s;
    }

    private int numberOf180s;

    private int numberOfDartsThrown;

    public int getNumberOfDartsThrown(){
        return numberOfDartsThrown;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getLosses() {
        return losses;
    }

    public int getWinRate(){
        return (getWins() / getTotalMatches()) * 100;
    }

    public void incrementWins(){
        wins++;
    }

    public void incrementLosses(){
       losses++;
    }

    public void incrementMatches(){
        totalMatches++;
    }

    public void increment180(){
        numberOf180s++;
    }


    public void incrementDartThrown(){
            numberOfDartsThrown += 3;
        /*
        / TODO: 22/01/2024 in GameController/Activity, if playerScore is 0 (and they've won the game) then
                we need to produce an Alert Dialogue that asks how many darts they threw. 1 dart, we minus 2 off their dartsThrown
                (as we've added 3 for each visit), 2 darts - minus 1 dart, 3 darts, we don't edit
         */
    }

    public int getTotalMatches() {
        return totalMatches;
    }



    /*
    todo
    Overall averages -
    flag for is checkout (or if score is 170 or less, we've already got that functionality within out subtract function; calcualte checkout rate

    leg win rate

    [x]wins, losses
    Match win rate?
    [x]no of 180s


    dartsThrown variable - getVisits *3.

    On game end - as how many darts to finish

        All of the metrics will be updated at the end of the match only. This will save number of updates
        + improve data quality.
     */

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
