package com.udacity.ahmed_eid.bakingapp.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.ahmed_eid.bakingapp.Adapters.IngredientAdapter;
import com.udacity.ahmed_eid.bakingapp.Fragments.RecipeDetailsFragment;
import com.udacity.ahmed_eid.bakingapp.Fragments.StepDetailsFragment;
import com.udacity.ahmed_eid.bakingapp.Utils.AppConstants;
import com.udacity.ahmed_eid.bakingapp.Model.*;
import com.udacity.ahmed_eid.bakingapp.R;
import com.udacity.ahmed_eid.bakingapp.Widget.Widget_ManageData_Helper;

import java.util.ArrayList;

public class RecipeDetailsActivity extends AppCompatActivity {

    private Recipe recipe;
    private ImageView recipeImage;
    private TextView recipeName, recipeServings, noIngredients;
    private RecyclerView ingredientsRecycler;
    private int spanCount = 2;
    private RecipeDetailsFragment recipeDetailsFragment;
    private StepDetailsFragment stepDetailsFragment;
    private Boolean isTwoPane = true;
    private ArrayList<step> steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        initializeUI();
        recipe = receiveDataFromMainActivity();
        if (findViewById(R.id.container_detailStep) != null) {
            isTwoPane = false;
            spanCount = 3;
        }
        populateUI();
        if (savedInstanceState == null) {
            manageFragment();
        }
    }

    private void initializeUI() {
        ingredientsRecycler = findViewById(R.id.itemIngredientsRecycler);
        recipeImage = findViewById(R.id.recipe_image_detail);
        recipeName = findViewById(R.id.recipe_name_detail);
        recipeServings = findViewById(R.id.recipe_servings_detail);
        noIngredients = findViewById(R.id.noIngredients);
    }

    private void populateUI() {
        if (recipe != null) {
            String rName = recipe.getName();
            String image = recipe.getImage();
            String name = recipe.getName();
            int servings = recipe.getServings();
            ArrayList<ingredient> ingredients = recipe.getIngredients();
            steps = recipe.getSteps();
            setTitle(rName);
            recipeName.setText(name);
            String Servings = String.valueOf(servings);
            recipeServings.setText(Servings + " " + AppConstants.RecipeServings);
            if (image == null || image.equals("") || image.isEmpty()) {
                recipeImage.setImageResource(R.drawable.noimage);
            } else {
                Picasso.with(this)
                        .load(image)
                        .placeholder(R.drawable.loadimage)
                        .error(R.drawable.no_internet_connection)
                        .into(recipeImage);
            }
            setIngredients(ingredients);
            new Widget_ManageData_Helper(this).setWidgetData(recipe);
        }
    }

    public Recipe receiveDataFromMainActivity() {
        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(AppConstants.recipeObjectKey)) {
            errorMassage();
            return null;
        } else {
            Recipe recipeObj = intent.getParcelableExtra(AppConstants.recipeObjectKey);
            return recipeObj;
        }
    }

    private void setIngredients(ArrayList<ingredient> ingredients) {
        if (ingredients == null || ingredients.equals("") || ingredients.isEmpty()) {
            noIngredients.setVisibility(View.VISIBLE);
            ingredientsRecycler.setVisibility(View.INVISIBLE);
        } else {
            setIngredientsAdapterToRecyclerView(ingredients);
        }
    }

    private void setIngredientsAdapterToRecyclerView(ArrayList<ingredient> ingredients) {
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), spanCount);
        ingredientsRecycler.setLayoutManager(layoutManager);
        IngredientAdapter adapter = new IngredientAdapter(getApplicationContext(), ingredients);
        ingredientsRecycler.setAdapter(adapter);
    }

    private void errorMassage() {
        finish();
        Toast.makeText(this, R.string.error_massage, Toast.LENGTH_LONG).show();
    }

    private void manageFragment() {
        recipeDetailsFragment = new RecipeDetailsFragment();
        recipeDetailsFragment.setRecipe(recipe);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isTwoPane == true) {
            recipeDetailsFragment.setTwoPane(isTwoPane);
            fragmentTransaction.replace(R.id.container_layout, recipeDetailsFragment);
        } else {
            recipeDetailsFragment.setTwoPane(isTwoPane);
            fragmentTransaction.replace(R.id.container_masterListStep, recipeDetailsFragment);
            addStepDetailsFragmentIndexZero(isTwoPane);
        }
        fragmentTransaction.commit();
    }

    private void addStepDetailsFragmentIndexZero(Boolean mIsTwoPane) {
        stepDetailsFragment = new StepDetailsFragment();
        stepDetailsFragment.setSteps(steps);
        stepDetailsFragment.setPosition(0);
        stepDetailsFragment.setTwoPane(mIsTwoPane);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_detailStep, stepDetailsFragment)
                .commit();
    }

}
