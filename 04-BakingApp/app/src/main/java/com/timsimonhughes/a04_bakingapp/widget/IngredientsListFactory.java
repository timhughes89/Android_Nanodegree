package com.timsimonhughes.a04_bakingapp.widget;

import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.timsimonhughes.a04_bakingapp.R;

class IngredientsListFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = IngredientsListService.class.getSimpleName();
    private final String[] ingredients;
    private final Context context;

    public IngredientsListFactory(Context context, String[] ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public int getCount() {
        Log.d(TAG, "getCount: " + (ingredients == null ? 0 : ingredients.length));
        return (ingredients == null ? 0 : ingredients.length);
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d(TAG, "getViewAt: " + position);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.list_item_widget_ingredient);
        views.setTextViewText(R.id.tv_ingredient, ingredients[position]);
        return views;
    }

    @Override
    public void onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged: ");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public RemoteViews getLoadingView() {
        Log.d(TAG, "getLoadingView: ");
        return null;
    }

    @Override
    public int getViewTypeCount() {
        Log.d(TAG, "getViewTypeCount: ");
        return 1;
    }

    @Override
    public long getItemId(int position) {
        Log.d(TAG, "getItemId: ");
        return position;
    }

    @Override
    public boolean hasStableIds() {
        Log.d(TAG, "hasStableIds: ");
        return true;
    }


}
