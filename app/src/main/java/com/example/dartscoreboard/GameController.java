package com.example.dartscoreboard;

import android.app.NotificationManager;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.example.dartscoreboard.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public final class GameController {

    public static GameController gameController;
    private long gameID;
    public int turnIndex = 0;
    private int turnIndexLegs = 0;
    private int turnIndexSets = 0;

    private List<User> playersList;

    private SelectGameActivity.GameType gameType;
    public static boolean gameStateEnd;

    private Stack<MatchState> matchStateStack = new Stack<>();

    private GameSettings gameSettings;

    private GameController() {
    }

    public static GameController getInstance() {
        if (gameController == null) {
            gameController = new GameController();
        }
        return gameController;
    }

    public void playerVisit(int scoreInt) throws CloneNotSupportedException {
//        //Save current gameState for undo
        saveForUndo();
        if (scoreInt <= 180) {
            // checks for valid score input
            User currentPlayer = playersList.get(turnIndex);
            int currentScore = currentPlayer.getPlayerScore();
            setIsCheckoutFlag(currentPlayer, currentScore);
            currentPlayer.setPlayerScore(subtract(currentScore, scoreInt));
            incrementTurnIndex();
        }
        nextLeg();
    }

    private void setIsCheckoutFlag(User currentPlayer, int currentScore) {
//       todo make this code below popup dialogue that asks how many attempts on double
        currentPlayer.setCheckout((currentScore <= 170) &&
                ((currentScore != 169) && (currentScore != 168) &&
                        (currentScore != 166) && (currentScore != 165) &&
                        (currentScore != 163) && (currentScore != 162) &&
                        (currentScore != 159)));
//        currentPlayer.setCheckout((currentScore == 50 || currentScore <= 40) && currentScore % 2 == 0);
    }


    private int subtract(int playerScore, int currentTypedScore) {
        User currentPlayer = playersList.get(turnIndex); //todo could be a switch below?
        if (currentPlayer.isGuy) {
            Log.d("dom test", "subtract guy" + currentTypedScore);

            if (playerScore > 100
                    && currentTypedScore > 10
                    && currentTypedScore % 5 != 0
                    && playerScore % 5 != 0
                    && playerScore != 501
                    && playerScore != 301)

                currentTypedScore = currentTypedScore - 3;
        }
        int newScore = playerScore - currentTypedScore;

        if ((((playerScore <= 180) && (playerScore >= 171)) ||
                (playerScore == 169) || (playerScore == 168) ||
                (playerScore == 166) || (playerScore == 165) ||
                (playerScore == 163) || (playerScore == 162) ||
                (playerScore == 159))
                && (currentTypedScore == playerScore)) {
            currentPlayer.addToPreviousScoresList(0);
            Toast.makeText(DartsScoreboardApplication.getContext(), "BUST", Toast.LENGTH_SHORT).show();
            return playerScore;
        }
        if (currentTypedScore > 180) {
            return playerScore;
        }

        if (currentPlayer.isCheckout()){
            if (currentTypedScore == playerScore){
                currentPlayer.incrementCheckoutMade();
            } else currentPlayer.incrementCheckoutMissed();
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
            Toast.makeText(DartsScoreboardApplication.getContext(), "BUST", Toast.LENGTH_SHORT).show();
            return playerScore;
        }
    }

    public void dartsThrownCO(){
        User currentPlayer = getPlayersList().get(turnIndex);
        if (currentPlayer.isCheckout()){
            //todo alert dialog?
        }
    }


    public void saveForUndo() throws CloneNotSupportedException {
        //Make a "deep copy" todo go back to serialization?
        ArrayList<User> playerListCopy =  new ArrayList<>();
        for (User user : getPlayersList()){
                playerListCopy.add((User) user.clone());
        }
        MatchState matchState = new MatchState(playerListCopy, getTurnIndex(), getTurnIndexLegs(), getTurnIndexSets());
        //saves matchStateStack within the controller to pass it to the db.
        getMatchStateStack().push(matchState);
    }


    public void undo(RecyclerAdapterGamePlayers adapter) throws CloneNotSupportedException {
        if (!getMatchStateStack().isEmpty()){
            MatchState matchState = getMatchStateStack().pop();
            List<User> previousUserList = matchState.getPlayerList();
            ArrayList<Integer> previousUserPreviousScoresList;
            previousUserPreviousScoresList = previousUserList.get(matchState.getTurnIndex()).getPreviousScoresList();
            if (!previousUserPreviousScoresList.isEmpty()) {
                //// TODO: 28/01/2024 Why does this need to happen. Having to manually remove last visit. Deep copy doesn't work
                previousUserPreviousScoresList.remove(previousUserPreviousScoresList.size() - 1);
            }

            setPlayersList(previousUserList);
            setTurnIndex(matchState.getTurnIndex());
            setTurnIndexLegs(matchState.getTurnIndexForLegs());
            setTurnIndexSets(matchState.getTurnIndexForSets());
            adapter.setUsersList(previousUserList);
            adapter.notifyDataSetChanged();
        }
    }

    public void setPlayerLegs() {
        for (int i = 0; i < playersList.size(); i++) {
            playersList.get(i).setCurrentLegs(0);
        }
    }

    public void setPlayerSets() {
        for (int i = 0; i < playersList.size(); i++) {
            playersList.get(i).setCurrentSets(0);
        }
    }


    public void nextLeg() {
        for (User player : playersList) {
            if (player.getPlayerScore() == 0) {
                player.setCurrentLegs(player.getCurrentLegs() + 1);
                incrementTurnIndexLegs();
                Log.d("dom test", player.getUsername() + " current legs = " + player.getCurrentLegs());
                nextSet();
                matchWonChecker();
                if (!gameStateEnd) {
                    setPlayerStartingScores();
                }
            }
        }
    }

    public void nextSet() {
        for (User player : playersList
        ) {
            if (player.getPlayerScore() == 0 && player.getCurrentLegs() == gameSettings.getTotalLegs()) {
                player.setCurrentSets(player.getCurrentSets() + 1);
                setPlayerLegs();
                incrementTurnIndexSets();
            }
        }
    }

    public void matchWonChecker() {
        for (User player : playersList
        ) {
            if (player.getPlayerScore() == 0 && player.getCurrentSets() == gameSettings.getTotalSets()) {
                setTurnIndex(playersList.indexOf(player));
                Toast.makeText(DartsScoreboardApplication.getContext(), player.getUsername() + " wins the match!", Toast.LENGTH_LONG).show();
                endGame();
            }
        }
    }

    public void setPlayerStartingScores() {
        for (User user : playersList) {
            user.setPlayerScore(gameType.startingScore);
        }
    }

    public void incrementTurnIndex() {
        turnIndex = (turnIndex + 1) % playersList.size();
    }

    public void incrementTurnIndexLegs() {
        turnIndexLegs = (turnIndexLegs + 1) % playersList.size();
        turnIndex = turnIndexLegs;
    }

    public void incrementTurnIndexSets() {
        turnIndexSets = (turnIndexSets + 1) % playersList.size();
        turnIndexLegs = turnIndexSets;
        turnIndex = turnIndexSets;
    }

    public void setPlayersList(List<User> playersList) {
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

    public int getTurnIndexLegs() {
        return turnIndexLegs;
    }

    public void setTurnIndexLegs(int turnIndexLegs) {
        this.turnIndexLegs = turnIndexLegs;
    }

    public int getTurnIndexSets() {
        return turnIndexSets;
    }

    public void setTurnIndexSets(int turnIndexSets) {
        this.turnIndexSets = turnIndexSets;
    }

    public List<User> getPlayersList() {
        return playersList;
    }

    public void clearTurnIndices() {
        setTurnIndex(0);
        setTurnIndexSets(0);
        setTurnIndexSets(0);
    }

    public long getGameID() {
        return gameID;
    }

    public void setGameID(long gameID) {
        this.gameID = gameID;
    }

    public void endGame() {
        //Clear down controller at end of game.
        gameStateEnd = true;
        for (User user : getPlayersList()){
            if  (getPlayersList().size() > 1) {
                if (user.getPlayerScore() == 0) {
                    user.incrementWins();
                } else user.incrementLosses();
            }
        }
        clearTurnIndices();
        gameSettings.clear();
    }


    public void initialiseGameController(
            SelectGameActivity.GameType gameType, GameSettings gameSettings, List<User> playersList, int turnIndex,
            int turnLeadForLegs, int turnLeadForSets, Stack<MatchState> matchStateStack, long gameID) {
        //boolean flag to tell controller whether we need to set starting scores or not.
        gameStateEnd = false;
        setPlayersList(playersList);
        setGameType(gameType);
        setGameSettings(gameSettings);
        setTurnIndex(turnIndex);
        setTurnIndexLegs(turnLeadForLegs);
        setTurnIndexSets(turnLeadForSets);
        setMatchStateStack(matchStateStack);
        if (gameID == 0) {
            setPlayerStartingScores();
        }
        setGameID(gameID);
    }


    public Stack<MatchState> getMatchStateStack() {
        if (matchStateStack == null) {
            matchStateStack = new Stack<>();
        }
        return matchStateStack;
    }

    public void setMatchStateStack(Stack<MatchState> matchStateStack) {
        this.matchStateStack = matchStateStack;
    }

    public double getPlayerAverage() {
        User activePlayer = getPlayersList().get(getTurnIndex());
        int totalScores = activePlayer.getTotalScores();
        double average = (double) totalScores / activePlayer.getVisits();
        return Math.round(average * 10.0) / 10.0;
    }

    public boolean bananaSplit(){
        return getPlayersList().get(getTurnIndex()).isGuy
                && getPlayersList().get(getTurnIndex()).getVisits() % 7 == 0;
    }
}
