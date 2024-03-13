package com.example.dartscoreboard.LiveMatches;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartscoreboard.R;

import java.util.Calendar;
import java.util.List;

public class LiveMatchesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerAdapterLiveMatches adapter;
    private Toolbar toolbar;
    private  LiveMatchesViewModel liveMatchesViewModel;

    /*
     TODO: 11/03/2024 Add a Textview, possibly a scrollable one that displays currently selected date.
      Calendar picker should open on this date.
      Also caching is important too
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        setAdapter();
        liveMatchesViewModel = new ViewModelProvider(this).get(LiveMatchesViewModel.class);
        liveMatchesViewModel.getDataFromApi(null);
        liveMatchesViewModel.getMatchesResponseList().observe(this, new Observer<List<MatchesResponse>>() {
            @Override
            public void onChanged(List<MatchesResponse> matchesResponseList) {
                if (matchesResponseList.isEmpty()){
                    Log.d("dom test","response is empty");
                    Toast.makeText(LiveMatchesActivity.this, "No Matches Found", Toast.LENGTH_SHORT).show();
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.setMatchesList(matchesResponseList.get(0).getMatches());
                }
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toolbar.setTitle("Live Pro Matches");
    }

    private void setupUI(){
        setContentView(R.layout.activity_live_matches);
        recyclerView = findViewById(R.id.live_matches_recycler_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.live_matches_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItem = item.getItemId();
        if (menuItem == R.id.calendarPicker){
            Log.d("dom test","calendar picker click");
            Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    getNewMatchList(year, month, dayOfMonth);
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getNewMatchList(int year, int month, int dayOfMonth) {
        String monthString;
        String dayOfMonthString;
        if (month <= 10) {
            monthString = "0" + (month + 1);
        } else monthString = String.valueOf(month + 1);

        if (dayOfMonth <= 9) {
            dayOfMonthString = "0" + dayOfMonth;
        } else dayOfMonthString = String.valueOf(dayOfMonth);

        String dateString = year + "-" + monthString + "-" + dayOfMonthString;
        Log.d("dom test", dateString);
        liveMatchesViewModel.getDataFromApi(dateString);
    }

    private void setAdapter(){
        adapter = new RecyclerAdapterLiveMatches();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}