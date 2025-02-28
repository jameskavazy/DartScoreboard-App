package com.example.dartscoreboard.SetupGame;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dartscoreboard.Game.GameSettings;
import com.example.dartscoreboard.Game.GameState;
import com.example.dartscoreboard.Game.GameType;
import com.example.dartscoreboard.MatchHistory.MatchState;
import com.example.dartscoreboard.User.User;
import com.example.dartscoreboard.User.UserRepository;
import com.example.dartscoreboard.Utils.PreferencesController;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SelectGameViewModel extends AndroidViewModel {

    private List<User> playersToGame = new ArrayList<>();

    private final Stack<MatchState> matchStateStack = new Stack<>();

    public SelectGameViewModel(@NonNull Application application) {
        super(application);
        playersToGame = PreferencesController.getInstance().getPlayers();
    }


    public GameSettings getGameSettings(int legs, int sets) {
        return new GameSettings(legs, sets);
    }

    public GameState createGameState(GameType gameType, int legs, int sets) {
        GameState gameState = new GameState(
                gameType, getGameSettings(legs, sets), playersToGame, 0, 0, 0,
                matchStateStack, false);
        gameState.setGameID(0); //TODO set up UUID so we can track games within our Program
        Log.d("db test", "the id as set" + gameState.gameID);
        return gameState;
    }

    public List<User> getSelectedPlayers() {
        return playersToGame;
    }
}
