package com.timsimonhughes.a04_bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class RecipeResponse implements Parcelable {

    private List<Recipe> mRecipes;

    public List<Recipe> getRecipes() {
        return mRecipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        mRecipes = recipes;
    }

    public static final Creator<RecipeResponse> CREATOR = new Creator<RecipeResponse>() {
        @Override
        public RecipeResponse createFromParcel(Parcel in) {
            return new RecipeResponse(in);
        }

        @Override
        public RecipeResponse[] newArray(int size) {
            return new RecipeResponse[size];
        }
    };

    protected RecipeResponse(Parcel in) {
        mRecipes = in.createTypedArrayList(Recipe.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeTypedList(mRecipes);
    }
}
