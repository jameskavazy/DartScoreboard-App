package com.example.dartscoreboard.match.presentation;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.dartscoreboard.application.DartsScoreboardApplication;
import com.example.dartscoreboard.user.User;
import com.example.dartscoreboard.match.data.models.Game;
import com.example.dartscoreboard.match.data.repository.GameRepository;
import com.example.dartscoreboard.match.data.models.GameWithVisits;
import com.example.dartscoreboard.match.data.models.MatchWithUsers;
import com.example.dartscoreboard.match.data.models.Set;
import com.example.dartscoreboard.match.data.models.Visit;

import org.reactivestreams.Subscription;

import java.util.UUID;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class GameViewModel extends AndroidViewModel {
    private MatchWithUsers matchWithUsers;
    private GameWithVisits gameWithVisits;
    private MutableLiveData<MatchWithUsers> matchWithUsersMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<GameWithVisits> gameWithVisitsMutableLiveData = new MutableLiveData<>();
    private final GameRepository gameRepository;

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

    private static final int NO_LEG_OR_SET_WON = -1;
    private static final int LEG_WON_NOT_SET = -2;
    public static int currentSetNumber = 0;

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
            User user = matchWithUsers.users.get(gameWithVisits.game.turnIndex);
            int userId = user.userID;
            int startingScore = matchWithUsers.match.getMatchType().startingScore;

            gameRepository.getGameTotalScoreByUser(gameWithVisits.game.getGameId(), userId)
                    .flatMap(sumOfVisits -> getNewScoreSingle(scoreInt, user, startingScore, sumOfVisits))
                    .flatMap(userScore -> checklegWonSingle(userId, userScore))
                    .flatMap(legsWon -> checkSetsWonSingle(userId, legsWon))
                    .subscribeOn(Schedulers.io())
                    .subscribe(setsWon -> handleGameProgression(user, setsWon), Throwable::printStackTrace);
        }
    }

    private int calculateScore(int playerScore, int input) {
        User currentPlayer = matchWithUsers.users.get(gameWithVisits.game.turnIndex);

        if (currentPlayer.isGuy) {
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

    private Single<Integer> getNewScoreSingle(int scoreInt, User user, int startingScore, Integer sumOfVisits) {
        int visitScore = calculateScore(startingScore - sumOfVisits, scoreInt);
        Visit visit = createVisit(user, visitScore);
        int finalScore = startingScore - sumOfVisits - visitScore;

        return gameRepository.insertVisit(visit).andThen(Single.just(finalScore));
    }

    private Single<Integer> checklegWonSingle(int userId, Integer userScore) {
        if (userScore == 0) {
            return gameRepository.setGameWinner(userId, gameWithVisits.game.getGameId())
                    .andThen(gameRepository.legsWon(gameWithVisits.game.setId, matchId, userId));
        }
        return Single.just(NO_LEG_OR_SET_WON);
    }

    private Single<Integer> checkSetsWonSingle(int userId, Integer legsWon) {
        if (legsWon == NO_LEG_OR_SET_WON) {
            return Single.just(NO_LEG_OR_SET_WON);
        }
        if (legsWon == matchWithUsers.match.matchSettings.getTotalLegs()) {
            return gameRepository.addSetWinner(gameWithVisits.game.setId, userId)
                    .andThen(gameRepository.getSetsWon(userId, matchId));
        }
        return Single.just(LEG_WON_NOT_SET);
    }

    private void handleGameProgression(User user, Integer setsWon) {
        // No game and no set winner
        if (setsWon == NO_LEG_OR_SET_WON) {
            incrementTurnIndex();
        } else if (setsWon == LEG_WON_NOT_SET) {
            int turnIndex = (int) (matchWithUsers.games.stream().filter(game -> game.setId.equals(gameWithVisits.game.setId)).count()
                    +  matchWithUsers.sets.size() - 1)
                    % matchWithUsers.users.size();
            Game game = new Game(UUID.randomUUID().toString(), gameWithVisits.game.setId, matchId, turnIndex, 0, 0);
            gameRepository.insertGame(game).subscribeOn(Schedulers.io()).subscribe();
        } else if (setsWon == matchWithUsers.match.matchSettings.getTotalSets()) {
            endGame(user);
        } else {
            // Set Won but not finished, need new set and new game
            Set set = new Set(UUID.randomUUID().toString(), matchId);
            gameRepository.insertSet(set).subscribeOn(Schedulers.io()).subscribe();

            int turnIndex = matchWithUsers.sets.size() % matchWithUsers.users.size();
            Game game = new Game(UUID.randomUUID().toString(), set.setId, matchId, turnIndex,0,0);
            gameRepository.insertGame(game).subscribeOn(Schedulers.io()).subscribe();
        }
    }

    public void undo() {
        _finished.postValue(false);
        int latestSetPosition = matchWithUsers.sets.size() - 1;

        boolean matchWon = matchWithUsers.match.winnerId != 0;
        if (matchWon) {
            handeMatchWonUndo(latestSetPosition);
            return;
        }

        boolean gameWon = gameWithVisits.visits.size() == 0;

        if (gameWon) {
            String penultimateGameSetId = getPenultimateGameSetId();
            boolean setWon = setWon(penultimateGameSetId);
            if (setWon) {
                String currentSetId = matchWithUsers.sets.get(latestSetPosition).setId;
                handleSetWonUndo(currentSetId);
            } else handleGameWonUndo(latestSetPosition);

        }
         else {
            gameRepository.deleteLatestVisit();
            decrementTurnIndex();
        }
    }

    private void toastMessage(String msg) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> Toast.makeText(DartsScoreboardApplication.getContext(),
                msg, Toast.LENGTH_SHORT).show());
    }

    public void incrementTurnIndex() {
        gameWithVisits.game.turnIndex = (gameWithVisits.game.turnIndex + 1) % matchWithUsers.users.size();
        gameRepository.updateTurnIndex(gameWithVisits.game.turnIndex, gameWithVisits.game.getGameId());
    }

    public void decrementTurnIndex() {
        gameWithVisits.game.turnIndex = (gameWithVisits.game.turnIndex - 1 + matchWithUsers.users.size()) % matchWithUsers.users.size();
        gameRepository.updateTurnIndex(gameWithVisits.game.turnIndex, gameWithVisits.game.getGameId());
    }



    public void fetchMatchData(){
        Flowable.combineLatest(
                gameRepository.getMatchWithUsers(matchId),
                gameRepository.getGameWithVisits(matchId),
                Pair::new
        ).subscribeOn(Schedulers.io())
                .subscribe(new FlowableSubscriber<Pair<MatchWithUsers, GameWithVisits>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Pair<MatchWithUsers, GameWithVisits> matchData) {
                        MatchWithUsers matchWithUsers = matchData.first;
                        setMatchWithUsers(matchWithUsers);
                        matchWithUsersMutableLiveData.postValue(matchWithUsers);

                        GameWithVisits gameWithVisits = matchData.second;
                        setGameWithVisits(gameWithVisits);
                        gameWithVisitsMutableLiveData.postValue(gameWithVisits);

                    }

                    @Override
                    public void onError(Throwable t) {}
                    @Override
                    public void onComplete() {}
                });
    }

    @NonNull
    private Visit createVisit(User user, int finalScore) {
        Visit visit = new Visit();
        visit.setGameId(gameWithVisits.game.getGameId());
        visit.setUserID(user.userID);
        visit.setScore(finalScore);
        return visit;
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


    private boolean setWon(String penultimateGameSetId) {
        return !gameWithVisits.game.setId.equals(penultimateGameSetId);
    }

    private void handleGameWonUndo(int latestSetPosition) {
        gameRepository.deleteGameById(gameWithVisits.game.gameId)
                .andThen(gameRepository.getLatestGameId(matchWithUsers.match.matchId))
                .flatMapCompletable(gameId -> gameRepository.setGameWinner(0, gameId)
                        .andThen(gameRepository.setMatchWinner(0, matchWithUsers.match.matchId)))
                .andThen(gameRepository.addSetWinner(matchWithUsers.sets.get(latestSetPosition).setId, 0))
                .subscribeOn(Schedulers.io())
                .subscribe(gameRepository::deleteLatestVisit, Throwable::printStackTrace);
    }

    private void handleSetWonUndo(String currentSetId) {
        gameRepository.deleteSet(currentSetId)
                .andThen(gameRepository.deleteGameById(gameWithVisits.game.gameId))
                .andThen(gameRepository.getLatestGameId(matchWithUsers.match.matchId))
                .flatMapCompletable(gameId -> gameRepository.setGameWinner(0, gameId))
                .andThen(gameRepository.getLatestSetId(matchId))
                .flatMapCompletable(setId -> gameRepository.addSetWinner(setId,0))
                .subscribeOn(Schedulers.io())
                .subscribe(gameRepository::deleteLatestVisit, Throwable::printStackTrace);
    }

    private void handeMatchWonUndo(int latestSetPosition) {
        //TODO dispose of these in onDestroy?
        gameRepository.setGameWinner(0, gameWithVisits.game.gameId)
                .andThen(gameRepository.addSetWinner(matchWithUsers.sets.get(latestSetPosition).setId, 0))
                .andThen(gameRepository.setMatchWinner(0, matchWithUsers.match.matchId))
                .subscribeOn(Schedulers.io())
                .subscribe(gameRepository::deleteLatestVisit, Throwable::printStackTrace);
    }

    @Nullable
    private String getPenultimateGameSetId() {
        String penultimateGameSetId = null;

        if (matchWithUsers.games.size() >= 2 ) {
            penultimateGameSetId = matchWithUsers.games.get(matchWithUsers.games.size() - 2).setId; // This should always be true because if one game the game will have visits
        }
        return penultimateGameSetId;
    }

    public LiveData<Boolean> getFinished() {
        return finished;
    }

    public MutableLiveData<MatchWithUsers> getMatchDataLiveData() {
        return matchWithUsersMutableLiveData;
    }

    public MutableLiveData<GameWithVisits> getGameWithVisitsMutableLiveData(){
        return gameWithVisitsMutableLiveData;
    }

    private void setMatchWithUsers(MatchWithUsers matchWithUsers){
        this.matchWithUsers = matchWithUsers;
    }

    public void setGameWithVisits(GameWithVisits gameWithVisits) {
        this.gameWithVisits = gameWithVisits;
    }

}
