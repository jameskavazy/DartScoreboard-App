package com.example.dartscoreboard.LiveMatches;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartscoreboard.R;

import java.util.List;

public class LiveMatchesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private List<MatchesResponse> matchList;

    private RecyclerAdapterLiveMatches adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        setAdapter();
        LiveMatchesViewModel liveMatchesViewModel = new ViewModelProvider(this).get(LiveMatchesViewModel.class);
        liveMatchesViewModel.getDataFromApi();
        liveMatchesViewModel.getMatchesResponseList().observe(this, new Observer<List<MatchesResponse>>() {
            @Override
            public void onChanged(List<MatchesResponse> matchesResponseList) {
                adapter.setMatchesList(matchesResponseList.get(0).getMatches());
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Live Pro Matches");
    }



    private void setupUI(){
        setContentView(R.layout.activity_live_matches);
        recyclerView = findViewById(R.id.live_matches_recycler_view);
    }


    private void setAdapter(){
        adapter = new RecyclerAdapterLiveMatches();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }




}