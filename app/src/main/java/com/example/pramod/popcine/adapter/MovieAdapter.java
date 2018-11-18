package com.example.pramod.popcine.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.pramod.popcine.R;
import com.example.pramod.popcine.model.Movie;
import com.example.pramod.popcine.ui.PopcineDetailActivity;
import com.example.pramod.popcine.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private Context context;
    private Activity activity;

    public MovieAdapter(List<Movie> movieList, Context context, Activity activity) {
        this.movieList = movieList;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popcine_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, final int position) {

        // Pasing all data to the detail activity through Intent
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PopcineDetailActivity.class);
                intent.putExtra(Constants.PARCEABLE_CLASS, movieList.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, holder.imageView, ViewCompat.getTransitionName(holder.imageView));
                context.startActivity(intent, options.toBundle());
            }
        });

        Picasso.get()
                .load(Constants.POSTER_URL + movieList.get(position).getPosterPath())
                .placeholder(R.drawable.popcine)
                .error(R.drawable.popcine)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.poster_image)
        ImageView imageView;

        public MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
