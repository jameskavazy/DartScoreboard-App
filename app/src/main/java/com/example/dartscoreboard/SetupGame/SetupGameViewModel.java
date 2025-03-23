package com.example.dartscoreboard.SetupGame;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.dartscoreboard.Game.Match;
import com.example.dartscoreboard.Game.MatchSettings;
import com.example.dartscoreboard.Game.MatchType;
import com.example.dartscoreboard.Game.MatchUsers;
import com.example.dartscoreboard.Game.GameRepository;
import com.example.dartscoreboard.User.User;
import com.example.dartscoreboard.User.UserRepository;
import com.example.dartscoreboard.Utils.PreferencesController;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import io.reactivex.rxjava3.schedulers.Schedulers;

public class SetupGameViewModel extends AndroidViewModel {

    private String matchId;
    private UserRepository userRepository;

    private GameRepository gameRepository;
    private List<User> playersToGame;


    public SetupGameViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        gameRepository = new GameRepository(application);
        playersToGame = PreferencesController.getInstance().getPlayers();
    }


    public MatchSettings getMatchSettings(int legs, int sets) {
        return new MatchSettings(legs, sets);
    }

    public Match createMatch(MatchType matchType, int legs, int sets) {
        matchId = UUID.randomUUID().toString();
        Match match = new Match(matchId, matchType, getMatchSettings(legs, sets), OffsetDateTime.now());
        match.setPlayersCSV(getSelectedPlayers());
        addUsersToMatch();
        gameRepository.insertMatch(match).subscribeOn(Schedulers.io()).subscribe();
        return match;
    }

//    public Game createGame(MatchType matchType, int legs, int sets) {
//        gameId = UUID.randomUUID().toString();
//        Game game = new Game(gameId, 0, 0, 0);
//        game.setPlayersCSV(getSelectedPlayers());
//        addUsersToGame();
//        gameRepository.insert(game);
//        return game;
//    }

    public List<User> getSelectedPlayers() {
        return playersToGame;
    }

    public void addUsersToMatch(){
        for (User player :
                playersToGame) {
            MatchUsers matchUsers = new MatchUsers(player.userID, matchId, playersToGame.indexOf(player));
            userRepository.addUsersToMatch(matchUsers);
        }
    }
}
