package com.udacity.popularMovies_p1.network;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDBApiConfig {

    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String API_KEY = "INSERT API KEY HERE";

    public static Retrofit newInstance() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieDBApiService service = retrofit.create(MovieDBApiService.class);
        return retrofit;
    }
}
