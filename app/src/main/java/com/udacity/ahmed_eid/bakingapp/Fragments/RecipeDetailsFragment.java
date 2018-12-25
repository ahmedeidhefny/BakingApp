package com.udacity.ahmed_eid.bakingapp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.ahmed_eid.bakingapp.Adapters.IngredientAdapter;
import com.udacity.ahmed_eid.bakingapp.Adapters.StepAdapter;
import com.udacity.ahmed_eid.bakingapp.Model.Recipe;
import com.udacity.ahmed_eid.bakingapp.Model.ingredient;
import com.udacity.ahmed_eid.bakingapp.Model.step;
import com.udacity.ahmed_eid.bakingapp.R;
import com.udacity.ahmed_eid.bakingapp.Utils.AppConstants;

import java.util.ArrayList;

public class RecipeDetailsFragment extends Fragment {
    private Recipe recipe;
    private Boolean isTwoPane;
    private RecyclerView stepsRecycler;
    private TextView noSteps;
    private View mView;
    private final String TAG = "RecipeDetailsFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        initializeUI();
        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(AppConstants.InstanceState_RecipeObjectKey);
            isTwoPane = savedInstanceState.getBoolean(AppConstants.InstanceState_IsTwoPaneKey);
        }
        populateUI();
        return mView;
    }

    private void initializeUI() {
        stepsRecycler = mView.findViewById(R.id.itemStepsRecycler);
        noSteps = mView.findViewById(R.id.noSteps);
    }

    private void populateUI() {
        if (recipe != null) {
            ArrayList<step> steps = recipe.getSteps();
            setSteps(steps);
        } else {
            Log.e(TAG, "Recipe Object Is Empty");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(AppConstants.InstanceState_RecipeObjectKey, recipe);
        outState.putBoolean(AppConstants.InstanceState_IsTwoPaneKey, isTwoPane);
    }

    private void setSteps(ArrayList<step> steps) {
        if (steps == null || steps.equals("") || steps.isEmpty()) {
            noSteps.setVisibility(View.VISIBLE);
            stepsRecycler.setVisibility(View.GONE);
        } else {
            setStepsAdapterToRecyclerView(steps);
        }
    }

    private void setStepsAdapterToRecyclerView(ArrayList<step> steps) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        stepsRecycler.setLayoutManager(layoutManager);
        StepAdapter adapter = new StepAdapter(getActivity(), steps, isTwoPane);
        stepsRecycler.setAdapter(adapter);
    }

    public void setTwoPane(Boolean twoPane) {
        isTwoPane = twoPane;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
