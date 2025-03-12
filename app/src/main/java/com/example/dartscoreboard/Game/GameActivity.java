package com.example.dartscoreboard.Game;

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
import com.example.dartscoreboard.User.User;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String GAME_STATE_KEY = "GAME_STATE";
    private Toolbar toolbar;
    private GameViewModel gameViewModel;
    private RecyclerView recyclerView;
    private EditText inputScoreEditText;
    private TextView averageScoreTextView;
    private TextView visitsTextView;
    private Button doneButton;
    private GameAdapter adapter;
    private View bananaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        setAdapter();
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
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        gameViewModel.setGameId(gameIdFromIntent());
    }

    private String gameIdFromIntent() {
        Bundle arguments = getIntent().getExtras();
        assert arguments != null;
        String gameId = arguments.getString(GAME_STATE_KEY);
        assert gameId != null;
        Log.d("gameState", "gameactivity ID: " + gameId);
        return gameId;
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

    private void setAdapter() {
        List<User> players = new ArrayList<>();
        gameViewModel.getPlayersList().observe(this, gameWithUsers -> {
            gameViewModel.setPlayersList(gameWithUsers.users);
            gameViewModel.setGameType(gameWithUsers.game.getGameType());
            players.addAll(gameWithUsers.users);
        });
        adapter = new GameAdapter(players);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        gameViewModel.getGame().observe(this, game -> {
            adapter.setGame(game);
            toolbar.setTitle(game.gameType.name);
        });
        gameViewModel.getVisits().observe(this, visits -> adapter.setVisits(visits));
        recyclerView.setAdapter(adapter);
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
            gameViewModel.playerVisit(input);
//            adapter.notifyDataSetChanged();
            if (input > 180) {
                Toast.makeText(GameActivity.this, "Invalid Score", Toast.LENGTH_SHORT).show();
            }
            ((EditText) findViewById(R.id.inputUserNameEditText)).getText().clear();
//            setAverageScoreTextView();
//            setVisitsTextView();
            endGameChecker();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public void endGameChecker() {
        if (gameViewModel.getFinished()) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(recyclerView.getApplicationWindowToken(), 0);
            inputScoreEditText.setVisibility(View.GONE);
            doneButton.setVisibility(View.GONE);
            gameViewModel.deleteGameStateByID(gameViewModel.getGameId());
//            gameViewModel.updateAllUsers();
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
            if (gameViewModel.getFinished()) {
                inputScoreEditText.setVisibility(View.VISIBLE);
                doneButton.setVisibility(View.VISIBLE);
                gameViewModel.setFinished(false);
            }
            gameViewModel.undo();
//            setAverageScoreTextView();
//            setVisitsTextView();
        }
        return super.onOptionsItemSelected(item);
    }

//    private void setBanana() {
//        if (gameViewModel.bananaSplit()) {
//            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputMethodManager.hideSoftInputFromWindow(recyclerView.getApplicationWindowToken(), 0);
//            bananaView.setVisibility(View.VISIBLE);
//            bananaView.postDelayed(() -> bananaView.setVisibility(View.GONE), 150);
//        } else bananaView.setVisibility(View.GONE);
//    }
}
