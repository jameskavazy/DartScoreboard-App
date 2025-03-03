package com.example.dartscoreboard.Game;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.dartscoreboard.Application.DartsScoreboardApplication;
import com.example.dartscoreboard.User.User;
import com.example.dartscoreboard.User.UserRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

public class GameViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private String gameID;
    public static int turnIndex = 0;
    private int turnIndexLegs = 0;
    private int turnIndexSets = 0;
    private List<User> playersList;
    private GameType gameType;
    private boolean finished;

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


    public void playerVisit(int scoreInt) {


        if (scoreInt == 0)
            Toast.makeText(DartsScoreboardApplication.getContext(), "No score.", Toast.LENGTH_SHORT).show(); 
        if (scoreInt <= 180) {
            // checks for valid score input
//            User currentPlayer = playersList.get(turnIndex);
//            int currentScore = currentPlayer.getPlayerScore();
//            setIsCheckoutFlag(currentPlayer, currentScore);
//            currentPlayer.setPlayerScore(subtract(currentScore, scoreInt));
            incrementTurnIndex();
        }
//        nextLeg();
    }

//    private void setIsCheckoutFlag(User currentPlayer, int currentScore) {
////       todo make this code below popup dialogue that asks how many attempts on double
//        currentPlayer.setCheckout((currentScore <= 170) &&
//                ((currentScore != 169) && (currentScore != 168) &&
//                        (currentScore != 166) && (currentScore != 165) &&
//                        (currentScore != 163) && (currentScore != 162) &&
//                        (currentScore != 159)));
////        currentPlayer.setCheckout((currentScore == 50 || currentScore <= 40) && currentScore % 2 == 0);
//    }


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
            Toast.makeText(DartsScoreboardApplication.getContext(), "BUST", Toast.LENGTH_SHORT).show();
            return playerScore;
        }


        if (newScore == 0) {
            if (playerScore >= 171) {
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
                    Toast.makeText(DartsScoreboardApplication.getContext(), "BUST", Toast.LENGTH_SHORT).show();
                    return playerScore;
            }
            return newScore;
        }

        if (newScore > 1) {
            return newScore;
        } else {
            Toast.makeText(DartsScoreboardApplication.getContext(), "BUST", Toast.LENGTH_SHORT).show();
            return playerScore;
        }
    }





//    public void undo(RecyclerAdapterGamePlayers adapter) {
//        if (!getMatchStateStack().isEmpty()) {
//            MatchState matchState = getMatchStateStack().pop();
//            List<User> previousUserList = matchState.getPlayerList();
//            ArrayList<Integer> previousUserPreviousScoresList = previousUserList.get(matchState.getTurnIndex()).getPreviousScoresList();
//            if (!previousUserPreviousScoresList.isEmpty()) {
//                //// TODO: 28/01/2024 Why does this need to happen. Having to manually remove last visit. Deep copy doesn't work
//                previousUserPreviousScoresList.remove(previousUserPreviousScoresList.size() - 1);
//            }
//
//            setPlayersList(previousUserList);
//            setTurnIndex(matchState.getTurnIndex());
//            setTurnIndexLegs(matchState.getTurnIndexForLegs());
//            setTurnIndexSets(matchState.getTurnIndexForSets());
//            adapter.setUsersList(previousUserList);
//            adapter.notifyDataSetChanged();
//        }
//    }

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

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

//    public void endGame() {
//        //Clear down controller at end of game.
//        setFinished(true);
//        for (User user : getPlayersList()) {
//            //Only add scores for non practice games
//            if (getPlayersList().size() > 1) {
//                StatsHelper.getInstance().updateLifeTimeScores(user);
//                StatsHelper.getInstance().updateLifeTimeVisits(user);
//                if (user.getPlayerScore() == 0) {
//                    user.incrementWins();
//                } else user.incrementLosses();
//            }
//        }
//        clearTurnIndices();
//        gameSettings.clear();
//    }

    public void setGameState(Game game) {
//        setGameID(gameState.getGameID());
        setGameType(game.getGameType());
        setGameSettings(game.getGameSettings());
        setTurnIndex(game.getTurnIndex());
        setTurnIndexLegs(game.getLegIndex());
        setTurnIndexSets(game.getSetIndex());
//        if (gameID == 0) {
//            setPlayerStartingScores();
//            finished = false;
//        }
    }

    public void updateAllUsers() {
        for (User user : getPlayersList()) {
            updateUser(user);
        }
    }

    public void saveGameStateToDb() {
////      Create GameState object + attach the id for DB update
//        GameState gameState = getGameInfo();
//        if (getGameID() != 0) {
//            gameState.setGameID(getGameID());
//            update(gameState);
//        } else {
//            insert(gameState).subscribe(new SingleObserver<Long>() {
//                @Override
//                public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
//
//                }
//
//                @Override
//                public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Long aLong) {
//                    Log.d("dom test", "onSuccess " + aLong);
//                    setGameID(aLong);
//                }
//
//                @Override
//                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
//                }
//            });
//        }
    }

    public Game getGameInfo() {
        return new Game(
                getGameType(),
                getGameSettings(),
                getTurnIndex(),
                getTurnIndexLegs(),
                getTurnIndexLegs(),
                getGameID());
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


    public boolean getFinished() {
        return finished;
    }

    public void setFinished(boolean f) {
        finished = f;
    }


}
