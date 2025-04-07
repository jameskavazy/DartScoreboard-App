package com.example.dartscoreboard.match.presentation;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartscoreboard.R;


public class MatchActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MATCH_KEY = "GAME_STATE";
    private Toolbar toolbar;
    private MatchViewModel matchViewModel;
    private RecyclerView recyclerView;
    private EditText inputScoreEditText;
    private TextView averageScoreTextView;
    private TextView visitsTextView;
    private Button doneButton;
    private MatchAdapter matchAdapter;
    private View bananaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        setAdapter();
        observeViewModel();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toolbar.setTitleMarginStart(24);
    }

    private void setupUI() {
        setContentView(R.layout.game_activity);
        bananaView = findViewById(R.id.banana_image);
        bananaView.setVisibility(View.GONE);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        averageScoreTextView = findViewById(R.id.avg_text_view);
        visitsTextView = findViewById(R.id.visits_text_view);
        doneButton = findViewById(R.id.done_button);
        recyclerView = findViewById(R.id.player_info_recycler_view);
        inputScoreEditText = findViewById(R.id.inputUserNameEditText);
        doneButton.setOnClickListener(this);
        inputScoreEditText.setOnEditorActionListener((v, actionId, event) -> onScoreEntered());
    }

    private String getMatchIdFromIntent() {
        Bundle arguments = getIntent().getExtras();
        assert arguments != null;
        String gameId = arguments.getString(MATCH_KEY);
        assert gameId != null;
        return gameId;
    }

    private void setAdapter() {
        matchAdapter = new MatchAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(matchAdapter);
    }

    private void observeViewModel() {
        matchViewModel = new ViewModelProvider(this).get(MatchViewModel.class);

        matchViewModel.setMatchId(getMatchIdFromIntent());
        matchViewModel.fetchMatchData();

        matchViewModel.getMatchDataLiveData()
                .observe(this, matchData -> {
                    matchAdapter.setMatchData(matchData);
                    toolbar.setTitle(matchData.match.getMatchType().name);
                });

        matchViewModel.getGameWithVisitsMutableLiveData()
                .observe(this, gameWithVisits -> {
                    matchAdapter.setGameWithVisits(gameWithVisits);
                });

        matchViewModel.getFinished().observe(this, isFinished -> {
            if (!isFinished) {
                inputScoreEditText.setVisibility(View.VISIBLE);
                doneButton.setVisibility(View.VISIBLE);
            } else {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(recyclerView.getApplicationWindowToken(), 0);
                inputScoreEditText.setVisibility(View.GONE);
                doneButton.setVisibility(View.GONE);
            }
        });
    }
    private Boolean onScoreEntered() {
        int input = 0;
        if (!inputScoreEditText.getText().toString().isEmpty()) {
            try {
                input = Integer.parseInt(inputScoreEditText.getText().toString());
            } catch (Exception ignored) {

            }
        }
        try {
            Log.d("dom test", "IME_ACTION_DONE");
//            setBanana();
            matchViewModel.playerVisit(input);

            if (input > 180) {
                Toast.makeText(MatchActivity.this, "Invalid Score", Toast.LENGTH_SHORT).show();
            }
            ((EditText) findViewById(R.id.inputUserNameEditText)).getText().clear();
//            setAverageScoreTextView();
//            setVisitsTextView();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.done_button) {
            Log.d("dom test", "Done Click");
            onScoreEntered();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.match_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull MenuItem item) {
        int menuItem = item.getItemId();
        if (menuItem == R.id.undo_menu_button) {
            Log.d("dom test", "Undo Click");
            matchViewModel.undo();
//            setAverageScoreTextView();
//            setVisitsTextView();
        }
        return super.onOptionsItemSelected(item);
    }

    //    private void setAverageScoreTextView() {
//        double avg = gameViewModel.getPlayerAverage();
//        averageScoreTextView.setText(String.valueOf(avg));
//    }

//    private void setVisitsTextView() {
//        User activeUser = gameViewModel.getPlayersList().get(GameViewModel.getTurnIndex());
//        int visits = activeUser.getVisits();
//        visitsTextView.setText(String.valueOf(visits));
//    }


//    private void setBanana() {
//        if (gameViewModel.bananaSplit()) {
//            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputMethodManager.hideSoftInputFromWindow(recyclerView.getApplicationWindowToken(), 0);
//            bananaView.setVisibility(View.VISIBLE);
//            bananaView.postDelayed(() -> bananaView.setVisibility(View.GONE), 150);
//        } else bananaView.setVisibility(View.GONE);
//    }
}
