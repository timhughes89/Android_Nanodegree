package com.timsimonhughes.popularmovies_p2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private List<Video> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Video> getVideoResults() {
        return results;
    }

    public void setResult(List<Video> results) {
        this.results = results;
    }
}
