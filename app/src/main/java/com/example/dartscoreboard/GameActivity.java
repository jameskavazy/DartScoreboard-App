package com.example.dartscoreboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {


    private TextView gameTitle;
    private TextView playerName;
    private TextView playerCurrentScore;
    private EditText inputScoreEditText;
    private Player testPlayer = new Player("Test1", 0);

    private int currentTypedScore;


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
        playerCurrentScore = findViewById(R.id.gameActivityPlayerCurrentScore);
        inputScoreEditText = findViewById(R.id.inputScoreEditText);

        gameTitle.setText(getGameType().name);
        playerName.setText(testPlayer.name);
        testPlayer.currentScore = getGameType().startingScore; // todo this may be called again
        int i = testPlayer.currentScore;
        playerCurrentScore.setText(String.valueOf(testPlayer.currentScore));


        inputScoreEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Log.d("dom test", "IME_ACTION_DONE");
                onScoreEntered(inputScoreEditText.getText().toString());
                return true;
            }
            return false;
        });

//        inputScoreEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
////                Log.d("dom test", "onTextChanged" + s);
////                try {
////                   currentTypedScore = Integer.parseInt((String) s);
////                    Log.d("dom test", String.valueOf(currentTypedScore));
////                } catch (NumberFormatException e) {
////                    Log.d("dom test", e.getMessage());
////                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                Log.d("dom test", "afterTextChanged");
//
//            }
//        });
    }

    private HomeActivity.GameType getGameType() {
        if (gameType != null) {
            return gameType;
        }

        Bundle arguments = getIntent().getExtras();
        gameType = (HomeActivity.GameType) arguments.getSerializable(HomeActivity.GAME_TYPE_KEY);
        return gameType;
    }

    private void onScoreEntered(String scoreString) {
        try {
            // todo add logic to check valid input
            int scoreInt = Integer.parseInt(scoreString);
            Log.d("dom test", Integer.toString(scoreInt));

            if (scoreInt > 180) {
                Toast.makeText(GameActivity.this, "Invalid Score: Score cannot be over 180", Toast.LENGTH_SHORT).show();
            }

        } catch (NumberFormatException e) {
            Log.d("dom test", e.getMessage());
        }
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