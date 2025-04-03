package com.example.dartscoreboard.Reminders;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.dartscoreboard.R;
import com.example.dartscoreboard.util.PermissionCheckController;
import com.example.dartscoreboard.util.PreferencesController;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity implements View.OnClickListener {

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
        boolean currentPermission = PermissionCheckController.getInstance().checkNotificationPermission(this);
        toggleNotificationPermissionsSwitch.setChecked(currentPermission);
    }

    private void setupUI() {
        setContentView(R.layout.activity_set_reminder);
        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);
        timeOfReminderTextView = findViewById(R.id.current_reminder_time);
        Button setTimeButton = findViewById(R.id.set_time_button);
        Button cancelReminderButton = findViewById(R.id.cancel_reminder_button);
        toggleNotificationPermissionsSwitch = findViewById(R.id.toggleNotificationPermissions);
        toggleNotificationPermissionsSwitch.setChecked(PermissionCheckController.getInstance().checkNotificationPermission(this));
        toggleNotificationPermissionsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                if (!PermissionCheckController.getInstance().checkNotificationPermission(ReminderActivity.this)) {
                    PermissionCheckController.getInstance().requestNotificationPermission(ReminderActivity.this,222);
                }
            } else {
                // Switch turned off, open system settings to block notifications
                openNotificationSettings();
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
            clearReminderTimeTextView();
        }
    }

    private void onSetTimeClicked() {
        openTimeDialog();
    }

    private void onCancelClicked() {
        reminderViewModel.cancelReminder();
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
            reminderViewModel.setTimeSetInMillis(timePicker.getTimeInMillis());
            reminderViewModel.setReceiverAlarm();
            timeOfReminderTextView.setText(formatDate(hourOfDay,minute));
            PreferencesController.getInstance().saveReminderTime(formatDate(hourOfDay,minute));
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), textClock.is24HourModeEnabled());
        timePickerDialog.show();
        showBatterSaverWarning();

    }

    private void showBatterSaverWarning(){
        if (reminderViewModel.isPowerSaveActive()){
            Snackbar snackbar = Snackbar.make(findViewById(R.id.reminder_coordinator_layout),
                    "Batter saver mode detected. Reminder functionality may not work correctly.",Snackbar.LENGTH_INDEFINITE).setAction("OK", v -> {

                    });
            snackbar.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 222) {
                Toast.makeText(this, "Notification Permission Granted", Toast.LENGTH_SHORT).show();
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

    private String formatDate(int hourOfDay, int minute){
        String minuteFormatted;
        if (minute < 10) {
            minuteFormatted = "0" + minute;
        } else minuteFormatted = String.valueOf(minute);
        return hourOfDay + ":" + minuteFormatted;
    }
}