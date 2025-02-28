package com.example.dartscoreboard.SetupGame;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.dartscoreboard.Game.GameSettings;
import com.example.dartscoreboard.Game.GameState;
import com.example.dartscoreboard.Game.GameType;
import com.example.dartscoreboard.Game.GameUsers;
import com.example.dartscoreboard.MatchHistory.MatchHistoryRepository;
import com.example.dartscoreboard.MatchHistory.MatchState;
import com.example.dartscoreboard.User.User;
import com.example.dartscoreboard.User.UserRepository;
import com.example.dartscoreboard.Utils.PreferencesController;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

public class SetupGameViewModel extends AndroidViewModel {

    private String gameId;
    private UserRepository userRepository;

    private MatchHistoryRepository matchHistoryRepository;
    private List<User> playersToGame = new ArrayList<>();

    private final Stack<MatchState> matchStateStack = new Stack<>();

    public SetupGameViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        matchHistoryRepository = new MatchHistoryRepository(application);
        playersToGame = PreferencesController.getInstance().getPlayers();
    }


    public GameSettings getGameSettings(int legs, int sets) {
        return new GameSettings(legs, sets);
    }

    public GameState createGameState(GameType gameType, int legs, int sets) {
        gameId = UUID.randomUUID().toString();
        GameState gameState = new GameState(
                gameType, getGameSettings(legs, sets), playersToGame, 0, 0, 0,
                matchStateStack, false, gameId);
        Log.d("db test", "the id as set" + gameState.gameID);
        addUsersToGame();
        matchHistoryRepository.insert(gameState);
        return gameState;
    }

    public List<User> getSelectedPlayers() {
        return playersToGame;
    }

    public void addUsersToGame(){
        for (User player :
                playersToGame) {
            GameUsers gameUsers = new GameUsers(player.userID, gameId, playersToGame.indexOf(player));
            userRepository.addUsersToMatch(gameUsers);
        }
    }
}
