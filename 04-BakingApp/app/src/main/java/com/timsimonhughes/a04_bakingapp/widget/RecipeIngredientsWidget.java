package com.timsimonhughes.a04_bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RemoteViews;

import com.timsimonhughes.a04_bakingapp.R;
import com.timsimonhughes.a04_bakingapp.controller.MainActivity;
import com.timsimonhughes.a04_bakingapp.model.Ingredient;
import com.timsimonhughes.a04_bakingapp.model.Recipe;

import java.util.HashSet;
import java.util.Set;


public class RecipeIngredientsWidget extends AppWidgetProvider {

    public static final String ACTION_UPDATE_WIDGET_RECIPE = "ACTION_UPDATE_WIDGET_RECIPE";
    private static final String ACTION_CLEAR_WIDGET_RECIPE = "ACTION_CLEAR_WIDGET_RECIPE";
    public static final String EXTRA_RECIPE = "EXTRA_RECIPE";
    private static final String KEY_INGREDIENT_SET = "KEY_INGREDIENT_SET";
    private static final String KEY_RECIPE_NAME = "KEY_RECIPE_NAME";


    private static void updateAppWidget(Context context,
                                        AppWidgetManager appWidgetManager,
                                        int appWidgetId,
                                        Set<String> ingredients,
                                        String name) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe_ingredients);

        if (name == null || ingredients == null) {

            // Name or Ingredients == null show no content placeholder
            views.setViewVisibility(R.id.rv_widget_layout_placeholder, View.VISIBLE);
            views.setViewVisibility(R.id.layout_content, View.GONE);

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.rv_widget_layout_placeholder, pendingIntent);
        } else {

            // Show the views
//            Toast.makeText(context, ingredients.toString(), Toast.LENGTH_LONG).show();
            views.setViewVisibility(R.id.layout_content, View.VISIBLE);
            views.setViewVisibility(R.id.rv_widget_layout_placeholder, View.GONE);

            String[] ingredientsArray = new String[ingredients.size()];
            int i = 0;
            for (String ingredientString : ingredients) {
                ingredientsArray[i++] = ingredientString;
            }

            // Creates an intent that has and String[] extra
            Intent intent = new Intent(context, IngredientsListService.class);
            intent.putExtra(IngredientsListService.EXTRA_INGREDIENTS_LIST, ingredientsArray);

            // Set the widget textView to name & add the intent to the adapter.
            views.setTextViewText(R.id.appwidget_text, name);
            views.setRemoteAdapter(R.id.lv_ingredients, intent);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_ingredients);

            Intent clearIntent = new Intent(context, RecipeIngredientsWidget.class);
            clearIntent.setAction(RecipeIngredientsWidget.ACTION_CLEAR_WIDGET_RECIPE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, clearIntent, 0);

            views.setOnClickPendingIntent(R.id.iv_close, pendingIntent);
        }

        // Tell the widget manager to update the adapter
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        //
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> ingredients = preferences.getStringSet(KEY_INGREDIENT_SET, null);
        String name = preferences.getString(KEY_RECIPE_NAME, null);

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, ingredients, name);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_UPDATE_WIDGET_RECIPE:
                    Recipe recipe = intent.getParcelableExtra(EXTRA_RECIPE);
                    storeRecipeIngredients(context, recipe);
                    requestUpdate(context, recipe);
                    break;
                case ACTION_CLEAR_WIDGET_RECIPE:
                    clearRecipeIngredients(context);
                    requestUpdate(context, null);
                    break;
            }
        }

    }

    private void storeRecipeIngredients(Context context, Recipe recipe) {

        Set<String> ingredients = new HashSet<>();
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            Ingredient ingredient = recipe.getIngredients().get(i);
            String ingredientItem = ingredient.getIngredients();
            ingredients.add(ingredientItem);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_RECIPE_NAME, recipe.getName());
        editor.putStringSet(KEY_INGREDIENT_SET, ingredients);
        editor.apply();
    }

    private void clearRecipeIngredients(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(KEY_RECIPE_NAME);
        editor.remove(KEY_INGREDIENT_SET);
        editor.apply();
    }

    private void requestUpdate(Context context, Recipe recipe) {

        Set<String> ingredients = null;
        String name = null;
        if (recipe != null) {
            ingredients = new HashSet<>();
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredients.add(ingredient.toString());
            }
            name = recipe.getName();
        }

        ComponentName appWidget = new ComponentName(context, RecipeIngredientsWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(appWidget);

        onUpdateWithData(context, appWidgetManager, appWidgetIds, ingredients, name);
    }

    private void onUpdateWithData(Context context,
                                  AppWidgetManager appWidgetManager,
                                  int[] appWidgetIds,
                                  Set<String> ingredients,
                                  String name) {

        // If there are multiple widgets, update them all
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, ingredients, name);
        }
    }
}

