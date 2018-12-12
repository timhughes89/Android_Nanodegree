package com.timsimonhughes.popularmovies_p2.controller.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.timsimonhughes.popularmovies_p2.R;
import com.timsimonhughes.popularmovies_p2.ViewModel.AppExecutors;
import com.timsimonhughes.popularmovies_p2.controller.adapters.ReviewAdapter;
import com.timsimonhughes.popularmovies_p2.controller.adapters.VideoAdapter;
import com.timsimonhughes.popularmovies_p2.database.AppDatabase;
import com.timsimonhughes.popularmovies_p2.model.Movie;
import com.timsimonhughes.popularmovies_p2.model.Review;
import com.timsimonhughes.popularmovies_p2.model.ReviewResponse;
import com.timsimonhughes.popularmovies_p2.model.Video;
import com.timsimonhughes.popularmovies_p2.model.VideoResponse;
import com.timsimonhughes.popularmovies_p2.network.MovieDBApiConfig;
import com.timsimonhughes.popularmovies_p2.network.MovieDbApiService;
import com.timsimonhughes.popularmovies_p2.ui.ItemOffsetDecoration;
import com.timsimonhughes.popularmovies_p2.utils.AnimationUtils;
import com.timsimonhughes.popularmovies_p2.utils.DateUtils;
import com.timsimonhughes.popularmovies_p2.utils.LanguageUtils;
import com.timsimonhughes.popularmovies_p2.utils.MovieUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class MovieDetailFragment extends Fragment {

    public static final String EXTRA_MOVIE_ITEM = "EXTRA_MOVIE_ITEM";
    private static final String EXTRA_DB_ITEM = "EXTRA_DB_ITEM";
    public static final String EXTRA_TRANSITION_NAME = "EXTRA_TRANSITION_NAME";

    private Context mContext;

    private AppDatabase mAppDatabase;
    private ImageView mImageViewMoviePoster, mImageViewMovieBackdrop;
    private TextView mTextViewMovieTitle, mTextViewMovieOverview, mTextViewMovieReleaseDate, mTextViewUserRating, mTextViewMoviePosterUrl, mTextViewOriginalTitle, mTextViewOriginalLanguage, mTextViewVoteCount;
    private String movieTitle, movieOverview, movieReleasedate, movieUserRating, moviePosterUrl, movieReview, movieBackdropUrl, movieOriginalTitle, movieOriginalLanguage;
    private int movieId;
    private String movieVoteCount;
    private Movie movieFromDb;
    private View mView;
    private Movie mMovie;
    private MenuItem mMenuItem;
    private boolean mFavorite, movieAdult;
    private String moviePopularity;
    private List<Integer> mMovieGenreIds;
    private LinearLayout mLinearLayoutTags, mLinearLayoutAdult;


    private RecyclerView mRecyclerViewVideos, mRecyclerViewReviews;

    private List<Review> mReviewList;
    private List<Video> mVideoList;

    public MovieDetailFragment() {
    }

    public static MovieDetailFragment newInstance(Movie movie, String transitionName) {
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_MOVIE_ITEM, movie);
        bundle.putString(EXTRA_TRANSITION_NAME, transitionName);
        movieDetailFragment.setArguments(bundle);
        return movieDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mAppDatabase = AppDatabase.getInstance(getContext());

        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkFavourite(movieId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_movie_detail, parent, false);
        initViews();
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_MOVIE_ITEM, mMovie);
        outState.putParcelable(EXTRA_DB_ITEM, movieFromDb);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable(EXTRA_MOVIE_ITEM);
            movieFromDb = savedInstanceState.getParcelable(EXTRA_DB_ITEM);
        }
    }

    private void initViews() {

        initToolbar(mView);
        initRecyclerView(mView);

        mImageViewMoviePoster = mView.findViewById(R.id.iv_movie_poster);
        mImageViewMovieBackdrop = mView.findViewById(R.id.iv_movie_backdrop);
        mTextViewMovieOverview = mView.findViewById(R.id.tv_overview);
        mTextViewMovieTitle = mView.findViewById(R.id.tv_game_title);
        mTextViewMovieReleaseDate = mView.findViewById(R.id.tv_release_date);
        mTextViewUserRating = mView.findViewById(R.id.tv_user_rating);
        mTextViewVoteCount = mView.findViewById(R.id.tv_movie_popularity);
        mTextViewOriginalTitle = mView.findViewById(R.id.tv_original_title);
        mTextViewOriginalLanguage = mView.findViewById(R.id.tv_original_language);

        mLinearLayoutTags = mView.findViewById(R.id.ll_tags);
        mLinearLayoutAdult = mView.findViewById(R.id.ll_adult);


        if (getArguments() != null) {
            mMovie = getArguments().getParcelable(EXTRA_MOVIE_ITEM);
            String transitionName = getArguments().getString(EXTRA_TRANSITION_NAME);

            if (mMovie != null) {
                movieId = mMovie.getId();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mImageViewMoviePoster.setTransitionName(transitionName);
                }

                moviePosterUrl = MovieUtils.buildPosterUrl(MovieUtils.API_POSTER_SIZE_W342, mMovie.getPosterpath());
                movieBackdropUrl = MovieUtils.buildPosterUrl(MovieUtils.API_POSTER_SIZE_W780, mMovie.getBackdropPath());
                movieTitle = mMovie.getTitle();
                movieOverview = mMovie.getOverview();
                movieReleasedate = DateUtils.formatDate(mMovie.getReleaseDate());
                movieUserRating = Double.toString(mMovie.getVoteAverage());
                movieOriginalTitle = mMovie.getOriginalTitle();
                movieOriginalLanguage = LanguageUtils.formatLanguageAbbreviation(mMovie.getOriginalLanguage());
                moviePopularity = Double.toString(mMovie.getPopularity());
                movieVoteCount = Integer.toString(mMovie.getVoteCount());
                movieAdult = mMovie.isAdult();
                mMovieGenreIds = mMovie.getGenreIds();

                for (int i = 0; i < mMovieGenreIds.size(); i++) {
                    String tag = String.valueOf(mMovieGenreIds.get(i));

                    String formattedTag = MovieUtils.buildFormattedGenre(tag);

                    if (getActivity() != null) {
                        TextView textView = new TextView(getActivity());
                        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        textView.setPadding(0, 0, 24, 0);
                        textView.setLayoutParams(layoutParams);
                        textView.setText(formattedTag);
                        textView.setTextColor(getResources().getColor(R.color.colorAccent));
                        mLinearLayoutTags.addView(textView);
                    }
                }
            }
        }

        Glide.with(getContext())
                .load(movieBackdropUrl)
                .into(mImageViewMovieBackdrop);

        Glide.with(getContext())
                .load(moviePosterUrl)
                .into(mImageViewMoviePoster);

        mTextViewMovieOverview.setText(movieOverview);
        mTextViewMovieTitle.setText(movieTitle);
        mTextViewMovieReleaseDate.setText(movieReleasedate);
        mTextViewUserRating.setText(movieUserRating);
        mTextViewOriginalTitle.setText(movieOriginalTitle);
        mTextViewOriginalLanguage.setText(movieOriginalLanguage);
        mTextViewVoteCount.setText(movieVoteCount);

        if (!movieAdult) {
            mLinearLayoutAdult.setVisibility(View.GONE);
        }

        checkFavourite(movieId);
        delayBackdropLoad();
        getTrailerVideos();
        getMovieReviews();

    }

    private void initRecyclerView(View view) {
        int spacing = getResources().getDimensionPixelOffset(R.dimen.spacing_X1);
        mRecyclerViewVideos = mView.findViewById(R.id.rv_videos);
        mRecyclerViewVideos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewVideos.setItemViewCacheSize(20);
        mRecyclerViewVideos.addItemDecoration(new ItemOffsetDecoration(spacing));

        mRecyclerViewReviews = mView.findViewById(R.id.rv_reviews);
        mRecyclerViewReviews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerViewReviews.setItemViewCacheSize(20);
        mRecyclerViewReviews.addItemDecoration(new ItemOffsetDecoration(spacing));
    }

    private void initToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);

        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();

        if (appCompatActivity != null) {
            appCompatActivity.getSupportActionBar();
            appCompatActivity.setSupportActionBar(toolbar);
        }

        collapsingToolbarLayout.setTitleEnabled(false);
        collapsingToolbarLayout.setTitle(null);
        toolbar.setTitle(null);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String transitionName = getResources().getString(R.string.transition_name);

                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });
    }

    private void getMovieReviews() {

        MovieDBApiConfig movieDBApiConfig = new MovieDBApiConfig();
        MovieDbApiService movieDbApiService = MovieDBApiConfig.getClient().create(MovieDbApiService.class);
        Call<ReviewResponse> movieReviewCall = movieDbApiService.getReviews(movieId, movieDBApiConfig.API_KEY);
        movieReviewCall.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.body() != null) {
                    mReviewList = response.body().getReviewResults();

                    if (mReviewList.isEmpty()) {
                        TextView reviewLabel = mView.findViewById(R.id.tv_reviews_label);
                        reviewLabel.setVisibility(View.GONE);
                    } else {
                        ReviewAdapter reviewAdapter = new ReviewAdapter(getContext(), mReviewList);
                        mRecyclerViewReviews.setAdapter(reviewAdapter);
                        reviewAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Log.d(TAG, "Error retrieving Movie Reviews");
            }
        });
    }

    private void getTrailerVideos() {
        MovieDBApiConfig movieDBApiConfig = new MovieDBApiConfig();
        MovieDbApiService movieDbApiService = MovieDBApiConfig.getClient().create(MovieDbApiService.class);
        Call<VideoResponse> movieVideoCall = movieDbApiService.getVideos(movieId, movieDBApiConfig.API_KEY);
        movieVideoCall.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if (response.body() != null) {
                    mVideoList = response.body().getVideoResults();

                    if (mVideoList.isEmpty()) {
                        TextView movieVideoLabel = mView.findViewById(R.id.tv_video_label);
                        movieVideoLabel.setVisibility(View.GONE);
                    } else {
                        VideoAdapter videoAdapter = new VideoAdapter(getContext(), mVideoList);
                        mRecyclerViewVideos.setAdapter(videoAdapter);
                        videoAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                Log.d(TAG, "Error retrieving Movie Trailers");
            }
        });
    }

    public void saveFavourite() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mAppDatabase.favoriteDao().insertFavorite(mMovie);
            }
        });
    }

    public void deleteFavourite(final int movieId) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mAppDatabase.favoriteDao().deleteFavoriteWithId(movieId);
            }
        });
    }

    public void checkFavourite(final int movieId) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                movieFromDb = mAppDatabase.favoriteDao().loadTaskById(movieId);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (movieFromDb != null) {
                                mMenuItem.setIcon(getResources().getDrawable(R.drawable.ic_favorite));
                                mFavorite = true;
                            } else {
                                mMenuItem.setIcon(getResources().getDrawable(R.drawable.ic_favorite_border));
                                mFavorite = false;
                            }
                        }
                    });
                }

            }
        });
    }

    private void delayBackdropLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationUtils.createCircularReveal(mImageViewMovieBackdrop);
            }
        }, 500);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);

        mMenuItem = menu.getItem(0);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_favourite:

                if (!mFavorite) {
                    item.setIcon(getResources().getDrawable(R.drawable.ic_favorite));
                    showSnackbar(mView, getResources().getString(R.string.add_to_favourites_label));
                    mFavorite = true;
                    saveFavourite();

                } else {
                    item.setIcon(getResources().getDrawable(R.drawable.ic_favorite_border));
                    showSnackbar(mView, getResources().getString(R.string.removed_from_favourites_label));
                    mFavorite = false;
                    deleteFavourite(movieId);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showSnackbar(View rootView, String message) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        view.setBackgroundColor(view.getContext().getResources().getColor(R.color.colorPrimaryDark));
//        TextView textView = view.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }
}
