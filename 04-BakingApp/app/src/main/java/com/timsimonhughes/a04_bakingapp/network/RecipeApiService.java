package com.timsimonhughes.a04_bakingapp.network;


import com.timsimonhughes.a04_bakingapp.model.Recipe;

import java.util.List;


import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeApiService {

    //@GET("l5bzk")
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getAllRecipes();

}
