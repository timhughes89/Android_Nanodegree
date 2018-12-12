package com.timsimonhughes.popularmovies_p2.network;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDBApiConfig {

    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String API_KEY = "API_KEY";

    public static Retrofit retrofit = null;


    public static Retrofit getClient(){

        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
