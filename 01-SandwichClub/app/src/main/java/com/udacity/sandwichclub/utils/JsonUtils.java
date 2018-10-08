package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    private static final String KEY_NAME = "name";
    private static final String KEY_MAIN_NAME = "mainName";
    private static final String KEY_ALSO_KNOWN_AS = "alsoKnownAs";
    private static final String KEY_PLACE_OF_ORIGIN = "placeOfOrigin";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_INGREDIENTS = "ingredients";

    public static Sandwich parseSandwichJson(String json) {

        Sandwich sandwich = null;

        try {
            JSONObject sandwichJson = new JSONObject(json);
            sandwich = new Sandwich(
                    sandwichJson.getJSONObject(KEY_NAME).getString(KEY_MAIN_NAME),
                    getAlsoKnownAs(sandwichJson),
                    sandwichJson.optString(KEY_PLACE_OF_ORIGIN),
                    sandwichJson.optString(KEY_DESCRIPTION),
                    sandwichJson.optString(KEY_IMAGE),
                    getIngredients(sandwichJson)
            );

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        try {
//            JSONObject sandwichJson = new JSONObject(json);
//
//            JSONObject nameJSONObject = sandwichJson.getJSONObject(KEY_NAME);
//            String name = nameJSONObject.getString(KEY_MAIN_NAME);
//
//            JSONArray alsoKnownAs = nameJSONObject.getJSONArray(KEY_ALSO_KNOWN_AS);
//            List<String> alsoKnownAsList = new ArrayList<>();
//            for (int i = 0; i < alsoKnownAs.length(); i++) {
//                alsoKnownAsList.add(alsoKnownAs.getString(i));
//            }
//
//            String origin = sandwichJson.getString(KEY_PLACE_OF_ORIGIN);
//            String description = sandwichJson.getString(KEY_DESCRIPTION);
//            String image = sandwichJson.getString(KEY_IMAGE);
//
//            JSONArray ingredients = sandwichJson.getJSONArray(KEY_INGREDIENTS);
//            List<String> ingredientsList = new ArrayList<>();
//            for (int i = 0; i < ingredients.length(); i++) {
//                ingredientsList.add(ingredients.getString(i));
//            }
//
//            sandwich = new Sandwich(name, alsoKnownAsList, origin, description, image, ingredientsList);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        return sandwich;

    }


    private static List<String> getAlsoKnownAs(JSONObject sandwichJson) throws JSONException {
        JSONArray alsoKnowAsJson = sandwichJson.getJSONObject(KEY_NAME).getJSONArray(KEY_ALSO_KNOWN_AS);
        return jsonArrayToList(alsoKnowAsJson);
    }

    private static List<String> getIngredients(JSONObject sandwichJson) throws JSONException {
        JSONArray ingredientsJson = sandwichJson.getJSONArray(KEY_INGREDIENTS);
        return jsonArrayToList(ingredientsJson);
    }

    private static List<String> jsonArrayToList(JSONArray array) {
        List<String> arrayList = new ArrayList<>();
        for (int i = 0; i <array.length(); i++) {
            arrayList.add(array.optString(i));
        }
        return arrayList;
    }

}
