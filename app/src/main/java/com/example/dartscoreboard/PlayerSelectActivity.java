package com.example.dartscoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartscoreboard.models.User;

import java.util.ArrayList;

public class PlayerSelectActivity extends AppCompatActivity implements View.OnClickListener {

    private recyclerAdapterPlayersToGame adapter;

    private recyclerAdapterPlayersToGame.ClickListen listen;

    private ArrayList<User> usersList;

    private RecyclerView recyclerView;

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
        Button doneButton = findViewById(R.id.button_done);
        recyclerView = findViewById(R.id.players_to_add);
        doneButton.setOnClickListener(this);
        usersList = PreferencesController.readSPUserList(this);
        if (usersList == null) {
            usersList = new ArrayList<>();
        }
        setAdapter();
    }

    private void setOnClickListener(){
        listen = (view, position) -> {
            User user = usersList.get(position);
            if (!user.getActive()) {
                user.setActive(true);
                adapter.notifyItemChanged(position);
            }
            else {
                user.setActive(false);
                adapter.notifyItemChanged(position);
            }
        };
    }



private void setUsersForGame(){
    ArrayList<User> usersForGame = new ArrayList<>();
    for (int i = 0; i < usersList.size(); i++) {
        if (usersList.get(i).getActive()){
            usersForGame.add(usersList.get(i));
            Log.d("dom test", String.valueOf(usersForGame));
            PreferencesController.saveUsersForGameSP(getApplicationContext(),usersForGame);
        }

    }

}

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_done){
            setUsersForGame();
            Log.d("dom test","onButtonDoneClick");
            for (int i = 0; i<usersList.size(); i++) {
                Log.d("dom test", i +" " + " " + usersList.get(i).getActive());
            }
            Intent intent = new Intent(this, SelectGameActivity.class);
            startActivity(intent);
            finish();
        }

    }
}