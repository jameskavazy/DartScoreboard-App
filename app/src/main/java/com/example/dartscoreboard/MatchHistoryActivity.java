package com.example.dartscoreboard;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MatchHistoryActivity extends AppCompatActivity implements View.OnClickListener {


    private ArrayList<GameState> gameStatesList;
    private Button clearGamesButton;

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_history);
        setupUI();
        setAdapter();
    }

    private void setAdapter() {
        RecyclerAdapterMatchHistory adapter = new RecyclerAdapterMatchHistory(gameStatesList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void setupUI(){
        recyclerView = findViewById(R.id.recyclerView_GamesList);
        clearGamesButton = findViewById(R.id.clear_games_button);
        clearGamesButton.setOnClickListener(this);
        gameStatesList = new ArrayList<>();
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.clear_games_button){
            //delete games from database


        }
    }

    private String usersListAsString(GameState gameState) {
        String playerNamesString = null;
        if (gameState.getPlayerList() != null) {
            String[] namesOfGame = new String[gameState.getPlayerList().size()];
            for (int i = 0; i < gameState.getPlayerList().size(); i++) {
                namesOfGame[i] = gameState.getPlayerList().get(i).getUsername();
            }
            playerNamesString = String.join(", ", namesOfGame);
        }
        return playerNamesString;
    }

}