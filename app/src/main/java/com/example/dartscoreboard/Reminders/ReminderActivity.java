package com.example.dartscoreboard.Reminders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.dartscoreboard.R;
import com.example.dartscoreboard.Utils.PreferencesController;

import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity implements View.OnClickListener {

    private AlarmManager alarmManager;

    private Intent receiverIntent;
    private PendingIntent pendingIntent;

    private TextView timeOfReminderTextView;
    private ReminderViewModel reminderViewModel;

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

    private void setupUI(){
        setContentView(R.layout.activity_set_reminder);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        reminderViewModel =  new ViewModelProvider(this).get(ReminderViewModel.class);
        timeOfReminderTextView = findViewById(R.id.current_reminder_time);
        Button setTimeButton = findViewById(R.id.set_time_button);
        Button cancelReminderButton = findViewById(R.id.cancel_reminder_button);
        cancelReminderButton.setOnClickListener(this);
        setTimeButton.setOnClickListener(this);
        if (PreferencesController.getInstance().getReminderTime() == null){
            clearReminderTimeTextView();
        } else timeOfReminderTextView.setText(PreferencesController.getInstance().getReminderTime());
    }

    @Override
    public void onClick(View v) {
        int viewID = v.getId();
        if (viewID == R.id.set_time_button){
            onSetTimeClicked();
        } else if (viewID == R.id.cancel_reminder_button){
            onCancelClicked();
        }
    }

    private void onSetTimeClicked() {
        openTimeDialog();
    }

    private void onCancelClicked(){
        cancelReminder();
    }

    private void cancelReminder() {
        if (alarmManager != null){
            alarmManager.cancel(PendingIntent.getBroadcast(getApplicationContext(),1,new Intent(), PendingIntent.FLAG_IMMUTABLE));
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
            timePicker.set(Calendar.HOUR_OF_DAY,hourOfDay);
            timePicker.set(Calendar.MINUTE, minute);
            timePicker.set(Calendar.SECOND,0);
            timePicker.set(Calendar.MILLISECOND,0);

            receiverIntent = new Intent(getApplicationContext(), ReminderNotificationReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),1,receiverIntent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, timePicker.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
            reminderViewModel.setHourOfDaySelected(hourOfDay);
            reminderViewModel.setMinuteOfDaySelected(minute);

            String minuteFormatted;
            if (minute <= 10){
                minuteFormatted = "0"+minute;
            } else minuteFormatted = String.valueOf(minute);

            String timeToDisplay = hourOfDay+":"+minuteFormatted;
            timeOfReminderTextView.setText(timeToDisplay);
            PreferencesController.getInstance().saveReminderTime(timeToDisplay);

        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), textClock.is24HourModeEnabled());
        timePickerDialog.show();
    }
}