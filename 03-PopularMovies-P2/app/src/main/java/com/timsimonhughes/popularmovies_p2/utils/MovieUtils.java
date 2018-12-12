package com.timsimonhughes.popularmovies_p2.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class MovieUtils {

//    https://image.tmdb.org/t/p/w780/2uNW4WbgBXL25BAbXGLnLqX71Sw.jpg

    public static final String API_POSTER_BASE_URL = "https://image.tmdb.org/t/p/";
    public static final String API_POSTER_SIZE_ORIGINAL = "original";
    public static final String API_POSTER_SIZE_W92 = "w92";
    public static final String API_POSTER_SIZE_W154 = "w154";
    public static final String API_POSTER_SIZE_W185 = "w185";
    public static final String API_POSTER_SIZE_W342 = "w342";
    public static final String API_POSTER_SIZE_W500 = "w500";
    public static final String API_POSTER_SIZE_W780 = "w780";

    private static final String GENRE_ACTION = "28";
    private static final String GENRE_ADVENTURE = "12";
    private static final String GENRE_ANIMATION = "16";
    private static final String GENRE_COMEDY = "35";
    private static final String GENRE_CRIME = "80";
    private static final String GENRE_DOCUMENTARY = "99";
    private static final String GENRE_DRAMA = "18";
    private static final String GENRE_FAMILY = "10751";
    private static final String GENRE_HISTORY = "36";
    private static final String GENRE_HORROR = "27";
    private static final String GENRE_MUSIC = "10402";
    private static final String GENRE_MYSTERY = "9648";
    private static final String GENRE_ROMANCE = "10749";
    private static final String GENRE_SCI_FI = "878";
    private static final String GENRE_TV_MOVIE = "10770";
    private static final String GENRE_THRILLER = "53";
    private static final String GENRE_WAR = "10752";
    private static final String GENRE_WESTERN = "37";
    private static final String GENRE_FANTASY = "14";


    public static String buildPosterUrl(String suffix) {
        return buildPosterUrl(suffix, API_POSTER_SIZE_ORIGINAL);
    }

    public static String buildPosterUrl(String size, String url) {
        return API_POSTER_BASE_URL + size + url;
    }

    public static void launchVideo(Context context, String id) {
        Intent openInAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent openInBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));

        try {
            context.startActivity(openInAppIntent);
        } catch (ActivityNotFoundException e) {
            context.startActivity(openInBrowserIntent);
        }
    }

    public static String buildFormattedGenre(String genre) {

        switch (genre) {
            case GENRE_ACTION:
                genre = "Action";
                break;
            case GENRE_ADVENTURE:
                genre = "Adventure";
                break;
            case GENRE_ANIMATION:
                genre = "Animation";
                break;
            case GENRE_COMEDY:
                genre = "Comedy";
                break;
            case GENRE_CRIME:
                genre = "Crime";
                break;
            case GENRE_DOCUMENTARY:
                genre = "Documentary";
                break;
            case GENRE_DRAMA:
                genre = "Drama";
                break;
            case GENRE_FAMILY:
                genre = "Family";
                break;
            case GENRE_HISTORY:
                genre = "History";
                break;
            case GENRE_HORROR:
                genre = "Horror";
                break;
            case GENRE_MUSIC:
                genre = "Music";
                break;
            case GENRE_MYSTERY:
                genre = "Mystery";
                break;
            case GENRE_ROMANCE:
                genre = "Romance";
                break;
            case GENRE_SCI_FI:
                genre = "Sci-Fi";
                break;
            case GENRE_TV_MOVIE:
                genre = "TV Movie";
                break;
            case GENRE_THRILLER:
                genre = "Thriller";
                break;
            case GENRE_WAR:
                genre = "War";
                break;
            case GENRE_WESTERN:
                genre = "Western";
                break;

            case GENRE_FANTASY:
                genre = "Fantasy";
                break;
        }
        return genre;
    }

}

