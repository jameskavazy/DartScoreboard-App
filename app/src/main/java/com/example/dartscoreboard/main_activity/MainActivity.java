package com.example.dartscoreboard.main_activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.dartscoreboard.live_matches.presentation.LiveProMatchesActivity;
import com.example.dartscoreboard.match.presentation.MatchHistoryActivity;
import com.example.dartscoreboard.R;
import com.example.dartscoreboard.reminders.ReminderActivity;
import com.example.dartscoreboard.match.presentation.SetupGameActivity;
import com.example.dartscoreboard.statistics.presentation.StatisticsActivity;
import com.example.dartscoreboard.user.UsersActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button continueButton;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("dom test", "MainActivityOnCreate");
        super.onCreate(savedInstanceState);
        setupUI();
    }



    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toolbar.setTitle("Dart Scoreboard");
    }

    private void setupUI(){ // todo look lifecycle methods of when to set up ui, oncreateview?
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        Button newGameButton = findViewById(R.id.startNewGameButton);
        Button usersButton = findViewById(R.id.users_button);
        continueButton = findViewById(R.id.continueButton);
        Button statsButton = findViewById(R.id.statsButton);
        Button trainingReminderButton = findViewById(R.id.trainingRemindersButton);
        Button liveProMatchesButton = findViewById(R.id.live_pro_matches_button);
        statsButton.setOnClickListener(this);
        newGameButton.setOnClickListener(this);
        usersButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);
        trainingReminderButton.setOnClickListener(this);
        liveProMatchesButton.setOnClickListener(this);
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
        } else if (viewId == R.id.users_button){
            Log.d("dom test", "usersButton click");
            onUsersButtonClick();
        } else if (viewId == R.id.continueButton) {
            onContinueClicked();
        } else if (viewId == R.id.statsButton){
            onStatsButtonClicked();
        } else if (viewId == R.id.trainingRemindersButton){
            onTrainingReminderClicked();
        } else if (viewId == R.id.live_pro_matches_button){
            onLiveProMatchesButtonClicked();
        }
    }

    private void onLiveProMatchesButtonClicked() {
        openLiveMatchesActivity();
    }

    private void openLiveMatchesActivity() {
        Intent intent = new Intent(this, LiveProMatchesActivity.class);
        startActivity(intent);
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
        Intent intent = new Intent(this, SetupGameActivity.class);
        startActivity(intent);
    }

    private void onContinueClicked() {
        Log.d("dom test", "onContinueClicked");
        openMatchHistoryActivity();
    }

    private void openMatchHistoryActivity(){
        Intent intent = new Intent(this, MatchHistoryActivity.class);
        startActivity(intent);
    }


    private void onTrainingReminderClicked(){
        openTrainingReminderActivity();
    }

    private void openTrainingReminderActivity() {
        Intent intent = new Intent(this, ReminderActivity.class);
        startActivity(intent);
    }
}