package com.example.dartscoreboard.Reminders;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.dartscoreboard.R;
import com.example.dartscoreboard.SetupGame.SelectGameActivity;


public class ReminderNotificationService {

    private final Context context;

    public static final String reminderChannel = "REMINDER_CHANNEL";

    public ReminderNotificationService(Context context) {
        this.context = context;
    }

    public void showNotification() {
        Intent selectGameActivityIntent = new Intent(context, SelectGameActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                1,
                selectGameActivityIntent,
                PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, reminderChannel)
                .setSmallIcon(R.drawable.baseline_timeline_24)
                .setCategory(String.valueOf(NotificationCompat.PRIORITY_HIGH))
                .setContentTitle("Training Reminder")
                .setContentText("You haven't played any darts today")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

//        NotificationManager notificationManager = getSystemService(context, NotificationManager.class);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: 01/03/2024 We need to remove the USE EXACT Alarm permission - as this will not be accepted by Google Play Store,
            //  the reminder is minor feature therefore not crucial to app therefore MUST request permissions.


            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        notificationManager.notify(1, notification.build());
    }
}

