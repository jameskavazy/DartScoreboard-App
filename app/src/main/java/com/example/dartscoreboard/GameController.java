package com.example.dartscoreboard;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public final class GameController {

    private int gameID;
    public static GameController gameController;
    public int turnIndex = 0;
    private int turnLeadForLegs = 0;
    private int turnLeadForSets = 0;

    private ArrayList<User> playersList;

    private SelectGameActivity.GameType gameType;
    public static boolean gameStateEnd;
    private Stack<GameState> gameStateStack;
    private GameSettings gameSettings;
    private GameController(){
    }
    public static GameController getInstance() {
        if (gameController == null){
            gameController = new GameController();
        }
        return gameController;
    }

    public void playerVisit(int scoreInt) {
//        //Save current gameState for undo
//        GameState previousGameState = new GameState(gameType,gameSettings,playersList,turnLead,turnLeadForSets);
//        saveForUndo(previousGameState);
//        Log.d("dom test", "playerVisit saveForUndo=  " + previousGameState);
//
//        for (User user:previousGameState.getPlayerList()
//        ) {
//            Log.d("dom test", "playerVisit saveForUndo= " + user.username + " score is " + user.playerScore);
//        } //todo re-enable once undo fixed
        if (scoreInt <= 180) { // checks for valid score input
            User currentPlayer = playersList.get(turnIndex);
            int currentScore = currentPlayer.getPlayerScore();
            currentPlayer.setPlayerScore(subtract(currentScore,scoreInt));
            Log.d("dom test", "playerVisit - afterSubtract : " + currentPlayer.playerScore);
            incrementTurnIndex();
        }
        nextLeg();

        //Ensures game state is not saved if the game has finished.
        if (!gameStateEnd) {
            saveGameState();
        }
    }

    public void saveGameState() {
////        if (gameID != -1){
//            GameState gameState = new GameState(gameType,gameSettings,playersList,turnIndex,turnLeadForLegs,turnLeadForSets);

//            matchHistoryViewModel = new ViewModelProvider.NewInstanceFactory().getClass()

////        }

    }




    private int subtract(int playerScore, int currentTypedScore) {
        int newScore = playerScore - currentTypedScore;
        User currentPlayer = playersList.get(turnIndex); //todo could be a switch below?

        if ((((playerScore <= 180) && (playerScore >= 171)) ||
                (playerScore == 169) || (playerScore == 168) ||
                (playerScore == 166) || (playerScore == 165) ||
                (playerScore == 163) || (playerScore == 162) ||
                (playerScore == 159))
                && (currentTypedScore == playerScore)) {
            currentPlayer.addToPreviousScoresList(0);
            //Toast.makeText(GameActivity.this, "BUST", Toast.LENGTH_SHORT).show();
            return playerScore;
        }
        if (currentTypedScore > 180) {
            return playerScore;
        }
        if (newScore > 1) {
            currentPlayer.addToPreviousScoresList(currentTypedScore);
            return newScore;
        }
        if (newScore == 0) {
            currentPlayer.addToPreviousScoresList(currentTypedScore);
            return newScore;
        } else {
          currentPlayer.addToPreviousScoresList(0);
          //Toast.makeText(GameActivity.this, "BUST", Toast.LENGTH_SHORT).show();
            return playerScore;
        }
    }

    public void saveForUndo(GameState previousGameState){
        gameStateStack.push(previousGameState);
    }



    public void undo() { // todo bring back the HashMap/map this worked well.

        //Brings back text input if game was finished.
//        if (gameStateEnd) {
//            inputScoreEditText.setVisibility(View.VISIBLE);
//            doneButton.setVisibility(View.VISIBLE);
//            gameStateEnd = false;
//        }
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
                incrementTurnForLegs();
                Log.d("dom test",player.username + " current legs = " + player.currentLegs);
                nextSet();
                matchWonChecker();
                if (!gameStateEnd) {
                    setPlayerStartingScores();
                }
            }
        }
    }

    public void nextSet(){
        for (User player:playersList
        ) {
            if (player.getPlayerScore() == 0 && player.getCurrentLegs() == gameSettings.getTotalLegs()) {
                player.currentSets++;
                setPlayerLegs();
                incrementTurnForSets();
            }
        }
    }

    public void matchWonChecker(){
        for (User player : playersList
        ) {
            if (player.getPlayerScore() == 0 && player.currentSets == gameSettings.getTotalSets()){
                //Toast.makeText(GameActivity.this, player.getUsername() + " wins the match!", Toast.LENGTH_LONG).show();
                endGame();
            }
        }
    }

    public void setPlayerStartingScores() {
        for (User user : playersList){
            user.setPlayerScore(gameType.startingScore);
        }
    }

    public void incrementTurnIndex(){
        turnIndex = (turnIndex + 1) % playersList.size();
    }
    public void incrementTurnForLegs(){
        turnLeadForLegs = (turnLeadForLegs + 1) % playersList.size();
        turnIndex = turnLeadForLegs;
    }

    public void incrementTurnForSets(){
        turnLeadForSets = (turnLeadForSets + 1) % playersList.size();
        turnLeadForLegs = turnLeadForSets;
        turnIndex = turnLeadForSets;
    }

    public void setPlayersList(ArrayList<User> playersList) {
        this.playersList = playersList;
    }

    public void setGameSettings(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }
    public void setGameType(SelectGameActivity.GameType gameType) {
        this.gameType = gameType;
    }

    public SelectGameActivity.GameType getGameType() {
        return gameType;
    }

    public void setTurnIndex(int turnIndex) {
        this.turnIndex = turnIndex;
    }

    public int getTurnIndex() {
        return turnIndex;
    }

    public int getTurnLeadForLegs() {
        return turnLeadForLegs;
    }
    public void setTurnLeadForLegs(int turnLeadForLegs) {
        this.turnLeadForLegs = turnLeadForLegs;
    }

    public int getTurnLeadForSets() {
        return turnLeadForSets;
    }
    public void setTurnLeadForSets(int turnLeadForSets) {
        this.turnLeadForSets = turnLeadForSets;
    }
    public ArrayList<User> getPlayersList() {
        return playersList;
    }

    public void clearTurnIndices(){
        setTurnIndex(0);
        setTurnLeadForSets(0);
        setTurnLeadForSets(0);
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public void endGame() {
        //Clear down controller at end of game.
        gameStateEnd = true;
        clearTurnIndices();
       //todo run playersList.clear() inside overridden onDestroy;
        gameSettings.clear();
    }


    public void initialiseGameController(
            SelectGameActivity.GameType gameType, GameSettings gameSettings, ArrayList<User> playersList, int turnIndex,
            int turnLeadForLegs, int turnLeadForSets) {
        gameStateEnd = false;
        setPlayersList(playersList);
        setGameType(gameType);
        setGameSettings(gameSettings);
        setTurnIndex(turnIndex);
        setTurnLeadForLegs(turnLeadForLegs);
        setTurnLeadForSets(turnLeadForSets);
    }

}
