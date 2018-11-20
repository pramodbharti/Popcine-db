package com.example.pramod.popcine.ui;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pramod.popcine.BuildConfig;
import com.example.pramod.popcine.R;
import com.example.pramod.popcine.adapter.MovieAdapter;
import com.example.pramod.popcine.model.Movie;
import com.example.pramod.popcine.model.MovieResponse;
import com.example.pramod.popcine.network.PopcineApiClient;
import com.example.pramod.popcine.network.PopcineApiInterface;
import com.example.pramod.popcine.utils.Constants;
import com.example.pramod.popcine.utils.MovieViewModel;
import com.example.pramod.popcine.widget.PopularMoviesWidget;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.pramod.popcine.network.PopcineApiClient.retrofit;

public class PopcineMainActivity extends AppCompatActivity {

    private final static String apiKey = BuildConfig.API_KEY;
    private static String DATA_KEY = "data_key";
    private static String STATE_KEY = "state_key";
    private static String TITLE = "title";
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    @BindView(R.id.popcine_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.placeholder_logo)
    ImageView placeholder_logo;
    @BindView(R.id.no_internet)
    TextView no_internet;
    @BindView(R.id.adView)
    AdView adView;
    private MovieResponse movieResponse;
    private List<Movie> movieList;
    private MovieAdapter movieAdapter;
    private String title;
    private Parcelable listState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_popcine);
        ButterKnife.bind(this);

        //Initialize AdMob
        MobileAds.initialize(this, getResources().getString(R.string.admob_app_id));

        movieList = new ArrayList<>();
        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        if (savedInstanceState != null) {
            movieList = savedInstanceState.getParcelableArrayList(DATA_KEY);
            listState = savedInstanceState.getParcelable(STATE_KEY);
            title = savedInstanceState.getString(TITLE);

        }

        int spanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), spanCount);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        movieAdapter = new MovieAdapter(movieList, this, PopcineMainActivity.this);
        recyclerView.setAdapter(movieAdapter);

        if (movieList.size() <= 0) {
            loadPopularMovies();
        } else {

            setTitle(title);
            placeholder_logo.setVisibility(View.GONE);
            no_internet.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            movieAdapter.update(movieList);
            if (listState != null) {
                recyclerView.getLayoutManager().onRestoreInstanceState(listState);
            }
        }
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
            no_internet.setText(getResources().getString(R.string.no_internet));
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
                if (movieList.size() > 0) {
                    placeholder_logo.setVisibility(View.GONE);
                    no_internet.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    placeholder_logo.setVisibility(View.VISIBLE);
                    no_internet.setText(getResources().getString(R.string.no_favorite));
                    no_internet.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
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
                    callSnackBar(getResources().getString(R.string.data_not));
                }
                saveDataToSharedPrefs(movieList);
                movieAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                callSnackBar(getResources().getString(R.string.failed_loading));
            }
        });
    }

    public void callSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(rlMain, Html.fromHtml(getResources().getString(R.string.html_open_tag) + message + getResources().getString(R.string.html_close_tag)), BaseTransientBottomBar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(DATA_KEY, (ArrayList<Movie>) movieList);
        outState.putString(TITLE, getTitle().toString());
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(STATE_KEY, listState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            movieList = savedInstanceState.getParcelableArrayList(DATA_KEY);
            listState = savedInstanceState.getParcelable(STATE_KEY);
            title = savedInstanceState.getString(TITLE);
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


    public void saveDataToSharedPrefs(List<Movie> movieList) {

        Intent intent = new Intent(getApplicationContext(), PopularMoviesWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] ids = AppWidgetManager.getInstance(getApplicationContext())
                .getAppWidgetIds(new ComponentName(getApplication(), PopularMoviesWidget.class));

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getApplication().sendBroadcast(intent);

        StringBuilder builder = new StringBuilder();
        int temp = 0;

        for (int i = 0; i < movieList.size(); i++) {
            if (movieList.get(i).getTitle() != null) {
                temp++;
                builder.append(temp)
                        .append(". ")
                        .append(movieList.get(i).getTitle())
                        .append("          (")
                        .append(movieList.get(i).getVoteAverage())
                        .append(")\n");
            }

            if (temp == 5) {
                break;
            }
        }

        SharedPreferences sharedPref = getApplication().getSharedPreferences(Constants.SHARED_PREF_MOVIE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.SHARED_PREF_KEY, builder.toString());
        editor.apply();
    }
}
