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

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView gameTitle;
    private ArrayList<User> playersList;
    private RecyclerView recyclerView;
    private EditText inputScoreEditText;

    private ArrayDeque<GameState> gameStateArrayDeque;
    private TextView averageScoreTextView;
    private TextView visitsTextView;
    private Button undoButton;

    private Button doneButton;
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
        this.setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT); //todo delete this eventually
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
    
    private void newGameStart() { // todo ,move earlier
        GameState existingGame = PreferencesController.getInstance().readGameState();
        if (existingGame != null) {
            playersList = existingGame.getPlayerList();
            gameSettings = existingGame.getGameSettings();
        } else {
            // new game
            gameStateArrayDeque = new ArrayDeque<>();
            playersList = (ArrayList<User>) getIntent().getExtras().getSerializable("PLAYERS_LIST_KEY");
            setGameSettings();
            setPlayerStartingScores();
            setPlayerTurns();
            setSaveGameState();
            setPlayerLegs();
            setPlayerSets();
        }

        setAdapter();


        gameStateEnd = false;
        turnLead = 0; // todo may need to save these
        turnLeadForSets = 0;
    }

    private void setGameSettings() {
        Bundle arguments = getIntent().getExtras();
        int totalLegs = arguments.getInt(SelectGameActivity.TOTAL_LEGS_KEY);
        int totalSets = arguments.getInt(SelectGameActivity.TOTAL_SETS_KEY);
        gameSettings = new GameSettings(totalLegs, totalSets);
    }


    private void setSaveGameState(){ // todo dom
        /*
         * Read gamestate from shared pref
         * If gs != null, use this state as there is an ongoing game
         * If gs == null, no ongoing game so start fresh
         * Make sure gs shared pref is cleared at end of game
         * Maybe add resume btn visibility based on this gs
         *
         *
         * */

        // after each turn update shared pref




//        for (User player:
//                playersList
//             ) {
//            saveGameState.currentScoresMap.put(player,player.playerScore);
//            saveGameState.turnsMap.put(player,player.turn);
//            //Log.d("dom test",player.username + " scorelist put to map" + player.getPreviousScoresList());
//            saveGameState.previousScoresListMap.put(player, player.getPreviousScoresList());
//            saveGameState.previousLegsMap.put(player,player.currentLegs);
//            saveGameState.previousSetsMap.put(player,player.currentSets);
//        }
//        gameStateArrayDeque.addFirst(new SaveGameState(gameType, playersList));
    }
    
    private void nextLeg(){
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

    private void nextSet(){
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
    private void matchWonChecker(){
        for (User player : playersList
             ) {
            if (player.getPlayerScore() == 0 && player.currentSets == gameSettings.getTotalSets()){
                adapter.notifyDataSetChanged();
                Toast.makeText(GameActivity.this, player.getUsername() + " wins the match!", Toast.LENGTH_LONG).show();
                endGame();
            }
        }
    }

    

    private void setPlayerStartingScores() {
        for (User user : playersList){
            user.setPlayerScore(gameType.startingScore);
        }
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
        //Loop through and save everyone's current position
        setSaveGameState();

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

        PreferencesController.getInstance().saveGameState(new GameState(gameType, gameSettings, playersList));
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


    private void setPlayerTurns() {
        playersList.get(0).setTurn(true); // todo try catch
        for (int i = 1; i < playersList.size(); i++) {
            playersList.get(i).setTurn(false);
        }
    }


    private void setPlayerLegs(){
        for (int i = 0; i < playersList.size(); i++) {
            playersList.get(i).setCurrentLegs(0);
        }
    }

    private void setPlayerSets(){
        for (int i = 0; i < playersList.size(); i++) {
            playersList.get(i).setCurrentSets(0);
        }
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

    private void incrementTurnForLegs(int turnLead){
        for (int i = 0; i < playersList.size(); i++) {
            playersList.get(i).setTurn(i == turnLead);
        }
    }

    private void incrementTurnForSets(int turnLeadForSets){
        for (int i = 0; i < playersList.size(); i++) {
            playersList.get(i).setTurn(i == turnLeadForSets);
        }
    }


    private void endGame() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(recyclerView.getApplicationWindowToken(), 0);
        inputScoreEditText.setVisibility(View.INVISIBLE);
        PreferencesController.getInstance().clearGameState();
        gameStateEnd = true;
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

    private void undo() { // todo undo
        //Brings back text input if game was finished.
//        if (gameStateEnd) {
//            inputScoreEditText.setVisibility(View.VISIBLE);
//        }
//
//        GameState previousGameState = gameStateArrayDeque.pollFirst();
//        if (!gameStateArrayDeque.isEmpty()){
//            previousGameState.loadPreviousGameState(playersList);
//        }
//
//        else {
//            Log.d("dom test","Deque Is Empty");
//            setSaveGameState();
//        }
//        setAverageScoreTextView();
//        setVisitsTextView();
//        adapter.notifyDataSetChanged();
    }
}
