package com.udacity.popularMovies_p1.controller.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.udacity.a02_popularmovies_p1.R;
import com.udacity.popularMovies_p1.controller.adapters.ReviewAdapter;
import com.udacity.popularMovies_p1.model.Movie;
import com.udacity.popularMovies_p1.model.Review;
import com.udacity.popularMovies_p1.model.ReviewResponse;
import com.udacity.popularMovies_p1.network.MovieDBApiConfig;
import com.udacity.popularMovies_p1.network.MovieDBApiService;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.udacity.popularMovies_p1.controller.fragments.MovieDetailFragment.EXTRA_MOVIE_ITEM;

public class ReviewFragment extends Fragment {

    private int mMovieId;
    private List<Review> mReviews = new ArrayList<>();
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private TextView mTextViewNoReviews;

    public static ReviewFragment newInstance(Movie movie) {
        ReviewFragment reviewFragment = new ReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_MOVIE_ITEM, movie);
        reviewFragment.setArguments(bundle);
        return reviewFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_reviews, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_reviews);
        mTextViewNoReviews = (TextView) view.findViewById(R.id.tv_noReview_label);

        if (getArguments() != null) {
            Movie movie = getArguments().getParcelable(EXTRA_MOVIE_ITEM);
            mMovieId = movie.getId();
        }

        getMovieReviews(view);

    }

    private void getMovieReviews(final View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieDBApiConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieDBApiService movieDBApiService = retrofit.create(MovieDBApiService.class);
        Call<ReviewResponse> call = movieDBApiService.getReviews(mMovieId, MovieDBApiConfig.API_KEY);
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReviewResponse> call, @NonNull Response<ReviewResponse> response) {
                mReviews = response.body().getResults();
                mProgressBar.setVisibility(View.INVISIBLE);
                initRecyclerView(view);

            }

            @Override
            public void onFailure(@NonNull Call<ReviewResponse> call, @NonNull Throwable t) {

            }
        });


    }

    private void initRecyclerView(View view) {

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_reviews);

        if (mReviews.isEmpty()) {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mTextViewNoReviews.setVisibility(View.VISIBLE);
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(linearLayoutManager);

            ReviewAdapter reviewAdapter = new ReviewAdapter(getContext(), mReviews);
            mRecyclerView.setAdapter(reviewAdapter);
        }

    }
}
