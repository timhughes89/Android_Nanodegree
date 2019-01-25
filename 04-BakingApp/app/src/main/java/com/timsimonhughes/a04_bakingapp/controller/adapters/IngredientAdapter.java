package com.timsimonhughes.a04_bakingapp.controller.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.timsimonhughes.a04_bakingapp.R;
import com.timsimonhughes.a04_bakingapp.model.Ingredient;
import com.timsimonhughes.a04_bakingapp.utils.RecipeUtils;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private Context mContext;
    private List<Ingredient> mIngredientList;

    public IngredientAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.list_item_ingrdient, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final IngredientAdapter.ViewHolder holder, int position) {
        Ingredient ingredient = mIngredientList.get(position);

        String ingredientItem = ingredient.getIngredients();
        String quantity = RecipeUtils.formatQuantity(String.valueOf(ingredient.getQuantity()));
        String measure = RecipeUtils.formatMeasurement(ingredient.getMeasure());

        String ingredientQuantityMeasure = quantity +  " " + measure;

        holder.textViewIngredient.setText(ingredientItem);
        holder.textViewMeasure.setText(ingredientQuantityMeasure);

    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.mIngredientList = ingredientList;
    }

    @Override
    public int getItemCount() {
        return mIngredientList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewIngredient, textViewMeasure;
        CheckBox checkBoxItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewIngredient = itemView.findViewById(R.id.tv_recipe_ingredient_item);
            textViewMeasure = itemView.findViewById(R.id.tv_recipe_ingredient_measure);
            checkBoxItem = itemView.findViewById(R.id.cb_recipe_ingredient_item);
        }
    }
}
