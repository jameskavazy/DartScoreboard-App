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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Stack;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private MatchHistoryViewModel matchHistoryViewModel;

    public static final String GAME_STATE_ID = "GAME_STATE_KEY";
    public static final String OPEN_GAME_ACTIVITY_KEY = "OPEN_GAME_ACTIVITY_KEY";



    public static final String GAME_STATE_SLOT1_KEY = "GAME_STATE_SLOT1_KEY";
    public static final String GAME_STATE_SLOT2_KEY = "GAME_STATE_SLOT2_KEY";
    public static final String GAME_STATE_SLOT3_KEY = "GAME_STATE_SLOT3_KEY";

    //public static final String STACK_KEY = "STACK_KEY";
    private String slotKey;
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
    private int playerStartingScore;
    private GameSettings gameSettings;
    private int turnLead;
    private int turnLeadForSets;
    private RecyclerAdapterGamePlayers adapter;
    private OnBackPressedDispatcher callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
//        matchHistoryViewModel = ViewModelProviders.of(this).get(MatchHistoryViewModel.class);
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
        initGameController();
        GameController.getInstance().gameStart();
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

        //Set Title based on Extras
        Intent intent = getIntent();
        Bundle arguments = getIntent().getExtras();
        if (intent.hasExtra(GAME_STATE_ID)){
            GameState gameState = (GameState) intent.getSerializableExtra(OPEN_GAME_ACTIVITY_KEY);
            gameTitle.setText(gameState.getGameType().name);
        } else {
            assert arguments != null;
            SelectGameActivity.GameType gameType = (SelectGameActivity.GameType) arguments.getSerializable(SelectGameActivity.GAME_TYPE_KEY);
            assert gameType != null;
            gameTitle.setText(gameType.name);
        }
    }
    private void initGameController() {
        Bundle arguments = getIntent().getExtras();
        slotKey = arguments.getString(SelectGameActivity.SLOT_KEY);

        if (playersList == null){
            playersList = new ArrayList<>();
        }
        if (PreferencesController.getInstance().readGameState(slotKey) == null){
            playersList = (ArrayList<User>) arguments.getSerializable(SelectGameActivity.PLAYERS_FOR_GAME_KEY);
        } else playersList = PreferencesController.getInstance().readGameState(slotKey).getPlayerList();

        GameController.getInstance().setPlayersList(playersList);
        GameController.getInstance().setSlotKey(arguments.getString(SelectGameActivity.SLOT_KEY));
        GameController.getInstance().setGameSettings((GameSettings) arguments.getSerializable(SelectGameActivity.GAME_SETTINGS_KEY));
        GameController.getInstance().setGameType((SelectGameActivity.GameType) arguments.getSerializable(SelectGameActivity.GAME_TYPE_KEY));
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
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public void endGameChecker() {
        if (gameStateEnd) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(recyclerView.getApplicationWindowToken(), 0);
            inputScoreEditText.setVisibility(View.GONE);
            doneButton.setVisibility(View.GONE);
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
