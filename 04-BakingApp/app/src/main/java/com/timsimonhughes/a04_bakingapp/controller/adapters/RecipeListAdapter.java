package com.timsimonhughes.a04_bakingapp.controller.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.timsimonhughes.a04_bakingapp.ui.OnItemClickListener;
import com.timsimonhughes.a04_bakingapp.R;
import com.timsimonhughes.a04_bakingapp.model.Recipe;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.MyViewHolder> {

    private Context mContext;
    private List<Recipe> mRecipeList;
    private OnItemClickListener mOnItemClickListener;

    public RecipeListAdapter(Context context, OnItemClickListener onItemClickListener) {
        mContext = context;
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecipeListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_recipes, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeListAdapter.MyViewHolder holder, int position) {

        final Recipe recipe = mRecipeList.get(position);

        String recipeName = recipe.getName();
        String recipeImageUri = recipe.getImage();
        String recipeServing = String.valueOf(recipe.getServings());


        if (recipeImageUri.isEmpty()) {
            String fallbackImage = mContext.getResources().getStringArray(R.array.recipe_image_fallbacks)[position];
            Glide.with(mContext)
                    .load(fallbackImage)
                    .into(holder.imageViewRecipeImage);
        } else {
            Glide.with(mContext)
                    .load(recipeImageUri)
                    .into(holder.imageViewRecipeImage);
        }

        holder.textViewRecipeName.setText(recipeName);
        holder.textViewRecipeServing.setText(recipeServing);

//        final int adapterPosition = holder.getAdapterPosition();

        ViewCompat.setTransitionName(holder.imageViewRecipeImage, mContext.getResources().getString(R.string.transitionName));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(holder.getAdapterPosition(), holder.imageViewRecipeImage, recipe);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.mRecipeList = recipeList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewRecipeImage;
        TextView textViewRecipeName, textViewRecipeServing;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewRecipeImage = (ImageView)itemView.findViewById(R.id.iv_recipe_image);
            textViewRecipeName = (TextView)itemView.findViewById(R.id.tv_recipe_name);
            textViewRecipeServing = (TextView)itemView.findViewById(R.id.tv_recipe_serving);

        }
    }
}


