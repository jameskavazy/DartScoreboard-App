package com.example.dartscoreboard.match.presentation;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

import com.example.dartscoreboard.R;
import com.example.dartscoreboard.match.data.models.Match;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.rxjava3.schedulers.Schedulers;


public class MatchHistoryActivity extends AppCompatActivity {

    private MatchHistoryViewModel matchHistoryViewModel;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TextView noRecentGamesTextView;

    private Snackbar undoSnackBar;

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
        noRecentGamesTextView = findViewById(R.id.no_games_alert);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclerView_GamesList);
        undoSnackBar = Snackbar.make(
                findViewById(R.id.recent_games_coordinator_layout), "Match Deleted", Snackbar.LENGTH_LONG);
        setAdapter();
    }


    private void setAdapter() {
        final MatchHistoryAdapter adapter = new MatchHistoryAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        matchHistoryViewModel = new ViewModelProvider(this).get(MatchHistoryViewModel.class);


        matchHistoryViewModel.getUnfinishedMatches().observe(this, matches -> {
            adapter.submitList(matches);
            noRecentGamesTextView.setVisibility(matches.isEmpty() ? View.VISIBLE : View.GONE);
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Match match = adapter.getMatchAtPosition(viewHolder.getBindingAdapterPosition());
                matchHistoryViewModel.delete(match);
                undoSnackBar.setAction("Undo", v -> matchHistoryViewModel.insertMatch(match).subscribeOn(Schedulers.io()).subscribe());
                undoSnackBar.show();
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                if (actionState == ACTION_STATE_SWIPE) {
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

        adapter.setOnItemClickListener(match -> {
            Intent intent = new Intent(MatchHistoryActivity.this, MatchActivity.class);
            Bundle arguments = new Bundle();
            arguments.putString(MatchActivity.MATCH_KEY, match.getMatchId());
            intent.putExtras(arguments);
            startActivity(intent);
            finish();
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
            onDeleteAllRecentGamesMenuItem().show();
        } else if (menuItem == R.id.more_info_menu_item) {
            onMoreInfoMenuItem().show();
        }
        return super.onOptionsItemSelected(item);
    }

    public Dialog onMoreInfoMenuItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Click on previous matches to continue a recent game. Swipe on a recent game to delete it from your history.")
                .setPositiveButton("OK", (dialog, which) -> {
                });
        return builder.create();
    }

    public Dialog onDeleteAllRecentGamesMenuItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete all match history?")
                .setPositiveButton("Yes", ((dialog, which) -> matchHistoryViewModel.deleteAllUnfinishedMatches()))
                .setNegativeButton("Cancel", ((dialog, which) -> {
                }));
        return builder.create();
    }
}