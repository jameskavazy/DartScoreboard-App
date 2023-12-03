package com.example.dartscoreboard;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

import android.content.Context;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Stack;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String GAME_STATE_SLOT1_KEY = "GAME_STATE_SLOT1_KEY";
    public static final String GAME_STATE_SLOT2_KEY = "GAME_STATE_SLOT2_KEY";
    public static final String GAME_STATE_SLOT3_KEY = "GAME_STATE_SLOT3_KEY";

    //public static final String STACK_KEY = "STACK_KEY";
    private String slotKey;
    private TextView gameTitle;
    private ArrayList<User> playersList;
    private RecyclerView recyclerView;
    private EditText inputScoreEditText;
    private ArrayDeque<GameState> gameStateArrayDeque;
    private TextView averageScoreTextView;
    private TextView visitsTextView;
    private Button undoButton;
    private Button doneButton;

    private Stack<GameState> gameStateStack;
    private boolean gameStateEnd;
    private SelectGameActivity.GameType gameType;
    private int playerStartingScore;
    private GameSettings gameSettings;
    private int turnLead;
    private int turnLeadForSets;
    private RecyclerAdapterGamePlayers adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("dom test", "GameActivityOnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
//        Log.d("dom test", "gameType\n-------\nname " + getGameType().name + "\nstartingScore " + getGameType().startingScore);
        setupUI();
        newGameStart();
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
//        gameTitle.setText(getGameType().name);
        averageScoreTextView.setText(String.valueOf(0.0));
        undoButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);
        inputScoreEditText.setOnEditorActionListener((v, actionId, event) -> onScoreEntered());
    }
    
    private void newGameStart() { // todo move earlier
        gameStateArrayDeque = new ArrayDeque<>();
        gameStateStack = new Stack<>();
        GameState existingGame = getGameState();
        if (existingGame == null) {
            // new game
            playersList = (ArrayList<User>) getIntent().getExtras().getSerializable(SelectGameActivity.PLAYERS_FOR_GAME_KEY);
            gameType = getGameType();
            gameTitle.setText(gameType.name);
            setGameSettings();
            setPlayerStartingScores();
            setPlayerTurns();
            setPlayerLegs();
            setPlayerSets();

        } else {
            playersList = existingGame.getPlayerList();
            gameSettings = existingGame.getGameSettings();
            gameType = existingGame.getGameType();
            gameTitle.setText(gameType.name);
        }
        setAdapter();
        gameStateEnd = false;
        turnLead = 0; // todo may need to save these
        turnLeadForSets = 0;
    }

    private GameState getGameState() {
        Bundle bundle = getIntent().getExtras();
        this.slotKey = bundle.getString(SelectGameActivity.SLOT_KEY);
        return PreferencesController.getInstance().readGameState(slotKey);
    }

    private void setGameSettings() {
        Bundle arguments = getIntent().getExtras();
        gameSettings = (GameSettings) arguments.getSerializable(SelectGameActivity.GAME_SETTINGS_KEY);
    }



    private void setAverageScoreTextView() {
        for (User user : playersList){
            if (user.turn){
                double avg = user.getAvg();
                averageScoreTextView.setText(String.valueOf(avg));
            }
        }
    }
    private void setVisitsTextView(){
        for (User user : playersList){
            if (user.turn){
                int visits = user.getVisits();
                visitsTextView.setText(String.valueOf(visits));
            }
        }
    }




    public void playerVisit(String scoreString) {
        //Save current gameState for undo
        GameState previousGameState = new GameState(gameType,gameSettings,playersList,turnLead,turnLeadForSets);
        saveForUndo(previousGameState);
        Log.d("dom test", "playerVisit saveForUndo=  " + previousGameState);
        for (User user:previousGameState.getPlayerList()
        ) {
            Log.d("dom test", "playerVisit saveForUndo= " + user.username + " score is " + user.playerScore);
        }


        int scoreInt = Integer.parseInt(scoreString); //todo try catch parseint
        if (scoreInt <= 180) { // checks for valid score input

            for (User player : playersList){
                int currentScore = player.getPlayerScore();
                //For current player - do calculation
                if (player.isTurn()){

                    //Calculate new score
                    player.setPlayerScore(subtract(currentScore,scoreInt));
                    Log.d("dom test", player.username + " previous scorelist after subtract =  " + player.getPreviousScoresList());


                    player.setTurn(false);
                    adapter.notifyItemChanged(playersList.indexOf(player));

                    //If player is not last in the list, set next player turn to true & exit loop
                    User firstPlayer = playersList.get(0);
                    User lastPlayer = playersList.get(playersList.size() - 1);

                    if (player != lastPlayer) { // todo have field of current player index instead of set turn field
                        playersList.get(playersList.indexOf(player) + 1).setTurn(true);
                        adapter.notifyItemChanged(playersList.indexOf(player) + 1);
                    }
                    else {
                        firstPlayer.setTurn(true);
                        adapter.notifyItemChanged(0);
                    }

                    break;
                }
            }
        }
        nextLeg();
        //Ensures game state is not saved if the game has finished.
        if (!gameStateEnd) {
            saveGameState();
        }
    }

    public void saveGameState() {
        PreferencesController.getInstance().saveGameState(new GameState(gameType, gameSettings, playersList,turnLead, turnLeadForSets), slotKey);
    }

    public int subtract(int playerScore, int currentTypedScore) {
        int newScore = playerScore - currentTypedScore;
        if ((((playerScore <= 180) && (playerScore >= 171)) || (playerScore == 169) || (playerScore == 168) || (playerScore == 166) || (playerScore == 165) || (playerScore == 163) || (playerScore == 162) || (playerScore == 159)) && (currentTypedScore == playerScore)) {
            for (User player : playersList
            ) {
                if (player.turn){
                    player.addToPreviousScoresList(0);
                }
            }
            Toast.makeText(GameActivity.this, "BUST", Toast.LENGTH_SHORT).show();
            return playerScore;
        }

        if (currentTypedScore > 180) {
            for (User player : playersList
            ) {
                if (player.turn){
                    player.addToPreviousScoresList(0);
                }
            }
            return playerScore;
        }

        if (newScore > 1) {
            for (User player : playersList
            ) {
                if (player.turn) {
                    player.addToPreviousScoresList(currentTypedScore);
                }
            }
            return newScore;
        }
        if (newScore == 0) {
            for (User player: playersList
            ) {
                if (player.turn) {
                    player.addToPreviousScoresList(currentTypedScore);
                }
            }
            return newScore;

        }
        else {
            for (User player : playersList
            ) {
                if (player.turn){
                    player.addToPreviousScoresList(0);
                }
            }
            Toast.makeText(GameActivity.this, "BUST", Toast.LENGTH_SHORT).show();
            return playerScore;
        }
    }

    public void setPlayerTurns() {
        playersList.get(0).setTurn(true); // todo try catch
        for (int i = 1; i < playersList.size(); i++) {
            playersList.get(i).setTurn(false);
        }
    }


    public void setPlayerLegs(){
        for (int i = 0; i < playersList.size(); i++) {
            playersList.get(i).setCurrentLegs(0);
        }
    }

    public void setPlayerSets(){
        for (int i = 0; i < playersList.size(); i++) {
            playersList.get(i).setCurrentSets(0);
        }
    }


    public void nextLeg(){
        for (User player : playersList){
            if (player.getPlayerScore() == 0) {
                player.currentLegs++;
                turnLead++;
                if (turnLead == playersList.size()){
                    turnLead = 0;
                }
                incrementTurnForLegs(turnLead);
                Log.d("dom test",player.username + " current legs = " + player.currentLegs);
                nextSet();
                matchWonChecker();
                if (!gameStateEnd) {
                    setPlayerStartingScores();
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void nextSet(){
        for (User player:playersList
        ) {
            if (player.getPlayerScore() == 0 && player.getCurrentLegs() == gameSettings.getTotalLegs()) {
                player.currentSets++;
                setPlayerLegs();
                turnLeadForSets++;
                if (turnLeadForSets == playersList.size()){
                    turnLeadForSets = 0;
                }
                turnLead = turnLeadForSets;
                incrementTurnForSets(turnLeadForSets);
                adapter.notifyDataSetChanged();
            }
        }
    }
    public void matchWonChecker(){
        for (User player : playersList
        ) {
            if (player.getPlayerScore() == 0 && player.currentSets == gameSettings.getTotalSets()){
                adapter.notifyDataSetChanged();
                Toast.makeText(GameActivity.this, player.getUsername() + " wins the match!", Toast.LENGTH_LONG).show();
                endGame();
            }
        }
    }

    public void setPlayerStartingScores() {
        for (User user : playersList){
            user.setPlayerScore(gameType.startingScore);
        }
    }

    public void incrementTurnForLegs(int turnLead){
        for (int i = 0; i < playersList.size(); i++) {
            playersList.get(i).setTurn(i == turnLead);
        }
    }

    public void incrementTurnForSets(int turnLeadForSets){
        for (int i = 0; i < playersList.size(); i++) {
            playersList.get(i).setTurn(i == turnLeadForSets);
        }
    }

    public void endGame() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(recyclerView.getApplicationWindowToken(), 0);
        inputScoreEditText.setVisibility(View.GONE);
        doneButton.setVisibility(View.GONE);
        PreferencesController.getInstance().clearGameState(slotKey);
        gameStateEnd = true;
    }





    public SelectGameActivity.GameType getGameType() {
        if (gameType != null) {
            return gameType;
        }
        Bundle arguments = getIntent().getExtras();
        gameType = (SelectGameActivity.GameType) arguments.getSerializable(SelectGameActivity.GAME_TYPE_KEY);
        return gameType;
    }
    
    private void setAdapter() {
        adapter = new RecyclerAdapterGamePlayers(playersList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }




    private Boolean onScoreEntered() {
        String input = inputScoreEditText.getText().toString();
        try {
            Log.d("dom test", "IME_ACTION_DONE");
            playerVisit(input);
            if (Integer.parseInt(input) > 180) {
                Toast.makeText(GameActivity.this, "Invalid Score", Toast.LENGTH_SHORT).show();
            }
            ((EditText) findViewById(R.id.inputUserNameEditText)).getText().clear();
            setAverageScoreTextView();
            setVisitsTextView();
            return true;
        } catch (Exception e) {
            playerVisit(String.valueOf(0)); // todo this should be an empty check. do nothing if exception
            setAverageScoreTextView();
            setVisitsTextView();
            return true;
        }
    }






    @Override
    public void onClick(View v) {

//        switch (v.getId()) { // todo this would be nice as a switch
//            case undoButton:
//                Log.d("dom test", "Undo Click");
//                undo();
//                break;
//            case doneButton:
//                onDoneClicked();
//                break;
//            default:
//                break;
//        }
        int viewId = v.getId();
        if (viewId == R.id.undo_button) {
            Log.d("dom test", "Undo Click");
            undo();
        } else if (viewId == R.id.done_button) {
            Log.d("dom test", "Done Click");
            onScoreEntered();
        }
    }

    private void saveForUndo(GameState previousGameState){
        //gameStateArrayDeque.add(previousGameState);
        gameStateStack.push(previousGameState);
    }

    private void undo() { // todo bring back the HashMap/map this worked well.
        //Brings back text input if game was finished.
        if (gameStateEnd) {
            inputScoreEditText.setVisibility(View.VISIBLE);
            doneButton.setVisibility(View.VISIBLE);
            gameStateEnd = false;
        }
//        //GameState previousGameState = gameStateArrayDeque.pollFirst();
//        GameState previousGameState = gameStateStack.pop();
//        Log.d("dom test","onUndo previousGameState = " + previousGameState);
//        if (previousGameState!= null){
//
//            for (User user:previousGameState.getPlayerList()
//            ) {
//                user.setPlayerScore(user.playerScore);
//                user.setTurn(user.turn);
//                user.setPlayerLegs(user.getCurrentLegs());
//                user.setPlayerSets(user.getCurrentSets());
//                if (!user.previousScoresList.isEmpty()) {
//                    user.previousScoresList.remove(user.previousScoresList.size() - 1);
//                }
//            }
//
//
//
//
//        } else saveGameState();
//
//        setVisitsTextView();
//        setAverageScoreTextView();
//        adapter.notifyDataSetChanged();
//
////
////        GameState previousGameState = gameStateArrayDeque.pollFirst();
////        if (!gameStateArrayDeque.isEmpty()){
////            previousGameState.loadPreviousGameState(playersList);
////        }
////
////        else {
////            Log.d("dom test","Deque Is Empty");
////            setSaveGameState();
////        }
////        setAverageScoreTextView();
////        setVisitsTextView();
////        adapter.notifyDataSetChanged();
    }
}
