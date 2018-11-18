package com.example.pramod.popcine.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.pramod.popcine.R;
import com.example.pramod.popcine.adapter.MovieAdapter;
import com.example.pramod.popcine.data.MovieDatabase;
import com.example.pramod.popcine.model.Movie;
import com.example.pramod.popcine.utils.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteMovieActivity extends AppCompatActivity {
    private static final String TAG = FavoriteMovieActivity.class.getSimpleName();
    private List<Movie> movieList;
    private MovieAdapter movieAdapter;

    @BindView(R.id.fav_popcine_recyclerview)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movie);
        ButterKnife.bind(this);

        int spanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), spanCount);

        movieList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        movieAdapter = new MovieAdapter(movieList, this, FavoriteMovieActivity.this);
        recyclerView.setAdapter(movieAdapter);

        setupViewModel();


    }

    private void setupViewModel() {
        setTitle(getResources().getString(R.string.favorite_movies));
        MovieViewModel viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        viewModel.getMoviesList().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movie) {
                if (movieList.size() > 0) {
                    movieList.clear();
                }
                movieList.addAll(movie);
                movieAdapter.notifyDataSetChanged();
            }
        });
    }

}
