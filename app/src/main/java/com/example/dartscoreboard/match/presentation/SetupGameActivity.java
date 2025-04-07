package com.example.dartscoreboard.match.presentation;


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

import com.example.dartscoreboard.match.data.models.Match;
import com.example.dartscoreboard.match.data.models.MatchType;
import com.example.dartscoreboard.R;
import com.example.dartscoreboard.user.User;
import com.example.dartscoreboard.util.PreferencesController;

import java.util.ArrayList;
import java.util.List;

public class SetupGameActivity extends AppCompatActivity implements View.OnClickListener {


    private final String[] gameSelectList = {"501", "301", "170"};

    private final String[] numberOfLegsSetsList = {"1", "2", "3", "4", "5"};

    private Toolbar toolbar;

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

        setupGameViewModel = new ViewModelProvider(this).get(SetupGameViewModel.class);
        setPlayersTextBox(setupGameViewModel.getSelectedPlayers());


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
            setupGameViewModel.randomisePlayerOrder();
            setPlayersTextBox(setupGameViewModel.getSelectedPlayers());
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
            MatchType matchType = getMatchType(gameTypeSelected);
            openGameActivity(matchType);
        }
    }

    private void openGameActivity(MatchType matchType) {
        if (matchType != null) {
            Intent intent = new Intent(this, MatchActivity.class);
            Bundle arguments = new Bundle();
            Match match = initialiseMatch(matchType);
            arguments.putString(MatchActivity.MATCH_KEY, match.matchId);
            intent.putExtras(arguments);
            startActivity(intent);
            finish();
        }
    }

    /**
     * GetMatchType takes a string value and returns the corresponding GameType enum. Returns null
     * GameType if string cannot be parsed.
    * */
    private MatchType getMatchType(String gameTypeSelected) {
        switch (gameTypeSelected) {
            case "501":
                return MatchType.FiveO;
            case "301":
                return MatchType.ThreeO;
            case "170":
                return MatchType.SevenO;
            default:
                return null;
        }
    }

    private Match initialiseMatch(MatchType matchType) {
        int legs = Integer.parseInt(legsAutoCompleteTextView.getText().toString());
        int sets = Integer.parseInt(setsAutoCompleteTextView.getText().toString());
        return setupGameViewModel.createMatch(matchType, legs, sets);
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




