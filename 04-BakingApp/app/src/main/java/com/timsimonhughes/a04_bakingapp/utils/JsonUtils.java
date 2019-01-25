package com.timsimonhughes.a04_bakingapp.utils;

import com.timsimonhughes.a04_bakingapp.model.Ingredient;
import com.timsimonhughes.a04_bakingapp.model.Recipe;
import com.timsimonhughes.a04_bakingapp.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Recipe parseBakingJsonFromAssets(String json) {
        Recipe recipe = null;

        try {
            JSONObject bakingJson = new JSONObject(json);
            recipe = new Recipe(
                    bakingJson.optInt(Constants.KEY_ID),
                    bakingJson.optString(Constants.KEY_NAME),
                    getIngredients(bakingJson),
                    getSteps(bakingJson),
                    bakingJson.optInt(Constants.KEY_SERVINGS),
                    bakingJson.optString(Constants.KEY_IMAGE)
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipe;
    }

    private static List<Ingredient> getIngredients(JSONObject jsonArray) throws JSONException {
        JSONArray ingredientsListArray = jsonArray.getJSONArray(Constants.KEY_INGREDIENTS_OBJECT);

        List<Ingredient> ingredientsList = new ArrayList<>();
        for (int i = 0; i < ingredientsListArray.length(); i++) {
            JSONObject ingredientObject = ingredientsListArray.getJSONObject(i);

            double quantity = ingredientObject.getDouble(Constants.KEY_INGREDIENTS_QUANTITY);
            String measure = ingredientObject.getString(Constants.KEY_INGREDIENTS_MEASURE);
            String ingredients = ingredientObject.getString(Constants.KEY_INGREDIENTS_INGREDIENT);

            Ingredient ingredient = new Ingredient(quantity, measure, ingredients);
            ingredientsList.add(ingredient);
        }
        return ingredientsList;
    }

    private static List<Step> getSteps(JSONObject jsonArray) throws JSONException {
        JSONArray stepsListArray = jsonArray.getJSONArray(Constants.KEY_STEPS_OBJECT);

        List<Step> stepsList = new ArrayList<>();
        for (int i = 0; i < stepsListArray.length(); i++) {
            JSONObject stepsObject = stepsListArray.getJSONObject(i);

            int stepId = stepsObject.getInt(Constants.KEY_STEP_ID);
            String shortDescription = stepsObject.getString(Constants.KEY_STEP_SHORT_DESCRIPTION);
            String description = stepsObject.getString(Constants.KEY_STEP_DESCRIPTION);
            String videoUrl = stepsObject.getString(Constants.KEY_STEP_VIDEO_URL);
            String thumbnailUrl = stepsObject.getString(Constants.KEY_STEP_THUMBNAIL_URL);

            Step step = new Step(stepId, shortDescription, description, videoUrl, thumbnailUrl);

            stepsList.add(step);
        }
        return stepsList;
    }

    public static List<String> getJsonRecipesListFromAssets(String json) {
        List<String> recipeList = new ArrayList<>();

        try {
            JSONObject recipesJsonObject = new JSONObject(json);
            JSONArray recipesJsonArray = recipesJsonObject.getJSONArray(Constants.KEY_RECIPES);

            for (int i = 0; i < recipesJsonArray.length(); i++) {
                String recipe = recipesJsonArray.get(i).toString();
                recipeList.add(recipe);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipeList;
    }

}
