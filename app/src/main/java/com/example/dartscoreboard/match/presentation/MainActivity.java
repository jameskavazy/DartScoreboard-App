package com.example.dartscoreboard.match.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.dartscoreboard.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button continueButton;
    private Toolbar toolbar;

    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        signInButton = findViewById(R.id.sign_in_button);
        Button statsButton = findViewById(R.id.statsButton);
        statsButton.setOnClickListener(this);
        newGameButton.setOnClickListener(this);
        usersButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        setContinueBtnVisibility();
    }

    private void setContinueBtnVisibility() {
        continueButton.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == R.id.startNewGameButton) {
            onStartGameActivityClick();
        } else if (viewId == R.id.users_button){
            onUsersButtonClick();
        } else if (viewId == R.id.continueButton) {
            onContinueClicked();
        } else if (viewId == R.id.statsButton){
            onStatsButtonClicked();
        } else if (viewId == R.id.sign_in_button) {
            onSignInClicked();
        }
    }

    private void onSignInClicked() {

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
        openStartGameActivity();
    }

    private void openStartGameActivity(){
        Intent intent = new Intent(this, SetupMatchActivity.class);
        startActivity(intent);
    }

    private void onContinueClicked() {
        openMatchHistoryActivity();
    }

    private void openMatchHistoryActivity(){
        Intent intent = new Intent(this, MatchHistoryActivity.class);
        startActivity(intent);
    }
}