package com.udacity.sandwichclub.controller.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.udacity.sandwichclub.R;
import com.udacity.sandwichclub.controller.fragments.FragmentDetail;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new FragmentDetail())
                    .commit();
        }
    }
}
