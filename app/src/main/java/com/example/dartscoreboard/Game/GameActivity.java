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

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String GAME_STATE_KEY = "GAME_STATE";

    public static final String GAME_TYPE_KEY = "GAME_TYPE_KEY";
    public static final String PLAYER_LIST_KEY = "PLAYER_LIST_KEY";
    public static final String GAME_SETTINGS_KEY = "GAME_SETTINGS_KEY";
    public static final String MATCH_STATE_STACK_KEY = "MATCH_STATE_STACK_KEY";
    public static final String TURN_INDEX ="TURN_INDEX";
    public static final String TURN_INDEX_LEGS ="TURN_INDEX_LEGS";
    public static final String TURN_INDEX_SETS ="TURN_INDEX_SETS";

    public static final String GAME_STATE_ID_KEY = "GAME_STATE_ID_KEY";
    public static final String MATCH_HISTORY_EXTRA_KEY = "OPEN_GAME_ACTIVITY_KEY";

    public static final String TURN_INDEX_KEY = "TURN_INDEX_KEY";
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
//        toolbar.setTitle(gameViewModel.getGameType().name);
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
    }

    private void launchGameSetUp() {
//        Intent intent = getIntent();
        Bundle arguments = getIntent().getExtras();
        gameViewModel.setGameState((GameState) arguments.getSerializable(GAME_STATE_KEY));
        setVisitsTextView();
        setAverageScoreTextView();
        saveGameStateToDb();







//        //-------------Set Title and GameSettings based on Extras------------------
//        Intent intent = getIntent();
//        Bundle arguments = getIntent().getExtras();
//
//        if (intent.hasExtra(GAME_STATE_ID)) {
//            //Get information for game from the MatchHistoryScreen - Existing Game
//            long gameId = intent.getLongExtra(GAME_STATE_ID, -1);
//            GameState gameState = (GameState) arguments.getSerializable(MATCH_HISTORY_EXTRA_KEY);
//            gameViewModel.initialiseGameController(gameState.getGameType(), gameState.getGameSettings()
//                    , gameState.getPlayerList(), gameState.getTurnIndex(), gameState.getTurnLeadForLegs(), gameState.getTurnLeadForSets(), gameState.getMatchStateStack(), gameId);
//            setVisitsTextView();
//            setAverageScoreTextView();
//            //clear the intent after use; only want to init GameController once with the initial GS data
//            intent.removeExtra(GAME_STATE_ID);
//        } else { //just use Controller info
//            // NEW GAME - (GameSettings are passed directly to controller) OR onCreate called again by .eg. orientation change (Controller already has relevant information)
//            saveGameStateToDb();
//        }
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext()) {
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
            setBanana();
            gameViewModel.playerVisit(input);

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
        // TODO: 15/03/2024 Move to GameViewModel
        GameState gameState = getGameInfo();
//      Create GameState object + attach the id for DB update
        if (gameViewModel.getGameID() != 0) {
            gameState.setGameID(gameViewModel.getGameID());
            gameViewModel.update(gameState);
        } else {
            gameViewModel.insert(gameState).subscribe(new SingleObserver<Long>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onSuccess(@NonNull Long aLong) {
                    Log.d("dom test", "onSuccess " + aLong);
                    gameViewModel.setGameID(aLong);
                }

                @Override
                public void onError(@NonNull Throwable e) {
                }
            });
        }
    }

    public void endGameChecker() {
        if (GameViewModel.gameStateEnd) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(recyclerView.getApplicationWindowToken(), 0);
            inputScoreEditText.setVisibility(View.GONE);
            doneButton.setVisibility(View.GONE);
            gameViewModel.deleteGameStateByID(gameViewModel.getGameID());
            updateAllUsers();
        }
    }

    @Override
    public void onClick(View v) {
        // todo this would be nice as a switch
        int viewId = v.getId();
        if (viewId == R.id.done_button) {
            Log.d("dom test", "Done Click");
            onScoreEntered();
        }
    }


    private GameState getGameInfo() {
        return new GameState(
                gameViewModel.getGameType(),
                gameViewModel.getGameSettings(),
                gameViewModel.getPlayersList(),
                GameViewModel.getTurnIndex(),
                gameViewModel.getTurnIndexLegs(),
                gameViewModel.getTurnIndexSets(),
                gameViewModel.getMatchStateStack());
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
            //Brings back text input if game was finished.
            if (GameViewModel.gameStateEnd) {
                inputScoreEditText.setVisibility(View.VISIBLE);
                doneButton.setVisibility(View.VISIBLE);
                GameViewModel.gameStateEnd = false;
                GameState gameState = getGameInfo();
                gameState.setGameID(gameViewModel.getGameID());
                gameViewModel.insert(gameState);
                updateAllUsers();
            }
            try {
                gameViewModel.undo(adapter);
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            setAverageScoreTextView();
            setVisitsTextView();
            GameState gameState = getGameInfo();
            gameState.setGameID(gameViewModel.getGameID());
            gameViewModel.update(gameState);
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateAllUsers() {
        for (User user : gameViewModel.getPlayersList()){
            gameViewModel.updateUser(user);
        }
    }

    private void setBanana() {
        if (gameViewModel.bananaSplit()) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(recyclerView.getApplicationWindowToken(), 0);
            bananaView.setVisibility(View.VISIBLE);
            bananaView.postDelayed(() -> {
                bananaView.setVisibility(View.GONE);
            }, 50);
        } else bananaView.setVisibility(View.GONE);
    }
}
