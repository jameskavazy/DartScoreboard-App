package com.example.dartscoreboard.match.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartscoreboard.R;
import com.example.dartscoreboard.user.User;
import com.example.dartscoreboard.user.UserViewModel;
import com.example.dartscoreboard.util.PreferencesController;

import java.util.ArrayList;
import java.util.List;

public class PlayerSelectActivity extends AppCompatActivity implements View.OnClickListener {

    private PlayerSelectAdapter adapter;

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_select);
        setupUI();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Select Players");
    }

    private void setAdapter(){
        adapter = new PlayerSelectAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this,adapter::setUsersList);

        adapter.setOnItemClickListener((user, position) -> {
            addRemoveUser(user);
            adapter.notifyItemChanged(position);
        });
    }

    private void setupUI(){
        Button doneButton = findViewById(R.id.button_done);
        recyclerView = findViewById(R.id.players_to_add);
        doneButton.setOnClickListener(this);
        setAdapter();
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_done){
            Intent intent = new Intent(this, SetupGameActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void addRemoveUser(User user) {
        List<User> players = PreferencesController.getInstance().getPlayers();
        if (players == null) {
            players = new ArrayList<>();
        }
        boolean removed = players.removeIf(player -> player.userID == user.userID);
        if (!removed) {
            players.add(user);
        }
        PreferencesController.getInstance().savePlayers(players);
    }
}