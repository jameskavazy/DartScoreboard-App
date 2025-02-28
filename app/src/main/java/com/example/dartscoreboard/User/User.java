package com.example.dartscoreboard.User;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "user")
public class User implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int userID;
    protected String username;

    public boolean isGuy;

    public User(String username) {
        this.username = username;
        if (this.username.equalsIgnoreCase("guy")) {
            isGuy = true;
        }
    }

     public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }














}
