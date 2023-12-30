package com.example.dartscoreboard;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

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

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Stack;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private MatchHistoryViewModel matchHistoryViewModel;
    public static final String GAME_STATE_ID = "GAME_STATE_ID_KEY";
    public static final String MATCH_HISTORY_EXTRA_KEY = "OPEN_GAME_ACTIVITY_KEY";
    private int id;

    private boolean existingGame;

    //public static final String STACK_KEY = "STACK_KEY";
    private TextView gameTitle;
    private ArrayList<User> playersList;
    private RecyclerView recyclerView;
    private EditText inputScoreEditText;
    private TextView averageScoreTextView;
    private TextView visitsTextView;
    private Button undoButton;
    private Button doneButton;
    private Stack<GameState> gameStateStack;
    public static boolean gameStateEnd;
    private SelectGameActivity.GameType gameType;
    private GameSettings gameSettings;
    private RecyclerAdapterGamePlayers adapter;
    private OnBackPressedDispatcher callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
//        callback = new OnBackPressedDispatcher();
//        callback.addCallback(this, new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                GameController.getInstance().clearTurnIndices();
//                finish();
//            }
//        }); //todo this isn't being called/run^^
//        Log.d("dom test", "gameType\n-------\nname " + getGameType().name + "\nstartingScore " + getGameType().startingScore);
        setupUI();
        setAdapter();
    }
    private void setupUI() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT); // todo delete this eventually
        averageScoreTextView = findViewById(R.id.avg_text_view);
        visitsTextView = findViewById(R.id.visits_text_view);
        undoButton = findViewById(R.id.undo_button);
        doneButton = findViewById(R.id.done_button);
        recyclerView = findViewById(R.id.player_info_recycler_view);
        gameTitle = findViewById(R.id.gameActivityTitle);
        inputScoreEditText = findViewById(R.id.inputUserNameEditText);
        averageScoreTextView.setText(String.valueOf(0.0));
        undoButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);
        inputScoreEditText.setOnEditorActionListener((v, actionId, event) -> onScoreEntered());
        matchHistoryViewModel = new ViewModelProvider(this).get(MatchHistoryViewModel.class);

        //-------------Set Title and GameSettings based on Extras------------------
        Intent intent = getIntent();
        Bundle arguments = getIntent().getExtras();

        if (playersList == null){
            playersList = new ArrayList<>();
        }
        //todo game controller turn index needs clearing -- is this working now?


        //todo When the new game is created, savetoDB. Then you can get the id off that entry and use that to update going forward

        if (intent.hasExtra(GAME_STATE_ID)){
            //Get information for game from the MatchHistoryScreen - Existing Game
            existingGame = true;
            id = intent.getIntExtra(GAME_STATE_ID, -1);
            GameState gameState = (GameState) arguments.getSerializable(MATCH_HISTORY_EXTRA_KEY);
            gameTitle.setText(gameState.getGameType().name);
            playersList = gameState.getPlayerList();
            GameController.getInstance().initialiseGameController(gameState.getGameType(),gameState.getGameSettings()
                    ,gameState.getPlayerList(), gameState.getTurnIndex(),gameState.getTurnLeadForLegs(),gameState.getTurnLeadForSets());
            GameController.getInstance().setGameID(id);

        } else {
            //GameSettings are passed directly to controller -- new game
            existingGame = false;
            gameType = GameController.getInstance().getGameType();
            gameSettings = GameController.getInstance().getGameSettings();
            playersList = GameController.getInstance().getPlayersList();
            GameController.getInstance().setTurnIndex(0);
            saveGameStateToDb();
            existingGame = true;
            GameController.getInstance().setPlayerStartingScores();
            gameTitle.setText(gameType.name);
        }
    }

    private void setAverageScoreTextView() {
        double avg = playersList.get(GameController.getInstance().getTurnIndex()).getAvg();
        averageScoreTextView.setText(String.valueOf(avg));
    }
    private void setVisitsTextView(){
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
        if (!inputScoreEditText.getText().toString().isEmpty()){
            try {
                input = Integer.parseInt(inputScoreEditText.getText().toString());
            } catch (Exception ignored){

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
            endGameChecker();
            saveGameStateToDb();

            return true;
        } catch (Exception e) {
//            e.printStackTrace();
            return true;
        }
    }

    private void saveGameStateToDb() {

        GameState gameState = new GameState(
                GameController.getInstance().getGameType(),
                GameController.getInstance().getGameSettings(),
                GameController.getInstance().getPlayersList(),
                GameController.getInstance().getTurnIndex(),
                GameController.getInstance().getTurnLeadForLegs(),
                GameController.getInstance().getTurnLeadForSets());

        Intent intent = getIntent();
        if (intent.hasExtra(GAME_STATE_ID) || existingGame) {
            gameState.setGameID(id);
            matchHistoryViewModel.update(gameState);
            //todo even if existing game is true, we need to find the id of the first game entered and get it
        } else {
            matchHistoryViewModel.insert(gameState);
            id = (int) matchHistoryViewModel.getInsertedId();
        }
    }


    public void endGameChecker() {
        if (gameStateEnd) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(recyclerView.getApplicationWindowToken(), 0);
            inputScoreEditText.setVisibility(View.GONE);
            doneButton.setVisibility(View.GONE);

//            GameState gameState = new GameState(
//                    GameController.getInstance().getGameType(),
//                    GameController.getInstance().getGameSettings(),
//                    GameController.getInstance().getPlayersList(),
//                    GameController.getInstance().getTurnIndex(),
//                    GameController.getInstance().getTurnLeadForLegs(),
//                    GameController.getInstance().getTurnLeadForSets());
//
//            matchHistoryViewModel.delete(gameState);
        }
    }



    @Override
    public void onClick(View v) {
 // todo this would be nice as a switch
        int viewId = v.getId();
        if (viewId == R.id.undo_button) {
            Log.d("dom test", "Undo Click");
            GameController.getInstance().undo();
        } else if (viewId == R.id.done_button) {
            Log.d("dom test", "Done Click");
            onScoreEntered();
        }
    }


}
