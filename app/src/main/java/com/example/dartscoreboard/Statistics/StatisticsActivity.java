package com.example.dartscoreboard.Statistics;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartscoreboard.R;
import com.example.dartscoreboard.User.User;
import com.example.dartscoreboard.User.UserViewModel;
import com.example.dartscoreboard.User.recyclerAdapterUsers;

public class StatisticsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private recyclerAdapterUsers adapter;
    Toolbar toolbar;

    public static String userStatKey = "USER_STAT_KEY";

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, adapter::setUsersList);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setToolbar();
    }

    private void setupUI(){
        setContentView(R.layout.activity_statistics);
        recyclerView = findViewById(R.id.stats_users_recycler_view);
        setAdapter();
    }

    private void setAdapter(){
        adapter = new recyclerAdapterUsers();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new recyclerAdapterUsers.OnItemClickListener() {
            @Override
            public void onItemClicked(User user) {
                Intent intent = new Intent(getApplicationContext(), UserStatisticsActivity.class);
                intent.putExtra(userStatKey,user);
                startActivity(intent);
            }
        });
    }

    private void setToolbar(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Statistics");
    }



}