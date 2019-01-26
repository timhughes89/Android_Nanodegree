package com.timsimonhughes.a04_bakingapp.controller;

import android.content.Context;
import android.content.res.Configuration;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.timsimonhughes.a04_bakingapp.R;
import com.timsimonhughes.a04_bakingapp.controller.fragments.ListFragment;

public class MainActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG = "ListFragment";

    public AppBarLayout mAppBarLayout;
    public Toolbar mToolbar;
    public TextView mToolbarTitle;
    public Fragment mFragment;
    public boolean isTablet;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAppBarLayout = findViewById(R.id.appBarLayout);
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbarTitle);
        progressBar = findViewById(R.id.pb_recipe_list);

        isTablet = isTablet();

        // No fragment exists
        if (savedInstanceState == null) {
            // Create new Fragment
            mFragment = ListFragment.newInstance();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, mFragment, FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();
        }
        // Fragment Exists
        else {
            mFragment = getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_TAG);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);

        if (getSupportFragmentManager() != null) {
            getSupportFragmentManager().putFragment(outState, FRAGMENT_TAG, mFragment);
        }

    }

    public boolean isTablet() {
        return (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
