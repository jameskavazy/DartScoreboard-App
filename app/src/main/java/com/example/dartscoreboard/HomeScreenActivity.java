package com.example.dartscoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private Button newGameButton;
    private Button usersButton;
    private Button statsButton;
    private Button continueButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("dom test", "newHomeActivityonCreate");
        super.onCreate(savedInstanceState);
        setupUI();
    }

    private void setupUI(){ // todo look lifecycle methods of when to set up ui, oncreateview?
        setContentView(R.layout.home_screen_activity);
        newGameButton = findViewById(R.id.startNewGameButton);
        usersButton = findViewById(R.id.usersButton);
        continueButton = findViewById(R.id.continueButton);
        newGameButton.setOnClickListener(this);
        usersButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);

        if (PreferencesController.getInstance().readGameState() != null) {
            continueButton.setVisibility(View.VISIBLE);
        } else {
            continueButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.startNewGameButton) {
            Log.d("dom test", "fiveoBtn click");
            onStartGameActivityClick();
        }
        if (v.getId() == R.id.usersButton){
            Log.d("dom test", "usersButton click");
            onUsersButtonClick();
        }
        if (v.getId() ==R.id.continueButton) {
            onContinueClicked();
        }
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
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

}