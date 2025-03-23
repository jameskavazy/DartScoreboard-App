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

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeSource;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class GameViewModel extends AndroidViewModel {

    private String currentGameId;
    private MatchData matchData;

    private GameWithVisits gameWithVisits;
    private Game game;
    private MutableLiveData<MatchData> matchDataMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<GameWithVisits> gameWithVisitsMutableLiveData = new MutableLiveData<>();
    private final GameRepository gameRepository;
//    public static int turnIndex = 0;
//    private int legIndex = 0;
//    private int setIndex = 0;

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    private String matchId;
    private final MutableLiveData<Boolean> _finished = new MutableLiveData<>(false);

    public LiveData<Boolean> finished = _finished;
    private final String BUST = "BUST";
    private final String NO_SCORE = "No score";

    public GameViewModel(@NonNull Application application) {
        super(application);
        gameRepository = new GameRepository(application);
    }

    @SuppressLint("CheckResult")
    public void playerVisit(int scoreInt) {
        if (scoreInt == 0) {
            toastMessage(NO_SCORE);
        }
        if (scoreInt <= 180) {
            User user = matchData.users.get(gameWithVisits.game.turnIndex);
            int userId = user.userID;
            gameRepository.getGameTotalScoreByUser(gameWithVisits.game.getGameId(), userId)
                    .flatMap((Function<Integer, SingleSource<Integer>>) sumOfVisits -> {
                        int startingScore = matchData.match.getMatchType().startingScore;
                        int visitScore = calculateScore(startingScore - sumOfVisits, scoreInt);
                        Visit visit = createVisit(user, visitScore);
                        int finalScore = startingScore - sumOfVisits - visitScore;
                        return gameRepository.insertVisit(visit).andThen(Single.just(finalScore));
                    })
                    .subscribeOn(Schedulers.io())
                    .subscribe(new SingleObserver<Integer>() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Integer userScore) {
                            if (userScore == 0){
                                //TODO chain thse rxjavas so we insert winner and create new game sequentially
                                gameRepository.setWinner(userId, currentGameId);
                                Log.d("jtest", "onSucess - gameId before update = " + currentGameId);
                                Game newGame = createGame();
                                setCurrentGameId(newGame.getGameId());
                                gameRepository.insertGame(newGame);
                                Log.d("jtest", "onSucess - gameId is now = " + currentGameId);
                            }
                            incrementTurnIndex();
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                        }
                    });
        }
//        nextLeg();
    }

    @NonNull
    private Visit createVisit(User user, int finalScore) {
        Visit visit = new Visit();
        visit.setGameId(gameWithVisits.game.getGameId());
        visit.setUserID(user.userID);
        visit.setScore(finalScore);
        return visit;
    }

    private int calculateScore(int playerScore, int input) {
        User currentPlayer = matchData.users.get(gameWithVisits.game.turnIndex);

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

//    private void endGame(User currentPlayer) {
//        _finished.postValue(true);
//        gameRepository.setWinner(currentPlayer.userID, matchData.game.getGameId());
//        toastMessage(currentPlayer.getUsername() + " wins the match!");
//    }
//
    private void toastMessage(String msg) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> Toast.makeText(DartsScoreboardApplication.getContext(),
                msg, Toast.LENGTH_SHORT).show());
    }
    public void undo() {
        _finished.postValue(false);
        gameRepository.setWinner(0, gameWithVisits.game.getGameId());
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
        gameWithVisits.game.turnIndex = (gameWithVisits.game.turnIndex + 1) % matchData.users.size();
        gameRepository.updateTurnIndex(gameWithVisits.game.turnIndex, gameWithVisits.game.getGameId());
    }

    public void decrementTurnIndex() {
        gameWithVisits.game.turnIndex = (gameWithVisits.game.turnIndex - 1 + matchData.users.size()) % matchData.users.size();
        gameRepository.updateTurnIndex(gameWithVisits.game.turnIndex, gameWithVisits.game.getGameId());
    }
//
//    public void incrementLegIndex() {
//        matchData.game.legIndex = (matchData.game.legIndex + 1) % matchData.users.size();
//        matchData.game.turnIndex = matchData.game.legIndex;
//        gameRepository.updateTurnIndex(matchData.game.turnIndex, matchData.game.getGameId());
//        gameRepository.updateLegIndex(matchData.game.legIndex, matchData.game.getGameId());
//    }
//
//    public void decrementLegIndex() {
//        matchData.game.legIndex = (matchData.game.legIndex - 1) % matchData.users.size();
//        matchData.game.turnIndex = matchData.game.legIndex;
//        gameRepository.updateTurnIndex(matchData.game.turnIndex, matchData.game.getGameId());
//        gameRepository.updateLegIndex(matchData.game.legIndex, matchData.game.getGameId());
//    }
//    public void incrementSetIndex() {
//        matchData.game.setIndex = (matchData.game.setIndex + 1) % matchData.users.size();
//        matchData.game.legIndex = matchData.game.setIndex;
//        matchData.game.turnIndex = matchData.game.setIndex;
//        gameRepository.updateTurnIndex(matchData.game.turnIndex, matchData.game.getGameId());
//        gameRepository.updateLegIndex(matchData.game.legIndex, matchData.game.getGameId());
//        gameRepository.updateSetIndex(matchData.game.setIndex, matchData.game.getGameId());
//    }
//
//    public void decrementSetIndex() {
//        matchData.game.setIndex = (matchData.game.setIndex - 1) % matchData.users.size();
//        matchData.game.legIndex = matchData.game.setIndex;
//        matchData.game.turnIndex = matchData.game.setIndex;
//        gameRepository.updateTurnIndex(matchData.game.turnIndex, matchData.game.getGameId());
//        gameRepository.updateLegIndex(matchData.game.legIndex, matchData.game.getGameId());
//        gameRepository.updateSetIndex(matchData.game.setIndex, matchData.game.getGameId());
//    }
//
//    public void setPlayersList(List<User> playersList) {
//        this.matchData.users = playersList;
//    }


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
//    public void fetchGameData(String gameId) {
//        gameRepository.getGameData(gameId)
//                .subscribeOn(Schedulers.io())
//                .subscribe(new FlowableSubscriber<MatchData>() {
//                    @Override
//                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
//                        s.request(Long.MAX_VALUE);
//                    }
//
//                    @Override
//                    public void onNext(MatchData matchData) {
//                        setGameData(matchData);
//                        gameDataLiveData.postValue(matchData);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//
//    }

    public void fetchGameWithVisits(){
        Log.d("flowable test", "fetchGameWithVisits currentgameId " + currentGameId);

        Single<String> gameIdSingle;

        if (currentGameId == null) {
            Game game = createGame();
            gameIdSingle = gameRepository.insertGame(game)
                    .doOnSuccess(s -> currentGameId = s);
        } else {
            gameIdSingle = Single.just(currentGameId);
        }

        gameIdSingle.flatMapPublisher(gameRepository::getGameWithVisits)
                        .subscribeOn(Schedulers.io())
                                .subscribe(new FlowableSubscriber<GameWithVisits>() {
                                    @Override
                                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
                                        s.request(Long.MAX_VALUE);
                                    }

                                    @Override
                                    public void onNext(GameWithVisits gameWithVisits) {
                                        Log.d("flowable test", "onNext flowable gameWithVisits = " + gameWithVisits);
                                        if (gameWithVisits != null) {
                                            setGameWithVisits(gameWithVisits);
                                        }
                                        gameWithVisitsMutableLiveData.postValue(gameWithVisits);
                                    }

                                    @Override
                                    public void onError(Throwable t) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
    }



    public MutableLiveData<GameWithVisits> getGameWithVisitsMutableLiveData(){
        return gameWithVisitsMutableLiveData;
    }

    private void setGameWithVisits(GameWithVisits gameWithVisits){
        this.gameWithVisits = gameWithVisits;
    }
    public Game createGame(){
        String gameId = UUID.randomUUID().toString();
        return new Game(gameId, matchId,0,0,0);
    }

    public void fetchMatchData(){
        gameRepository.getMatchData(matchId)
                .subscribeOn(Schedulers.io())
                .subscribe(new FlowableSubscriber<MatchData>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(MatchData matchData) {
                        setMatchData(matchData);
                        List<Game> games = matchData.games;
                        String gameId = null;
                        if (games == null || games.isEmpty()){
                            Log.d("flowable test", "games is null or empty");
                            //gameId = createGame().getGameId();
                        } else {
                            gameId = games.stream()
                                    .filter(game -> game.winnerId == 0)
                                    .collect(Collectors.toList())
                                    .get(0).gameId; // TODO could there be issues if more than one game with winnerId 0;
                        }
                        setCurrentGameId(gameId);
                        matchDataMutableLiveData.postValue(matchData);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setMatchData(MatchData matchData){
        this.matchData = matchData;
    }

    private void setCurrentGameId(String gameId){
        this.currentGameId = gameId;
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

//    public LiveData<List<Visit>> getVisits(){
//        return gameRepository.getVisitsInGame(matchData.game.getGameId());
//    }
//
//    public LiveData<Boolean> getFinished() {
//        return finished;
//    }

//    public int getCurrentLegsSets(int userId, MatchLegsSets.Type type){
//
//
//    }
//    public void setGameData(MatchData matchData) {
//        this.matchData = matchData;
//    }
//
    public MutableLiveData<MatchData> getMatchDataLiveData() {
        return matchDataMutableLiveData;
    }
//
//    public void setGameDataLiveData(MutableLiveData<MatchData> gameDataLiveData) {
//        this.gameDataLiveData = gameDataLiveData;
//    }
}
