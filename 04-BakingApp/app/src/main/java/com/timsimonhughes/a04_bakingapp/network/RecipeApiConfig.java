package com.timsimonhughes.a04_bakingapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeApiConfig {

//    public static final String BASE_URL = "https://api.myjson.com/bins/";
    public static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
    public static Retrofit retrofit = null;

    public static Retrofit getClient() {

        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
