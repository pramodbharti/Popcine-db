package com.example.pramod.popcine.model;

import com.example.pramod.popcine.utils.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewResponse {
    @SerializedName(Constants.ID)
    private int id;

    @SerializedName(Constants.RESULTS)
    private List<Review> results;

    @SerializedName(Constants.PAGE)
    private int page;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
