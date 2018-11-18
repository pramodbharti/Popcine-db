package com.example.pramod.popcine.model;

import com.example.pramod.popcine.utils.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerResponse {
    @SerializedName(Constants.ID)
    private int id;

    @SerializedName(Constants.YOUTUBE)
    private List<Trailer> youtube;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Trailer> getYoutube() {
        return youtube;
    }

    public void setYoutube(List<Trailer> youtube) {
        this.youtube = youtube;
    }
}
