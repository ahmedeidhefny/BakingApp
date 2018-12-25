package com.udacity.ahmed_eid.bakingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.ahmed_eid.bakingapp.Activities.RecipeDetailsActivity;
import com.udacity.ahmed_eid.bakingapp.Utils.AppConstants;
import com.udacity.ahmed_eid.bakingapp.Model.Recipe;
import com.udacity.ahmed_eid.bakingapp.R;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context mContext;
    private ArrayList<Recipe> recipes;

    public RecipeAdapter(Context mContext, ArrayList<Recipe> recipes) {
        this.mContext = mContext;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView;
        myView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {

        final Recipe recipe = recipes.get(position);
        holder.recipeName.setText(recipe.getName());
        String servings = String.valueOf(recipe.getServings());
        holder.recipe_servings.setText(servings + " " + AppConstants.RecipeServings);
        String image = recipe.getImage();
        if (image == null || image.equals("") || image.isEmpty()) {
            holder.recipeImage.setImageResource(R.drawable.noimage);
        } else {
            Picasso.with(mContext)
                    .load(recipe.getImage())
                    .placeholder(R.drawable.loadimage)
                    .error(R.drawable.no_internet_connection)
                    .into(holder.recipeImage);
        }
        holder.linearOnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRecipeDetail(recipe);
            }
        });
    }

    public void goToRecipeDetail(Recipe recipe) {
        Intent intent = new Intent(mContext, RecipeDetailsActivity.class);
        intent.putExtra(AppConstants.recipeObjectKey, recipe);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        ImageView recipeImage;
        TextView recipeName;
        LinearLayout linearOnClick;
        TextView recipe_servings;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeName = itemView.findViewById(R.id.recipe_name);
            linearOnClick = itemView.findViewById(R.id.itemRecipeRoot);
            recipe_servings = itemView.findViewById(R.id.recipe_servings);
        }
    }
}
