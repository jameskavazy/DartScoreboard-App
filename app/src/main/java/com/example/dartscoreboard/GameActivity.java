package com.example.dartscoreboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartscoreboard.models.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    private MatchHistoryViewModel matchHistoryViewModel;
    public static final String GAME_STATE_ID = "GAME_STATE_ID_KEY";
    public static final String MATCH_HISTORY_EXTRA_KEY = "OPEN_GAME_ACTIVITY_KEY";
    private RecyclerView recyclerView;
    private EditText inputScoreEditText;
    private TextView averageScoreTextView;
    private TextView visitsTextView;
    private Button undoButton;
    private Button doneButton;
    private RecyclerAdapterGamePlayers adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        setAdapter();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toolbar.setTitle(GameController.getInstance().getGameType().name);
        toolbar.setTitleMarginStart(24);
    }

    private void setupUI() {
        setContentView(R.layout.game_activity);
        toolbar = findViewById(R.id.toolbar);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        averageScoreTextView = findViewById(R.id.avg_text_view);
        visitsTextView = findViewById(R.id.visits_text_view);
        doneButton = findViewById(R.id.done_button);
        recyclerView = findViewById(R.id.player_info_recycler_view);
        inputScoreEditText = findViewById(R.id.inputUserNameEditText);
        setUndoButton();
        doneButton.setOnClickListener(this);
        inputScoreEditText.setOnEditorActionListener((v, actionId, event) -> onScoreEntered());
        matchHistoryViewModel = new ViewModelProvider(this).get(MatchHistoryViewModel.class);

        //-------------Set Title and GameSettings based on Extras------------------
        Intent intent = getIntent();
        Bundle arguments = getIntent().getExtras();

        if (intent.hasExtra(GAME_STATE_ID)) {
            //Get information for game from the MatchHistoryScreen - Existing Game
            long gameId = intent.getLongExtra(GAME_STATE_ID, -1);
            GameState gameState = (GameState) arguments.getSerializable(MATCH_HISTORY_EXTRA_KEY);
            GameController.getInstance().initialiseGameController(gameState.getGameType(), gameState.getGameSettings()
                    , gameState.getPlayerList(), gameState.getTurnIndex(), gameState.getTurnLeadForLegs(), gameState.getTurnLeadForSets(), gameState.getMatchStateStack(), gameId);
            setVisitsTextView();
            setAverageScoreTextView();
            //clear the intent after use; only want to init GameController once with the initial GS data
            intent.removeExtra(GAME_STATE_ID);
        } else { //just use Controller info
            // NEW GAME - (GameSettings are passed directly to controller) OR onCreate called again by .eg. orientation change (Controller already has relevant information)
            saveGameStateToDb();
        }

    }

    private void setUndoButton() {
        undoButton = new Button(new ContextThemeWrapper(getBaseContext(),R.style.Base_Theme_DartScoreboard));
        undoButton.setOnClickListener(this);
        Toolbar.LayoutParams undoV2Params  = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        undoV2Params.gravity = Gravity.END;
        undoV2Params.setMarginEnd(24);
        undoButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.undo_icon,0,0,0);
//        undoButton.setText(R.string.undo);
        toolbar.addView(undoButton, undoV2Params);
    }

    private void setAverageScoreTextView() {
        double avg = GameController.getInstance().getPlayersList().get(GameController.getInstance().getTurnIndex()).getAvg();
        averageScoreTextView.setText(String.valueOf(avg));
    }

    private void setVisitsTextView() {
        User activeUser = GameController.getInstance().getPlayersList().get(GameController.getInstance().getTurnIndex());
        int visits = activeUser.getVisits();
        visitsTextView.setText(String.valueOf(visits));
    }

    private void setAdapter() {
        adapter = new RecyclerAdapterGamePlayers(GameController.getInstance().getPlayersList());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
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
            GameController.getInstance().playerVisit(input);
            adapter.notifyDataSetChanged(); //todo once turn is used by game controller as int, change this?
            if (input > 180) {
                Toast.makeText(GameActivity.this, "Invalid Score", Toast.LENGTH_SHORT).show();
            }
            ((EditText) findViewById(R.id.inputUserNameEditText)).getText().clear();
            setAverageScoreTextView();
            setVisitsTextView();
            saveGameStateToDb();
            endGameChecker();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
    private void saveGameStateToDb() {
        GameState gameState = getGameInfo();
//      Create GameState object + attach the id for DB update
        if (GameController.getInstance().getGameID() != 0) {
            gameState.setGameID(GameController.getInstance().getGameID());
            matchHistoryViewModel.update(gameState);
        } else {
            matchHistoryViewModel.insert(gameState).subscribe(new SingleObserver<Long>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onSuccess(@NonNull Long aLong) {
                    Log.d("dom test", "onSuccess " + aLong);
                    GameController.getInstance().setGameID(aLong);
                }

                @Override
                public void onError(@NonNull Throwable e) {

                }
            });

        }
    }

    public void endGameChecker() {
        if (GameController.gameStateEnd) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(recyclerView.getApplicationWindowToken(), 0);
            inputScoreEditText.setVisibility(View.GONE);
            doneButton.setVisibility(View.GONE);
            matchHistoryViewModel.deleteGameStateByID(GameController.getInstance().getGameID());
        }
    }

    @Override
    public void onClick(View v) {
        // todo this would be nice as a switch
        int viewId = v.getId();
        if (v == undoButton) {
            Log.d("dom test", "Undo Click");

            //Brings back text input if game was finished.
            if (GameController.gameStateEnd) {
                inputScoreEditText.setVisibility(View.VISIBLE);
                doneButton.setVisibility(View.VISIBLE);
                GameController.gameStateEnd = false;
                GameState gameState = getGameInfo();
                gameState.setGameID(GameController.getInstance().getGameID());
                matchHistoryViewModel.insert(gameState);
            }
            GameController.getInstance().undo(adapter);
            setAverageScoreTextView();
            setVisitsTextView();
            GameState gameState = getGameInfo();
            gameState.setGameID(GameController.getInstance().getGameID());
            matchHistoryViewModel.update(gameState);
        } else if (viewId == R.id.done_button) {
            Log.d("dom test", "Done Click");
            onScoreEntered();
        }
    }

    private GameState getGameInfo(){
        return new GameState(
                GameController.getInstance().getGameType(),
                GameController.getInstance().getGameSettings(),
                GameController.getInstance().getPlayersList(),
                GameController.getInstance().getTurnIndex(),
                GameController.getInstance().getTurnIndexLegs(),
                GameController.getInstance().getTurnIndexSets(),
                GameController.getInstance().getMatchStateStack());
    }
}
