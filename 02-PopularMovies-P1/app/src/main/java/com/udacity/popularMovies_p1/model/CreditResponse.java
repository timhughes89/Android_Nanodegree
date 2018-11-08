package com.udacity.popularMovies_p1.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// https://api.themoviedb.org/3/movie/335983/credits?api_key=c9bbb3817e680c9cddfedf98a135de10

public class CreditResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("cast")
    private List<Cast> castResults;
    @SerializedName("crew")
    private List<Crew> crewResults;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Cast> getCastResults() {
        return castResults;
    }

    public void setCastResults(List<Cast> castResults) {
        this.castResults = castResults;
    }

    public List<Crew> getCrewResults() {
        return crewResults;
    }

    public void setCrewResults(List<Crew> crewResults) {
        this.crewResults = crewResults;
    }

}
