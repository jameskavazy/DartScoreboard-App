package com.example.dartscoreboard.User;

import android.util.Log;

import java.io.Serializable;


public class GuyUser extends User implements Serializable {
    public GuyUser(String username, boolean active) {
        super(username, active);
    }

    @Override
    public void setPlayerScore (int playerScore) {
        this.playerScore = playerScore + 3;
        Log.d("dom test", "setPlayerScore " + this.playerScore);
    }
}


