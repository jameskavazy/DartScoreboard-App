package com.example.dartscoreboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
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
    private int currentUserScore;

    private int scoreInt;



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
        int i = testPlayer.currentScore; // todo find commented out work from Dom underneath to get the current "typedScore" which is a Integer "parse" Int method. i is the current playerscore for test player.
        playerCurrentScore.setText(String.valueOf(testPlayer.currentScore));


        inputScoreEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Log.d("dom test", "IME_ACTION_DONE");
                onScoreEntered(inputScoreEditText.getText().toString());
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                ((EditText) findViewById(R.id.inputScoreEditText)).getText().clear();

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

    public int subtract() {
        int new_score = testPlayer.currentScore - scoreInt;
        if (( ((testPlayer.currentScore <= 180) && (testPlayer.currentScore >= 171)) || (testPlayer.currentScore == 169) || (testPlayer.currentScore == 168) || (testPlayer.currentScore == 166) || (testPlayer.currentScore == 165) || (testPlayer.currentScore == 163) || (testPlayer.currentScore == 162) || (testPlayer.currentScore == 159)) && (scoreInt == testPlayer.currentScore)){
            Toast.makeText(GameActivity.this, "Invalid Score", Toast.LENGTH_SHORT).show();
            return testPlayer.currentScore;
        }
        if (new_score == 0 || new_score > 1) {
            return new_score;
        }
        else {
            Toast.makeText(GameActivity.this, "BUST", Toast.LENGTH_SHORT).show(); // toast
        } return testPlayer.currentScore;
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