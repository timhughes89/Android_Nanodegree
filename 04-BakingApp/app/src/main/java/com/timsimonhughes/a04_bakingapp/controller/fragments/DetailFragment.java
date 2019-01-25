package com.timsimonhughes.a04_bakingapp.controller.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;
import com.timsimonhughes.a04_bakingapp.R;
import com.timsimonhughes.a04_bakingapp.controller.MainActivity;
import com.timsimonhughes.a04_bakingapp.controller.adapters.StepsAdapter;
import com.timsimonhughes.a04_bakingapp.model.Ingredient;
import com.timsimonhughes.a04_bakingapp.model.Recipe;
import com.timsimonhughes.a04_bakingapp.model.Step;
import com.timsimonhughes.a04_bakingapp.utils.Constants;
import com.timsimonhughes.a04_bakingapp.widget.RecipeIngredientsWidget;

import java.util.List;

public class DetailFragment extends Fragment {

    private Recipe mRecipe;
    private List<Ingredient> mIngredientList;
    private List<Step> mStepsList;
    private Menu mMenu;

    private int pagePosition = 0;

    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private ViewPager mStepsViewPager;
    private ImageButton imageButtonNext, imageButtonPrevious;
    private PageIndicatorView pageIndicatorView;

    public DetailFragment() {}

    public static DetailFragment newInstance(Recipe recipe, String transitionName) {
        DetailFragment recipeDetailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.EXTRA_RECIPE_ITEM, recipe);
        bundle.putString(Constants.EXTRA_TRANSITION_ITEM, transitionName);
        recipeDetailFragment.setArguments(bundle);
        return recipeDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postponeEnterTransition();
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_recipe_detail, container, false);

        if (getArguments() != null) {
            mRecipe = getArguments().getParcelable(Constants.EXTRA_RECIPE_ITEM);
            if (mRecipe != null) {
                mIngredientList = mRecipe.getIngredients();
                mStepsList = mRecipe.getSteps();
            }
        }

        MainActivity mainActivity = (MainActivity) getActivity();

        if (mainActivity != null) {
            mToolbar = mainActivity.mToolbar;
            mToolbarTitle = mainActivity.mToolbarTitle;
            mToolbarTitle.setText(mRecipe.getName());
            mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);

            Animation toolbarTitleAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
            mToolbarTitle.setAnimation(toolbarTitleAnimation);
            toolbarTitleAnimation.start();


            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getFragmentManager() != null) {
                        getFragmentManager().popBackStack();
                    }
                }
            });
        }

        // Fragment Views
        mStepsViewPager = (ViewPager) view.findViewById(R.id.view_pager_frag_detail);
        pageIndicatorView = view.findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setCount(mStepsList.size());
        pageIndicatorView.setAnimationType(AnimationType.WORM);
        imageButtonNext = (ImageButton) view.findViewById(R.id.ib_next);
        imageButtonPrevious = (ImageButton) view.findViewById(R.id.ib_previous);
        imageButtonPrevious.setVisibility(View.GONE);

        StepsAdapter stepsAdapter = new StepsAdapter(getChildFragmentManager());
        stepsAdapter.setStepsList(mStepsList);
        stepsAdapter.setRecipe(mRecipe);
        mStepsViewPager.setAdapter(stepsAdapter);
        mStepsViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                pagePosition = position;
                pageIndicatorView.setSelection(pagePosition);

                if (pagePosition == 0) {
                    imageButtonPrevious.setVisibility(View.GONE);
                    imageButtonNext.setVisibility(View.VISIBLE);
                } else if (pagePosition == mStepsList.size() - 1){
                    imageButtonNext.setVisibility(View.GONE);
                } else {
                    imageButtonPrevious.setVisibility(View.VISIBLE);
                    imageButtonNext.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrolled(int i, float v, int i1) {}

            @Override
            public void onPageScrollStateChanged(int i) {}
        });

        imageButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagePosition -= 1;
                mStepsViewPager.setCurrentItem(pagePosition, true);
            }
        });

        imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagePosition += 1;
                mStepsViewPager.setCurrentItem(pagePosition, true);
            }
        });

        broadcastToWidgets(mRecipe);

        return view;
    }

    private void broadcastToWidgets(Recipe recipe) {
        Intent widgetIntent = new Intent(getContext(), RecipeIngredientsWidget.class);
        widgetIntent.setAction(RecipeIngredientsWidget.ACTION_UPDATE_WIDGET_RECIPE);
        widgetIntent.putExtra(RecipeIngredientsWidget.EXTRA_RECIPE, recipe);
        getActivity().sendBroadcast(widgetIntent);
    }

}
