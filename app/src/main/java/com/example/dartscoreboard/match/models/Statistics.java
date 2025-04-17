package com.example.dartscoreboard.match.models;

public class Statistics {

    private int wins;
    private int losses;
    private int matchWinRate;
    private int averageScore;
    private int matchesPlayed;
    private int legsWon;
    private int legWinRate;


    private int checkoutRate;

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getMatchWinRate() {
        return matchWinRate;
    }

    public void setMatchWinRate(int matchWinRate) {
        this.matchWinRate = matchWinRate;
    }

    public int getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(int averageScore) {
        this.averageScore = averageScore;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public int getLegsWon() {
        return legsWon;
    }

    public void setLegsWon(int legsWon) {
        this.legsWon = legsWon;
    }
    public int getLegWinRate() {
        return legWinRate;
    }

    public void setLegWinRate(int legWinRate) {
        this.legWinRate = legWinRate;
    }
    public int getCheckoutRate() {
        return checkoutRate;
    }

    public void setCheckoutRate(int checkoutRate) {
        this.checkoutRate = checkoutRate;
    }
}
