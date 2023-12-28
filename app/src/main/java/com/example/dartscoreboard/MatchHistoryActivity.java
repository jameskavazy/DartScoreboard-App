package com.example.dartscoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MatchHistoryActivity extends AppCompatActivity implements View.OnClickListener {

    private MatchHistoryViewModel matchHistoryViewModel;
    private Button clearGamesButton;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_history);
        setupUI();
    }

    public void setupUI(){
        recyclerView = findViewById(R.id.recyclerView_GamesList);
        clearGamesButton = findViewById(R.id.clear_games_button);
        clearGamesButton.setOnClickListener(this);
        setAdapter();
    }

    private void setAdapter() {
        final RecyclerAdapterMatchHistory adapter = new RecyclerAdapterMatchHistory();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        matchHistoryViewModel = ViewModelProviders.of(this).get(MatchHistoryViewModel.class);
        matchHistoryViewModel.getAllGames().observe(this, adapter::setGameStatesList);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                matchHistoryViewModel.delete(adapter.getGameStateAtPosition(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new RecyclerAdapterMatchHistory.OnItemClickListener() {
            @Override
            public void onItemClick(GameState gameState) {
               Intent intent = new Intent(MatchHistoryActivity.this, GameActivity.class);
               Bundle arguments = new Bundle();
               arguments.putSerializable(GameActivity.OPEN_GAME_ACTIVITY_KEY, gameState);
               intent.putExtra(GameActivity.GAME_STATE_ID, gameState.gameID);
               intent.putExtras(arguments);
               startActivity(intent); //todo codinginFlow tut - do we need edit note request ID(int value for callback)
            }
        });
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.clear_games_button){
            //delete games from database
            matchHistoryViewModel.deleteAllMatches();
            /*
            /todo make a drop down menu from top banner (in manifest, xml etc...
              code below already does java functionality)
             */

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.match_history_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all_recent_games){
            matchHistoryViewModel.deleteAllMatches();
        }
        return super.onOptionsItemSelected(item);
    }
}