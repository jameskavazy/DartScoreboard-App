package com.example.dartscoreboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {


    private TextView gameTitle;
    private TextView playerName;
    private TextView playerCurrentScore;

    private Player testPlayer = new Player("Test1", 0);

    private HomeActivity.GameType gameType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("dom test", "GameActivityonCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        Log.d("dom test", "gameType\n-------\nname " + getGameType().name + "\nstartingScore " + getGameType().startingScore);

        setupUI();
    }

    private void setupUI() {
        gameTitle = findViewById(R.id.gameActivityTitle);
        playerName = findViewById(R.id.gameActivityPlayerName);
        playerCurrentScore = findViewById(R.id.gameActivityPoints);
        gameTitle.setText(getGameType().name);
        playerName.setText(testPlayer.name);
        testPlayer.currentScore = getGameType().startingScore; // todo this may be called again
        int i = testPlayer.currentScore;
        playerCurrentScore.setText(String.valueOf(testPlayer.currentScore));
    }

    private HomeActivity.GameType getGameType() {
        if (gameType != null) {
            return gameType;
        }

        Bundle arguments = getIntent().getExtras();
        gameType = (HomeActivity.GameType) arguments.getSerializable(HomeActivity.GAME_TYPE_KEY);
        return gameType;
    }

    class Player {
        String name;
        int currentScore;

        Player(String name, int currentScore) {
            this.name = name;
            this.currentScore = currentScore;
        }

        private void printOutDetails() {
            Log.d("dom test", "Name: " + name);
        }
    }
}