package com.timsimonhughes.popularmovies_p2.controller.activities;

import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.timsimonhughes.popularmovies_p2.R;
import com.timsimonhughes.popularmovies_p2.controller.fragments.MovieListFragment;
import com.timsimonhughes.popularmovies_p2.helpers.NetworkHelper;

public class MainActivity extends AppCompatActivity {

    public static final String CURRENT_FRAGMENT = "BASE";

    public AppBarLayout appBarLayout;
    private Bundle mSavedInstanceState;
    private LinearLayout mLinearLayoutError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            checkConnection();
        }
    }

    public void checkConnection() {

        if (NetworkHelper.checkConnection(getApplicationContext())) {

            // Network Connected - Check if any previous fragments attached
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(CURRENT_FRAGMENT);

            if (fragment == null) {
                // No Fragment Exists
                if (mSavedInstanceState == null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(CURRENT_FRAGMENT)
                            .add(R.id.container, MovieListFragment.newInstance())
                            .commit();
                }
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .detach(fragment)
                        .commit();
            }

        } else {
            // Network Disconnected - Show disconnected screen
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
