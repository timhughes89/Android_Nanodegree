package com.timsimonhughes.a04_bakingapp.ui;

import android.view.View;

import com.timsimonhughes.a04_bakingapp.model.Recipe;

public interface OnItemClickListener {
    void onItemClick(int position, View sharedView, Recipe recipe);
}
