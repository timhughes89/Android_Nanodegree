package com.udacity.popularMovies_p1.controller.activities;

import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.LinearLayout;

import com.udacity.popularMovies_p1.helpers.NetworkHelper;
import com.udacity.a02_popularmovies_p1.R;
import com.udacity.popularMovies_p1.controller.fragments.MovieListFragment;


public class MainActivity extends AppCompatActivity {

    public AppBarLayout appBarLayout;
    private Bundle mSavedInstanceState;
    private LinearLayout mLinearLayoutError;

    @Override
    protected void onCreate(Bundle mSavedInstanceState) {
        super.onCreate(mSavedInstanceState);
        setContentView(R.layout.activity_main);

        checkConnection();
    }

    public void checkConnection() {

        if (NetworkHelper.checkConnection(getApplicationContext())) {

            MovieListFragment movieListFragment = (MovieListFragment) getSupportFragmentManager().findFragmentByTag("BASE");

            if (movieListFragment == null) {
                // No Fragment Exists
                if (mSavedInstanceState == null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("BASE")
                            .add(R.id.container, MovieListFragment.newInstance())
                            .commit();
                }
            } else {
                // Fragment Exists
                getSupportFragmentManager()
                        .beginTransaction()
                        .detach(movieListFragment)
                        .commit();
            }

        } else {
            //Show disconnected screen
            mLinearLayoutError = (LinearLayout) findViewById(R.id.ll_network_error);
            mLinearLayoutError.setVisibility(View.VISIBLE);

            Button refreshButton = (Button) findViewById(R.id.bt_refresh);
            refreshButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLinearLayoutError.setVisibility(View.INVISIBLE);
                    checkConnection();
                }
            });
        }

    }
}
