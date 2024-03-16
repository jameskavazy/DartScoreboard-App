package com.example.dartscoreboard.LiveMatches;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartscoreboard.R;

import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public class LiveProMatchesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerAdapterLiveProMatches adapter;
    private Toolbar toolbar;
    private LiveProMatchesViewModel liveProMatchesViewModel;

    private ProgressBar progressBar;

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
        liveProMatchesViewModel = new ViewModelProvider(this).get(LiveProMatchesViewModel.class);
        liveProMatchesViewModel.deleteAll();
        getProMatchesList();
    }


    private void getProMatchesList() {
        //Checks Cache first -> API if cache empty
        liveProMatchesViewModel.getAllProMatches().subscribe(new SingleObserver<List<Match>>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                Log.d("dom test","onSubscribe hit");
            }

            @Override
            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<Match> matches) {
                Log.d("dom test","onSuccess hit");
                if (!matches.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    adapter.setMatchesList(matches);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    liveProMatchesViewModel.getDataFromApi(null, progressBar); //todo don't pass a null string isn't clear, pass a STATIC string
                    //getMatchReponse on mainthread because must observe the live data
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> getMatchesResponse());
                }
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                Log.d("dom test","onError hit");
                e.printStackTrace();
            }
        });


    }

    private void getMatchesResponse() {
        liveProMatchesViewModel.getMatchesResponseList().observe(this, matchesResponseList -> {
            if (matchesResponseList.isEmpty()) {
                Log.d("dom test", "response is empty");
                Toast.makeText(LiveProMatchesActivity.this, "No Matches Found", Toast.LENGTH_SHORT).show();
                recyclerView.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                liveProMatchesViewModel.upsertAll(matchesResponseList.get(0).getMatches());
                adapter.setMatchesList(matchesResponseList.get(0).getMatches());
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toolbar.setTitle("Live Pro Matches");
    }

    private void setupUI() {
        setContentView(R.layout.activity_live_matches);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.live_matches_recycler_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.live_matches_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItem = item.getItemId();
        if (menuItem == R.id.calendarPicker) {
            Log.d("dom test", "calendar picker click");
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
        liveProMatchesViewModel.deleteAll();
        liveProMatchesViewModel.getDataFromApi(dateString, progressBar);

        //TODO we've left off where we need build logic to determine whether or not to pull data from the cache or from the API.

    }

    private void setAdapter() {
        adapter = new RecyclerAdapterLiveProMatches();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}