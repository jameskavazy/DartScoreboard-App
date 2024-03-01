package com.example.dartscoreboard.Reminders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReminderNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ReminderNotificationService service = new ReminderNotificationService(context);
        service.showNotification();
    }
}
