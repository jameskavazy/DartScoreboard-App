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

import java.util.ArrayList;

public class PlayerSelectActivity extends AppCompatActivity implements View.OnClickListener {

    private recyclerAdapterPlayersToGame adapter;

    private recyclerAdapterPlayersToGame.ClickListen listen;

    private ArrayList<User> usersList;

    private RecyclerView recyclerView;

    public final String USER_KEY = "USER_STRING";


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
       // createUsersForGameList().clear();
    }

    private void setOnClickListener(){
        listen = new recyclerAdapterPlayersToGame.ClickListen() {
            @Override
            public void onClick(View view, int position) {
                int userToAdd = usersList.indexOf(usersList.get(position));
                String nameToAdd = usersList.get(position).getUsername();
                Log.d("dom test",nameToAdd);
                Log.d("dom test", String.valueOf(userToAdd));
                User user = usersList.get(position);
                if (!user.getActive()) {
                    user.setActive(true);
                    adapter.notifyItemChanged(position);
                }
                else if (user.getActive()){
                    user.setActive(false);
                    adapter.notifyItemChanged(position);
                }

            }
        };
    }

private ArrayList<User> createUsersForGameList(){
        ArrayList<User> usersForGame = new ArrayList<>();
        return usersForGame;
}

private void setUsersForGame(){
    ArrayList<User> usersForGame = createUsersForGameList();
    for (int i = 0; i < usersList.size(); i++) {
        if (usersList.get(i).active){
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
                Log.d("dom test", i +" " + " " + Boolean.toString(usersList.get(i).getActive()));
            }
            Intent intent = new Intent(this, SelectGameActivity.class);
            startActivity(intent);
        }

    }
}