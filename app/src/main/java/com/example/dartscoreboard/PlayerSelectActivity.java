package com.example.dartscoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartscoreboard.models.User;

import java.util.ArrayList;

public class PlayerSelectActivity extends AppCompatActivity implements View.OnClickListener {

    private recyclerAdapterPlayersToGame adapter;

    private UserViewModel userViewModel;

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_select);
        setupUI();
    }
    private void setAdapter(){
        adapter = new recyclerAdapterPlayersToGame();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this,adapter::setUsersList);
        adapter.setOnItemClickListener(new recyclerAdapterPlayersToGame.OnItemClickListener() {
            @Override
            public void onClick(User user) {
                Log.d("dom test", "setAdapter onClick user " + user.getUsername() + user.getActive());
                user.setActive(!user.getActive());
                userViewModel.updateUser(user);
                Log.d("dom test", "setAdapter onClick user. Post update" + user.getUsername() + user.getActive());
                adapter.notifyDataSetChanged();
            }
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
            Intent intent = new Intent(this, SelectGameActivity.class);
            startActivity(intent);
            finish();
        }
    }
}