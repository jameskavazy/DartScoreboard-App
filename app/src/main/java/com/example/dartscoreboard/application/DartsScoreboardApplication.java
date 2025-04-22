package com.example.dartscoreboard.application;


import android.app.Application;
import android.content.Context;

public class DartsScoreboardApplication extends Application {
    private static DartsScoreboardApplication sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    public static Context getContext() {
        return sApplication.getApplicationContext();
    }

    public static DartsScoreboardApplication getInstance(){
        return sApplication;
    }
}
