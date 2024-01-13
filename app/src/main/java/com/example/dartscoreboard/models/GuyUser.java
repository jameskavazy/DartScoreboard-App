package com.example.dartscoreboard.models;

import android.util.Log;

import com.example.dartscoreboard.iGuy;

import java.io.Serializable;


public class GuyUser extends User implements iGuy, Serializable {

    public GuyUser(String username, boolean active) {
        super(username, active);
    }

    @Override
    public void setPlayerScore (int playerScore, boolean isStarting) {
        this.playerScore = playerScore + 3;
        Log.d("dom test", "setPlayerScore " + this.playerScore);
    }
}


