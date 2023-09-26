package com.example.dartscoreboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class HomeScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private Button newGameButton;
    private Button usersButton;
    private Button statsButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("dom test", "newHomeActivityonCreate");
        super.onCreate(savedInstanceState);
        setupUI();
    }

    private void setupUI(){
        setContentView(R.layout.home_screen_activity);
        newGameButton = findViewById(R.id.startNewGameButton);
        usersButton = findViewById(R.id.usersButton);
        newGameButton.setOnClickListener(this);
        usersButton.setOnClickListener(this);

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

}