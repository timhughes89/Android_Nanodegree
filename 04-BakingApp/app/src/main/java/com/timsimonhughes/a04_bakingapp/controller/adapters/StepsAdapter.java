package com.timsimonhughes.a04_bakingapp.controller.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.timsimonhughes.a04_bakingapp.controller.fragments.RecipeOverviewFragment;
import com.timsimonhughes.a04_bakingapp.controller.fragments.StepFragment;
import com.timsimonhughes.a04_bakingapp.model.Recipe;
import com.timsimonhughes.a04_bakingapp.model.Step;

import java.util.List;

public class StepsAdapter extends FragmentStatePagerAdapter {

    private List<Step> mStepsList;
    private Recipe mRecipe;

    public StepsAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setStepsList(List<Step> stepsList) {
        this.mStepsList = stepsList;
    }

    public void setRecipe(Recipe recipe) {
        this.mRecipe = recipe;
    }

    @Override
    public Fragment getItem(int position) {

       Fragment fragment = null;

       if (position == 0) {
           fragment = RecipeOverviewFragment.newInstance(mRecipe);
       } else {
           Step step = mStepsList.get(position - 1);
           fragment = StepFragment.newInstance(step);
       }
       return fragment;
    }

    @Override
    public int getCount() {
        return mStepsList.size();
    }

}
