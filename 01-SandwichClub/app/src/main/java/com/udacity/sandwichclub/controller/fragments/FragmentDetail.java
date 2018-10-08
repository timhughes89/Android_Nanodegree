package com.udacity.sandwichclub.controller.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.R;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDetail extends Fragment {

    private static final String TAG = "FragmentDetail";

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private int position = 0;
    private ImageView mIngredientsTv;
    private FloatingActionButton mFloatingActionButton;
    private boolean isChecked = false;
    private Sandwich mSandwich;
    private android.support.v7.widget.Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    public FragmentDetail() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        mIngredientsTv = view.findViewById(R.id.image_iv);

        Intent intent = getActivity().getIntent();
        if (intent == null) {
            closeOnError();
        } else {
            position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        }

        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        mSandwich = JsonUtils.parseSandwichJson(json);
        if (mSandwich == null) {
            // Sandwich data unavailable
            closeOnError();
        }

        populateUI(view, mSandwich);

        if (mSandwich.getImage() != null) {
            Picasso.get()
                    .load(mSandwich.getImage())
                    .into(mIngredientsTv);
        } else {
            ImageView noImageBackground = (ImageView) view.findViewById(R.id.no_image_cl);
            noImageBackground.setVisibility(View.VISIBLE);
        }

        initToolbar(view);

        return view;
    }

    private void initToolbar(View view) {
        mCollapsingToolbarLayout = view.findViewById(R.id.collaspingToolbarLayout);
        mToolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null) {
            activity.getSupportActionBar();
            activity.setSupportActionBar(mToolbar);
            mCollapsingToolbarLayout.setTitle(mSandwich.getMainName());
        }
    }

    private void closeOnError() {
        getActivity().finish();
        Toast.makeText(getContext(), R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(View view, Sandwich sandwich) {

        TextView description = (TextView) view.findViewById(R.id.description_tv);
        TextView origin = (TextView) view.findViewById(R.id.origin_tv);
        TextView alsoKnownAs = (TextView) view.findViewById(R.id.also_known_as_tv);
        TextView ingredients = (TextView) view.findViewById(R.id.ingredients_tv);

        LinearLayout descriptionLl = view.findViewById(R.id.description_ll);
        LinearLayout originLl =  view.findViewById(R.id.origin_ll);
        LinearLayout ingredientsLl = view.findViewById(R.id.ingredients_ll);
        LinearLayout alsoKnowAsLl = view.findViewById(R.id.also_know_as_ll);

        description.setText(sandwich.getDescription());
        origin.setText(sandwich.getPlaceOfOrigin());
        alsoKnownAs.setText(sandwich.getAlsoKnownAsString().trim());
        ingredients.setText(sandwich.getIngredientsString().trim());

        if (sandwich.getDescription().trim().isEmpty()) {
            descriptionLl.setVisibility(View.GONE);

        } else if (sandwich.getPlaceOfOrigin().isEmpty()) {
            originLl.setVisibility(View.GONE);

        } else if (sandwich.getAlsoKnownAsString().isEmpty()) {
            alsoKnowAsLl.setVisibility(View.GONE);

        } else if (sandwich.getIngredientsString().trim().isEmpty()) {
            ingredientsLl.setVisibility(View.GONE);
        }

        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChecked = !isChecked) {
                    mFloatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_white_24dp));
                } else {
                    mFloatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_white_24dp));
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_next:
                toast("Next");
                // TODO: Implement ViewPager
                break;

            case R.id.action_previous:
                toast("Previous");
                // TODO: Implement ViewPager
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void toast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

}
