package com.example.dartscoreboard.LiveMatches;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartscoreboard.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LiveProMatchesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerAdapterLiveProMatches adapter;
    private Toolbar toolbar;
    private LiveProMatchesViewModel liveProMatchesViewModel;
    private TextView dateSelectedTextView;
    private ProgressBar progressBar;
    public static String DATE_SELECTED = "TODAY";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        setAdapter();
        liveProMatchesViewModel = new ViewModelProvider(this).get(LiveProMatchesViewModel.class);
        getCachedProMatches();
    }


    private void getCachedProMatches() {
        liveProMatchesViewModel.getAllProMatches().observe(this, matches -> {
            Log.d("dom test","onChanged hit");
            if (matches.isEmpty()) {
                liveProMatchesViewModel.getDataFromApi(DATE_SELECTED,progressBar,recyclerView);
                progressBar.setVisibility(View.GONE);
                return;
            }
            String uglyOffsetDateTime = matches.get(0).getStart_time();
            dateSelectedTextView.setText(uglyOffsetDateTime.substring(0,10));
            adapter.setMatchesList(matches);
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
        dateSelectedTextView = findViewById(R.id.date_selected_text_view);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.live_matches_recycler_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (dateSelectedTextView.toString().equals("TODAY")){
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String dateString = simpleDateFormat.format(calendar.getTime());
            dateSelectedTextView.setText(dateString);
        }
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

        DATE_SELECTED = year + "-" + monthString + "-" + dayOfMonthString;
        dateSelectedTextView.setText(DATE_SELECTED);
        Log.d("dom test", DATE_SELECTED);
        progressBar.setVisibility(View.VISIBLE);
        liveProMatchesViewModel.getDataFromApi(DATE_SELECTED, progressBar, recyclerView);
    }



    private void setAdapter() {
        adapter = new RecyclerAdapterLiveProMatches();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}