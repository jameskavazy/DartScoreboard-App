package com.example.dartscoreboard;

import android.util.Log;
import android.widget.Toast;

import com.example.dartscoreboard.models.GuyUser;
import com.example.dartscoreboard.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Stack;

public final class GameController {

    private int gameID;
    public static GameController gameController;
    public int turnIndex = 0;
    private int turnIndexLegs = 0;
    private int turnIndexSets = 0;

    private ArrayList<User> playersList;

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

    public void playerVisit(int scoreInt) {
//        //Save current gameState for undo
        saveForUndo();

        if (scoreInt <= 180) { // checks for valid score input

            User currentPlayer = playersList.get(turnIndex);
            int currentScore = currentPlayer.getPlayerScore();

            if (currentPlayer instanceof GuyUser) {
                GuyUser guy = (GuyUser) currentPlayer;
                guy.setPlayerScore(subtract(currentScore, scoreInt));
                currentPlayer.setPlayerScore(guy.getPlayerScore());
            } else {
                currentPlayer.setPlayerScore(subtract(currentScore, scoreInt));
            }

            incrementTurnIndex();
        }
        nextLeg();
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
            Toast.makeText(DartsScoreboardApplication.getContext(), "BUST", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(DartsScoreboardApplication.getContext(), "BUST", Toast.LENGTH_SHORT).show();
            return playerScore;
        }
    }

    public void saveForUndo() {
        //Make a "deep copy"
        String playerListCopyJsonString = new Gson().toJson(playersList);
        ArrayList<User> playerListCopy = new Gson().fromJson(playerListCopyJsonString, new TypeToken<ArrayList<User>>() {
        }.getType());
        MatchState matchState = new MatchState(playerListCopy, getTurnIndex(), getTurnIndexLegs(), getTurnIndexSets());
        //saves matchStateStack within the controller to pass it to the db.
        getMatchStateStack().push(matchState);
    }


    public void undo(RecyclerAdapterGamePlayers adapter) {
        MatchState matchState = getMatchStateStack().pop();
        setPlayersList(matchState.getPlayerList());
        setTurnIndex(matchState.getTurnIndex());
        setTurnIndexLegs(matchState.getTurnIndexForLegs());
        setTurnIndexSets(matchState.getTurnIndexForSets());
        adapter.setUsersList(matchState.getPlayerList());
        adapter.notifyDataSetChanged();
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

    public ArrayList<User> getPlayersList() {
        return playersList;
    }

    public void clearTurnIndices() {
        setTurnIndex(0);
        setTurnIndexSets(0);
        setTurnIndexSets(0);
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
        gameSettings.clear();
    }


    public void initialiseGameController(
            SelectGameActivity.GameType gameType, GameSettings gameSettings, ArrayList<User> playersList, int turnIndex,
            int turnLeadForLegs, int turnLeadForSets, Stack<MatchState> matchStateStack) {
        gameStateEnd = false;
        setPlayersList(playersList);
        setGameType(gameType);
        setGameSettings(gameSettings);
        setTurnIndex(turnIndex);
        setTurnIndexLegs(turnLeadForLegs);
        setTurnIndexSets(turnLeadForSets);
        setMatchStateStack(matchStateStack);
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

}
