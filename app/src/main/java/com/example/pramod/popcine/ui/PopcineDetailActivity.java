package com.example.pramod.popcine.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pramod.popcine.R;
import com.example.pramod.popcine.model.Popcine;
import com.example.pramod.popcine.utils.Constants;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PopcineDetailActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_popcine);
        ButterKnife.bind(this);

        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);

        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupUI();

    }

    public void setupUI() {
        //Getting all data through intent and setting into the views
        Intent intent = getIntent();
        if (intent != null) {
            Popcine popcine = getIntent().getParcelableExtra(Constants.PARCEABLE_CLASS);
            getSupportActionBar().setTitle(popcine.getTitle());
            Picasso.with(this)
                    .load(Constants.DETAIL_POSTER_URL + popcine.getPosterPath())
                    .placeholder(R.drawable.popcine)
                    .error(R.drawable.popcine)
                    .into(posterImage);

            Picasso.with(this)
                    .load(Constants.BACKDROP_URL + popcine.getBackdropPath())
                    .placeholder(R.drawable.popcine)
                    .error(R.drawable.popcine)
                    .into(backdropImage);
            movieTitle.setText(popcine.getTitle());
            voteAverage.setText(String.valueOf(popcine.getVoteAverage()));
            releaseDate.setText(popcine.getReleaseDate());
            overview.setText(popcine.getOverview());
        }
    }
}
