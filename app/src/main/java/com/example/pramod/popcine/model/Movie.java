package com.example.pramod.popcine.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.pramod.popcine.utils.Constants;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = Constants.TABLE)
public class Movie implements Parcelable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = Constants.ID)
    @SerializedName(Constants.ID)
    private String id ;

    @SerializedName(Constants.TITLE)
    private String title;

    @SerializedName(Constants.RELEASE_DATE)
    private String releaseDate;

    @SerializedName(Constants.VOTE_AVERAGE)
    private String voteAverage;

    @SerializedName(Constants.POSTER_PATH)
    private String posterPath;

    @SerializedName(Constants.BACKDROP_PATH)
    private String backdropPath;

    @SerializedName(Constants.OVERVIEW)
    private String overview;

    public Movie(@NonNull String id, String title, String releaseDate, String voteAverage,
                 String posterPath, String backdropPath, String overview) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.overview = overview;
    }

    // write object to parcel for storage

    public void writeToParcel(Parcel dest, int flags) {

        // Write all properties to the parcel

        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(voteAverage);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(overview);

    }

    // Constructor used for parcel

    public Movie(Parcel parcel) {

        // read and set saved values from parcel

        id = parcel.readString();
        title = parcel.readString();
        releaseDate = parcel.readString();
        voteAverage = parcel.readString();
        posterPath = parcel.readString();
        backdropPath = parcel.readString();
        overview = parcel.readString();
    }


    // creator - used when un-parceling our parcel (creating the object)

    public static final Parcelable.Creator<Movie> CREATOR = new
            Parcelable.Creator<Movie>() {
                @Override
                public Movie createFromParcel(Parcel source) {
                    return new Movie(source);
                }

                @Override
                public Movie[] newArray(int size) {
                    return new Movie[0];
                }
            };

    // return hash code of object

    @Override
    public int describeContents() {
        return hashCode();
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
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
