package com.example.dartscoreboard.NotificationService;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.dartscoreboard.R;
import com.example.dartscoreboard.SetupGame.SelectGameActivity;


public class ReminderNotificationService {

    private Context context;



    public static final String reminderChannel = "REMINDER_CHANNEL";

    public ReminderNotificationService(Context context){
        this.context = context;
    }

    public void showNotification(){
        Intent selectGameActivityIntent = new Intent(context, SelectGameActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                1,
                selectGameActivityIntent,
                PendingIntent.FLAG_IMMUTABLE
        );


        NotificationCompat.Builder notification = new NotificationCompat.Builder(context,reminderChannel)
                .setSmallIcon(R.drawable.baseline_timeline_24)
                .setContentTitle("Training Reminder")
                .setContentText("You haven't played any darts today")
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = getSystemService(context, NotificationManager.class);
        notificationManager.notify(1,notification.build());
    }
}

