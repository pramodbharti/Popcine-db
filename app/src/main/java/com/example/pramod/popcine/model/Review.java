package com.example.pramod.popcine.model;

import com.example.pramod.popcine.utils.Constants;
import com.google.gson.annotations.SerializedName;

public class Review{

    @SerializedName(Constants.REVIEW_AUTHOR)
    private String author;

    @SerializedName(Constants.REVIEW_CONTENT)
    private String content;

    private Review() {
    }

    public Review(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
