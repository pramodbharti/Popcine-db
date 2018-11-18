package com.example.pramod.popcine.network;

import com.example.pramod.popcine.model.MovieResponse;
import com.example.pramod.popcine.model.Review;
import com.example.pramod.popcine.model.ReviewResponse;
import com.example.pramod.popcine.model.TrailerResponse;
import com.example.pramod.popcine.utils.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PopcineApiInterface {
    @GET(Constants.TOP_RATED)
    Call<MovieResponse> getTopRatedMovies(@Query(Constants.API_KEY) String apiKey);

    @GET(Constants.POPULAR)
    Call<MovieResponse> getPopularMovies(@Query(Constants.API_KEY) String apiKey);

    @GET(Constants.REVIEWS)
    Call<ReviewResponse> getReviews(@Query(Constants.API_KEY) String apiKey);

    @GET(Constants.TRAILERS)
    Call<TrailerResponse> getTrailers(@Query(Constants.API_KEY) String apiKey);
}
