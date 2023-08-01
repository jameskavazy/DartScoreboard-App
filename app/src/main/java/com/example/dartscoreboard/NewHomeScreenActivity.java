package com.example.dartscoreboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class NewHomeScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private Button newGameButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("dom test", "newHomeActivityonCreate");
        super.onCreate(savedInstanceState);
        setupUI();
    }

    private void setupUI(){
        setContentView(R.layout.activity_new_home_screen);
        newGameButton = findViewById(R.id.startNewGameButton);
        newGameButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.startNewGameButton) {
            Log.d("dom test", "fiveoBtn click");
            onStartGameActivityClick();
        }
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