package com.example.dartscoreboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("dom test", "GameActivityonCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        Bundle arguments = getIntent().getExtras();
        HomeActivity.GameType gameType = (HomeActivity.GameType) arguments.getSerializable(HomeActivity.GAME_TYPE_KEY);
        Log.d("dom test", "gameType\n-------\nname " + gameType.name + "\nstartingScore " + gameType.startingScore);
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