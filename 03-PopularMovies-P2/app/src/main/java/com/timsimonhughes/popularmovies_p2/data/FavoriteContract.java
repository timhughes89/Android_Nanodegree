package com.timsimonhughes.popularmovies_p2.data;

import android.provider.BaseColumns;

public class FavoriteContract {

    public static final class FavoriteEntry implements BaseColumns {

        public static final String TABLE_NAME = "favoritetable";
        public static final String COLUMN_MOVIEID = "movieid";
        public static final String COLUMN_TITLE = "mTextViewMovieTitle";
        public static final String COLUMN_USERRATING = "mTextViewMovieRating";
        public static final String COLUMN_POSTER_PATH = "posterpath";
        public static final String COLUMN_PLOT_SYNOPSIS = "overview";


    }
}
