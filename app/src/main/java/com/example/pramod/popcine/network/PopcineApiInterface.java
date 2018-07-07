package com.example.pramod.popcine.network;

import com.example.pramod.popcine.model.PopcineResponse;
import com.example.pramod.popcine.utils.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PopcineApiInterface {
    @GET(Constants.TOP_RATED)
    Call<PopcineResponse> getTopRatedMovies(@Query(Constants.API_KEY) String apiKey);

    @GET(Constants.POPULAR)
    Call<PopcineResponse> getPopularMovies(@Query(Constants.API_KEY) String apiKey);

}
