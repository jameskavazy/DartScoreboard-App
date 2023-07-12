package com.example.dartscoreboard;

import static java.lang.String.valueOf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {


    private TextView gameTitle;
    private TextView playerName;
    private TextView playerNameTwo;
    private TextView playerCurrentScore;
    private TextView PlayerCurrentScoreTwo;
    private EditText inputScoreEditText;
    private Player testPlayer = new Player("test 1", 0);
    private Player testPlayer2 = new Player("test 2",0);

    private String playerNameKey;

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
        playerName = findViewById(R.id.gameActivityPlayerOneName);
        playerCurrentScore = findViewById(R.id.gameActivityPlayerCurrentScore);
        playerNameTwo = findViewById(R.id.gameActivityPlayerTwoName);
        PlayerCurrentScoreTwo = findViewById(R.id.gameActivityPlayerTwoCurrentScore);
        inputScoreEditText = findViewById(R.id.inputScoreEditText);


        gameTitle.setText(getGameType().name);
      //  playerName.setText(setPlayerOneName()); todo move this so that the information for player names is added on the home activity.

        testPlayer.currentScore = getGameType().startingScore;// todo this may be called again
        testPlayer2.currentScore = getGameType().startingScore;
        playerCurrentScore.setText(valueOf(testPlayer.currentScore));
        PlayerCurrentScoreTwo.setText(valueOf(testPlayer2.currentScore));



        //determines score for processing
        inputScoreEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Log.d("dom test", "IME_ACTION_DONE");
                onScoreEntered(inputScoreEditText.getText().toString());
//                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                ((EditText) findViewById(R.id.inputScoreEditText)).getText().clear();
                return true;
            }
            return false;
        });

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
           int scoreInt = Integer.parseInt(scoreString);
            Log.d("dom test", Integer.toString(scoreInt));
          //  scoreInt = currentTypedScore;
            testPlayer.currentScore = subtract(scoreInt);
            playerCurrentScore.setText(String.valueOf(testPlayer.currentScore));
            Log.d("dom test","Current Score: " + testPlayer.currentScore);





        } catch (NumberFormatException e) {
            Log.d("dom test", e.getMessage());
        }
    }

    public int subtract(int currentTypedScore) {
        int newScore = testPlayer.currentScore - currentTypedScore;
        if (( ((testPlayer.currentScore <= 180) && (testPlayer.currentScore >= 171)) || (testPlayer.currentScore == 169) || (testPlayer.currentScore == 168) || (testPlayer.currentScore == 166) || (testPlayer.currentScore == 165) || (testPlayer.currentScore == 163) || (testPlayer.currentScore == 162) || (testPlayer.currentScore == 159)) && (currentTypedScore == testPlayer.currentScore)){
            Toast.makeText(GameActivity.this, "Invalid Score", Toast.LENGTH_SHORT).show();
            return testPlayer.currentScore;
        }

        if (currentTypedScore > 180) {
            Toast.makeText(GameActivity.this, "Invalid Score", Toast.LENGTH_SHORT).show();
            return testPlayer.currentScore;
        }

        if (newScore > 1) {
            return newScore;
        }
        if (newScore == 0) {
            Toast.makeText(GameActivity.this, testPlayer.name + "wins!", Toast.LENGTH_LONG).show();
            return newScore;
        }
        else {
            Toast.makeText(GameActivity.this, "BUST", Toast.LENGTH_SHORT).show(); // toast
        }
        return testPlayer.currentScore;
    }

    private String setPlayerOneName(){
        Intent intent = getIntent();
        playerNameKey = intent.getStringExtra("send_name_one");
        return playerNameKey;

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

