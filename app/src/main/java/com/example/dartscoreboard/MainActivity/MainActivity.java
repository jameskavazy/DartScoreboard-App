package com.example.dartscoreboard.MainActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.dartscoreboard.MatchHistory.MatchHistoryActivity;
import com.example.dartscoreboard.NotificationService.ReminderNotificationReceiver;
import com.example.dartscoreboard.R;
import com.example.dartscoreboard.SetupGame.SelectGameActivity;
import com.example.dartscoreboard.Statistics.StatisticsActivity;
import com.example.dartscoreboard.User.UsersActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button newGameButton;
    private Button usersButton;
    private Button statsButton;
    private Button continueButton;
    private Toolbar toolbar;

    private Calendar calendar;

    private AlarmManager alarmManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("dom test", "MainActivityOnCreate");
        super.onCreate(savedInstanceState);
        setupUI();
        setReminderNotificationTime();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toolbar.setTitle("Dart Scoreboard");
    }

    private void setupUI(){ // todo look lifecycle methods of when to set up ui, oncreateview?
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        newGameButton = findViewById(R.id.startNewGameButton);
        usersButton = findViewById(R.id.usersButton);
        continueButton = findViewById(R.id.continueButton);
        statsButton = findViewById(R.id.statsButton);
        statsButton.setOnClickListener(this);
        newGameButton.setOnClickListener(this);
        usersButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);
        setContinueBtnVisibility();
    }

    private void setContinueBtnVisibility() {
        continueButton.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == R.id.startNewGameButton) {
            Log.d("dom test", "fiveoBtn click");
            onStartGameActivityClick();
        } else if (viewId == R.id.usersButton){
            Log.d("dom test", "usersButton click");
            onUsersButtonClick();
        } else if (viewId == R.id.continueButton) {
            onContinueClicked();
        } else if (viewId == R.id.statsButton){
            onStatsButtonClicked();
        }
    }

    private void onStatsButtonClicked() {
        openStatsActivity();
    }

    private void openStatsActivity() {
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
    }

    private void onUsersButtonClick() {
        openUsersActivity();
    }

    private void openUsersActivity() {
        Intent intent = new Intent(this, UsersActivity.class);
        startActivity(intent);
    }

    private void onStartGameActivityClick(){
        Log.d("dom test", "onStartGameActivity");
        openStartGameActivity();
    }

    private void openStartGameActivity(){
        Log.d("dom test", "openStartGameActivity");
        Intent intent = new Intent(this, SelectGameActivity.class);
        startActivity(intent);
    }

    private void onContinueClicked() {
        Log.d("dom test", "onContinueClicked");
        Intent intent = new Intent(this, MatchHistoryActivity.class);
        startActivity(intent);
    }

    public void setReminderNotificationTime(){
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,20);
        calendar.set(Calendar.MINUTE, 15);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
    }


}