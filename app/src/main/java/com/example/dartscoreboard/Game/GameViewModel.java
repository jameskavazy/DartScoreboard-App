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
import androidx.lifecycle.Transformations;

import com.example.dartscoreboard.Application.DartsScoreboardApplication;
import com.example.dartscoreboard.User.User;
import com.example.dartscoreboard.User.UserRepository;

import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class GameViewModel extends AndroidViewModel {

    private GameData gameData;

    private final GameRepository gameRepository;
//    public static int turnIndex = 0;
//    private int legIndex = 0;
//    private int setIndex = 0;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    private String gameId;

    public MutableLiveData<GameData> getGameDataLiveData() {
        return gameDataLiveData;
    }

    public void setGameDataLiveData(MutableLiveData<GameData> gameDataLiveData) {
        this.gameDataLiveData = gameDataLiveData;
    }

    private MutableLiveData<GameData> gameDataLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> _finished = new MutableLiveData<>(false);

    public LiveData<Boolean> finished = _finished;
    private final String BUST = "BUST";
    private final String NO_SCORE = "No score";


    public GameViewModel(@NonNull Application application) {
        super(application);
        gameRepository = new GameRepository(application);
    }

//    public void updateUser(User user) {
//        userRepository.updateUser(user);
//    }

//    public Completable insert(Game game) {
//        return gameRepository.insert(game);
//    }

//    public void update(Game game) {
//        gameRepository.update(game);
//    }

    @SuppressLint("CheckResult")
    public void playerVisit(int scoreInt) {
        if (scoreInt == 0) {
            toastMessage(NO_SCORE);
        }
        if (scoreInt <= 180) {
            User user = gameData.users.get(gameData.game.turnIndex);
            int userId = user.userID;
//            int startingScore = gameData.game.getGameType().startingScore;
//
//            int sumOfVisits = gameData.visits.stream()
//                    .filter(visit -> userId == visit.userID)
//                    .mapToInt(visit -> visit.score).sum();
//
//            int visitScore = calculateScore(startingScore - sumOfVisits, scoreInt);
//            Visit visit = createVisit(user, visitScore);
//            gameRepository.insertVisit(visit);
//
//            int finalScore = startingScore - sumOfVisits - visitScore;
//            if (finalScore == 0) {
//                gameRepository.insertLegSetWinner(new MatchLegsSets(
//                        gameData.game.getGameId(), userId, MatchLegsSets.Type.Leg));
//            }
//
//            int currentLegs = (int) gameData.legsSets.stream()
//                    .filter(matchLegsSets -> userId == matchLegsSets.userID && matchLegsSets.type == MatchLegsSets.Type.Leg)
//                            .count();
//
//            if (currentLegs == gameData.game.getGameSettings().getTotalLegs()) {
//                endGame(user);
//            } else {
//                incrementTurnIndex();
//            }

            gameRepository.getGameTotalScoreByUser(gameData.game.getGameId(), userId)
                    .flatMapMaybe(sumOfVisits -> {
                        int startingScore = gameData.game.getGameType().startingScore;
                        int visitScore = calculateScore(startingScore - sumOfVisits, scoreInt);
                        Visit visit = createVisit(user, visitScore);
                        int finalScore = startingScore - sumOfVisits - visitScore;
                        return gameRepository.insertVisit(visit)
                                .andThen(Single.just(finalScore))
                                .flatMapMaybe(userScore -> {
                                    if (userScore == 0){
                                        return gameRepository.insertLegSetWinner(new MatchLegsSets(
                                                        gameData.game.getGameId(), userId, MatchLegsSets.Type.Leg))
                                                .andThen(gameRepository.getCurrentLegsSets(userId, MatchLegsSets.Type.Leg, gameData.game.getGameId()));
                                    }
                                    return Maybe.empty();
                                });

                    })
                    .defaultIfEmpty(-1)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new SingleObserver<Integer>() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Integer currentLegs) {
                            Log.d("EndGame", "onSuccessHit current legs: " + currentLegs.toString());
                            if (currentLegs != -1){
                                if (gameData.game.getGameSettings().getTotalLegs() == currentLegs){
                                   endGame(user);
                                } else {

                                }
                            }
                            incrementTurnIndex();
                            Log.d("Insert", "Visit inserted successfully");
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                            Log.e("Error", "Failed to insert visit", e);
                        }
                    });
        }
//        nextLeg();
    }

    @NonNull
    private Visit createVisit(User user, int finalScore) {
        Visit visit = new Visit();
        visit.setGameId(gameData.game.getGameId());
        visit.setUserID(user.userID);
        visit.setScore(finalScore);
        return visit;
    }

    private int calculateScore(int playerScore, int input) {
        User currentPlayer = gameData.users.get(gameData.game.turnIndex);

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
        gameRepository.setWinner(currentPlayer.userID, gameData.game.getGameId());
        toastMessage(currentPlayer.getUsername() + " wins the match!");
    }

    private void toastMessage(String msg) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> Toast.makeText(DartsScoreboardApplication.getContext(),
                msg, Toast.LENGTH_SHORT).show());
    }


    public void undo() {
        _finished.postValue(false);
        gameRepository.setWinner(0, gameData.game.getGameId());
        gameRepository.deleteLatestVisit();
        decrementTurnIndex();
//            decrementLegIndex();
//            decrementSetIndex();
    }



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
        gameData.game.turnIndex = (gameData.game.turnIndex + 1) % gameData.users.size();
        gameRepository.updateTurnIndex(gameData.game.turnIndex, gameData.game.getGameId());
    }

    public void decrementTurnIndex() {
        gameData.game.turnIndex = (gameData.game.turnIndex - 1 + gameData.users.size()) % gameData.users.size();
        gameRepository.updateTurnIndex(gameData.game.turnIndex, gameData.game.getGameId());
    }

    public void incrementLegIndex() {
        gameData.game.legIndex = (gameData.game.legIndex + 1) % gameData.users.size();
        gameData.game.turnIndex = gameData.game.legIndex;
        gameRepository.updateTurnIndex(gameData.game.turnIndex, gameData.game.getGameId());
        gameRepository.updateLegIndex(gameData.game.legIndex, gameData.game.getGameId());
    }

    public void decrementLegIndex() {
        gameData.game.legIndex = (gameData.game.legIndex - 1) % gameData.users.size();
        gameData.game.turnIndex = gameData.game.legIndex;
        gameRepository.updateTurnIndex(gameData.game.turnIndex, gameData.game.getGameId());
        gameRepository.updateLegIndex(gameData.game.legIndex, gameData.game.getGameId());
    }
    public void incrementSetIndex() {
        gameData.game.setIndex = (gameData.game.setIndex + 1) % gameData.users.size();
        gameData.game.legIndex = gameData.game.setIndex;
        gameData.game.turnIndex = gameData.game.setIndex;
        gameRepository.updateTurnIndex(gameData.game.turnIndex, gameData.game.getGameId());
        gameRepository.updateLegIndex(gameData.game.legIndex, gameData.game.getGameId());
        gameRepository.updateSetIndex(gameData.game.setIndex, gameData.game.getGameId());
    }

    public void decrementSetIndex() {
        gameData.game.setIndex = (gameData.game.setIndex - 1) % gameData.users.size();
        gameData.game.legIndex = gameData.game.setIndex;
        gameData.game.turnIndex = gameData.game.setIndex;
        gameRepository.updateTurnIndex(gameData.game.turnIndex, gameData.game.getGameId());
        gameRepository.updateLegIndex(gameData.game.legIndex, gameData.game.getGameId());
        gameRepository.updateSetIndex(gameData.game.setIndex, gameData.game.getGameId());
    }

    public void setPlayersList(List<User> playersList) {
        this.gameData.users = playersList;
    }


//    public void setTurnIndex(int turnIndex) {
//        GameViewModel.turnIndex = turnIndex;
//    }
//
//    public static int getTurnIndex() {
//        return turnIndex;
//    }
//
//    public int getLegIndex() {
//        return legIndex;
//    }
//
//    public void setLegIndex(int legIndex) {
//        this.legIndex = legIndex;
//    }
//
//    public int getSetIndex() {
//        return setIndex;
//    }
//
//    public void setSetIndex(int setIndex) {
//        this.setIndex = setIndex;
//    }

//    public List<User> getPlayersList(){
//        return gameData.users;
//    }
    public void fetchGameData(String gameId) {
        gameRepository.getGameData(gameId)
                .subscribeOn(Schedulers.io())
                .subscribe(new FlowableSubscriber<GameData>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(GameData gameData) {
                        setGameData(gameData);
                        gameDataLiveData.postValue(gameData);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

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



//    public LiveData<Game> getGame(){
//        return gameRepository.getGameStateById(gameId);
//    }

    public LiveData<List<Visit>> getVisits(){
        return gameRepository.getVisitsInGame(gameData.game.getGameId());
    }

    public LiveData<Boolean> getFinished() {
        return finished;
    }

//    public int getCurrentLegsSets(int userId, MatchLegsSets.Type type){
//
//
//    }
    public GameData getGameData() {
        return gameData;
    }

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }
}
