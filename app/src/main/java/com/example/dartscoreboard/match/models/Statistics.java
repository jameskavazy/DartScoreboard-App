package com.example.dartscoreboard.match.models;

public class Statistics {

    private int wins;
    private int losses;
    private int matchesPlayed;
    private double averageScore;
    private int matchWinRate;
    private int nonCheckoutAvg;
    private int legsWon;
    private int checkoutRate;
    private int legWinRate;
    private int segmentBelow60;
    private int segment60To99;
    private int segment100To139;
    private int segment140To179;
    private int segment180;

    public Statistics(int wins, int losses, int matchesPlayed, double averageScore, int matchWinRate,
                      int nonCheckoutAvg, int legsWon, int checkoutRate, int legWinRate,
                      int segmentBelow60, int segment60To99, int segment100To139,
                      int segment140To179, int segment180) {
        this.wins = wins;
        this.losses = losses;
        this.matchesPlayed = matchesPlayed;
        this.averageScore = averageScore;
        this.matchWinRate = matchWinRate;
        this.nonCheckoutAvg = nonCheckoutAvg;
        this.legsWon = legsWon;
        this.checkoutRate = checkoutRate;
        this.legWinRate = legWinRate;
        this.segmentBelow60 = segmentBelow60;
        this.segment60To99 = segment60To99;
        this.segment100To139 = segment100To139;
        this.segment140To179 = segment140To179;
        this.segment180 = segment180;
    }


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

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
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

    public int getNonCheckoutAvg() {
        return nonCheckoutAvg;
    }

    public void setNonCheckoutAvg(int nonCheckoutAvg) {
        this.nonCheckoutAvg = nonCheckoutAvg;
    }

    public int getSegmentBelow60() {
        return segmentBelow60;
    }

    public void setSegmentBelow60(int segmentBelow60) {
        this.segmentBelow60 = segmentBelow60;
    }

    public int getSegment60To99() {
        return segment60To99;
    }

    public void setSegment60To99(int segment60To99) {
        this.segment60To99 = segment60To99;
    }

    public int getSegment100To139() {
        return segment100To139;
    }

    public void setSegment100To139(int segment100To139) {
        this.segment100To139 = segment100To139;
    }

    public int getSegment140To179() {
        return segment140To179;
    }

    public void setSegment140To179(int segment140To179) {
        this.segment140To179 = segment140To179;
    }

    public int getSegment180() {
        return segment180;
    }

    public void setSegment180(int segment180) {
        this.segment180 = segment180;
    }
}
