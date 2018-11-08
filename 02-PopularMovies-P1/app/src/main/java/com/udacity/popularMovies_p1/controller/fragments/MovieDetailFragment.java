package com.udacity.popularMovies_p1.controller.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import com.udacity.a02_popularmovies_p1.R;
import com.udacity.popularMovies_p1.controller.adapters.PagerAdapter;
import com.udacity.popularMovies_p1.model.Movie;
import com.udacity.popularMovies_p1.utils.ImageUtils;
import com.udacity.popularMovies_p1.utils.MovieUtils;

public class MovieDetailFragment extends Fragment {

    public static final String EXTRA_MOVIE_ITEM = "EXTRA_MOVIE_ITEM";
    public static final String EXTRA_TRANSITION_NAME = "EXTRA_TRANSITION_NAME";
    public int mMovieId;
    private boolean mIsChecked = false;
    private boolean isImageBackdropVisible;
    private ImageView mBackdropImage;

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

        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.ctl_detail);
        LinearLayout linearLayoutHeader = (LinearLayout) view.findViewById(R.id.ll_header);
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_detail);
        ImageView imageViewPoster = (ImageView) view.findViewById(R.id.iv_poster);
        TextView textViewHeaderTitle = (TextView) view.findViewById(R.id.tv_header_title);
        TextView textViewRatingCount = (TextView) view.findViewById(R.id.tv_rating_count);
        TextView textViewVoteCount = (TextView) view.findViewById(R.id.tv_vote_count);
        TextView textViewGenreIds = (TextView) view.findViewById(R.id.tv_genre_ids);

        collapsingToolbarLayout.setTitleEnabled(false);
        mBackdropImage = (ImageView) view.findViewById(R.id.iv_backdrop);

        if (getArguments() != null) {

            initToolbar(view);

            Movie movie = getArguments().getParcelable(EXTRA_MOVIE_ITEM);

            String transitionName = getArguments().getString(EXTRA_TRANSITION_NAME);
            String posterUrl = MovieUtils.buildPosterUrl(MovieUtils.API_POSTER_SIZE_W500, movie.getPosterPath());
            String backdropUrl = MovieUtils.buildPosterUrl(MovieUtils.API_POSTER_SIZE_W780, movie.getBackdropPath());
            String overview = movie.getOverview();
            String title = movie.getTitle();
            int rating = movie.getVoteCount();
            double average = movie.getVoteAverage();

            mMovieId = movie.getId();
            List<Integer> genreIds = movie.getGenreIds();
            String voteCount = String.valueOf(rating);
            String voteAverage = String.valueOf(average);

            for (int genreId : genreIds) {
                String genre = String.valueOf(genreId);
                String formattedGenre = MovieUtils.buildFormattedGenre(genre);
                textViewGenreIds.setText(String.format("%s\n", formattedGenre));
            }

            collapsingToolbarLayout.setTitle(title);
            textViewHeaderTitle.setText(title);
            textViewRatingCount.setText(voteAverage);
            textViewVoteCount.setText(voteCount);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageViewPoster.setTransitionName(transitionName);

            }
            ImageUtils.loadImage(view, posterUrl, imageViewPoster);
            ImageUtils.loadImageWithPalette(view, backdropUrl, mBackdropImage, linearLayoutHeader, getContext());

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isImageBackdropVisible) {
                        createCircularReveal(mBackdropImage);
                    } else {
                        hideCircularReveal(mBackdropImage);
                    }
                }
            });

            final ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
            PagerAdapter pagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
            pagerAdapter.addFragment(OverviewFragment.newInstance(movie), "Overview");
            pagerAdapter.addFragment(CastFragment.newInstance(movie), "Cast");
            pagerAdapter.addFragment(ReviewFragment.newInstance(movie), "Reviews");
            viewPager.setAdapter(pagerAdapter);

            TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
            tabLayout.addTab(tabLayout.newTab().setText(pagerAdapter.getPageTitle(0)));
            tabLayout.addTab(tabLayout.newTab().setText(pagerAdapter.getPageTitle(1)));
            tabLayout.addTab(tabLayout.newTab().setText(pagerAdapter.getPageTitle(2)));

            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createCircularReveal(View view) {
        int x = view.getRight() / 2;
        int y = view.getBottom() / 2;
        int finalRadius = Math.max(view.getWidth(), view.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(view, x, y, 0, finalRadius);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        view.setVisibility(View.VISIBLE);
        isImageBackdropVisible = true;
        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void hideCircularReveal(final View view) {
        int x = view.getRight() / 2;
        int y = view.getBottom() / 2;
        int initialRadius = view.getWidth();

        Animator anim = ViewAnimationUtils.createCircularReveal(view, x, y, initialRadius, 0);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });
        isImageBackdropVisible = false;
        anim.start();
    }

    private void initToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tb_detail);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();

        if (appCompatActivity != null) {
            appCompatActivity.getSupportActionBar();
            appCompatActivity.setSupportActionBar(toolbar);
        }

        toolbar.setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String transitionName = getResources().getString(R.string.transition_name);

                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
//                    getFragmentManager()
//                            .beginTransaction()
//                            .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
//                            .replace(R.id.container, MovieListFragment.newInstance())
//                            .commit();
                }
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_favourite:
                item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (mIsChecked = !mIsChecked) {
                            item.setIcon(R.drawable.ic_favorite_white_24dp);
                        } else {
                            item.setIcon(R.drawable.ic_favorite_border_white_24dp);
                        }
                        return false;
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
