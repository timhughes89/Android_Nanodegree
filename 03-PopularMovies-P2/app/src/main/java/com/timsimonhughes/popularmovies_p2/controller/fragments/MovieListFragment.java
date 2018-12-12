package com.timsimonhughes.popularmovies_p2.controller.fragments;

import android.content.res.Configuration;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.timsimonhughes.popularmovies_p2.R;
import com.timsimonhughes.popularmovies_p2.ViewModel.AppExecutors;
import com.timsimonhughes.popularmovies_p2.controller.activities.MainActivity;
import com.timsimonhughes.popularmovies_p2.controller.adapters.MoviesAdapter;
import com.timsimonhughes.popularmovies_p2.database.AppDatabase;

import com.timsimonhughes.popularmovies_p2.model.Movie;
import com.timsimonhughes.popularmovies_p2.model.MoviesResponse;
import com.timsimonhughes.popularmovies_p2.network.MovieDBApiConfig;
import com.timsimonhughes.popularmovies_p2.network.MovieDbApiService;
import com.timsimonhughes.popularmovies_p2.ui.ItemOffsetDecoration;
import com.timsimonhughes.popularmovies_p2.ui.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListFragment extends Fragment implements OnItemClickListener {

    private static final String TAG = MovieListFragment.class.getSimpleName();

    private List<Movie> mMovieList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private View mView;
    private AppDatabase mAppDatabase;
    private MoviesAdapter mMoviesAdapter;
    private ConstraintLayout mFallback;

    public MovieListFragment() {
    }

    public static MovieListFragment newInstance() {
        return new MovieListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mAppDatabase = AppDatabase.getInstance(getContext());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mAppDatabase.favoriteDao().deleteTable();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_movie_list, container, false);
        initViews();
        return mView;
    }

    private void initViews() {

        initSpinner();
        initRecyclerView();
        mProgressBar = mView.findViewById(R.id.pb_movie_list);

    }

    private void initRecyclerView() {
        int spacing = getResources().getDimensionPixelOffset(R.dimen.default_gap);
        mRecyclerView = mView.findViewById(R.id.rv_movie_list);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        }
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.addItemDecoration(new ItemOffsetDecoration(spacing));
    }

    private void updateAdapter(List<Movie> movieList) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mMoviesAdapter = new MoviesAdapter(getContext(), this);
        mRecyclerView.setAdapter(mMoviesAdapter);
        mMoviesAdapter.setOnItemClickListener(this);
        mMoviesAdapter.setMovieList(movieList);
        mMoviesAdapter.notifyDataSetChanged();

        runLayoutAnimation();
    }

    private void runLayoutAnimation() {
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);

        if (mRecyclerView.getAdapter() != null) {
            mRecyclerView.setLayoutAnimation(layoutAnimationController);
            mRecyclerView.scheduleLayoutAnimation();
        }
    }

    private void loadPopularMovies() {
        MovieDbApiService movieDbApiService = MovieDBApiConfig.getClient().create(MovieDbApiService.class);
        Call<MoviesResponse> popularMoviesCall = movieDbApiService.getPopularMovies(MovieDBApiConfig.API_KEY);
        popularMoviesCall.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                mMovieList = response.body().getResults();
                updateAdapter(mMovieList);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.d(TAG, "Failed Retrieving Popular Movies");
            }
        });
    }

    private void loadTopRatedMovies() {
        MovieDbApiService movieDbApiService = MovieDBApiConfig.getClient().create(MovieDbApiService.class);
        Call<MoviesResponse> topRatedCall = movieDbApiService.getTopRatedMovies(MovieDBApiConfig.API_KEY);
        topRatedCall.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                mMovieList = response.body().getResults();
                updateAdapter(mMovieList);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.d(TAG, "Failed Retrieving Top Rated Movies!");
            }
        });
    }

    private void loadFavouriteMovies() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mMovieList = mAppDatabase.favoriteDao().loadAllMovies();

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateAdapter(mMovieList);
                        }
                    });
                }
            }
        });
    }

    private void initSpinner() {
        AppCompatSpinner appCompatSpinner = mView.findViewById(R.id.spinner_movie_list);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        appCompatSpinner.setAdapter(adapter);

        appCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int item = (int) parent.getItemIdAtPosition(position);

                switch (item) {
                    case 0:
                        loadPopularMovies();
                        break;
                    case 1:
                        loadTopRatedMovies();
                        break;
                    case 2:
                        loadFavouriteMovies();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onItemClick(int position, View sharedView, Movie movie) {
        String transitionName = getResources().getString(R.string.transition_name);

        Fragment detailFragment = MovieDetailFragment.newInstance(movie, transitionName);

        if (getFragmentManager() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getFragmentManager()
                        .beginTransaction()
                        .addSharedElement(sharedView, transitionName)
                        .addToBackStack(MainActivity.CURRENT_FRAGMENT)
                        .replace(R.id.container, detailFragment)
                        .commit();
            }
        }

    }
}
