package com.example.pramod.popcine.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pramod.popcine.R;
import com.example.pramod.popcine.model.Trailer;
import com.example.pramod.popcine.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private List<Trailer> trailerList;
    private Context context;

    public TrailerAdapter(List<Trailer> trailerList, Context context) {
        this.trailerList = trailerList;
        this.context = context;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrailerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.trailers, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {

        holder.trailerName.setText(trailerList.get(position).getName());

        final String videoUrl = Constants.TRAILER_BASE + trailerList.get(position).getKey();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent videoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                videoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(videoIntent);
            }
        });
    }

    public static class TrailerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.trailer_name)
        TextView trailerName;

        public TrailerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }
}
