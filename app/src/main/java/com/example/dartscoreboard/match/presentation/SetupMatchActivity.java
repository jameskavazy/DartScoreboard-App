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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartscoreboard.match.data.models.Match;
import com.example.dartscoreboard.match.data.models.MatchType;
import com.example.dartscoreboard.R;
import com.example.dartscoreboard.match.data.models.User;
import com.example.dartscoreboard.util.PreferencesController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class SetupMatchActivity extends AppCompatActivity implements View.OnClickListener {
    private final String[] gameSelectList = {"501", "301", "170"};
    private final String[] numberOfLegsSetsList = {"1", "2", "3", "4", "5"};
    private Toolbar toolbar;
    private PlayerSelectAdapter adapter;
    private AutoCompleteTextView gameTypeAutoCompleteTextView;
    private AutoCompleteTextView legsAutoCompleteTextView;

    private AutoCompleteTextView setsAutoCompleteTextView;

    private RecyclerView recyclerView;

    private SetupMatchViewModel setupMatchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("dom test", "HomeActivityOnCreate");
        super.onCreate(savedInstanceState);
        setupUI();
        setAdapter();
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
        Button addPlayersBtn = findViewById(R.id.name_drop_down_box);
        recyclerView = findViewById(R.id.selected_players_recycler);
        legsAutoCompleteTextView = findViewById(R.id.legs_drop_down);
        setsAutoCompleteTextView = findViewById(R.id.sets_drop_down);
        gameTypeAutoCompleteTextView = findViewById(R.id.gameTypeDropDownBox);
        toolbar = findViewById(R.id.toolbar);
        startGameBtn.setOnClickListener(this);
        addPlayersBtn.setOnClickListener(this);
        randomisePlayersBtn.setOnClickListener(this);
        clearPlayersBtn.setOnClickListener(this);
        gameTypeAutoCompleteTextView.setOnClickListener(this);

        if (PreferencesController.getInstance().getGameSelected() != null) {
            gameTypeAutoCompleteTextView.setText(PreferencesController.getInstance().getGameSelected());
        }

        setupMatchViewModel = new ViewModelProvider(this).get(SetupMatchViewModel.class);

        setUpGameTypeDropDownMenu();
        setUpLegsListDropDownMenu();
        setUpSetsListDropDownMenu();
    }

    private void setAdapter(){
        adapter = new PlayerSelectAdapter(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setUsersList(PreferencesController.getInstance().getPlayers());

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

            int source = -1;
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getBindingAdapterPosition();
                int toPosition = target.getBindingAdapterPosition();
                adapter.notifyItemMoved(fromPosition, toPosition);
                List<User> savedPlayers = PreferencesController.getInstance().getPlayers();
                Collections.swap(savedPlayers, fromPosition, toPosition);
                PreferencesController.getInstance().savePlayers(savedPlayers);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }
            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && viewHolder != null){
                    source = viewHolder.getBindingAdapterPosition();
                }
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onClick(View v) {
        List<User> selectedPlayers = PreferencesController.getInstance().getPlayers();
        String gameTypeSelected = gameTypeAutoCompleteTextView.getText().toString();
        int viewId = v.getId();
        if (viewId == R.id.gameStartButton) {
            if (selectedPlayers.isEmpty()) {
                Toast.makeText(this, "You must select at least one player to start the match", Toast.LENGTH_SHORT).show();
            } else if (gameTypeSelected.equals("")) {
                Toast.makeText(this, "You must select a game type", Toast.LENGTH_SHORT).show();
            } else {
                startGameActivity(gameTypeSelected);
                finish();
            }
        } else if (viewId == R.id.name_drop_down_box) {
            openPlayerSelectActivity();
            PreferencesController.getInstance().saveSelectedGame(gameTypeAutoCompleteTextView.getText().toString());
            finish();
        } else if (viewId == R.id.remove_players_button) {
            PreferencesController.getInstance().savePlayers(new ArrayList<>());
            adapter.setUsersList(PreferencesController.getInstance().getPlayers());
        } else if (viewId == R.id.randomise_players_button) {
            setupMatchViewModel.randomisePlayerOrder(selectedPlayers);
            PreferencesController.getInstance().savePlayers(selectedPlayers);
            adapter.setUsersList(selectedPlayers);
        }
    }

    public void openPlayerSelectActivity() {
        Intent intent = new Intent(this, PlayerSelectActivity.class);
        startActivity(intent);
    }

    public void startGameActivity(String gameTypeSelected) {
        MatchType matchType = getMatchType(gameTypeSelected);
        PreferencesController.getInstance().clearSelectedGame();
        openGameActivity(matchType);
    }

    private void openGameActivity(MatchType matchType) {
        if (matchType != null) {
            Intent intent = new Intent(this, MatchActivity.class);
            Bundle arguments = new Bundle();
            String matchId = initialiseMatch(matchType);
            arguments.putString(MatchActivity.MATCH_KEY, matchId);
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

    private String initialiseMatch(MatchType matchType) {
        int legs = Integer.parseInt(legsAutoCompleteTextView.getText().toString());
        int sets = Integer.parseInt(setsAutoCompleteTextView.getText().toString());
        return setupMatchViewModel.createMatch(matchType, legs, sets);
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


}




