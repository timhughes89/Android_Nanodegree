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
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

import com.udacity.a02_popularmovies_p1.R;
import com.udacity.popularMovies_p1.controller.adapters.MovieImagesAdapter;
import com.udacity.popularMovies_p1.model.Movie;
import com.udacity.popularMovies_p1.model.MovieResponse;
import com.udacity.popularMovies_p1.network.MovieDBApiConfig;
import com.udacity.popularMovies_p1.network.MovieDBApiService;
import com.udacity.popularMovies_p1.ui.ItemOffsetDecoration;
import com.udacity.popularMovies_p1.utils.DateUtils;
import com.udacity.popularMovies_p1.utils.LanguageUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.udacity.popularMovies_p1.controller.fragments.MovieDetailFragment.EXTRA_MOVIE_ITEM;

public class OverviewFragment extends Fragment {

    private List<Movie> mSimilarList = new ArrayList<>();
    private RecyclerView mRecyclerView;

    private TextView mTextViewDetailOverview;
    private TextView mTextViewReleaseDate;
    private TextView mTextViewOriginalTitle;
    private TextView mTextViewLanguage;
    private LinearLayout mLinearLayoutSimilarMovies;

    private Movie mMovie;
    private int mMovieId;

    public static OverviewFragment newInstance(Movie movie) {
        OverviewFragment overviewFragment = new OverviewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_MOVIE_ITEM, movie);
        overviewFragment.setArguments(bundle);
        return overviewFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {

            mMovie = getArguments().getParcelable(EXTRA_MOVIE_ITEM);

            String overview = mMovie.getOverview();

            mMovieId = mMovie.getId();
            String originalLanguage = mMovie.getOriginalLanguage();
            String originalTitle = mMovie.getOriginalTitle();
            String releaseDate = mMovie.getReleaseDate();

            String formattedReleaseDate = DateUtils.formatDate(releaseDate);
            String formattedLanguage = LanguageUtils.formatLanguageAbbreviation(originalLanguage);

            mLinearLayoutSimilarMovies = (LinearLayout) view.findViewById(R.id.ll_similar_movies);

            mTextViewDetailOverview = (TextView) view.findViewById(R.id.tv_detail_overview);
            mTextViewOriginalTitle = (TextView) view.findViewById(R.id.tv_detail_original_title);
            mTextViewReleaseDate = (TextView) view.findViewById(R.id.tv_detail_release_date);
            mTextViewLanguage = (TextView) view.findViewById(R.id.tv_detail_language);

            mTextViewDetailOverview.setText(overview);
            mTextViewReleaseDate.setText(formattedReleaseDate);
            mTextViewOriginalTitle.setText(originalTitle);
            mTextViewLanguage.setText(formattedLanguage);
        }

        getSimilarMovies(view);

    }

    private void getSimilarMovies(final View view) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieDBApiConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieDBApiService service = retrofit.create(MovieDBApiService.class);

        Call<MovieResponse> call = service.getSimilarMovies(mMovieId, MovieDBApiConfig.API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                mSimilarList = response.body().getResults();
                initRecyclerView(view);
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
            }
        });
    }

    private void initRecyclerView(View view) {

        if (mSimilarList.isEmpty()) {
            mLinearLayoutSimilarMovies.setVisibility(View.GONE);
        }

        final int spacing = getResources().getDimensionPixelOffset(R.dimen.default_gap);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_movie_detail_images);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new ItemOffsetDecoration(spacing));

        MovieImagesAdapter movieImagesAdapter = new MovieImagesAdapter(getActivity(), mSimilarList);
        mRecyclerView.setAdapter(movieImagesAdapter);
    }


}
