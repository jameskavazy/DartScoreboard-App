//package com.example.dartscoreboard;
//
//import android.app.Application;
//import android.util.Log;
//
//import androidx.lifecycle.LiveData;
//import java.util.ArrayList;
//import java.util.List;
//
//public class GameRepository {
//
//    private int turnIndex;
//    private int turnLeadForLegs;
//    private int turnLeadForSets;
//
//    private LiveData<List<User>> playersList;
//
//
//    private SelectGameActivity.GameType gameType;
//
//    private GameSettings gameSettings;
//    private boolean gameStateEnd;
//
//    public GameRepository(Application application){
//        //todo get active players from the userDb
//        playersList = PreferencesController.readUsersForGameSP(application);
//    }
//
//    public ArrayList<User> getPlayersList() {
//        return playersList;
//    }
//
//    public void setPlayersList(ArrayList<User> playersList) {
//        this.playersList = playersList;
//    }
//
//    public void playerVisit(int scoreInt) {
////        //Save current gameState for undo
////        GameState previousGameState = new GameState(gameType,gameSettings,playersList,turnLead,turnLeadForSets);
////        saveForUndo(previousGameState);
////        Log.d("dom test", "playerVisit saveForUndo=  " + previousGameState);
////
////        for (User user:previousGameState.getPlayerList()
////        ) {
////            Log.d("dom test", "playerVisit saveForUndo= " + user.username + " score is " + user.playerScore);
////        } //todo re-enable once undo fixed
//        if (scoreInt <= 180) { // checks for valid score input
//            User currentPlayer = playersList.get(turnIndex);
//            int currentScore = currentPlayer.getPlayerScore();
//            currentPlayer.setPlayerScore(subtract(currentScore,scoreInt));
//            Log.d("dom test", "playerVisit - afterSubtract : " + currentPlayer.playerScore);
//            incrementTurnIndex();
//        }
//        nextLeg();
//
//        //Ensures game state is not saved if the game has finished.
//        if (!gameStateEnd) {
//            saveGameState();
//        }
//    }
//
//
//    public int subtract(int playerScore, int currentTypedScore) {
//        int newScore = playerScore - currentTypedScore;
//        User currentPlayer = playersList.get(turnIndex); //todo could be a switch below?
//
//        if ((((playerScore <= 180) && (playerScore >= 171)) ||
//                (playerScore == 169) || (playerScore == 168) ||
//                (playerScore == 166) || (playerScore == 165) ||
//                (playerScore == 163) || (playerScore == 162) ||
//                (playerScore == 159))
//                && (currentTypedScore == playerScore)) {
//            currentPlayer.addToPreviousScoresList(0);
//            //Toast.makeText(GameActivity.this, "BUST", Toast.LENGTH_SHORT).show();
//            return playerScore;
//        }
//        if (currentTypedScore > 180) {
//            return playerScore;
//        }
//        if (newScore > 1) {
//            currentPlayer.addToPreviousScoresList(currentTypedScore);
//            return newScore;
//        }
//        if (newScore == 0) {
//            currentPlayer.addToPreviousScoresList(currentTypedScore);
//            return newScore;
//        } else {
//            currentPlayer.addToPreviousScoresList(0);
//            //Toast.makeText(GameActivity.this, "BUST", Toast.LENGTH_SHORT).show();
//            return playerScore;
//        }
//    }
//
//    public void setPlayerLegs(){
//        for (int i = 0; i < playersList.size(); i++) {
//            playersList.get(i).setCurrentLegs(0);
//        }
//    }
//
//    public void setPlayerSets(){
//        for (int i = 0; i < playersList.size(); i++) {
//            playersList.get(i).setCurrentSets(0);
//        }
//    }
//
//
//    public void nextLeg(){
//        for (User player : playersList){
//            if (player.getPlayerScore() == 0) {
//                player.currentLegs++;
//                incrementTurnForLegs();
//                Log.d("dom test",player.username + " current legs = " + player.currentLegs);
//                nextSet();
//                matchWonChecker();
//                if (!gameStateEnd) {
//                    setPlayerStartingScores();
//                }
//            }
//        }
//    }
//
//    public void nextSet(){
//        for (User player:playersList
//        ) {
//            if (player.getPlayerScore() == 0 && player.getCurrentLegs() == gameSettings.getTotalLegs()) {
//                player.currentSets++;
//                setPlayerLegs();
//                incrementTurnForSets();
//            }
//        }
//    }
//
//    public void matchWonChecker(){
//        for (User player : playersList
//        ) {
//            if (player.getPlayerScore() == 0 && player.currentSets == gameSettings.getTotalSets()){
//                //Toast.makeText(GameActivity.this, player.getUsername() + " wins the match!", Toast.LENGTH_LONG).show();
//                endGame();
//            }
//        }
//    }
//    public void setPlayerStartingScores() {
//        for (User user : playersList){
//            user.setPlayerScore(gameType.startingScore);
//        }
//    }
//
//    public void incrementTurnIndex(){
//        turnIndex = (turnIndex + 1) % playersList.size();
//    }
//
//    public void incrementTurnForLegs(){
//        turnLeadForLegs = (turnLeadForLegs + 1) % playersList.size();
//        turnIndex = turnLeadForLegs;
//    }
//    public void incrementTurnForSets(){
//        turnLeadForSets = (turnLeadForSets + 1) % playersList.size();
//        turnLeadForLegs = turnLeadForSets;
//        turnIndex = turnLeadForSets;
//    }
//
//
//
//
//
//
//
//}
