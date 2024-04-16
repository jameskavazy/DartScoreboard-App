package com.example.dartscoreboard.Reminders;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.dartscoreboard.Application.DartsScoreboardApplication;
import com.example.dartscoreboard.Utils.PreferencesController;

public class ReminderViewModel extends AndroidViewModel {

    private long timeSetInMillis;
    private final AlarmManager alarmManager;

    private final PowerManager powerManager;

    public ReminderViewModel(@NonNull Application application) {
        super(application);
        alarmManager = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
        powerManager = (PowerManager) application.getSystemService(Context.POWER_SERVICE);
    }

    public void setReceiverAlarm(){
        Intent receiverIntent = new Intent(DartsScoreboardApplication.getContext(), ReminderNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(DartsScoreboardApplication.getContext(), 1, receiverIntent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,getTimeSetInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void cancelReminder() {
        if (alarmManager != null) {
            alarmManager.cancel(PendingIntent.getBroadcast(DartsScoreboardApplication.getContext(), 1, new Intent(), PendingIntent.FLAG_IMMUTABLE));
            PreferencesController.getInstance().clearReminderTime();
        }
    }

    public boolean isPowerSaveActive(){
        return powerManager.isPowerSaveMode();
    }

    public void setTimeSetInMillis(long timeSetInMillis) {
        this.timeSetInMillis = timeSetInMillis;
    }

    public long getTimeSetInMillis() {
        return timeSetInMillis;
    }
}
