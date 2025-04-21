package com.example.dartscoreboard.reminders;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.dartscoreboard.R;
import com.example.dartscoreboard.match.presentation.SetupMatchActivity;


public class ReminderNotificationService {

    private final Context context;

    public static final String reminderChannel = "REMINDER_CHANNEL";

    public ReminderNotificationService(Context context) {
        this.context = context;
    }
    public void showNotification() {
        Intent selectGameActivityIntent = new Intent(context, SetupMatchActivity.class);
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


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            notificationManager.notify(1, notification.build());
        }
    }
}

