package com.example.dartscoreboard.Game;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.dartscoreboard.Application.DartsScoreboardApplication;
import com.example.dartscoreboard.MatchHistory.MatchHistoryRepository;
import com.example.dartscoreboard.MatchHistory.MatchState;
import com.example.dartscoreboard.SetupGame.GameType;
import com.example.dartscoreboard.User.User;
import com.example.dartscoreboard.User.UserRepository;
import com.example.dartscoreboard.Utils.PreferencesController;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public class GameViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final MatchHistoryRepository matchHistoryRepository;
    private long gameID;
    public static int turnIndex = 0;
    private int turnIndexLegs = 0;
    private int turnIndexSets = 0;
    private List<User> playersList;
    private GameType gameType;
    public static boolean gameStateEnd;
    private Stack<MatchState> matchStateStack = new Stack<>();

    private GameSettings gameSettings;

    public GameViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        matchHistoryRepository = new MatchHistoryRepository(application);
    }

    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    public Single<Long> insert(GameState gameState) {
        return matchHistoryRepository.insert(gameState);
    }

    public void update(GameState gameState) {
        matchHistoryRepository.update(gameState);
    }

    public void deleteGameStateByID(long id) {
        matchHistoryRepository.deleteGameStateByID(id);
    }


    public void playerVisit(int scoreInt) {
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


    private int subtract(int playerScore, int input) {
        User currentPlayer = playersList.get(turnIndex);

        if (currentPlayer.isGuy) {
            Log.d("dom test", "subtract guy" + input);
            if (playerScore > 100
                    && input > 10
                    && input % 5 != 0
                    && playerScore % 5 != 0
                    && playerScore != 501
                    && playerScore != 301)

                input = input - 3;
        }
        int newScore = playerScore - input;

        if (newScore < 0) {
            //BUST
            currentPlayer.addToPreviousScoresList(0);
            Toast.makeText(DartsScoreboardApplication.getContext(), "BUST", Toast.LENGTH_SHORT).show();
            return playerScore;
        }


        if (newScore == 0) {
            if (playerScore >= 171) {
                currentPlayer.addToPreviousScoresList(0);
                Toast.makeText(DartsScoreboardApplication.getContext(), "BUST", Toast.LENGTH_SHORT).show();
                return playerScore;
            }

            switch (playerScore) {
                case 169:
                case 168:
                case 166:
                case 165:
                case 163:
                case 162:
                case 159:
                    currentPlayer.addToPreviousScoresList(0);
                    Toast.makeText(DartsScoreboardApplication.getContext(), "BUST", Toast.LENGTH_SHORT).show();
                    return playerScore;
            }
            currentPlayer.incrementCheckoutMade();
            currentPlayer.addToPreviousScoresList(input);
            return newScore;
        }

        if (newScore > 1) {
            if (currentPlayer.isCheckout()) currentPlayer.incrementCheckoutMissed();
            currentPlayer.addToPreviousScoresList(input);
            return newScore;
        } else {
            //score is 1
            currentPlayer.addToPreviousScoresList(0);
            Toast.makeText(DartsScoreboardApplication.getContext(), "BUST", Toast.LENGTH_SHORT).show();
            return playerScore;
        }
    }


    public void saveForUndo() {
        PreferencesController.getInstance().copyPlayerList(getPlayersList());
        List<User> playerListCopy = PreferencesController.getInstance().getPlayerListCopy();
        MatchState matchState = new MatchState(playerListCopy, getTurnIndex(), getTurnIndexLegs(), getTurnIndexSets());
        getMatchStateStack().push(matchState);
    }


    public void undo(RecyclerAdapterGamePlayers adapter) {
        if (!getMatchStateStack().isEmpty()) {
            MatchState matchState = getMatchStateStack().pop();
            List<User> previousUserList = matchState.getPlayerList();
            ArrayList<Integer> previousUserPreviousScoresList = previousUserList.get(matchState.getTurnIndex()).getPreviousScoresList();
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

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setTurnIndex(int turnIndex) {
        GameViewModel.turnIndex = turnIndex;
    }

    public static int getTurnIndex() {
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
        for (User user : getPlayersList()) {
            if (getPlayersList().size() > 1) {
                if (user.getPlayerScore() == 0) {
                    user.incrementWins();
                } else user.incrementLosses();
            }
        }
        clearTurnIndices();
        gameSettings.clear();
    }

    public void setGameState(GameState gameState) {
        //todo do we need boolean flag to determine if new starting scores needed??
        setPlayersList(gameState.getPlayerList());
        setGameID(gameState.getGameID());
        setGameType(gameState.getGameType());
        setGameSettings(gameState.getGameSettings());
        setMatchStateStack(gameState.getMatchStateStack());
        setTurnIndex(gameState.getTurnIndex());
        setTurnIndexLegs(gameState.getTurnLeadForLegs());
        setTurnIndexSets(gameState.getTurnLeadForSets());
        if (gameID == 0) {
            setPlayerStartingScores();
            gameStateEnd = false;
        }
    }

    public void updateAllUsers() {
        for (User user : getPlayersList()) {
            updateUser(user);
        }
    }

    public void saveGameStateToDb() {
//      Create GameState object + attach the id for DB update
        GameState gameState = getGameInfo();
        if (getGameID() != 0) {
            gameState.setGameID(getGameID());
            update(gameState);
        } else {
            insert(gameState).subscribe(new SingleObserver<Long>() {
                @Override
                public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                }

                @Override
                public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Long aLong) {
                    Log.d("dom test", "onSuccess " + aLong);
                    setGameID(aLong);
                }

                @Override
                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                }
            });
        }
    }

    public GameState getGameInfo() {
        return new GameState(
                getGameType(),
                getGameSettings(),
                getPlayersList(),
                getTurnIndex(),
                getTurnIndexLegs(),
                getTurnIndexSets(),
                getMatchStateStack());
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
        int activePlayerVisits = activePlayer.getVisits();

        if (activePlayerVisits == 0) {
            activePlayerVisits++;
        }

        double average = (double) totalScores / activePlayerVisits;
        return Math.round(average * 10.0) / 10.0;
    }

    public boolean bananaSplit() {
        return getPlayersList().get(getTurnIndex()).isGuy
                && getPlayersList().get(getTurnIndex()).getVisits() % 7 == 0;
    }


}
