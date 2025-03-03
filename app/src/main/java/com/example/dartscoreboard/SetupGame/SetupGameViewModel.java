package com.example.dartscoreboard.SetupGame;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.dartscoreboard.Game.GameSettings;
import com.example.dartscoreboard.Game.Game;
import com.example.dartscoreboard.Game.GameType;
import com.example.dartscoreboard.Game.GameUsers;
import com.example.dartscoreboard.Game.GameRepository;
import com.example.dartscoreboard.User.User;
import com.example.dartscoreboard.User.UserRepository;
import com.example.dartscoreboard.Utils.PreferencesController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SetupGameViewModel extends AndroidViewModel {

    private String gameId;
    private UserRepository userRepository;

    private GameRepository gameRepository;
    private List<User> playersToGame;


    public SetupGameViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        gameRepository = new GameRepository(application);
        playersToGame = PreferencesController.getInstance().getPlayers();
    }


    public GameSettings getGameSettings(int legs, int sets) {
        return new GameSettings(legs, sets);
    }

    public Game createGame(GameType gameType, int legs, int sets) {
        gameId = UUID.randomUUID().toString();
        Game game = new Game(
                gameType, getGameSettings(legs, sets),
                0, 0, 0,
                gameId);
        game.setPlayersCSV(getSelectedPlayers());
        Log.d("db test", "the id as set" + game.gameID);
        addUsersToGame();
        gameRepository.insert(game);
        return game;
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
