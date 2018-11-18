package com.example.pramod.popcine.model;

import com.example.pramod.popcine.utils.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponse {
    @SerializedName(Constants.PAGE)
    private int page;

    @SerializedName(Constants.RESULTS)
    private List<Movie> results;

    @SerializedName(Constants.TOTAL_PAGES)
    private int totalPages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
