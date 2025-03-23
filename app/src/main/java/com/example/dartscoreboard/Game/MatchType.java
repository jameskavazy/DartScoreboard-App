package com.example.dartscoreboard.Game;

public enum MatchType {
    FiveO("501", 501),
    ThreeO("301", 301),
    SevenO("170", 170);

    public final String name;
    public final int startingScore;

    MatchType(String name, int startingScore) {
        this.name = name;
        this.startingScore = startingScore;
    }
}
