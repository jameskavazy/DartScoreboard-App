package com.example.dartscoreboard.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.dartscoreboard.Reminders.ReminderNotificationReceiver;

import java.util.Calendar;
import java.util.Objects;

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED)){
            //Gets saved time from prefs, parse data and set to calendar instance for resetting reminder

            if (PreferencesController.getInstance().getReminderTime() == null) return;

            String hourOfDay = PreferencesController.getInstance().getReminderTime();
            String[] timeArray = hourOfDay.split(":");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
            calendar.set(Calendar.MINUTE,Integer.parseInt(timeArray[1]));
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent receiverIntent = new Intent(context, ReminderNotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,1,receiverIntent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
}
