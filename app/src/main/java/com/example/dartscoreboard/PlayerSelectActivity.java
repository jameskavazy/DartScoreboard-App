package com.example.dartscoreboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class PlayerSelectActivity extends AppCompatActivity {

    private recyclerAdapterPlayersToGame adapter;

    private recyclerAdapterPlayersToGame.ClickListen listen;

    private ArrayList<User> usersList;

    private RecyclerView recyclerView;

    private Button doneButton;

    private final String USER_KEY = "USER_STRING";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_select);
        setupUI();
    }

    private void setAdapter(){
        setOnClickListener();
        adapter = new recyclerAdapterPlayersToGame(usersList, listen);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setupUI(){
        doneButton = findViewById(R.id.button_done);
        recyclerView = findViewById(R.id.players_to_add);


        usersList = PrefConfig.readSPUserList(this);
        if (usersList == null) {
            usersList = new ArrayList<>();
        }
        setAdapter();
    }

    private void setOnClickListener(){
        listen = new recyclerAdapterPlayersToGame.ClickListen() {
            @Override
            public void onClick(View view, int position) {
                int userToAdd = usersList.indexOf(usersList.get(position));
                String nameToAdd = usersList.get(position).getUsername();
                Log.d("dom test",nameToAdd);
               // PrefConfig.saveUsersForGameSP(getApplicationContext(),usersList);
               //todo send this back to previous activity as an intent? or as a SharedPreference
                Log.d("dom test", String.valueOf(userToAdd));


            }
        };




    }





}