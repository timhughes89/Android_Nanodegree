package com.timsimonhughes.a04_bakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class IngredientsListService extends RemoteViewsService {

    public static final String EXTRA_INGREDIENTS_LIST = "EXTRA_INGREDIENTS_LIST";
    
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        String[] ingredients = intent.getStringArrayExtra(EXTRA_INGREDIENTS_LIST);
        return new IngredientsListFactory(getApplicationContext(), ingredients);
    }
}

