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

import org.reactivestreams.Subscription;

import java.util.List;
import java.util.UUID;

import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeSource;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class GameViewModel extends AndroidViewModel {

    private MatchWithUsers matchWithUsers;

    private GameWithVisits gameWithVisits;

    private MutableLiveData<List<Game>> gamesInMatchLiveData = new MutableLiveData<>();

    private MutableLiveData<MatchWithUsers> matchWithUsersMutableLiveData = new MutableLiveData<>();
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
    private final static String BUST = "BUST";
    private final static String NO_SCORE = "No score";

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
            Log.d("jtest", "playerVisit gameId " + gameWithVisits.game.gameId);
            User user = matchWithUsers.users.get(gameWithVisits.game.turnIndex);
            int userId = user.userID;
            gameRepository.getGameTotalScoreByUser(gameWithVisits.game.getGameId(), userId)
                    .flatMap((Function<Integer, SingleSource<Integer>>) sumOfVisits -> {
                        int startingScore = matchWithUsers.match.getMatchType().startingScore;
                        int visitScore = calculateScore(startingScore - sumOfVisits, scoreInt);
                        Visit visit = createVisit(user, visitScore);
                        int finalScore = startingScore - sumOfVisits - visitScore;
                        return gameRepository.insertVisit(visit).andThen(Single.just(finalScore));
                    })
                    .flatMapMaybe(new Function<Integer, MaybeSource<Integer>>() {
                        @Override
                        public MaybeSource<Integer> apply(Integer userScore) {
                            if (userScore == 0) {
                                return gameRepository.setWinner(userId, gameWithVisits.game.getGameId()).andThen(Maybe.just(1));
                            }
                            return Maybe.empty();
                        }
                    })
                    .defaultIfEmpty(-1)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new SingleObserver<Integer>() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Integer gameWon) {
                            if (gameWon == 1) {
                                Log.d("jtest", "matchWon");
                                Game game = createGame();

                                gameRepository.legsWon(matchId, userId).subscribeOn(Schedulers.io()).subscribe(new SingleObserver<Integer>() {
                                    @Override
                                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Integer count) {
                                        if (count == matchWithUsers.match.matchSettings.getTotalLegs()) {
                                            endGame(user);
                                        }
                                        else {
                                            gameRepository.insertGame(game).subscribeOn(Schedulers.io()).subscribe();
                                        }
                                    }

                                    @Override
                                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                                    }
                                });
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
        User currentPlayer = matchWithUsers.users.get(gameWithVisits.game.turnIndex);

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
        gameRepository.setMatchWinner(currentPlayer.userID, matchWithUsers.match.matchId).subscribeOn(Schedulers.io()).subscribe();
        toastMessage(currentPlayer.getUsername() + " wins the match!");
    }

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
        gameWithVisits.game.turnIndex = (gameWithVisits.game.turnIndex + 1) % matchWithUsers.users.size();
        gameRepository.updateTurnIndex(gameWithVisits.game.turnIndex, gameWithVisits.game.getGameId());
    }

    public void decrementTurnIndex() {
        gameWithVisits.game.turnIndex = (gameWithVisits.game.turnIndex - 1 + matchWithUsers.users.size()) % matchWithUsers.users.size();
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
        gameRepository.getMatchWithUsers(matchId)
                .subscribeOn(Schedulers.io())
                .subscribe(new FlowableSubscriber<MatchWithUsers>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(MatchWithUsers matchWithUsers) {
                        setMatchData(matchWithUsers);
                        matchWithUsersMutableLiveData.postValue(matchWithUsers);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


        gameRepository.getGameWithVisits(matchId)
                .subscribeOn(Schedulers.io())
                .subscribe(new FlowableSubscriber<GameWithVisits>() {
                   @Override
                   public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
                       s.request(Integer.MAX_VALUE);
                       Log.d("jtest", "onSubscribe matchId" + matchId);
                   }

                   @Override
                   public void onNext(GameWithVisits gameWithVisits) {
                       Log.d("jtest", "gameWithVisits onNext: " + gameWithVisits.game.gameId);
                       setGameWithVisits(gameWithVisits);
                       gameWithVisitsMutableLiveData.postValue(gameWithVisits);
                   }

                   @Override
                   public void onError(Throwable t) {
                       Log.d("jtest", "Error fetching game visits: "+t);
                   }

                   @Override
                   public void onComplete() {
                       Log.d("jtest", "onComplete: Game visits fetch completed");
                   }
               });

        gameRepository.getGamesInMatch(matchId)
                .subscribeOn(Schedulers.io())
                .subscribe(new FlowableSubscriber<List<Game>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
                        s.request(Integer.MAX_VALUE);
                    }

                    @Override
                    public void onNext(List<Game> games) {
//                        listOfGames = games;
                        gamesInMatchLiveData.postValue(games);
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

//    public LiveData<List<Visit>> getVisits(){
//        return gameRepository.getVisitsInGame(matchData.game.getGameId());
//    }
//
    public LiveData<Boolean> getFinished() {
        return finished;
    }

    public MutableLiveData<MatchWithUsers> getMatchDataLiveData() {
        return matchWithUsersMutableLiveData;
    }


    private void setMatchData(MatchWithUsers matchWithUsers){
        this.matchWithUsers = matchWithUsers;
    }

    public MutableLiveData<List<Game>> getGamesInMatchLiveData() {
        return gamesInMatchLiveData;
    }

}
