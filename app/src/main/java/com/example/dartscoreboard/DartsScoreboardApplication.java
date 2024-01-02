package com.example.dartscoreboard;

import android.app.Application;
import android.content.Context;

public class DartsScoreboardApplication extends Application {
    private static DartsScoreboardApplication sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

//    public static DartsScoreboardApplication getApplication() {
//        return sApplication;
//    }

    public static Context getContext() {
        return sApplication.getApplicationContext();
    }
}