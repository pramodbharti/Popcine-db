package com.example.pramod.popcine.model;

import com.example.pramod.popcine.utils.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Trailer {

    @SerializedName(Constants.TRAILER_NAME)
    private String name;

    @SerializedName(Constants.TRAILER_SOURCE)
    private String source;

    private Trailer() {

    }

    private Trailer(String name, String source) {
        this.name = name;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return source;
    }

}
