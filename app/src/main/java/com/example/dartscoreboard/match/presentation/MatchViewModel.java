package com.example.dartscoreboard.match.presentation;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.dartscoreboard.application.DartsScoreboardApplication;
import com.example.dartscoreboard.match.data.models.Leg;
import com.example.dartscoreboard.match.data.models.LegWithVisits;
import com.example.dartscoreboard.match.data.models.MatchData;
import com.example.dartscoreboard.match.data.models.User;
import com.example.dartscoreboard.match.data.repository.MatchRepository;
import com.example.dartscoreboard.match.data.models.MatchWithUsers;
import com.example.dartscoreboard.match.data.models.Set;
import com.example.dartscoreboard.match.data.models.Visit;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;


public class MatchViewModel extends AndroidViewModel {

    private final MutableLiveData<MatchData> liveMatchData = new MutableLiveData<>();
    private MatchWithUsers matchWithUsers;
    private LegWithVisits legWithVisits;
    private final MatchRepository matchRepository;
    private final MutableLiveData<Boolean> finished = new MutableLiveData<>(false);
    private final static String BUST = "BUST";
    private final static String NO_SCORE = "No score";
    private static final int NO_LEG_OR_SET_WON = -1;
    private static final int LEG_WON_NOT_SET = -2;

    java.util.Set<Integer> impossibleCheckouts = new HashSet<>(Arrays.asList(169, 168, 166, 165, 163, 162, 159));
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MatchViewModel(@NonNull Application application) {
        super(application);
        matchRepository = new MatchRepository(application);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }

    @SuppressLint("CheckResult")
    public void playerVisit(int scoreInt) {
        if (scoreInt == 0) {
            toastMessage(NO_SCORE);
        }
        if (scoreInt <= 180) {
            User user = matchWithUsers.getOrderedUsers().get(legWithVisits.leg.turnIndex);
            int userId = user.userId;
            int startingScore = matchWithUsers.match.getMatchType().startingScore;

            Disposable d = matchRepository.getLegTotalScore(legWithVisits.leg.getLegId(), userId)
                    .flatMap(sumOfVisits -> getNewScoreSingle(scoreInt, user, startingScore, sumOfVisits))
                    .flatMap(userScore -> checklegWonSingle(userId, userScore))
                    .flatMap(legsWon -> checkSetsWonSingle(userId, legsWon))
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            setsWon -> handleGameProgression(user, setsWon),
                            throwable -> {
                                throwable.printStackTrace();
                                Log.e("ScoreInsert", "Error processing score submission", throwable);
                                toastMessage("Could not update the game. Please try again.");
                            }
                    );
            compositeDisposable.add(d);
        }
    }

    private int calculateScore(int playerScore, int input) {
        User currentPlayer = matchWithUsers.getOrderedUsers().get(legWithVisits.leg.turnIndex);

        if (currentPlayer.isGuy) {
            if (playerScore > 100
                    && input > 10
                    && input % 5 != 0
                    && playerScore % 5 != 0
                    && playerScore != 501
                    && playerScore != 301)

                input -= 3;
        }
        int newScore = playerScore - input;

        if (newScore < 0) {
            toastMessage(BUST);
            return 0;
        }

        if (newScore == 0) {
            if (playerScore >= 171 || impossibleCheckouts.contains(playerScore)) {
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
        finished.postValue(true);
        matchRepository.setMatchWinner(currentPlayer.userId, matchWithUsers.match.matchId).subscribeOn(Schedulers.io()).subscribe();
        toastMessage(currentPlayer.getUsername() + " wins the match!");
    }

    private Single<Integer> getNewScoreSingle(int scoreInt, User user, int startingScore, Integer sumOfVisits) {
        int prevScore = startingScore - sumOfVisits;
        int visitScore = calculateScore(prevScore, scoreInt);

        Visit visit = createVisit(user, visitScore);
        if (prevScore <= 170 && !impossibleCheckouts.contains(prevScore)){
            visit.checkout = true;
        }
        int finalScore = startingScore - sumOfVisits - visitScore;
        return matchRepository.insertVisit(visit).andThen(Single.just(finalScore));
    }

    private Single<Integer> checklegWonSingle(int userId, Integer userScore) {
        if (userScore == 0) {
            return matchRepository.setLegWinner(userId, legWithVisits.leg.getLegId())
                    .andThen(matchRepository.legsWon(legWithVisits.leg.setId, matchWithUsers.match.matchId, userId));
        }
        return Single.just(NO_LEG_OR_SET_WON);
    }

    private Single<Integer> checkSetsWonSingle(int userId, Integer legsWon) {
        if (legsWon == NO_LEG_OR_SET_WON) {
            return Single.just(NO_LEG_OR_SET_WON);
        }
        if (legsWon == matchWithUsers.match.matchSettings.getTotalLegs()) {
            return matchRepository.addSetWinner(legWithVisits.leg.setId, userId)
                    .andThen(matchRepository.getSetsWon(userId, matchWithUsers.match.matchId));
        }
        return Single.just(LEG_WON_NOT_SET);
    }

    private void handleGameProgression(User user, Integer setsWon) {
        // No game and no set winner
        if (setsWon == NO_LEG_OR_SET_WON) {
            incrementTurnIndex();
        } else if (setsWon == LEG_WON_NOT_SET) {
            int turnIndex = (int) (matchWithUsers.legs.stream().filter(game -> game.setId.equals(legWithVisits.leg.setId)).count()
                    +  matchWithUsers.sets.size() - 1)
                    % matchWithUsers.matchUserWithUserData.size();
            Leg leg = new Leg(UUID.randomUUID().toString(), legWithVisits.leg.setId, matchWithUsers.match.matchId, turnIndex);
            matchRepository.insertLeg(leg).subscribeOn(Schedulers.io()).subscribe();
        } else if (setsWon == matchWithUsers.match.matchSettings.getTotalSets()) {
            endGame(user);
        } else {
            // Set Won but not finished, need new set and new game
            Set set = new Set(UUID.randomUUID().toString(), matchWithUsers.match.matchId);
            matchRepository.insertSet(set).subscribeOn(Schedulers.io()).subscribe();

            int turnIndex = matchWithUsers.sets.size() % matchWithUsers.matchUserWithUserData.size();
            Leg leg = new Leg(UUID.randomUUID().toString(), set.setId, matchWithUsers.match.matchId, turnIndex);
            matchRepository.insertLeg(leg).subscribeOn(Schedulers.io()).subscribe();
        }
    }

    public void undo() {
        finished.postValue(false);
        int latestSetPosition = matchWithUsers.sets.size() - 1;

        boolean matchWon = matchWithUsers.match.winnerId != 0;
        if (matchWon) {
            handeMatchWonUndo(latestSetPosition);
            return;
        }

        boolean gameWon = legWithVisits.visits.size() == 0;

        if (gameWon) {
            String penultimateGameSetId = getPenultimateGameSetId();
            boolean setWon = isSetWon(penultimateGameSetId);
            if (setWon) {
                String currentSetId = matchWithUsers.sets.get(latestSetPosition).setId;
                handleSetWonUndo(currentSetId);
            } else handleGameWonUndo(latestSetPosition);

        }
         else {
            matchRepository.deleteLatestVisit();
            decrementTurnIndex();
        }
    }

    private void toastMessage(String msg) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> Toast.makeText(DartsScoreboardApplication.getContext(),
                msg, Toast.LENGTH_SHORT).show());
    }

    public void incrementTurnIndex() {
        legWithVisits.leg.turnIndex = (legWithVisits.leg.turnIndex + 1) % matchWithUsers.getOrderedUsers().size();
        matchRepository.updateTurnIndex(legWithVisits.leg.turnIndex, legWithVisits.leg.getLegId());
    }

    public void decrementTurnIndex() {
        legWithVisits.leg.turnIndex = (legWithVisits.leg.turnIndex - 1 + matchWithUsers.getOrderedUsers().size()) % matchWithUsers.getOrderedUsers().size();
        matchRepository.updateTurnIndex(legWithVisits.leg.turnIndex, legWithVisits.leg.getLegId());
    }


    public void fetchMatchData(String matchId){
       Flowable<Pair<MatchWithUsers, LegWithVisits>> flowable =  Flowable.combineLatest(
                matchRepository.getMatchWithUsers(matchId),
                matchRepository.getLegWithVisits(matchId),
                Pair::new
        );

        compositeDisposable.add(
            flowable.subscribeOn(Schedulers.io())
               .subscribeWith(new DisposableSubscriber<Pair<MatchWithUsers, LegWithVisits>>() {
                   @Override
                   public void onNext(Pair<MatchWithUsers, LegWithVisits> data) {
                       if (data == null) {
                           throw new NullPointerException("Received null Pair<MatchWithUsers, LegWithVisits>");
                       }
                       if (data.first == null) {
                           throw new NullPointerException("MatchWithUsers (data.first) is null");
                       }
                       if (data.second == null) {
                           throw new NullPointerException("LegWithVisits (data.second) is null");
                       }
                       MatchData matchData = new MatchData(data.first, data.second);
                       liveMatchData.postValue(matchData);
                       setMatchWithUsers(data.first);
                       setLegWithVisits(data.second);
                   }

                   @Override
                   public void onError(Throwable t) {
                        Log.e("MatchDataFlowable", "Error encountered in data flow", t);
                   }

                   @Override
                   public void onComplete() {

                   }
               }));
    }

    @NonNull
    private Visit createVisit(User user, int finalScore) {
        Visit visit = new Visit();
        visit.setLegId(legWithVisits.leg.getLegId());
        visit.setUserId(user.userId);
        visit.setScore(finalScore);
        return visit;
    }
    private boolean isSetWon(String penultimateGameSetId) {
        return !legWithVisits.leg.setId.equals(penultimateGameSetId);
    }

    private void handleGameWonUndo(int latestSetPosition) {
        Disposable d = matchRepository.deleteLegById(legWithVisits.leg.legId)
                .andThen(matchRepository.getLatestLegId(matchWithUsers.match.matchId))
                .flatMapCompletable(gameId ->
                        matchRepository.setLegWinner(0, gameId)
                        .andThen(matchRepository.setMatchWinner(0, matchWithUsers.match.matchId))
                )
                .andThen(matchRepository.addSetWinner(matchWithUsers.sets.get(latestSetPosition).setId, 0))
                .subscribeOn(Schedulers.io())
                .subscribe(
                        matchRepository::deleteLatestVisit,
                        handleError("Undo latest visit")
                );

        compositeDisposable.add(d);
    }

    private void handleSetWonUndo(String currentSetId) {
        Disposable d = matchRepository.deleteSet(currentSetId)
                .andThen(matchRepository.deleteLegById(legWithVisits.leg.legId))
                .andThen(matchRepository.getLatestLegId(matchWithUsers.match.matchId))
                .flatMapCompletable(gameId -> matchRepository.setLegWinner(0, gameId))
                .andThen(matchRepository.getLatestSetId(matchWithUsers.match.matchId))
                .flatMapCompletable(setId -> matchRepository.addSetWinner(setId,0))
                .subscribeOn(Schedulers.io())
                .subscribe(
                        matchRepository::deleteLatestVisit,
                        handleError("Undo latest visit")
                );
        compositeDisposable.add(d);
    }

    private void handeMatchWonUndo(int latestSetPosition) {
        Disposable d =  matchRepository.setLegWinner(0, legWithVisits.leg.legId)
                .andThen(matchRepository.addSetWinner(matchWithUsers.sets.get(latestSetPosition).setId, 0))
                .andThen(matchRepository.setMatchWinner(0, matchWithUsers.match.matchId))
                .subscribeOn(Schedulers.io())
                .subscribe(
                        matchRepository::deleteLatestVisit,
                        handleError("Undo latest visit")
                );
        compositeDisposable.add(d);
    }

    @Nullable
    private String getPenultimateGameSetId() {
        String penultimateGameSetId = null;

        if (matchWithUsers.legs.size() >= 2 ) {
            penultimateGameSetId = matchWithUsers.legs.get(matchWithUsers.legs.size() - 2).setId; // This should always be true because if one game the game will have visits
        }
        return penultimateGameSetId;
    }

    public LiveData<Boolean> getFinished() {
        return finished;
    }

    public MutableLiveData<MatchData> getLiveMatchData(){
        return liveMatchData;
    }

    private void setMatchWithUsers(MatchWithUsers matchWithUsers){
        this.matchWithUsers = matchWithUsers;
    }

    public void setLegWithVisits(LegWithVisits legWithVisits) {
        this.legWithVisits = legWithVisits;
    }

    private Consumer<Throwable> handleError(String operation) {
        return throwable -> {
            Log.e(operation, "Error occurred", throwable);
            toastMessage("An error occurred during " + operation + ". Please try again.");
        };
    }
}