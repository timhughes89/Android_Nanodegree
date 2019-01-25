package com.timsimonhughes.a04_bakingapp.controller.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.timsimonhughes.a04_bakingapp.R;
import com.timsimonhughes.a04_bakingapp.controller.adapters.IngredientAdapter;
import com.timsimonhughes.a04_bakingapp.model.Ingredient;
import com.timsimonhughes.a04_bakingapp.model.Recipe;
import com.timsimonhughes.a04_bakingapp.utils.Constants;
import com.timsimonhughes.a04_bakingapp.utils.ImageUtils;


import java.util.List;

public class RecipeOverviewFragment extends Fragment {

    private Recipe recipe;
    private int recipeId;
    private String recipeName;
    private String recipeImageUri;
    private String recipeFallbackImage;
    private double recipeServings;

    private List<Ingredient> recipeIngredientList;

    public RecipeOverviewFragment() {}

    public static RecipeOverviewFragment newInstance(Recipe recipe) {
        RecipeOverviewFragment recipeIntroFragment = new RecipeOverviewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.EXTRA_RECIPE_ITEM, recipe);
        recipeIntroFragment.setArguments(bundle);
        return recipeIntroFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_recipe_overview, container, false);

        if (getArguments() != null) {
            recipe = getArguments().getParcelable(Constants.EXTRA_RECIPE_ITEM);

            if (recipe != null) {

                // Recipe Values
                recipeId = recipe.getId();
                recipeName = recipe.getName();
                recipeImageUri = recipe.getImage();
                recipeServings = recipe.getServings();
                recipeIngredientList = recipe.getIngredients();

                // Set fallback image for the imageView, if the recipeImageUrl is empty.
                if (getContext() != null && recipeImageUri.isEmpty()) {
                    recipeFallbackImage = getContext().getResources().getStringArray(R.array.recipe_image_fallbacks)[recipeId - 1];
                }

                // Fragment Views
                ImageView recipeImageView = view.findViewById(R.id.iv_recipe_image_detail);
                TextView textViewRecipeName = view.findViewById(R.id.tv_recipe_title_detail);
                TextView textViewRecipeServing = view.findViewById(R.id.tv_recipe_serving_detail);
                RecyclerView ingredientRecyclerView = view.findViewById(R.id.rv_ingredients);

                if (recipeImageUri.isEmpty()) {
                    ImageUtils.loadImage(getContext(), recipeFallbackImage, recipeImageView);
                } else {
                    ImageUtils.loadImage(getContext(), recipeImageUri, recipeImageView);
                }

                ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                IngredientAdapter ingredientAdapter = new IngredientAdapter(getContext());
                ingredientAdapter.setIngredientList(recipeIngredientList);
                ingredientRecyclerView.setAdapter(ingredientAdapter);

                textViewRecipeName.setText(recipeName);
                textViewRecipeServing.setText(String.valueOf(recipeServings));

            }
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
