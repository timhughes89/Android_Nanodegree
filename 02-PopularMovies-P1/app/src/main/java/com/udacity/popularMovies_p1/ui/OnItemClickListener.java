package com.udacity.popularMovies_p1.ui;

import android.view.View;

import com.udacity.popularMovies_p1.model.Movie;

public interface OnItemClickListener {
    void onItemClick(int position, View sharedView, Movie movie);
}
