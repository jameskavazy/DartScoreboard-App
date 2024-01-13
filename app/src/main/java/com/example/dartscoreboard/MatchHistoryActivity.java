package com.example.dartscoreboard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MatchHistoryActivity extends AppCompatActivity  {

    private MatchHistoryViewModel matchHistoryViewModel;
    private Button clearGamesButton;
    private RecyclerView recyclerView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toolbar.setTitle("Recent Matches");
        toolbar.setTitleTextColor(Color.WHITE);
    }

    public void setupUI(){
        setContentView(R.layout.activity_match_history);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclerView_GamesList);
        setAdapter();
    }

    private void setAdapter() {
        final RecyclerAdapterMatchHistory adapter = new RecyclerAdapterMatchHistory();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        matchHistoryViewModel = new ViewModelProvider(this).get(MatchHistoryViewModel.class);
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
               arguments.putSerializable(GameActivity.MATCH_HISTORY_EXTRA_KEY, gameState);
               intent.putExtra(GameActivity.GAME_STATE_ID, gameState.getGameID());
               intent.putExtras(arguments);
               startActivity(intent);
               finish();
            }
        });
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