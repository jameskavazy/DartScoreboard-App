package com.example.dartscoreboard.Application;

import static com.example.dartscoreboard.NotificationService.ReminderNotificationService.reminderChannel;

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
        channel.setDescription("Reminder to play darts if you've missed your practice for the week");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }
}
