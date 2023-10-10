package com.example.dartscoreboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class PlayerSelectActivity extends AppCompatActivity {

    private recyclerAdapter adapter;

    private ArrayList<User> usersList;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_select);
//        setAdapter();
    }

//    private void setAdapter(){
//        adapter = new recyclerAdapter(usersList, new recyclerAdapter.ClickHandler() {
//            @Override
//            public void onMyButtonClicked(int position) {
//                //todo add code for adding player onto the previous screen
//
//
//            }
//        });
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(adapter);
//    }



}