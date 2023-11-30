package com.example.dartscoreboard;

import java.io.Serializable;

public class GameSettings implements Serializable {

    private int totalLegs;
    private int totalSets;

    public GameSettings(int totalLegs, int totalSets) {
        this.totalLegs = totalLegs;
        this.totalSets = totalSets;
    }

    public int getTotalLegs() {
        return totalLegs;
    }

    public void setTotalLegs(int totalLegs) {
        this.totalLegs = totalLegs;
    }

    public int getTotalSets() {
        return totalSets;
    }

    public void setTotalSets(int totalSets) {
        this.totalSets = totalSets;
    }
}
