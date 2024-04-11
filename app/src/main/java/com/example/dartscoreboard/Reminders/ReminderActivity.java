package com.example.dartscoreboard.Reminders;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.dartscoreboard.R;
import com.example.dartscoreboard.Utils.PreferencesController;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity implements View.OnClickListener {

    private AlarmManager alarmManager;

    private Intent receiverIntent;
    private PendingIntent pendingIntent;

    private TextView timeOfReminderTextView;
    private ReminderViewModel reminderViewModel;

    private SwitchMaterial toggleNotificationPermissionsSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Set Reminder");
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean currentPermission = checkNotificationPermission(this);
        // Optionally, display a message about permission change
        toggleNotificationPermissionsSwitch.setChecked(currentPermission);
    }

    private void setupUI() {
        setContentView(R.layout.activity_set_reminder);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);
        timeOfReminderTextView = findViewById(R.id.current_reminder_time);
        Button setTimeButton = findViewById(R.id.set_time_button);
        Button cancelReminderButton = findViewById(R.id.cancel_reminder_button);
        toggleNotificationPermissionsSwitch = findViewById(R.id.toggleNotificationPermissions);
        toggleNotificationPermissionsSwitch.setOnClickListener(this);
        toggleNotificationPermissionsSwitch.setChecked(checkNotificationPermission(this));
        toggleNotificationPermissionsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (!checkNotificationPermission(ReminderActivity.this)) {
                        requestNotificationPermission(ReminderActivity.this,222);
                    } else {
                        //permission already granted
                    }
                } else {
                    // Switch turned off, open system settings to block notifications
                    openNotificationSettings();

                }
            }
        });

        cancelReminderButton.setOnClickListener(this);
        setTimeButton.setOnClickListener(this);
        if (PreferencesController.getInstance().getReminderTime() == null) {
            clearReminderTimeTextView();
        } else
            timeOfReminderTextView.setText(PreferencesController.getInstance().getReminderTime());
    }

    @Override
    public void onClick(View v) {
        int viewID = v.getId();
        if (viewID == R.id.set_time_button) {
            onSetTimeClicked();
        } else if (viewID == R.id.cancel_reminder_button) {
            onCancelClicked();
        }
//        else if (viewID == R.id.toggleNotificationPermissions){
//            if (toggleNotificationPermissionsSwitch.isChecked()){
//                //remove permissions
//            } else {
//                requestNotificationPermission(this,222);
//                toggleNotificationPermissionsSwitch.setChecked(true);
//
//
////                if (checkNotificationPermission(this)) {
////                    toggleNotificationPermissionsSwitch.setActivated(true);
//////                Toast.makeText(this, "Notification Permission Already Granted", Toast.LENGTH_SHORT).show();
////                } else {
////                    requestNotificationPermission(this, 222);
////                }
//            }
//        }
    }

    private void onSetTimeClicked() {
        openTimeDialog();
    }

    private void onCancelClicked() {
        cancelReminder();
    }

    private void cancelReminder() {
        if (alarmManager != null) {
            alarmManager.cancel(PendingIntent.getBroadcast(getApplicationContext(), 1, new Intent(), PendingIntent.FLAG_IMMUTABLE));
            clearReminderTimeTextView();
            PreferencesController.getInstance().clearReminderTime();
        }
    }

    private void clearReminderTimeTextView() {
        timeOfReminderTextView.setText("--:--");
    }

    private void openTimeDialog() {
        TextClock textClock = new TextClock(this);
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            Calendar timePicker = Calendar.getInstance();
            timePicker.set(Calendar.HOUR_OF_DAY, hourOfDay);
            timePicker.set(Calendar.MINUTE, minute);
            timePicker.set(Calendar.SECOND, 0);
            timePicker.set(Calendar.MILLISECOND, 0);

            //TODO broadcast receiver for battery saver detected - alert user this function may not perform as intended.

            receiverIntent = new Intent(getApplicationContext(), ReminderNotificationReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, receiverIntent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, timePicker.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            reminderViewModel.setHourOfDaySelected(hourOfDay);
            reminderViewModel.setMinuteOfDaySelected(minute);

            String minuteFormatted;
            if (minute <= 10) {
                minuteFormatted = "0" + minute;
            } else minuteFormatted = String.valueOf(minute);

            String timeToDisplay = hourOfDay + ":" + minuteFormatted;
            timeOfReminderTextView.setText(timeToDisplay);
            PreferencesController.getInstance().saveReminderTime(timeToDisplay);

        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), textClock.is24HourModeEnabled());
        timePickerDialog.show();
    }

    public static boolean checkNotificationPermission(Activity activity) { //true if GRANTED
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public static void requestNotificationPermission(Activity activity, int requestId) {
        boolean isGranted = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            isGranted = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        }

        if (!isGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.POST_NOTIFICATIONS,}, requestId);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 222) {
                Toast.makeText(this, "NotificationPermission Granted Successfully", Toast.LENGTH_SHORT).show();
            }
        } else {
            toggleNotificationPermissionsSwitch.setChecked(false);
        }
    }

    private void openNotificationSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(intent);
    }
}