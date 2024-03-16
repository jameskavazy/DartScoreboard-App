package com.example.dartscoreboard.LiveMatches;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

@Dao
public interface LiveProMatchesDao {

    @Upsert
    void upsertAll(List<Match> matchList);

    @Query("DELETE FROM pro_match_cache")
    void deleteAll();

    @Query("SELECT * FROM pro_match_cache")
    List<Match> getAll();
}
