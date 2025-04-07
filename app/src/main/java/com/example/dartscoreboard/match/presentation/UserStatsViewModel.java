package com.example.dartscoreboard.match.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.dartscoreboard.user.User;

public class UserStatsViewModel extends AndroidViewModel {

    private User user;


    public UserStatsViewModel(@NonNull Application application) {
        super(application);
    }


    public void setUser(User user) {
        this.user = user;
    }

    public User getUser(){
        return user;
    }






}
