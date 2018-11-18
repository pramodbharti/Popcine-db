package com.example.pramod.popcine.utils;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.pramod.popcine.data.MovieDatabase;
import com.example.pramod.popcine.model.Movie;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private LiveData<List<Movie>> moviesList;

    public LiveData<List<Movie>> getMoviesList() {
        return moviesList;
    }

    public MovieViewModel(@NonNull Application application) {
        super(application);
        MovieDatabase database = MovieDatabase.getInstance(this.getApplication());
        moviesList = database.movieDao().getAllMovies();
    }
}
