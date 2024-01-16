package com.example.dartscoreboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dartscoreboard.models.User;

import java.util.ArrayList;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private MatchHistoryViewModel matchHistoryViewModel;
    public static final String GAME_STATE_ID = "GAME_STATE_ID_KEY";
    public static final String MATCH_HISTORY_EXTRA_KEY = "OPEN_GAME_ACTIVITY_KEY";
    private long id;
    private boolean existingGame;
    private TextView gameTitle;

    private ArrayList<User> playersList;
    private RecyclerView recyclerView;
    private EditText inputScoreEditText;
    private TextView averageScoreTextView;
    private TextView visitsTextView;
    private Button undoButton;
    private Button doneButton;
    private SelectGameActivity.GameType gameType;
    private GameSettings gameSettings;
    private RecyclerAdapterGamePlayers adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        setAdapter();
    }

    private void setupUI() {
        setContentView(R.layout.game_activity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        averageScoreTextView = findViewById(R.id.avg_text_view);
        visitsTextView = findViewById(R.id.visits_text_view);
        undoButton = findViewById(R.id.undo_button);
        doneButton = findViewById(R.id.done_button);
        recyclerView = findViewById(R.id.player_info_recycler_view);
        gameTitle = findViewById(R.id.gameActivityTitle);
        inputScoreEditText = findViewById(R.id.inputUserNameEditText);
        undoButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);
        inputScoreEditText.setOnEditorActionListener((v, actionId, event) -> onScoreEntered());
        matchHistoryViewModel = new ViewModelProvider(this).get(MatchHistoryViewModel.class);

        //-------------Set Title and GameSettings based on Extras------------------
        Intent intent = getIntent();
        Bundle arguments = getIntent().getExtras();

        if (playersList == null) {
            playersList = new ArrayList<>();
        }


        if (intent.hasExtra(GAME_STATE_ID)) {
            //Get information for game from the MatchHistoryScreen - Existing Game
            existingGame = true;
            // TODO: 11/01/2024 Refactor so that id is managed by controller
//            id = intent.getIntExtra(GAME_STATE_ID, -1);
            id = intent.getLongExtra(GAME_STATE_ID,-1);
            GameState gameState = (GameState) arguments.getSerializable(MATCH_HISTORY_EXTRA_KEY);
            gameTitle.setText(gameState.getGameType().name);
            setPlayersList(gameState.getPlayerList());
            GameController.getInstance().initialiseGameController(gameState.getGameType(), gameState.getGameSettings()
                    , gameState.getPlayerList(), gameState.getTurnIndex(), gameState.getTurnLeadForLegs(), gameState.getTurnLeadForSets(), gameState.getMatchStateStack());
            setVisitsTextView();
            setAverageScoreTextView();
        } else {
            //todo pass info with intents rather than set into GC if from new game...?

            //GameSettings are passed directly to controller -- new game
            existingGame = false;
            gameType = GameController.getInstance().getGameType();
            gameSettings = GameController.getInstance().getGameSettings();
            setPlayersList(GameController.getInstance().getPlayersList());
            GameController.getInstance().setTurnIndex(0);
            saveGameStateToDb();
            existingGame = true;
            GameController.getInstance().setPlayerStartingScores();
            gameTitle.setText(gameType.name);
            averageScoreTextView.setText("-");
            visitsTextView.setText("-");
        }
    }

    private void setAverageScoreTextView() {
        double avg = playersList.get(GameController.getInstance().getTurnIndex()).getAvg();
        averageScoreTextView.setText(String.valueOf(avg));
    }

    private void setVisitsTextView() {
        int visits = playersList.get(GameController.getInstance().getTurnIndex()).getVisits();
        visitsTextView.setText(String.valueOf(visits));
    }

    private void setAdapter() {
        adapter = new RecyclerAdapterGamePlayers(playersList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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

//    private void undo() {
//        matchHistoryViewModel.findGameById(id).observe(this, gameState -> {
//            MatchState matchState = gameState.getMatchStateStack().pop();
//            GameController.getInstance().initialiseGameController(gameState.getGameType(), gameState.getGameSettings(), matchState.getPlayerList()
//                    , matchState.getTurnIndex(), matchState.getTurnIndexForLegs(), matchState.getTurnIndexForSets(), gameState.getMatchStateStack());
//            adapter.setUsersList(matchState.getPlayerList());
//            adapter.notifyDataSetChanged();
//        });
//    }

    private void saveGameStateToDb() {
        Log.d("dom test","saveGameStateToDb " + id );
        GameState gameState = getGameInfo();
        Intent intent = getIntent();
//      If either existing game by boolean or because a game state id already exists, attach the id & update
        if (intent.hasExtra(GAME_STATE_ID) || existingGame) {
            gameState.setGameID(id);
            matchHistoryViewModel.update(gameState);
        } else {
            matchHistoryViewModel.insert(gameState).subscribe(new SingleObserver<Long>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onSuccess(@NonNull Long aLong) {
                    Log.d("dom test", "onSuccess " + aLong);
                    id = aLong;
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
            matchHistoryViewModel.deleteGameStateByID(id);
//            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        // todo this would be nice as a switch
        int viewId = v.getId();
        if (viewId == R.id.undo_button) {
            Log.d("dom test", "Undo Click");

            //Brings back text input if game was finished.
            if (GameController.gameStateEnd) {
                inputScoreEditText.setVisibility(View.VISIBLE);
                doneButton.setVisibility(View.VISIBLE);
                GameController.gameStateEnd = false;
                GameState gameState = getGameInfo();
                gameState.setGameID(id);
                matchHistoryViewModel.insert(gameState);
            }
            GameController.getInstance().undo(adapter);
            setPlayersList(GameController.getInstance().getPlayersList());
            setAverageScoreTextView();
            setVisitsTextView();
            //todo update the inserted gameState
            GameState gameState = getGameInfo();
            gameState.setGameID(id);
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

    public void setPlayersList(ArrayList<User> playersList) {
        this.playersList = playersList;
    }

}
