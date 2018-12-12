package com.timsimonhughes.popularmovies_p2.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.timsimonhughes.popularmovies_p2.database.AppDatabase;
import com.timsimonhughes.popularmovies_p2.model.Movie;

public class AddFavoriteViewModel extends ViewModel {

    private LiveData<Movie> favorite;

    public AddFavoriteViewModel(AppDatabase database, int favoriteId) {
//        favorite = database.favoriteDao().loadFavoriteById(favoriteId);
    }

    public LiveData<Movie> getFavorite() {
        return favorite;
    }
}
