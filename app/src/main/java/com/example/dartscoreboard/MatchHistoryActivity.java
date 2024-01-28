package com.example.dartscoreboard;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_IDLE;
import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MatchHistoryActivity extends AppCompatActivity {

    private MatchHistoryViewModel matchHistoryViewModel;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

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

    public void setupUI() {
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
        matchHistoryViewModel.getAllGames().observe(this, adapter::submitList);


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

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {

                super.onSelectedChanged(viewHolder, actionState);
                if (actionState == ACTION_STATE_SWIPE){
                    if (viewHolder != null) {
                        viewHolder.itemView.setAlpha(0.25F);
                    }
                }
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setAlpha(1F);
            }

            @Override
            public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return 0.65F;
            }

            @Override
            public float getSwipeVelocityThreshold(float defaultValue) {
                return 0.6F;
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
        menuInflater.inflate(R.menu.match_history_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItem = item.getItemId();
        if (menuItem == R.id.delete_all_recent_games) {
            matchHistoryViewModel.deleteAllMatches();
        } else if (menuItem == R.id.more_info_menu_item){
            onMoreInfoMenuItem().show();
        }
        return super.onOptionsItemSelected(item);
    }

    public Dialog onMoreInfoMenuItem(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Click on previous matches to continue a recent game. Swipe on a recent game to delete it from your history.")
                .setPositiveButton("OK", (dialog, which) -> {});
        return builder.create();
    }
}