package com.example.dartscoreboard.Game;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.dartscoreboard.Application.DartsScoreboardApplication;
import com.example.dartscoreboard.User.User;
import com.example.dartscoreboard.User.UserRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class GameViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private String gameId;
    public static int turnIndex = 0;
    private int legIndex = 0;
    private int setIndex = 0;
    private List<User> playersList = new ArrayList<>();
    private GameType gameType;

    private final MutableLiveData<Boolean> _finished = new MutableLiveData<>(false);

    public LiveData<Boolean> finished = _finished;
    private final String BUST = "BUST";
    private final String NO_SCORE = "No score";

    private Game game;

    private GameSettings gameSettings;

    public GameViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        gameRepository = new GameRepository(application);
    }

    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    public Completable insert(Game game) {
        return gameRepository.insert(game);
    }

    public void update(Game game) {
        gameRepository.update(game);
    }

    public void deleteGameStateByID(String id) {
        gameRepository.deleteGameStateByID(id);
    }


    @SuppressLint("CheckResult")
    public void playerVisit(int scoreInt) {
        if (scoreInt == 0) {
            toastMessage(NO_SCORE);
        }
        if (scoreInt <= 180) {
            // checks for valid score input
            User user = playersList.get(turnIndex);
            int userId = user.userID;

            gameRepository.getGameTotalScoreByUser(gameId, userId)
                    .flatMapCompletable(sumOfVisits -> {
                        int finalScore = calculateScore(gameType.startingScore - sumOfVisits, scoreInt);
                        Visit visit = createVisit(user, finalScore);
                        return gameRepository.insertVisit(visit);
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.single())
                    .subscribe(
                            () -> {
                                incrementTurnIndex();
                                Log.d("Insert", "Visit inserted successfully");
                            },
                            throwable -> Log.e("Error", "Failed to insert visit", throwable)
                    );
        }
//        nextLeg();
    }

    @NonNull
    private Visit createVisit(User user, int finalScore) {
        Visit visit = new Visit();
        visit.setGameId(gameId);
        visit.setUserID(user.userID);
        visit.setScore(finalScore);
        return visit;
    }

    private int calculateScore(int playerScore, int input) {
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
            toastMessage(BUST);
            return 0;
        }


        if (newScore == 0) {
            if (playerScore >= 171) {
                toastMessage(BUST);
                return 0;
            }

            switch (playerScore) {
                case 169:
                case 168:
                case 166:
                case 165:
                case 163:
                case 162:
                case 159:
                    toastMessage(BUST);
                    return 0;
            }
            endGame(currentPlayer);
            return input;
        }

        if (newScore > 1) {
            return input;
        } else {
            toastMessage(BUST);
            return 0;
        }
    }

    private void endGame(User currentPlayer) {
        _finished.postValue(true);
        gameRepository.setWinner(currentPlayer.userID, gameId);
        toastMessage(currentPlayer.getUsername() + " wins the match!");
    }

    private void toastMessage(String msg) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> Toast.makeText(DartsScoreboardApplication.getContext(),
                msg, Toast.LENGTH_SHORT).show());
    }


    public void undo() {
        _finished.postValue(false);
        gameRepository.setWinner(0, gameId);
        gameRepository.deleteLatestVisit();
        decrementTurnIndex();
//            decrementLegIndex();
//            decrementSetIndex();
    }

//    public void setPlayerLegs() {
//        for (int i = 0; i < playersList.size(); i++) {
//            playersList.get(i).setCurrentLegs(0);
//        }
//    }
//
//    public void setPlayerSets() {
//        for (int i = 0; i < playersList.size(); i++) {
//            playersList.get(i).setCurrentSets(0);
//        }
//    }


//    public void nextLeg() {
//        for (User player : playersList) {
//            if (player.getPlayerScore() == 0) {
//                player.setCurrentLegs(player.getCurrentLegs() + 1);
//                incrementTurnIndexLegs();
//                Log.d("dom test", player.getUsername() + " current legs = " + player.getCurrentLegs());
//                nextSet();
//                matchWonChecker();
//                if (!finished) {
//                    setPlayerStartingScores();
//                }
//            }
//        }
//    }

//    public void nextSet() {
//        for (User player : playersList
//        ) {
//            if (player.getPlayerScore() == 0 && player.getCurrentLegs() == gameSettings.getTotalLegs()) {
//                player.setCurrentSets(player.getCurrentSets() + 1);
//                setPlayerLegs();
//                incrementTurnIndexSets();
//            }
//        }
//    }

//    public void matchWonChecker() {
//        for (User player : playersList
//        ) {
//            if (player.getPlayerScore() == 0 && player.getCurrentSets() == gameSettings.getTotalSets()) {
//                setTurnIndex(playersList.indexOf(player));
//                Toast.makeText(DartsScoreboardApplication.getContext(), player.getUsername() + " wins the match!", Toast.LENGTH_LONG).show();
//                endGame();
//            }
//        }
//    }

//    public void setPlayerStartingScores() {
//        for (User user : playersList) {
//            user.setPlayerScore(gameType.startingScore);
//        }
//    }

    public void incrementTurnIndex() {
        turnIndex = (turnIndex + 1) % playersList.size();
        gameRepository.updateTurnIndex(turnIndex, gameId);
    }

    public void decrementTurnIndex() {
        turnIndex = (turnIndex - 1 + playersList.size()) % playersList.size();
        gameRepository.updateTurnIndex(turnIndex, gameId);
    }

    public void incrementLegIndex() {
        legIndex = (legIndex + 1) % playersList.size();
        turnIndex = legIndex;
        gameRepository.updateTurnIndex(turnIndex, gameId);
        gameRepository.updateLegIndex(legIndex, gameId);
    }

    public void decrementLegIndex() {
        legIndex = (legIndex - 1) % playersList.size();
        turnIndex = legIndex;
        gameRepository.updateTurnIndex(turnIndex, gameId);
        gameRepository.updateLegIndex(legIndex, gameId);
    }
    public void incrementSetIndex() {
        setIndex = (setIndex + 1) % playersList.size();
        legIndex = setIndex;
        turnIndex = setIndex;
        gameRepository.updateTurnIndex(turnIndex, gameId);
        gameRepository.updateLegIndex(legIndex, gameId);
        gameRepository.updateSetIndex(setIndex, gameId);
    }

    public void decrementSetIndex() {
        setIndex = (setIndex - 1) % playersList.size();
        legIndex = setIndex;
        turnIndex = setIndex;
        gameRepository.updateTurnIndex(turnIndex, gameId);
        gameRepository.updateLegIndex(legIndex, gameId);
        gameRepository.updateSetIndex(setIndex, gameId);
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

    public int getLegIndex() {
        return legIndex;
    }

    public void setLegIndex(int legIndex) {
        this.legIndex = legIndex;
    }

    public int getSetIndex() {
        return setIndex;
    }

    public void setSetIndex(int setIndex) {
        this.setIndex = setIndex;
    }

    public List<User> getPlayersList(){
        return playersList;
    }
    public LiveData<GameWithUsers> fetchGameWithUsers(String gameId) {
        return userRepository.getUserFromGame(gameId);
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }


//    public double getPlayerAverage() {
//        User activePlayer = getPlayersList().get(getTurnIndex());
//        int totalScores = activePlayer.getTotalScores();
//        int activePlayerVisits = activePlayer.getVisits();
//
//        if (activePlayerVisits == 0) {
//            activePlayerVisits++;
//        }
//
//        double average = (double) totalScores / activePlayerVisits;
//        return Math.round(average * 10.0) / 10.0;
//    }

//    public boolean bananaSplit() {
//        return getPlayersList().get(getTurnIndex()).isGuy
//                && getPlayersList().get(getTurnIndex()).getVisits() % 7 == 0;
//    }



    public void setGame(Game game) {
        this.game = game;
    }

    public LiveData<Game> getGame(){
        return gameRepository.getGameStateById(gameId);
    }

    public LiveData<List<Visit>> getVisits(){
        return gameRepository.getVisitsInGame(gameId);
    }

    public LiveData<Boolean> getFinished() {
        return finished;
    }



}
