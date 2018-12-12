package com.timsimonhughes.popularmovies_p2.ui;

import android.view.View;

import com.timsimonhughes.popularmovies_p2.model.Movie;

public interface OnItemClickListener {
    void onItemClick(int position, View sharedView, Movie movie);
}
