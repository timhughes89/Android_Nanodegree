package com.timsimonhughes.a04_bakingapp.utils;

import com.timsimonhughes.a04_bakingapp.controller.MainActivity;
import com.timsimonhughes.a04_bakingapp.controller.fragments.DetailFragment;
import com.timsimonhughes.a04_bakingapp.controller.fragments.ListFragment;
import com.timsimonhughes.a04_bakingapp.controller.fragments.RecipeOverviewFragment;
import com.timsimonhughes.a04_bakingapp.controller.fragments.StepFragment;

public class Constants {

    // App Constants
    public static final String APP_PACKAGE_NAME = "com.timsimonhughes.a04_bakingapp";

    // Json Constants
    public static final String KEY_RECIPE_JSON = "baking.json";
    public static final String KEY_CHARSET = "UTF-8";
    public static final String KEY_RECIPES = "recipes";

    // Recipe Constants
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_INGREDIENTS_OBJECT = "ingredients";
    public static final String KEY_STEPS_OBJECT = "steps";
    public static final String KEY_SERVINGS = "servings";
    public static final String KEY_IMAGE = "image";

    // Ingredient Constants
    public static final String KEY_INGREDIENTS_QUANTITY = "quantity";
    public static final String KEY_INGREDIENTS_MEASURE = "measure";
    public static final String KEY_INGREDIENTS_INGREDIENT = "ingredient";

    // Step Constants
    public static final String KEY_STEP_ID = "id";
    public static final String KEY_STEP_SHORT_DESCRIPTION = "shortDescription";
    public static final String KEY_STEP_DESCRIPTION = "description";
    public static final String KEY_STEP_VIDEO_URL = "videoURL";
    public static final String KEY_STEP_THUMBNAIL_URL = "thumbnailURL";

    // Extra Constants
    public static final String EXTRA_RECIPE_ITEM = "recipe_item";
    public static final String EXTRA_TRANSITION_ITEM = "recipe_transition_item";
    public static final String EXTRA_STEP_ITEM = "step_item";

    // Fragment Constants
    public static final String CURRENT_FRAGMENT = "BASE";

    // Tag Constants
    public static final String TAG_MAIN_ACTIVITY = MainActivity.class.getSimpleName();
    public static final String TAG_RECIPE_LIST = ListFragment.class.getSimpleName();
    public static final String TAG_RECIPE_DETAIL = DetailFragment.class.getSimpleName();
    public static final String TAG_RECIPE_OVERVIEW = RecipeOverviewFragment.class.getSimpleName();
    public static final String TAG_RECIPE_STEP = StepFragment.class.getSimpleName();

    // ExoPlayer Constants
    public static final String KEY_PREVIOUS_ACTIVE_POSITION = "KEY_PREVIOUS_ACTIVE_POSITION";
    public static final String KEY_PLAYER_POSITION = "KEY_PLAYER_POSITION";
    public static final String KEY_PLAY_WHEN_READY = "KEY_PLAY_WHEN_READY";
}


