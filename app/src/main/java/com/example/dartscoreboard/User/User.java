package com.example.dartscoreboard.User;

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

    @Ignore
    protected boolean isCheckout;

    public boolean isGuy;

    public int checkoutMade;

    public int checkoutMissed;


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


    public int getVisits() {
        if (previousScoresList == null) {
            return 0;
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


    public void setNumberOf180s(int numberOf180s) {
        this.numberOf180s = numberOf180s;
    }

    public void setNumberOfDartsThrown(int numberOfDartsThrown) {
        this.numberOfDartsThrown = numberOfDartsThrown;
    }



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

    public int getCheckoutMade(){
        return checkoutMade;
    }

    public int getCheckoutMissed(){
        return checkoutMissed;
    }

    public int getWinRate(){
        return Math.round(((float) getWins() / getTotalMatches()) * 100);
    }

    public void incrementWins(){
        wins++;
    }

    public void incrementLosses(){
       losses++;
    }

    public void incrementCheckoutMade(){
        checkoutMade++;
    }

    public void incrementCheckoutMissed(){
        checkoutMissed++;
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
        return wins + losses;
    }

    public int getTotalScores(){
        int totalScores = 0;
        for (int i = 0; i < getPreviousScoresList().size(); i++) {
            if (!getPreviousScoresList().isEmpty()) {
               totalScores += getPreviousScoresList().get(i);
            } else return 0;
        }
        return totalScores;
    }

    public boolean isCheckout() {
        return isCheckout;
    }

    public void setCheckout(boolean checkout) {
        this.isCheckout = checkout;
    }

//    @NonNull
//    @Override
//    public Object clone() throws CloneNotSupportedException {
//        return super.clone();
//    }
}
