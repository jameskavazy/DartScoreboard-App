package com.example.dartscoreboard;

public class User {

    public String username;

    public int playerScore;

    public boolean active;

    public boolean turn;

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

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public boolean isTurn(){
        return turn;
    }

}
