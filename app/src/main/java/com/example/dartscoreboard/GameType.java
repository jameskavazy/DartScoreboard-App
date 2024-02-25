package com.example.dartscoreboard;

public enum GameType {
    FiveO("501", 501),
    ThreeO("301", 301),
    SevenO("170", 170);

    public final String name;
    public final int startingScore;

    GameType(String name, int startingScore) {
        this.name = name;
        this.startingScore = startingScore;
    }
}
