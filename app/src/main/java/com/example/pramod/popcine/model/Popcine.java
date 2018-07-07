package com.example.pramod.popcine.model;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.pramod.popcine.utils.Constants;
import com.google.gson.annotations.SerializedName;

public class Popcine implements Parcelable {

    @SerializedName(Constants.TITLE)
    private String title;

    @SerializedName(Constants.RELEASE_DATE)
    private String releaseDate;

    @SerializedName(Constants.VOTE_AVERAGE)
    private Double voteAverage;

    @SerializedName(Constants.POSTER_PATH)
    private String posterPath;

    @SerializedName(Constants.BACKDROP_PATH)
    private String backdropPath;

    @SerializedName(Constants.OVERVIEW)
    private String overview;



    public Popcine(String title, String releaseDate, Double voteAverage,
                   String posterPath, String backdropPath, String overview){
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.overview = overview;
    }

    // write object to parcel for storage

    public void writeToParcel(Parcel dest, int flags){

        // Write all properties to the parcel

        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeDouble(voteAverage);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(overview);

    }

    // Constructor used for parcel

    public Popcine(Parcel parcel){

        // read and set saved values from parcel

        title = parcel.readString();
        releaseDate = parcel.readString();
        voteAverage = parcel.readDouble();
        posterPath = parcel.readString();
        backdropPath = parcel.readString();
        overview = parcel.readString();
    }


    // creator - used when un-parceling our parcel (creating the object)

    public static final Parcelable.Creator<Popcine> CREATOR = new
            Parcelable.Creator<Popcine>(){
                @Override
                public Popcine createFromParcel(Parcel source) {
                    return new Popcine(source);
                }

                @Override
                public Popcine[] newArray(int size) {
                    return new Popcine[0];
                }
            };

    // return hash code of object

    @Override
    public int describeContents() {
        return hashCode();
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

}
