package com.example.nidhij1.flickster.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Movie {

    //values from API
    public String title;
    public String overview;
    public String posterPath; //only the path
    public String backdropPath;
    public Double voteAverage;
    public String releaseDate;
    public Integer id;

    //empty, no-arg constructor
    public Movie() {}

    //initialize from JSON ADATA
    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
        voteAverage = object.getDouble("vote_average");
        releaseDate = object.getString("release_date");
        id = object.getInt("id");
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Integer getId() {
        return id;
    }
}
