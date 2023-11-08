package com.example.dartscoreboard;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView gameTitle;
    private ArrayList<User> playersList;
    private RecyclerView recyclerView;
    private EditText inputScoreEditText;
    private SaveGameState saveGameState;
    private ArrayDeque<SaveGameState> gameStateArrayDeque;
    private TextView averageScoreTextView;
    private Button undoButton;
    private boolean gameStateEnd;
    private SelectGameActivity.GameType gameType;
    private int playerStartingScore;

    private int totalLegs;

    //private int totalSets = 2;
    private RecyclerAdapterGamePlayers adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("dom test", "GameActivityOnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        Log.d("dom test", "gameType\n-------\nname " + getGameType().name + "\nstartingScore " + getGameType().startingScore);
        setupUI();
        onScoreEntered();
    }

    private void setupUI() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT); //todo delete this eventually
        averageScoreTextView = findViewById(R.id.avg_text_view);
        undoButton = findViewById(R.id.undo_button);
        undoButton.setOnClickListener(this);
        recyclerView = findViewById(R.id.player_info_recycler_view);
        gameTitle = findViewById(R.id.gameActivityTitle);
        inputScoreEditText = findViewById(R.id.inputUserNameEditText);
        playersList = PrefConfig.readUsersForGameSP(this);
        gameTitle.setText(getGameType().name);
        averageScoreTextView.setText(String.valueOf(0.0));
        newGameStart();
        setAdapter();
    }
    
    private void newGameStart(){
        saveGameState = new SaveGameState(null,null,null);
        gameStateArrayDeque = new ArrayDeque<>();
        setPlayerStartingScores();
        setPlayerTurns();
        setSaveGameState();
//        setPlayerLegs();
//        setPlayerSets();
//        setTotalLegs();
        //setTotalSets();
        gameStateEnd = false;
    }

    private void setSaveGameState(){
        saveGameState.currentScoresMap = new HashMap<>();
        saveGameState.turnsMap = new HashMap<>();
        saveGameState.previousScoresListMap = new HashMap<>();
        for (User player:
                playersList
             ) {
            saveGameState.currentScoresMap.put(player,player.playerScore);
            saveGameState.turnsMap.put(player,player.turn);
            Log.d("dom test", player.username + "  :  " + String.valueOf(player.isTurn()));
            saveGameState.previousScoresListMap.put(player, player.getPreviousScoresList());
        }
        gameStateArrayDeque.addFirst(new SaveGameState(saveGameState.currentScoresMap,saveGameState.turnsMap,saveGameState.previousScoresListMap));


    }
    
//    private void nextLeg(){
//        for (User player : playersList){
//            if (player.getPlayerScore() == 0) {
//                player.currentLegs++;
//                nextSet();
//                matchWonChecker();
//                if (!gameStateEnd) {
//                    setPlayerStartingScores();
//                }
//                adapter.notifyDataSetChanged();
//            }
//        }
//    }
//
//    private void nextSet(){
//        for (User player:playersList
//             ) {
//            if (player.getPlayerScore() == 0 && player.getCurrentLegs() == totalLegs) {
//                player.currentSets++;
//                setPlayerLegs();
//                adapter.notifyDataSetChanged();
//            }
//        }
//    }
//    private void matchWonChecker(){
//        for (User player : playersList
//             ) {
//            if (player.getPlayerScore() == 0 && player.currentSets == totalSets){
//                adapter.notifyDataSetChanged();
//                Toast.makeText(GameActivity.this, player.getUsername() + " wins the match!", Toast.LENGTH_LONG).show();
//                endGame();
//            }
//        }
//    }

    

    private void setPlayerStartingScores() {
        playerStartingScore = getGameType().startingScore;
        for (User user : playersList){
            user.setPlayerScore(playerStartingScore);
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



    public void playerVisit(String scoreString) {
        //Loop through an save everyone's current position
        setSaveGameState();

        int scoreInt = Integer.parseInt(scoreString);
        if (scoreInt <= 180) { // checks for valid score input

            for (User player : playersList){
                int currentScore = player.getPlayerScore();


                //int currentLegs = player.getCurrentLegs();
                //int currentSets = player.getCurrentSets();


                //For current player - do calculation
                if (player.isTurn()){
                    //save all players scores to list
                    player.addToPreviousScoresList(scoreInt);

                    Log.d("dom test","Previous Scores List: " + Arrays.toString(player.getPreviousScoresList().toArray()));




//                    player.setPreviousLegs(currentLegs);
//                    player.setPreviousSets(currentSets);

                    //Calculate new score
                    player.setPlayerScore(subtract(currentScore,scoreInt));

                    Log.d("dom test", player.getUsername() + " Average Score: " + player.getAvg());

                   //Log.d("dom test", "Previous Scores List: " + Arrays.toString(player.getScoresList().toArray()));

                    player.setTurn(false);
                    Log.d("dom test", player.username + "  isTurn is now :  " + String.valueOf(player.isTurn()));
                    adapter.notifyItemChanged(playersList.indexOf(player));

                    Log.d("dom test", player.getUsername() + " " + scoreString);
                    Log.d("dom test", player.getUsername() + " " + player.getPlayerScore());

                    //If player is not last in the list, set next player turn to true & exit loop
                    User firstPlayer = playersList.get(0);
                    User lastPlayer = playersList.get(playersList.size() - 1);

                    if (player != lastPlayer) {
                        playersList.get(playersList.indexOf(player) + 1).setTurn(true);
                        adapter.notifyItemChanged(playersList.indexOf(player) + 1);
                        break;
                    }
                    else if (player == lastPlayer) {
                        firstPlayer.setTurn(true);
                        adapter.notifyItemChanged(0);
                        break;
                    }

                }

            }

           // Log.d("dom test", "OnPlayerVisit Stack = " + String.valueOf(gameStateStack));
        }

    }

    public int subtract(int playerScore, int currentTypedScore) {
        int newScore = playerScore - currentTypedScore;
        if ((((playerScore <= 180) && (playerScore >= 171)) || (playerScore == 169) || (playerScore == 168) || (playerScore == 166) || (playerScore == 165) || (playerScore == 163) || (playerScore == 162) || (playerScore == 159)) && (currentTypedScore == playerScore)) {
            Toast.makeText(GameActivity.this, "BUST", Toast.LENGTH_SHORT).show();
            return playerScore;
        }

        if (currentTypedScore > 180) {
            return playerScore;
        }

        if (newScore > 1) {
            return newScore;
        }
        if (newScore == 0) {
            for (User player: playersList
                 ) {
                if (player.turn) {
                    Toast.makeText(GameActivity.this, player.getUsername() + " wins the match!", Toast.LENGTH_LONG).show();
                    endGame();
                    return newScore;
                }
            }

        } else Toast.makeText(GameActivity.this, "BUST", Toast.LENGTH_SHORT).show();
        return playerScore;
    }



    public SelectGameActivity.GameType getGameType() {
        if (gameType != null) {
            return gameType;
        }
        Bundle arguments = getIntent().getExtras();
        gameType = (SelectGameActivity.GameType) arguments.getSerializable(SelectGameActivity.GAME_TYPE_KEY);
        return gameType;
    }

    public void setTotalLegs(){
        Bundle arguments = getIntent().getExtras();
        totalLegs = arguments.getInt(SelectGameActivity.TOTAL_LEGS_KEY);
        Log.d("dom test","Number of legs: " + totalLegs);
    }



    private void setAdapter() {
        adapter = new RecyclerAdapterGamePlayers(playersList, playerStartingScore);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }


    private void setPlayerTurns() {
        playersList.get(0).setTurn(true);
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

    private void onScoreEntered() {
        inputScoreEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String input = inputScoreEditText.getText().toString();
                try {
                    Log.d("dom test", "IME_ACTION_DONE");
                    playerVisit(input);
                    if (Integer.parseInt(input) > 180) {
                        Toast.makeText(GameActivity.this, "Invalid Score", Toast.LENGTH_SHORT).show();
                    }
                    ((EditText) findViewById(R.id.inputUserNameEditText)).getText().clear();
                    setAverageScoreTextView();
                    return true;
                } catch (Exception e) {
                    playerVisit(String.valueOf(0));
                    setAverageScoreTextView();
                    return true;
                }
            }
            return false;
        });
    }


    private void endGame() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(recyclerView.getApplicationWindowToken(), 0);
        inputScoreEditText.setVisibility(View.INVISIBLE);
        gameStateEnd = true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.undo_button) {
            Log.d("dom test", "Undo Click");
            undo();
        }
    }



    private void undo() {

        SaveGameState previousGameState = gameStateArrayDeque.pollFirst();
        if (!gameStateArrayDeque.isEmpty()){
            previousGameState.loadPreviousGameState(playersList);
        }
        else Log.d("dom test","Deque Is Empty");
        adapter.notifyDataSetChanged();

//gotta do something

//        //Brings back text input if game was finished.
//        if (gameStateEnd) {
//            inputScoreEditText.setVisibility(View.VISIBLE);
//        }
//
//        for (User player : playersList){
//
//            User firstPlayer = playersList.get(0);
//            User lastPlayer = playersList.get(playersList.size() - 1);
//
//
//            //Logic for all players apart from first player
//            if (player.turn && player != firstPlayer) {
//                User previousPlayer = playersList.get(playersList.indexOf(player) - 1);
//
//                //Get previous user and undo by resetting to their previous score, legs and sets
//                previousPlayer.setPlayerScore(previousPlayer.getPreviousScore());
////                previousPlayer.setPlayerLegs(previousPlayer.getPreviousLegs());
////                previousPlayer.setPlayerSets(previousPlayer.getPreviousSets());
////
////                player.setPlayerLegs(player.getPreviousLegs());
////                player.setPlayerSets(player.getPreviousSets());
//
//                //Set current player turn to false
//                player.setTurn(false);
//
//                if (player != lastPlayer){
//                    User nextPlayer = playersList.get(playersList.indexOf(player) + 1);
//                    nextPlayer.setTurn(false);
//                }
//                if (player == lastPlayer){
//                    firstPlayer.setTurn(false);
//                }
//                //set previous player turn to true
//                previousPlayer.setTurn(true);
//                adapter.notifyItemChanged(playersList.indexOf(previousPlayer));
//                adapter.notifyItemChanged(playersList.indexOf(player));
//                break;
//            }
//
//
//            //Logic for first player
//            if (player == firstPlayer && firstPlayer.turn && !lastPlayer.previousScoreList.isEmpty()){
//                lastPlayer.setPlayerScore(lastPlayer.getPreviousScore());
////                lastPlayer.setPreviousLegs(lastPlayer.getPreviousLegs());
////                lastPlayer.setPreviousSets(lastPlayer.getPreviousSets());
//                firstPlayer.setTurn(false);
//                lastPlayer.setTurn(true);
//                adapter.notifyItemChanged(playersList.indexOf(firstPlayer));
//                adapter.notifyItemChanged(playersList.indexOf(lastPlayer));
//                break;
//            }
//        }

    }
}
