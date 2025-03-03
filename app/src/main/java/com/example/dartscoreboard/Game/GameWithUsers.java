package com.example.dartscoreboard.Game;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.dartscoreboard.User.User;

import java.util.List;

public class GameWithUsers {
    @Embedded public Game game;
    @Relation(
            parentColumn = "gameId",
            entityColumn = "userID",
            associateBy = @Junction(GameUsers.class)
    )
    public List<User> users;

}
