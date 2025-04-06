package com.example.dartscoreboard.application;

import static com.example.dartscoreboard.reminders.ReminderNotificationService.reminderChannel;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

public class DartsScoreboardApplication extends Application {
    private static DartsScoreboardApplication sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        createNotificationChannel();
    }

    public static Context getContext() {
        return sApplication.getApplicationContext();
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                reminderChannel,
                "Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.setDescription("Remind yourself to practice darts to build a daily habit.");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }
}
