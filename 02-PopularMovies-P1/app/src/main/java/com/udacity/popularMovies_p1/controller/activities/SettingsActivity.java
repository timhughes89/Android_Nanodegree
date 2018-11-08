package com.udacity.popularMovies_p1.controller.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.udacity.a02_popularmovies_p1.R;
import com.udacity.popularMovies_p1.controller.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    //TODO: Implement settings

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.settings_container, new SettingsFragment())
                    .commit();
        }
    }
}
