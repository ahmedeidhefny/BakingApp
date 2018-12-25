package com.udacity.ahmed_eid.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.ahmed_eid.bakingapp.Model.*;
import com.udacity.ahmed_eid.bakingapp.R;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private Context mContext;
    private ArrayList<ingredient> ingredients;

    public IngredientAdapter(Context mContext, ArrayList<ingredient> ingredients) {
        this.mContext = mContext;
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView;
        myView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        ingredient ingredient = ingredients.get(position);
        float qu = ingredient.getQuantity();
        String mea = ingredient.getMeasure();
        String in = ingredient.getIngredient();
        holder.ingredient.setText(in);
        holder.measure.setText(mea);
        holder.quantity.setText(String.valueOf(qu));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView ingredient;
        TextView quantity;
        TextView measure;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ingredient = itemView.findViewById(R.id.ingredientText);
            quantity = itemView.findViewById(R.id.quantityText);
            measure = itemView.findViewById(R.id.measureText);
        }
    }
}
