package com.udacity.popularMovies_p1.controller.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import java.util.List;

import com.udacity.popularMovies_p1.controller.activities.SettingsActivity;
import com.udacity.popularMovies_p1.ui.ItemOffsetDecoration;
import com.udacity.popularMovies_p1.ui.OnItemClickListener;
import com.udacity.popularMovies_p1.controller.adapters.MovieListAdapter;
import com.udacity.popularMovies_p1.model.Movie;
import com.udacity.popularMovies_p1.model.MovieResponse;
import com.udacity.popularMovies_p1.network.MovieDBApiConfig;
import com.udacity.popularMovies_p1.network.MovieDBApiService;
import com.udacity.a02_popularmovies_p1.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieListFragment extends Fragment implements OnItemClickListener {

    private static final String CURRENT_CALL_ID = "CURRENT_CALL_ID";
    private static final String BUNDLE_RECYCLER_LAYOUT = "BASE_FRAGMENT_RECYCLER";

    private List<Movie> mMovieList;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private MovieListAdapter mMovieListAdapter;
    private ProgressBar mProgressBar;
    private Toolbar mToolbar;
    private LayoutAnimationController mLayoutAnimationController;
    private AppCompatActivity mAppCompatActivity;
    private AppCompatSpinner mAppCompatSpinner;
    private int currentCall;
    private int mSpacing;
    private View mView;
    private Parcelable listState;

    private MovieDBApiService mService;

    public MovieListFragment() {
    }

    public static MovieListFragment newInstance() {
        return new MovieListFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable("ListState");
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        destroy();
//    }
//
//    private void destroy() {
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//
//        if (activity != null) {
//            activity.getSupportFragmentManager()
//                    .beginTransaction()
//                    .remove(this)
//                    .commitAllowingStateLoss();
//        }
//    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("ListState", mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_movie_list, container, false);

        mProgressBar = (ProgressBar) mView.findViewById(R.id.pb_movie_list);
        mToolbar = (Toolbar) mView.findViewById(R.id.toolbar);
        mAppCompatActivity = (AppCompatActivity) getActivity();

        if (mAppCompatActivity != null) {
            mAppCompatActivity.getSupportActionBar();
            mAppCompatActivity.setSupportActionBar(mToolbar);
            mAppCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        initRecyclerView();
        initRetrofit(currentCall);
        initSpinner();

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initRetrofit(int callId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieDBApiConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit.create(MovieDBApiService.class);

        callType(callId);
    }


    private void callType(int callId) {
        if (callId == 0) {
            Call<MovieResponse> popularCall = mService.getPopularMovies(MovieDBApiConfig.API_KEY);
            MovieCall(popularCall);

        } else {
            Call<MovieResponse> topRatedCall = mService.getTopRatedMovies(MovieDBApiConfig.API_KEY);
            MovieCall(topRatedCall);
        }
    }

    private void MovieCall(Call<MovieResponse> call) {
        call.enqueue(new Callback<MovieResponse>() {

            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                mMovieList = response.body().getResults();
                updateAdapter(mMovieList);
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
            }
        });
    }

    private void initRecyclerView() {
        mSpacing = getResources().getDimensionPixelOffset(R.dimen.default_gap);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.rv_movie_list);
        mGridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.addItemDecoration(new ItemOffsetDecoration(mSpacing));

    }

    private void updateAdapter(List<Movie> movieList) {

        mProgressBar.setVisibility(View.INVISIBLE);

        mMovieListAdapter = new MovieListAdapter(getContext(), this);
        mRecyclerView.setAdapter(mMovieListAdapter);

        mMovieListAdapter.setOnItemClickListener(this);
        mMovieListAdapter.setMovieList(movieList);
        mMovieListAdapter.notifyDataSetChanged();
        runLayoutAnimation();
    }


    private void runLayoutAnimation() {

        mLayoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_from_bottom);

        if (mRecyclerView.getAdapter() != null) {
            mRecyclerView.setLayoutAnimation(mLayoutAnimationController);
            mRecyclerView.scheduleLayoutAnimation();
        }
    }

    private void initSpinner() {
        mAppCompatSpinner = (AppCompatSpinner) mView.findViewById(R.id.spinner_movie_list);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAppCompatSpinner.setAdapter(adapter);

        mAppCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int item = (int) parent.getItemIdAtPosition(position);

                switch (item) {
                    case 0:
                        currentCall = 0;
                        callType(currentCall);
                        break;
                    case 1:
                        currentCall = 1;
                        callType(currentCall);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
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
                        .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
                        .addToBackStack("BASE")
                        .replace(R.id.container, detailFragment)
                        .commit();
            }
        }
    }
}
