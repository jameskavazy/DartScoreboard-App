package com.example.dartscoreboard.Statistics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartscoreboard.R;
import com.example.dartscoreboard.User.UserViewModel;
import com.example.dartscoreboard.User.recyclerAdapterUsers;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StatisticsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private recyclerAdapterUsers adapter;
    Toolbar toolbar;

    public static String userStatKey = "USER_STAT_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, adapter::setUsersList);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setToolbar();
    }

    private void setupUI(){
        setContentView(R.layout.activity_users);
        recyclerView = findViewById(R.id.recycler_view_username_list);
        FloatingActionButton floatingActionButton = findViewById(R.id.add_new_user_button);
        floatingActionButton.setVisibility(View.GONE);
        setAdapter();
    }

    private void setAdapter(){
        adapter = new recyclerAdapterUsers();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(user -> {
            Intent intent = new Intent(getApplicationContext(), UserStatisticsActivity.class);
            intent.putExtra(userStatKey,user);
            startActivity(intent);
        });
    }

    private void setToolbar(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Statistics");
    }



}