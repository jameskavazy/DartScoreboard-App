package com.example.dartscoreboard.Statistics;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.dartscoreboard.User.User;

public class UserStatsViewModel extends AndroidViewModel {

    private User user;


    public UserStatsViewModel(@NonNull Application application) {
        super(application);
    }

    public int getCheckoutRate(){
        return Math.round((float) getUser().getCheckoutMade() / (getUser().getCheckoutMade() + getUser().getCheckoutMissed()) * 100);
    }


    public void setUser(User user) {
        this.user = user;
    }

    public User getUser(){
        return user;
    }






}
