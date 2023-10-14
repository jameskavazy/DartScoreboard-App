package com.example.dartscoreboard;

import static java.lang.String.valueOf;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SelectGameActivity extends AppCompatActivity implements View.OnClickListener {

    public static String GAME_TYPE_KEY = "GAME_TYPE";

    private Button startGameBtn;

    private Button randomisePlayersBtn;
    private Button clearPlayersBtn;
    String[] gameSelectList = {"501","301","170"};

    ArrayList<User> playersToGame;

    ArrayList<Integer> playerList = new ArrayList<>();

    AutoCompleteTextView autoCompleteTextView;
    AutoCompleteTextView playerListCheckBox;
    ArrayAdapter<String> adapterItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("dom test", "HomeActivityonCreate");
        super.onCreate(savedInstanceState);
        setupUI();
    }

    private void setupUI() {
        Log.d("dom test", "setupUI");
        setContentView(R.layout.select_game_activity);
        startGameBtn = findViewById(R.id.gameStartButton);
        randomisePlayersBtn = findViewById(R.id.randomise_players_button);
        clearPlayersBtn = findViewById(R.id.remove_players_button);
        playerListCheckBox = findViewById(R.id.NameDropDownBox);
        startGameBtn.setOnClickListener(this);
        randomisePlayersBtn.setOnClickListener(this);
        clearPlayersBtn.setOnClickListener(this);
        autoCompleteTextView = findViewById(R.id.gameTypeDropDownBox);
        setUpGameTypeDropDownMenu();
        playerListCheckBox.setOnClickListener(this);
        setPlayersTextBox();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.gameStartButton){
            openGameActivity();
        }
        if (v.getId() == R.id.NameDropDownBox){
           //alertDialogueLaunch();
           openPlayerSelectActivity();
        }
        if (v.getId() == R.id.remove_players_button){
            playersToGame = PrefConfig.readUsersForGameSP(this);
            playersToGame.clear();
            PrefConfig.saveUsersForGameSP(getApplicationContext(),playersToGame);
            playerListCheckBox.setText("");
        }
        if (v.getId() == R.id.randomise_players_button){
            playersToGame = PrefConfig.readUsersForGameSP(this);
            Collections.shuffle(playersToGame);
            PrefConfig.saveUsersForGameSP(getApplicationContext(),playersToGame);
            setPlayersTextBox();
        }
    }

    public void openPlayerSelectActivity(){
        Intent intent = new Intent(this, PlayerSelectActivity.class);
        startActivity(intent);
    }

    public void openGameActivity(){
        if (autoCompleteTextView.getText().toString().equals("501")){
            Log.d("dom test","openFiveoGameActivity");
            openFiveoGameActivity();
        }
        if (autoCompleteTextView.getText().toString().equals("301")){
            Log.d("dom test","openThreeoGameActivity");
            openThreeoGameActivity();
        }
        if (autoCompleteTextView.getText().toString().equals("170")){
            Log.d("dom test","openSevenoGameActivity");
            openSevenoGameActivity();
        }
//        else if (autoCompleteTextView.getText().toString() == "") {
//            Toast.makeText(this,"You must select a game type",Toast.LENGTH_SHORT).show();
//        }
    }

    private void openFiveoGameActivity() {
//        String playersFromTextView = playerListCheckBox.getText().toString();
//        String[] listOfPlayersFromTextView = playersFromTextView.split(",");
        Log.d("dom test", "openFiveoGameActivity");
        Bundle arguments = new Bundle();
        arguments.putSerializable("GAME_TYPE", GameType.FiveO);
//        arguments.putString("PLAYER_NAME",listOfPlayersFromTextView[0]);
//        arguments.putString("PLAYER_NAME_2",listOfPlayersFromTextView[1]);
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtras(arguments);
        startActivity(intent);
    }

    private void openThreeoGameActivity() {
//        String playersFromTextView = playerListCheckBox.getText().toString();
//        String[] listOfPlayersFromTextView = playersFromTextView.split(",");
        Log.d("dom test", "openThreeoGameActivity");
        Bundle arguments = new Bundle();
        arguments.putSerializable("GAME_TYPE", GameType.ThreeO);
//        arguments.putString("PLAYER_NAME",listOfPlayersFromTextView[0]);
//        arguments.putString("PLAYER_NAME_2",listOfPlayersFromTextView[1]);
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtras(arguments);
        startActivity(intent);
    }

    private void openSevenoGameActivity() {
        String playersFromTextView = playerListCheckBox.getText().toString();
        String[] listOfPlayersFromTextView = playersFromTextView.split(",");
        Log.d("dom test", "openSevenoGameActivity");
        Bundle arguments = new Bundle();
        arguments.putSerializable("GAME_TYPE", GameType.SevenO);
        arguments.putString("PLAYER_NAME",listOfPlayersFromTextView[0]);
        arguments.putString("PLAYER_NAME_2",listOfPlayersFromTextView[1]);
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtras(arguments);
        startActivity(intent);
    }

    private void setUpGameTypeDropDownMenu(){
    adapterItems = new ArrayAdapter<String>(this,R.layout.list_item,gameSelectList);
    autoCompleteTextView.setAdapter(adapterItems);
    }

    private void setPlayersTextBox(){
        playerListCheckBox.setText("");
        ArrayList<User> playersToGame = PrefConfig.readUsersForGameSP(this);
        if (playersToGame != null){
            String[] namesToGame = new String[playersToGame.size()];
            for (int i = 0; i < playersToGame.size(); i++) {
                namesToGame[i] = playersToGame.get(i).getUsername();
            }

            String playersToGameString = String.join(", ",namesToGame);
            playerListCheckBox.setText(playersToGameString);
            Log.d("dom test",playersToGameString);
        }
    }


    enum GameType {
        FiveO("501", 501),
        ThreeO("301", 301),
        SevenO("170", 170);

        String name;
        int startingScore;

        GameType(String name, int startingScore) {
            this.name = name;
            this.startingScore = startingScore;
        }
    }



}




