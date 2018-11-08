package com.udacity.popularMovies_p1.controller.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import com.udacity.a02_popularmovies_p1.R;
import com.udacity.popularMovies_p1.controller.adapters.CastAdapter;
import com.udacity.popularMovies_p1.model.Cast;
import com.udacity.popularMovies_p1.model.CreditResponse;

import com.udacity.popularMovies_p1.model.Movie;
import com.udacity.popularMovies_p1.network.MovieDBApiConfig;
import com.udacity.popularMovies_p1.network.MovieDBApiService;
import com.udacity.popularMovies_p1.ui.ItemOffsetDecoration;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.udacity.popularMovies_p1.controller.fragments.MovieDetailFragment.EXTRA_MOVIE_ITEM;

public class CastFragment extends Fragment {

    private int mMovieId;
    private List<Cast> mCastList = new ArrayList<>();
    private ProgressBar mProgressBar;

    public static CastFragment newInstance(Movie movie) {
        CastFragment castFragment = new CastFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_MOVIE_ITEM, movie);
        castFragment.setArguments(bundle);
        return castFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_cast, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgressBar = (ProgressBar)view.findViewById(R.id.pb_cast);

        if (getArguments() != null) {
            Movie movie = getArguments().getParcelable(EXTRA_MOVIE_ITEM);
            mMovieId = movie.getId();
        }

        getCast(view);
    }

    private void initRecyclerView(View view) {

        int spacing = getResources().getDimensionPixelOffset(R.dimen.spacing_X1);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.rv_movie_detail_cast);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new ItemOffsetDecoration(spacing));

        CastAdapter castAdapter = new CastAdapter(getContext(), mCastList);
        recyclerView.setAdapter(castAdapter);

    }

    private void getCast(final View view) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieDBApiConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieDBApiService service = retrofit.create(MovieDBApiService.class);

        Call<CreditResponse> call = service.getCredits(mMovieId, MovieDBApiConfig.API_KEY);
        call.enqueue(new Callback<CreditResponse>() {
            @Override
            public void onResponse(@NonNull Call<CreditResponse> call, @NonNull Response<CreditResponse> response) {
                mCastList = response.body().getCastResults();
                mProgressBar.setVisibility(View.GONE);
                initRecyclerView(view);
            }

            @Override
            public void onFailure(@NonNull Call<CreditResponse> call, @NonNull Throwable t) {

            }
        });
    }
}
