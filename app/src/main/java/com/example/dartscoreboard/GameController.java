package com.example.dartscoreboard;

import android.util.Log;

import java.util.ArrayList;
import java.util.Stack;

public final class GameController {


    public static GameController gameController;

    private ArrayList<User> playersList;
    private SelectGameActivity.GameType gameType;
    private String slotKey;
    private int turnLead;
    private int turnLeadForSets;
    private boolean gameStateEnd;

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

    //todo game logic here

    public void newGameStart() { // todo move earlier
        gameStateStack = new Stack<>();
        GameState existingGame = getGameState();
        if (existingGame == null) {
            // new game
            setPlayerStartingScores();
            setPlayerTurns();
            setPlayerLegs();
            setPlayerSets();
            turnLead = 0;
            turnLeadForSets = 0;

        } else {
            //Existing game : assign fields from existing match
            playersList = existingGame.getPlayerList();
            gameSettings = existingGame.getGameSettings();
            gameType = existingGame.getGameType();
            turnLead = existingGame.getTurnLead();
            turnLeadForSets = existingGame.getTurnLeadForSets();
        }
        gameStateEnd = false;
    }

    private GameState getGameState() {
        return PreferencesController.getInstance().readGameState(slotKey);
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
//        } //todo reenable once undo fixed
        if (scoreInt <= 180) { // checks for valid score input
            for (User player : playersList){
                int currentScore = player.getPlayerScore();
                //For current player - do calculation
                if (player.isTurn()){

                    //Calculate new score
                    player.setPlayerScore(subtract(currentScore,scoreInt));
                    Log.d("dom test", player.username + " previous scorelist after subtract =  " + player.getPreviousScoresList());
                    player.setTurn(false);
                    //If player is not last in the list, set next player turn to true & exit loop
                    User firstPlayer = playersList.get(0);
                    User lastPlayer = playersList.get(playersList.size() - 1);
                    if (player != lastPlayer) { // todo have field of current player index instead of set turn field
                        playersList.get(playersList.indexOf(player) + 1).setTurn(true);
                    }
                    else firstPlayer.setTurn(true);
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

    private int subtract(int playerScore, int currentTypedScore) {
        int newScore = playerScore - currentTypedScore;
        if ((((playerScore <= 180) && (playerScore >= 171)) || (playerScore == 169) || (playerScore == 168) || (playerScore == 166) || (playerScore == 165) || (playerScore == 163) || (playerScore == 162) || (playerScore == 159)) && (currentTypedScore == playerScore)) {
            for (User player : playersList
            ) {
                if (player.turn){
                    player.addToPreviousScoresList(0);
                }
            }
            //Toast.makeText(GameActivity.this, "BUST", Toast.LENGTH_SHORT).show();
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

    public void setPlayersList(ArrayList<User> playersList) {
        this.playersList = playersList;
    }
    public void setSlotKey(String slotKey) {
        this.slotKey = slotKey;
    }

    public void setGameSettings(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }


    public void setGameType(SelectGameActivity.GameType gameType) {
        this.gameType = gameType;
    }

    public void endGame() {
        PreferencesController.getInstance().clearGameState(slotKey);
        gameStateEnd = true;
        GameActivity.gameStateEnd = true;
    }


}
