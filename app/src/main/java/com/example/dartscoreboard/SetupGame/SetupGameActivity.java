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

import com.example.dartscoreboard.Game.Game;
import com.example.dartscoreboard.Game.GameActivity;
import com.example.dartscoreboard.Game.GameType;
import com.example.dartscoreboard.R;
import com.example.dartscoreboard.User.User;
import com.example.dartscoreboard.Utils.PreferencesController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SetupGameActivity extends AppCompatActivity implements View.OnClickListener {


    private final String[] gameSelectList = {"501", "301", "170"};

    private final String[] numberOfLegsSetsList = {"1", "2", "3", "4", "5"};

    private Toolbar toolbar;

    private List<User> selectedPlayers;
    private AutoCompleteTextView gameTypeAutoCompleteTextView;
    private AutoCompleteTextView legsAutoCompleteTextView;

    private AutoCompleteTextView setsAutoCompleteTextView;

    private TextView playerListTextViewButton;

    private SetupGameViewModel setupGameViewModel;

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

        if (PreferencesController.getInstance().getGameSelected() != null) {
            gameTypeAutoCompleteTextView.setText(PreferencesController.getInstance().getGameSelected());
        }
        selectedPlayers = PreferencesController.getInstance().getPlayers();
        if (selectedPlayers == null) {
            selectedPlayers = new ArrayList<>();
        }
        setPlayersTextBox(selectedPlayers);
        setupGameViewModel = new ViewModelProvider(this).get(SetupGameViewModel.class);

        setUpGameTypeDropDownMenu();
        setUpLegsListDropDownMenu();
        setUpSetsListDropDownMenu();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.gameStartButton) {
            if (setupGameViewModel.getSelectedPlayers().isEmpty()) {
                Toast.makeText(this, "You must select at least one player to start the match", Toast.LENGTH_SHORT).show();
            } else {
                startGameActivity();
                PreferencesController.getInstance().clearSelectedGame();
                finish();
            }
        } else if (viewId == R.id.NameDropDownBox) {
            openPlayerSelectActivity();
            PreferencesController.getInstance().saveSelectedGame(gameTypeAutoCompleteTextView.getText().toString());
            finish();
        } else if (viewId == R.id.remove_players_button) {
            PreferencesController.getInstance().savePlayers(new ArrayList<>());
            playerListTextViewButton.setText(null);
        } else if (viewId == R.id.randomise_players_button) {
            Collections.shuffle(selectedPlayers);
            setPlayersTextBox(selectedPlayers);
        }
    }

    public void openPlayerSelectActivity() {
        Intent intent = new Intent(this, PlayerSelectActivity.class);
        startActivity(intent);
    }

    public void startGameActivity() {
        String gameTypeSelected = gameTypeAutoCompleteTextView.getText().toString();
        if (gameTypeSelected.equals("")) {
            Toast.makeText(this, "You must select a game type", Toast.LENGTH_SHORT).show();
        } else {
            GameType gameType = getGameType(gameTypeSelected);
            openGameActivity(gameType);
        }
    }

    private void openGameActivity(GameType gameType) {
        if (gameType != null) {
            Log.d("dom test", "openGameActivity");
            Intent intent = new Intent(this, GameActivity.class);
            Bundle arguments = new Bundle();
            Game game = initialiseGameState(gameType);
            Log.d("gameState", "setupGame ID:  " + game.getGameID());
            arguments.putString(GameActivity.GAME_STATE_KEY, game.getGameID());
            intent.putExtras(arguments);
            startActivity(intent);
            finish();
        }
    }

    /**
     * GetGameType takes a string value and returns the corresponding GameType enum. Returns null
     * GameType if string cannot be parsed.
    * */
    private GameType getGameType(String gameTypeSelected) {
        switch (gameTypeSelected) {
            case "501":
                return GameType.FiveO;
            case "301":
                return GameType.ThreeO;
            case "170":
                return GameType.SevenO;
            default:
                return null;
        }
    }

    private Game initialiseGameState(GameType gameType) {
        int legs = Integer.parseInt(legsAutoCompleteTextView.getText().toString());
        int sets = Integer.parseInt(setsAutoCompleteTextView.getText().toString());
        return setupGameViewModel.createGameState(gameType, legs, sets);
    }

    private void setUpGameTypeDropDownMenu() {
        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(this, R.layout.list_item, gameSelectList);
        gameTypeAutoCompleteTextView.setAdapter(adapterItems);
    }


    private void setUpLegsListDropDownMenu() {
        ArrayAdapter<String> adapterLegsItems = new ArrayAdapter<>(this, R.layout.list_item, numberOfLegsSetsList);
        legsAutoCompleteTextView.setAdapter(adapterLegsItems);
        legsAutoCompleteTextView.setText(adapterLegsItems.getItem(0), false);
    }

    private void setUpSetsListDropDownMenu() {
        ArrayAdapter<String> adapterSetsItems = new ArrayAdapter<>(this, R.layout.list_item, numberOfLegsSetsList);
        setsAutoCompleteTextView.setAdapter(adapterSetsItems);
        setsAutoCompleteTextView.setText(adapterSetsItems.getItem(0), false);
    }

    private void setPlayersTextBox(List<User> playersToGame) {
        if (!playersToGame.isEmpty()) {
            String[] namesToGame = new String[playersToGame.size()];
            for (int i = 0; i < playersToGame.size(); i++) {
                namesToGame[i] = playersToGame.get(i).getUsername();
            }
            playerListTextViewButton.setText(String.join("\n", namesToGame));
        }
    }
}




