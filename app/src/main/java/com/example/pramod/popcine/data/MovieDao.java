package com.example.pramod.popcine.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.pramod.popcine.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie movie);

    @Query("DELETE FROM movies_table")
    void deleteAll();

    @Query("SELECT * FROM movies_table")
    LiveData<List<Movie>> getAllMovies();

    @Query("DELETE FROM movies_table WHERE id= :movieID")
    void deleteMovieWithId(String movieID);

    @Query("SELECT * FROM movies_table WHERE id= :movieID")
    Movie loadMovieById(String movieID);
}