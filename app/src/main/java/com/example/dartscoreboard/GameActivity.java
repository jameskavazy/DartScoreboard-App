package com.example.dartscoreboard;

import static java.lang.String.valueOf;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {


    private TextView gameTitle;
    private EditText inputScoreEditText;
    private Player testPlayer = new Player("test 1", 0, true);
    private Player testPlayer2 = new Player("test 2",0,false);



    private SelectGameActivity.GameType gameType;

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
        inputScoreEditText = findViewById(R.id.inputUserNameEditText);
        //addPlayerNames();


        gameTitle.setText(getGameType().name);


//        testPlayer.currentScore = getGameType().startingScore;// todo this may be called again
//        testPlayer2.currentScore = getGameType().startingScore;
//        playerCurrentScore.setText(valueOf(testPlayer.currentScore));
//        playerCurrentScoreTwo.setText(valueOf(testPlayer2.currentScore));
//
//            if (testPlayer.playerTurn) {
//                testPlayer.turn();
//            }
//            else {
//                testPlayer2.turn();
//            }
    }
    private SelectGameActivity.GameType getGameType() {
        if (gameType != null) {
            return gameType;
        }

        Bundle arguments = getIntent().getExtras();
        gameType = (SelectGameActivity.GameType) arguments.getSerializable(SelectGameActivity.GAME_TYPE_KEY);
        return gameType;
    }

//    private void addPlayerNames(){
////        Bundle arguments = getIntent().getExtras();
////        String nameToAdd = arguments.getString("PLAYER_NAME");
////        String nameToAdd2 = arguments.getString("PLAYER_NAME_2");
//
//
////        ArrayList<User> playersForGame = new ArrayList<>();
////        for (int i = 0; i <playersForGame.size(); i++){
////
//////        }
////        playerName.setText(testPlayer.name = PrefConfig.readUsersForGameSP(this).get(0).getUsername());
////        Log.d("dom test",PrefConfig.readUsersForGameSP(this).get(0).getUsername());
////        playerNameTwo.setText(testPlayer2.name = PrefConfig.readUsersForGameSP(this).get(1).getUsername());
//
//    }

//    private void onScoreEntered(String scoreString) {
//        try {
//            if (testPlayer.playerTurn) {
//                int scoreInt = Integer.parseInt(scoreString);
//                Log.d("dom test", Integer.toString(scoreInt));
//                testPlayer.currentScore = subtract(testPlayer.currentScore, scoreInt);
//                Log.d("dom test","score subtract run");
//                playerCurrentScore.setText(String.valueOf(testPlayer.currentScore));
//                Log.d("dom test","Current Score: " + testPlayer.currentScore);
//            }
//
//            else {
//                int scoreInt = Integer.parseInt(scoreString);
//                Log.d("dom test", Integer.toString(scoreInt));
//                testPlayer2.currentScore = subtract(testPlayer2.currentScore, scoreInt);
//                playerCurrentScoreTwo.setText(String.valueOf(testPlayer2.currentScore));
//                Log.d("dom test","Current Score: " + testPlayer2.currentScore);
//            }
//
//
//        } catch (NumberFormatException e) {
//            Log.d("dom test", e.getMessage());
//        }
//    }



//    public int subtract(int playerScore, int currentTypedScore) {
//        int playerIndicator = Color.rgb(42,213,114);
//        int newScore = playerScore - currentTypedScore;
//        if (( ((playerScore <= 180) && (playerScore >= 171)) || (playerScore == 169) || (playerScore == 168) || (playerScore == 166) || (playerScore == 165) || (playerScore == 163) || (playerScore == 162) || (playerScore == 159)) && (currentTypedScore == playerScore)){
//            if (testPlayer.playerTurn) {
//                playerName.setBackgroundColor(playerIndicator);
//                playerNameTwo.setBackgroundColor(Color.WHITE);
//                testPlayer.playerTurn = true;
//                testPlayer2.playerTurn = false;
//            }
//            else {
//                playerNameTwo.setBackgroundColor(playerIndicator);
//                playerName.setBackgroundColor(Color.WHITE);
//                testPlayer.playerTurn = false;
//                testPlayer2.playerTurn = true;
//
//            }
//            Toast.makeText(GameActivity.this, "Invalid Score", Toast.LENGTH_SHORT).show();
//            return playerScore;
//        }
//
//        if (currentTypedScore > 180) {
//            if (testPlayer.playerTurn) {
//                playerName.setBackgroundColor(playerIndicator);
//                playerNameTwo.setBackgroundColor(Color.WHITE);
//                testPlayer.playerTurn = true;
//                testPlayer2.playerTurn = false;
//            }
//            else {
//                playerNameTwo.setBackgroundColor(playerIndicator);
//                playerName.setBackgroundColor(Color.WHITE);
//                testPlayer.playerTurn = false;
//                testPlayer2.playerTurn = true;
//            }
//            Toast.makeText(GameActivity.this, "Invalid Score", Toast.LENGTH_SHORT).show();
//            return playerScore;
//        }
//
//        if (newScore > 1) {
//            if (testPlayer.playerTurn) {
//                playerNameTwo.setBackgroundColor(playerIndicator);
//                playerName.setBackgroundColor(Color.WHITE);
//                testPlayer.playerTurn = false;
//                testPlayer2.playerTurn = true;
//            } else {
//                playerName.setBackgroundColor(playerIndicator);
//                playerNameTwo.setBackgroundColor(Color.WHITE);
//                testPlayer2.playerTurn = false;
//                testPlayer.playerTurn = true;
//            }
//            return newScore;
//        }
//        if (newScore == 0) {
//            if (testPlayer.playerTurn) {
//                playerName.setBackgroundColor(playerIndicator);
//                playerNameTwo.setBackgroundColor(Color.WHITE);
//                Toast.makeText(GameActivity.this, testPlayer.name + " wins!", Toast.LENGTH_LONG).show();
//            } else {
//                playerNameTwo.setBackgroundColor(playerIndicator);
//                playerName.setBackgroundColor(Color.WHITE);
//                Toast.makeText(GameActivity.this, testPlayer2.name + " wins!", Toast.LENGTH_LONG).show();
//            }
//            return newScore;
//        }
//        else {
//            if (testPlayer.playerTurn) {
//
//                testPlayer.playerTurn = false;
//                testPlayer2.playerTurn = true;
//                playerNameTwo.setBackgroundColor(playerIndicator);
//                playerName.setBackgroundColor(Color.WHITE);
//            }
//            else {
//                testPlayer.playerTurn = true;
//                testPlayer2.playerTurn = false;
//                playerName.setBackgroundColor(playerIndicator);
//                playerNameTwo.setBackgroundColor(Color.WHITE);
//
//            }
//            Toast.makeText(GameActivity.this, "BUST", Toast.LENGTH_SHORT).show();
//
//        return playerScore;
//        }
//
//    }







}

