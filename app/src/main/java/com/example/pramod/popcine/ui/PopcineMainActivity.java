package com.example.pramod.popcine.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pramod.popcine.BuildConfig;
import com.example.pramod.popcine.R;
import com.example.pramod.popcine.adapter.MovieAdapter;
import com.example.pramod.popcine.model.Movie;
import com.example.pramod.popcine.model.MovieResponse;
import com.example.pramod.popcine.network.PopcineApiClient;
import com.example.pramod.popcine.network.PopcineApiInterface;
import com.example.pramod.popcine.utils.Constants;
import com.example.pramod.popcine.utils.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.pramod.popcine.network.PopcineApiClient.retrofit;

public class PopcineMainActivity extends AppCompatActivity {

    private MovieResponse movieResponse;
    private List<Movie> movieList;
    private MovieAdapter movieAdapter;

    @BindView(R.id.popcine_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.placeholder_logo)
    ImageView placeholder_logo;

    @BindView(R.id.no_internet)
    TextView no_internet;

    private final static String apiKey = BuildConfig.API_KEY;
    private static final String LIST_STATE = "list_state";
    private Parcelable savedRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_popcine);
        ButterKnife.bind(this);
        movieList = new ArrayList<>();
        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);

        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
        restorePosition();
        //Loads popular movies on the launch of app
        loadPopularMovies();
        int spanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), spanCount);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        movieAdapter = new MovieAdapter(movieList, this, PopcineMainActivity.this);
        recyclerView.setAdapter(movieAdapter);


    }


    //This method will check whether the API KEY is empty or not
    public boolean checkApiKey() {
        if (apiKey.isEmpty()) {
            no_internet.setText(R.string.insert_api);
            return false;
        } else {
            return true;
        }
    }

    // This method will check if there internet connectivity is available or not.
    public boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            placeholder_logo.setVisibility(View.GONE);
            no_internet.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            return true;
        } else {
            placeholder_logo.setVisibility(View.VISIBLE);
            no_internet.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return false;
        }

    }

    //This method will load Popular movies
    public void loadPopularMovies() {
        setTitle(getResources().getString(R.string.popular_movies));
        if (checkApiKey()) {
            if (checkInternet()) {
                PopcineApiInterface popcineApiInterface = PopcineApiClient.getClient(Constants.BASE_URL).
                        create(PopcineApiInterface.class);
                Call<MovieResponse> movieResponseCall = popcineApiInterface.getPopularMovies(apiKey);
                makeMovieApiRequest(movieResponseCall);
                retrofit = null;
            }
        }
    }

    //This method will load top rated movies
    public void loadTopRatedMovies() {
        setTitle(getResources().getString(R.string.top_rated_movies));
        if (checkApiKey()) {
            if (checkInternet()) {
                PopcineApiInterface popcineApiInterface = PopcineApiClient.getClient(Constants.BASE_URL).
                        create(PopcineApiInterface.class);
                Call<MovieResponse> movieResponseCall = popcineApiInterface.getTopRatedMovies(apiKey);
                makeMovieApiRequest(movieResponseCall);
                retrofit = null;
            }
        }
    }

    private void loadFavoriteMovies() {
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

    public void makeMovieApiRequest(Call<MovieResponse> call) {
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                movieResponse = response.body();

                if (movieList != null) {
                    movieList.removeAll(movieList);
                }

                if (movieResponse != null) {
                    movieList.addAll(movieResponse.getResults());
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_not), Toast.LENGTH_LONG).show();
                }
                movieAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.failed_loading), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        savedRecyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(LIST_STATE, savedRecyclerViewState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            savedRecyclerViewState = savedInstanceState.getParcelable(LIST_STATE);
        }
    }

    private void restorePosition() {
        if (savedRecyclerViewState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerViewState);
            savedRecyclerViewState = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflating menu here
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular_movies:
                loadPopularMovies();
                return true;
            case R.id.toprated_movies:
                loadTopRatedMovies();
                return true;
            case R.id.favorite_movies:
                loadFavoriteMovies();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
