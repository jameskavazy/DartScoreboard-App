package com.example.dartscoreboard.livematches;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

@Dao
public interface LiveProMatchesDao {

    @Upsert
    void upsertAll(List<ProMatch> proMatchList);

    @Query("DELETE FROM pro_match_cache")
    void deleteAll();

    @Query("SELECT * FROM pro_match_cache")
    LiveData<List<ProMatch>> getAll();
}
