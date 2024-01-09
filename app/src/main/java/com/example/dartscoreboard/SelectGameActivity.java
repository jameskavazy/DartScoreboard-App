package com.example.dartscoreboard;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class SelectGameActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String GAME_TYPE_KEY = "GAME_TYPE";
    public static final String GAME_SETTINGS_KEY = "GAME_SETTINGS_KEY";
    public static final String PLAYERS_FOR_GAME_KEY = "PLAYERS_FOR_GAME_KEY";

    String[] gameSelectList = {"501","301","170"};

    String[] numberOfLegsSetsList = {"1","2","3","4","5"};

    ArrayList<User> playersToGame;
    AutoCompleteTextView gameTypeAutoCompleteTextView;
    AutoCompleteTextView legsAutoCompleteTextView;

    AutoCompleteTextView setsAutoCompleteTextView;

    AutoCompleteTextView playerListCheckBox;

    ArrayAdapter<String> adapterItems;

    ArrayAdapter<String> adapterLegsItems;

    ArrayAdapter<String> adapterSetsItems;

    private Stack<MatchState> matchStateStack = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("dom test", "HomeActivityOnCreate");
        super.onCreate(savedInstanceState);
        setupUI();
    }

    private void setupUI() {
        Log.d("dom test", "setupUI");
        setContentView(R.layout.select_game_activity);
        Button startGameBtn = findViewById(R.id.gameStartButton);
        Button randomisePlayersBtn = findViewById(R.id.randomise_players_button);
        Button clearPlayersBtn = findViewById(R.id.remove_players_button);
        playerListCheckBox = findViewById(R.id.NameDropDownBox);
        startGameBtn.setOnClickListener(this);
        randomisePlayersBtn.setOnClickListener(this);
        clearPlayersBtn.setOnClickListener(this);
        gameTypeAutoCompleteTextView = findViewById(R.id.gameTypeDropDownBox);
        if (PreferencesController.getInstance().getGameSelected() != null){
            gameTypeAutoCompleteTextView.setText(PreferencesController.getInstance().getGameSelected());
        }
        legsAutoCompleteTextView = findViewById(R.id.legs_drop_down);
        setsAutoCompleteTextView = findViewById(R.id.sets_drop_down);
        if (playersToGame == null){
            playersToGame = new ArrayList<>();
        }
        playersToGame = PreferencesController.readUsersForGameSP(this);
        setUpGameTypeDropDownMenu();
        setUpLegsListDropDownMenu();
        setUpSetsListDropDownMenu();
        playerListCheckBox.setOnClickListener(this);
        setPlayersTextBox();
        gameTypeAutoCompleteTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.gameStartButton){ // switch statement
            openGameActivity();
            PreferencesController.getInstance().clearSelectedGame();
            finish();
        }
        if (v.getId() == R.id.NameDropDownBox){
           openPlayerSelectActivity();
           PreferencesController.getInstance().saveSelectedGame(gameTypeAutoCompleteTextView.getText().toString());
           finish();
        }
        if (v.getId() == R.id.remove_players_button){
            playersToGame = PreferencesController.readUsersForGameSP(this);
            playersToGame.clear();
            PreferencesController.saveUsersForGameSP(getApplicationContext(),playersToGame);
            playerListCheckBox.setText("");
        }
        if (v.getId() == R.id.randomise_players_button){
            playersToGame = PreferencesController.readUsersForGameSP(this);
            Collections.shuffle(playersToGame);
            PreferencesController.saveUsersForGameSP(getApplicationContext(),playersToGame);
            setPlayersTextBox();
        }
    }

    public void openPlayerSelectActivity(){
        Intent intent = new Intent(this, PlayerSelectActivity.class);
        startActivity(intent);
    }

    public void openGameActivity(){
        if (gameTypeAutoCompleteTextView.getText().toString().equals("501")){ // todo make switch
            Log.d("dom test","openFiveoGameActivity");
            openFiveoGameActivity();
        }
        if (gameTypeAutoCompleteTextView.getText().toString().equals("301")){
            Log.d("dom test","openThreeoGameActivity");
            openThreeoGameActivity();
        }
        if (gameTypeAutoCompleteTextView.getText().toString().equals("170")){
            Log.d("dom test","openSevenoGameActivity");
            openSevenoGameActivity();
        }
        else if (gameTypeAutoCompleteTextView.getText().toString().isEmpty()) {
            Toast.makeText(this,"You must select a game type",Toast.LENGTH_SHORT).show();
        }
    }

    private void openFiveoGameActivity() {
        Log.d("dom test", "openFiveoGameActivity");
        launchGameActivity();
        PreferencesController.getInstance().clearUsersForGameSP();
        GameController.getInstance().initialiseGameController(GameType.FiveO,getGameSettings(),playersToGame,
                0,0,0,matchStateStack);
        finish();
    }

    private void openThreeoGameActivity() {
        Log.d("dom test", "openThreeoGameActivity");
        launchGameActivity();
        PreferencesController.getInstance().clearUsersForGameSP();
        GameController.getInstance().initialiseGameController(GameType.ThreeO,getGameSettings(),playersToGame,
                0,0,0,matchStateStack);
        finish();
    }

    private void openSevenoGameActivity() {
        Log.d("dom test", "openSevenoGameActivity");
        launchGameActivity();
        PreferencesController.getInstance().clearUsersForGameSP();
        GameController.getInstance().initialiseGameController(GameType.SevenO,getGameSettings(),playersToGame,
                0,0,0,matchStateStack);
        finish();
    }

    private void launchGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    private void setUpGameTypeDropDownMenu(){
        adapterItems = new ArrayAdapter<>(this,R.layout.list_item,gameSelectList);
        gameTypeAutoCompleteTextView.setAdapter(adapterItems);
        //gameTypeAutoCompleteTextView.setText(adapterItems.getItem(0),false);
    }


    private void setUpLegsListDropDownMenu(){
        adapterLegsItems = new ArrayAdapter<>(this,R.layout.list_item, numberOfLegsSetsList);
        legsAutoCompleteTextView.setAdapter(adapterLegsItems);
        legsAutoCompleteTextView.setText(adapterLegsItems.getItem(0),false);

    }

    private void setUpSetsListDropDownMenu(){
        adapterSetsItems = new ArrayAdapter<>(this,R.layout.list_item, numberOfLegsSetsList);
        setsAutoCompleteTextView.setAdapter(adapterSetsItems);
        setsAutoCompleteTextView.setText(adapterSetsItems.getItem(0),false);
    }

    private void setPlayersTextBox(){
        playerListCheckBox.setText("");
        ArrayList<User> playersToGame = PreferencesController.readUsersForGameSP(this);
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

    public GameSettings getGameSettings(){
        int totalLegs = Integer.parseInt(legsAutoCompleteTextView.getText().toString());
        int totalSets = Integer.parseInt(setsAutoCompleteTextView.getText().toString());
       return new GameSettings(totalLegs,totalSets);
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




