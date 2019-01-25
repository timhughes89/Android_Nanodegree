package com.timsimonhughes.a04_bakingapp.controller.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timsimonhughes.a04_bakingapp.ui.OnItemClickListener;
import com.timsimonhughes.a04_bakingapp.R;
import com.timsimonhughes.a04_bakingapp.controller.MainActivity;
import com.timsimonhughes.a04_bakingapp.controller.adapters.RecipeListAdapter;
import com.timsimonhughes.a04_bakingapp.model.Recipe;
import com.timsimonhughes.a04_bakingapp.utils.Constants;
import com.timsimonhughes.a04_bakingapp.utils.JsonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment implements OnItemClickListener {

    private static final String TAG = ListFragment.class.getSimpleName();


    private View mView;
    private List<String> mRecipes = new ArrayList<>();
    private List<Recipe> mRecipesList = new ArrayList<>();

    public ListFragment() {}

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        String recipeJson = getJsonFromAssets();
        mRecipes = JsonUtils.getJsonRecipesListFromAssets(recipeJson);

        for (int i = 0; i < mRecipes.size(); i++) {
            Recipe recipe = JsonUtils.parseBakingJsonFromAssets(mRecipes.get(i));
            mRecipesList.add(recipe);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_recipe_list, container, false);
        initViews();
        return mView;
    }

    private void initViews() {

        MainActivity mainActivity = (MainActivity) getActivity();

        if (mainActivity != null) {
            Toolbar toolbar = mainActivity.mToolbar;
            TextView toolbarTitle = mainActivity.mToolbarTitle;
            toolbarTitle.setText(getResources().getString(R.string.app_name));
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
            toolbar.setNavigationIcon(null);

//            Animation toolbarTitleAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);
//            toolbarTitle.setAnimation(toolbarTitleAnimation);
//            toolbarTitleAnimation.start();
        }

        RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.rv_recipe_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RecipeListAdapter recipeListAdapter = new RecipeListAdapter(getContext(), this);
        recyclerView.setAdapter(recipeListAdapter);
        recipeListAdapter.setOnItemClickListener(this);
        recipeListAdapter.setRecipeList(mRecipesList);

    }

    @Override
    public void onItemClick(int position, View sharedView, Recipe recipe) {

        String transitionName = getResources().getString(R.string.transitionName);
        Fragment detailFragment = DetailFragment.newInstance(recipe, transitionName);

        MainActivity mainActivity = (MainActivity) getActivity();

        if (mainActivity != null) {
            boolean isTablet = mainActivity.isTablet;

            if (!isTablet) {
                if (getFragmentManager() != null) {
                    getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom, R.anim.slide_in_bottom, R.anim.slide_out_bottom)
                            .addToBackStack(Constants.CURRENT_FRAGMENT)
                            .replace(R.id.container, detailFragment)
                            .commit();
                }

            } else {
                if (getFragmentManager() != null) {
                    getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
                            .addToBackStack(null)
                            .replace(R.id.detail, detailFragment)
                            .commit();
                }
            }
        }
    }

    private String getJsonFromAssets() {
        String recipeJson = null;
        try {
            if (getActivity() != null) {
                InputStream inputStream = getActivity().getAssets().open(Constants.KEY_RECIPE_JSON);
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                inputStream.close();
                recipeJson = new String(buffer, Constants.KEY_CHARSET);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return recipeJson;
    }
}
