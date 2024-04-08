package com.example.dartscoreboard.SetupGame;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.dartscoreboard.Game.GameActivity;
import com.example.dartscoreboard.Game.GameSettings;
import com.example.dartscoreboard.Game.GameState;
import com.example.dartscoreboard.MatchHistory.MatchState;
import com.example.dartscoreboard.R;
import com.example.dartscoreboard.User.User;
import com.example.dartscoreboard.User.UserViewModel;
import com.example.dartscoreboard.Utils.PreferencesController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class SelectGameActivity extends AppCompatActivity implements View.OnClickListener {


    private final String[] gameSelectList = {"501","301","170"};

    private final String[] numberOfLegsSetsList = {"1","2","3","4","5"};

    private Toolbar toolbar;

    private List<User> playersToGame;
    private AutoCompleteTextView gameTypeAutoCompleteTextView;
    private AutoCompleteTextView legsAutoCompleteTextView;

    private AutoCompleteTextView setsAutoCompleteTextView;

    private TextView playerListTextViewButton;

    private UserViewModel userViewModel;

    private Stack<MatchState> matchStateStack = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("dom test", "HomeActivityOnCreate");
        super.onCreate(savedInstanceState);
        setupUI();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toolbar.setTitle("Match Settings");
    }

    private void setupUI() {
        Log.d("dom test", "setupUI");
        setContentView(R.layout.activity_select_game);
        Button startGameBtn = findViewById(R.id.gameStartButton);
        Button randomisePlayersBtn = findViewById(R.id.randomise_players_button);
        Button clearPlayersBtn = findViewById(R.id.remove_players_button);
        Button addPlayersBtn = findViewById(R.id.NameDropDownBox);
        playerListTextViewButton = findViewById(R.id.list_of_match_players);
        legsAutoCompleteTextView = findViewById(R.id.legs_drop_down);
        setsAutoCompleteTextView = findViewById(R.id.sets_drop_down);
        gameTypeAutoCompleteTextView = findViewById(R.id.gameTypeDropDownBox);
        toolbar = findViewById(R.id.toolbar);

        startGameBtn.setOnClickListener(this);
        addPlayersBtn.setOnClickListener(this);
        randomisePlayersBtn.setOnClickListener(this);
        clearPlayersBtn.setOnClickListener(this);
        playerListTextViewButton.setOnClickListener(this);
        gameTypeAutoCompleteTextView.setOnClickListener(this);

        if (PreferencesController.getInstance().getGameSelected() != null){
            gameTypeAutoCompleteTextView.setText(PreferencesController.getInstance().getGameSelected());
        }

        setUpGameTypeDropDownMenu();
        setUpLegsListDropDownMenu();
        setUpSetsListDropDownMenu();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getActiveUsers(true).observe(this, this::setPlayersToGame);
        //todo make a SelectGameViewModel Class & remove call to gameViewModel From here
        setPlayersTextBox(getPlayersToGame());
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.gameStartButton){ // switch statement
            if (getPlayersToGame().isEmpty()){
                Toast.makeText(this, "You must select at least one player to start the match", Toast.LENGTH_SHORT).show();
            } else {
                openGameActivity();
                PreferencesController.getInstance().clearSelectedGame();
                finish();
            }
        } else if (viewId == R.id.NameDropDownBox){
           openPlayerSelectActivity();
           PreferencesController.getInstance().saveSelectedGame(gameTypeAutoCompleteTextView.getText().toString());
           finish();
        } else if (viewId == R.id.remove_players_button){
            for (User user : playersToGame) {
                user.setActive(false);
                userViewModel.updateUser(user);
            }
            playerListTextViewButton.setText(null);
        } else if (viewId == R.id.randomise_players_button){
            Collections.shuffle(getPlayersToGame());
            setPlayersTextBox(getPlayersToGame());
        }
    }

    public void openPlayerSelectActivity(){
        Intent intent = new Intent(this, PlayerSelectActivity.class);
        startActivity(intent);
    }

    public void openGameActivity(){
        if (gameTypeAutoCompleteTextView.getText().toString().equals("501")){ // todo make switch
            Log.d("dom test","openFiveOGameActivity");
            openFiveOGameActivity();
        } else if (gameTypeAutoCompleteTextView.getText().toString().equals("301")){
            Log.d("dom test","openThreeOGameActivity");
            openThreeOGameActivity();
        } else if (gameTypeAutoCompleteTextView.getText().toString().equals("170")){
            Log.d("dom test","openSevenOGameActivity");
            openSevenOGameActivity();
        } else if (gameTypeAutoCompleteTextView.getText().toString().isEmpty()) {
            Toast.makeText(this,"You must select a game type",Toast.LENGTH_SHORT).show();
        }
    }

    private void openFiveOGameActivity() {
        Log.d("dom test", "openFiveOGameActivity");
        Intent intent = new Intent(this,GameActivity.class);
        Bundle arguments = new Bundle();
        GameState gameState = initialiseGameState(GameType.FiveO);
        arguments.putSerializable(GameActivity.GAME_STATE_KEY,gameState);
        intent.putExtras(arguments);
        startActivity(intent);
        finish();
    }

    private void openThreeOGameActivity() {
        Log.d("dom test", "openThreeOGameActivity");
        Intent intent = new Intent(this,GameActivity.class);
        Bundle arguments = new Bundle();
        GameState gameState = initialiseGameState(GameType.ThreeO);
        arguments.putSerializable(GameActivity.GAME_STATE_KEY,gameState);
        intent.putExtras(arguments);
        startActivity(intent);
        finish();
    }
    private void openSevenOGameActivity() {
        Log.d("dom test", "openSevenOGameActivity");
        Intent intent = new Intent(this,GameActivity.class);
        Bundle arguments = new Bundle();
        GameState gameState = initialiseGameState(GameType.SevenO);
        arguments.putSerializable(GameActivity.GAME_STATE_KEY,gameState);
        intent.putExtras(arguments);
        startActivity(intent);
        finish();
    }

    private GameState initialiseGameState(GameType gameType){
        GameState gameState = new GameState(gameType,getGameSettings(),getPlayersToGame(),0,0,0,matchStateStack);
        gameState.setGameID(0);
        return gameState;
    }

    private void setUpGameTypeDropDownMenu(){
        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(this, R.layout.list_item, gameSelectList);
        gameTypeAutoCompleteTextView.setAdapter(adapterItems);
    }


    private void setUpLegsListDropDownMenu(){
        ArrayAdapter<String> adapterLegsItems = new ArrayAdapter<>(this, R.layout.list_item, numberOfLegsSetsList);
        legsAutoCompleteTextView.setAdapter(adapterLegsItems);
        legsAutoCompleteTextView.setText(adapterLegsItems.getItem(0),false);
    }

    private void setUpSetsListDropDownMenu(){
        ArrayAdapter<String> adapterSetsItems = new ArrayAdapter<>(this, R.layout.list_item, numberOfLegsSetsList);
        setsAutoCompleteTextView.setAdapter(adapterSetsItems);
        setsAutoCompleteTextView.setText(adapterSetsItems.getItem(0),false);
    }

    private void setPlayersTextBox(List<User> playersToGame){
        if (!playersToGame.isEmpty()){
            String[] namesToGame = new String[playersToGame.size()];
            for (int i = 0; i < playersToGame.size(); i++) {
                namesToGame[i] = playersToGame.get(i).getUsername();
            }
            playerListTextViewButton.setText(String.join("\n", namesToGame));
        }
    }

    public void setPlayersToGame(List<User> playersToGame) {
        this.playersToGame = playersToGame;
        setPlayersTextBox(playersToGame);
    }

    public List<User> getPlayersToGame(){
        if (playersToGame == null){
            playersToGame = new ArrayList<>();
        }
        return playersToGame;
    }

    public GameSettings getGameSettings(){
        int totalLegs = Integer.parseInt(legsAutoCompleteTextView.getText().toString());
        int totalSets = Integer.parseInt(setsAutoCompleteTextView.getText().toString());
       return new GameSettings(totalLegs,totalSets);
    }
}




