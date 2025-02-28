package com.example.dartscoreboard.Game;

import android.content.Context;
import android.content.Intent;
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

import java.util.Objects;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String GAME_STATE_KEY = "GAME_STATE";
    private Toolbar toolbar;
    private GameViewModel gameViewModel;
    private RecyclerView recyclerView;
    private EditText inputScoreEditText;
    private TextView averageScoreTextView;
    private TextView visitsTextView;
    private Button doneButton;
    private RecyclerAdapterGamePlayers adapter;
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
        toolbar.setTitle(gameViewModel.getGameType().name);
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
        launchGameSetUp();
        Log.d("dom test", "game id currently is: " + String.valueOf(gameViewModel.getGameID()));

    }

    private void launchGameSetUp() {
        // Reads from intent once, then removes extra. If onCreate is called again, data is pulled directly from ViewModel class.
        Bundle arguments = getIntent().getExtras();
        assert arguments != null;
        String gameId = arguments.getString(GAME_STATE_KEY);
        assert gameId != null;
        Log.d("gameState", "gameactivity ID: " + gameId);

        setVisitsTextView();
        setAverageScoreTextView();
        gameViewModel.saveGameStateToDb();
    }

    private void setAverageScoreTextView() {
        double avg = gameViewModel.getPlayerAverage();
        averageScoreTextView.setText(String.valueOf(avg));
    }

    private void setVisitsTextView() {
        User activeUser = gameViewModel.getPlayersList().get(GameViewModel.getTurnIndex());
        int visits = activeUser.getVisits();
        visitsTextView.setText(String.valueOf(visits));
    }

    private void setAdapter() {
        adapter = new RecyclerAdapterGamePlayers(gameViewModel.getPlayersList());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
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
            setBanana();
            gameViewModel.playerVisit(input);
            adapter.notifyDataSetChanged();
            if (input > 180) {
                Toast.makeText(GameActivity.this, "Invalid Score", Toast.LENGTH_SHORT).show();
            }
            ((EditText) findViewById(R.id.inputUserNameEditText)).getText().clear();
            setAverageScoreTextView();
            setVisitsTextView();
            gameViewModel.saveGameStateToDb();
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
            gameViewModel.deleteGameStateByID(gameViewModel.getGameID());
            gameViewModel.updateAllUsers();
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
                GameState gameState = gameViewModel.getGameInfo();
//                gameState.setGameID(gameViewModel.getGameID());
//                gameViewModel.insert(gameState).subscribe();
                gameViewModel.updateAllUsers();
            }
            gameViewModel.undo(adapter);
            setAverageScoreTextView();
            setVisitsTextView();
            GameState gameState = gameViewModel.getGameInfo();
//            gameState.setGameID(gameViewModel.getGameID());
            gameViewModel.update(gameState);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setBanana() {
        if (gameViewModel.bananaSplit()) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(recyclerView.getApplicationWindowToken(), 0);
            bananaView.setVisibility(View.VISIBLE);
            bananaView.postDelayed(() -> bananaView.setVisibility(View.GONE), 150);
        } else bananaView.setVisibility(View.GONE);
    }
}
