package com.example.pramod.popcine.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pramod.popcine.BuildConfig;
import com.example.pramod.popcine.R;
import com.example.pramod.popcine.adapter.ReviewAdapter;
import com.example.pramod.popcine.adapter.TrailerAdapter;
import com.example.pramod.popcine.data.MovieDatabase;
import com.example.pramod.popcine.model.Movie;
import com.example.pramod.popcine.model.Review;
import com.example.pramod.popcine.model.ReviewResponse;
import com.example.pramod.popcine.model.Trailer;
import com.example.pramod.popcine.model.TrailerResponse;
import com.example.pramod.popcine.network.PopcineApiClient;
import com.example.pramod.popcine.network.PopcineApiInterface;
import com.example.pramod.popcine.utils.AppExecutors;
import com.example.pramod.popcine.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.pramod.popcine.network.PopcineApiClient.retrofit;

public class PopcineDetailActivity extends AppCompatActivity {

    @BindView(R.id.cl_detail)
    ConstraintLayout clDetail;

    @BindView(R.id.backdrop_poster)
    ImageView backdropImage;

    @BindView(R.id.poster)
    ImageView posterImage;

    @BindView(R.id.movie_title)
    TextView movieTitle;

    @BindView(R.id.vote_average)
    TextView voteAverage;

    @BindView(R.id.release_date)
    TextView releaseDate;

    @BindView(R.id.overview)
    TextView overview;

    @BindView(R.id.favorite_button)
    ImageButton favButton;

    // Reviews
    @BindView(R.id.rv_reviews)
    RecyclerView rv_reviews;
    private List<Review> reviewList;
    private ReviewAdapter reviewAdapter;

    // Trailers
    @BindView(R.id.rv_trailers)
    RecyclerView rv_trailers;
    private List<Trailer> trailerList;
    private TrailerAdapter trailerAdapter;

    private ReviewResponse reviewResponse;
    private TrailerResponse trailerResponse;

    private MovieDatabase movieDatabase;
    private Movie favoriteMovie;
    private Movie clickedMovie;
    private boolean isFavorite = false;
    private String movieID;

    private final static String apiKey = BuildConfig.API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_popcine);
        ButterKnife.bind(this);

        trailerList = new ArrayList<>();
        reviewList = new ArrayList<>();

        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);

        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        movieDatabase = MovieDatabase.getInstance(getApplicationContext());
        setupUI();
        isFavorite();
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavoriteButtonClicked();
            }
        });

    }

    private void isFavorite() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                favoriteMovie = movieDatabase.movieDao().loadMovieById(movieID);
                isFavorite = favoriteMovie != null;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Changing the background of the button.
                        if (isFavorite) {
                            favButton.setBackground(getResources().getDrawable(R.drawable.ic_is_favorite));
                        }
                    }
                });
            }
        });
    }

    private void onFavoriteButtonClicked() {
        isFavorite();

        if (isFavorite) {
            //Remove the movie from the favorites database.
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    movieDatabase.movieDao().deleteMovieWithId(movieID);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            favButton.setBackground(getResources().getDrawable(R.drawable.ic_is_favorite_border));
                            isFavorite = false;
                            callSnackBar(getResources().getString(R.string.removed_from_favorite));
                        }
                    });
                }
            });

        } else {
            //Add the movie to the favorites database.
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    movieDatabase.movieDao().insert(clickedMovie);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            favButton.setBackground(getResources().getDrawable(R.drawable.ic_is_favorite));
                            isFavorite = true;
                            callSnackBar(getResources().getString(R.string.added_to_favorite));
                        }
                    });
                }
            });

        }

    }

    public void setupUI() {
        //Getting all data through intent and setting into the views
        Intent intent = getIntent();
        if (intent != null) {
            clickedMovie = getIntent().getParcelableExtra(Constants.PARCEABLE_CLASS);
            getSupportActionBar().setTitle(clickedMovie.getTitle());
            movieID = clickedMovie.getId();
            Picasso.get()
                    .load(Constants.DETAIL_POSTER_URL + clickedMovie.getPosterPath())
                    .placeholder(R.drawable.popcine)
                    .error(R.drawable.popcine)
                    .into(posterImage);

            Picasso.get()
                    .load(Constants.BACKDROP_URL + clickedMovie.getBackdropPath())
                    .placeholder(R.drawable.popcine)
                    .error(R.drawable.popcine)
                    .into(backdropImage);

            movieTitle.setText(clickedMovie.getTitle());
            voteAverage.setText(clickedMovie.getVoteAverage());
            releaseDate.setText(clickedMovie.getReleaseDate());
            overview.setText(clickedMovie.getOverview());

            if (checkInternet()) {
                loadReviews(movieID);
                LinearLayoutManager trailerLinearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                rv_trailers.setHasFixedSize(true);
                rv_trailers.setLayoutManager(trailerLinearLayoutManager);
                trailerAdapter = new TrailerAdapter(trailerList, this);
                rv_trailers.setAdapter(trailerAdapter);

                loadTrailers(movieID);
                LinearLayoutManager reviewLinearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                rv_reviews.setHasFixedSize(true);
                rv_reviews.setLayoutManager(reviewLinearLayoutManager);
                reviewAdapter = new ReviewAdapter(reviewList, this);
                rv_reviews.setAdapter(reviewAdapter);
            } else {
                callSnackBar(getResources().getString(R.string.no_internet));
            }


        }
    }

    public void callSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(clDetail, Html.fromHtml(getResources().getString(R.string.html_open_tag) + message + getResources().getString(R.string.html_close_tag)), BaseTransientBottomBar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }

    public void loadTrailers(String id) {
        String url = Constants.BASE_URL + id + "/";
        PopcineApiInterface popcineApiInterface = PopcineApiClient.getClient(url).
                create(PopcineApiInterface.class);
        Call<TrailerResponse> trailerResponseCall = popcineApiInterface.getTrailers(apiKey);
        makeTrailerApiRequest(trailerResponseCall);
        retrofit = null;
    }

    public void loadReviews(String id) {
        String url = Constants.BASE_URL + id + "/";
        PopcineApiInterface popcineApiInterface = PopcineApiClient.getClient(url).
                create(PopcineApiInterface.class);
        Call<ReviewResponse> reviewResponseCall = popcineApiInterface.getReviews(apiKey);
        makeReviewApiRequest(reviewResponseCall);
        retrofit = null;
    }


    public void makeReviewApiRequest(Call<ReviewResponse> call) {
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                reviewResponse = response.body();

                if (reviewList != null) {
                    reviewList.removeAll(reviewList);
                }

                if (reviewList != null) {
                    reviewList.addAll(reviewResponse.getResults());
                } else {
                    callSnackBar(getResources().getString(R.string.data_not));
                }
                reviewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                callSnackBar(getResources().getString(R.string.failed_loading));
            }
        });
    }

    public boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }

    }

    public void makeTrailerApiRequest(Call<TrailerResponse> call) {
        call.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                trailerResponse = response.body();

                if (trailerList != null) {
                    trailerList.removeAll(trailerList);
                }

                if (trailerList != null) {
                    trailerList.addAll(trailerResponse.getYoutube());
                } else {
                    callSnackBar(getResources().getString(R.string.data_not));
                }
                trailerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                callSnackBar(getResources().getString(R.string.failed_loading));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}