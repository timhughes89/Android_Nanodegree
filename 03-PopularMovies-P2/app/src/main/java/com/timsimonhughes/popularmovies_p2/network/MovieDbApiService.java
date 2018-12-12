package com.timsimonhughes.popularmovies_p2.network;

import com.timsimonhughes.popularmovies_p2.model.MoviesResponse;
import com.timsimonhughes.popularmovies_p2.model.ReviewResponse;
import com.timsimonhughes.popularmovies_p2.model.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDbApiService {

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}/similar")
    Call<MoviesResponse> getSimilarMovies(@Path("movie_id") int movie_id, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/credits")
    Call<MoviesResponse> getCredits(@Path("movie_id") int movie_id, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewResponse> getReviews(@Path("movie_id") int movie_id, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Call<VideoResponse> getVideos(@Path("movie_id") int movie_id, @Query("api_key") String apiKey);

    @GET("genre/movie/list")
    Call<MoviesResponse> getGenres(@Query("api_key") String apiKey);
}
