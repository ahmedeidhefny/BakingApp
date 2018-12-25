package com.udacity.ahmed_eid.bakingapp.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.udacity.ahmed_eid.bakingapp.Fragments.StepDetailsFragment;
import com.udacity.ahmed_eid.bakingapp.Utils.AppConstants;
import com.udacity.ahmed_eid.bakingapp.Model.step;
import com.udacity.ahmed_eid.bakingapp.R;

import java.util.ArrayList;

public class StepDetailsActivity extends AppCompatActivity {

    private ArrayList<step> steps;
    private int position;
    private StepDetailsFragment stepDetailsFragment;
    private Boolean isTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);
        receiveDataFromRecipeDetails();
        if (savedInstanceState == null) {
            stepDetailsFragment = new StepDetailsFragment();
            stepDetailsFragment.setPosition(position);
            stepDetailsFragment.setSteps(steps);
            stepDetailsFragment.setTwoPane(isTwoPane);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container_step, stepDetailsFragment)
                    .commit();
        }
    }

    public void receiveDataFromRecipeDetails() {
        Intent intent = getIntent();
        if (intent == null) {
            errorMassage();
            return;
        }
        if (!intent.hasExtra(AppConstants.Intent_IsTwoPaneKey)
                && !intent.hasExtra(AppConstants.ListStepsKey)
                && !intent.hasExtra(AppConstants.StepPositionKey)) {
            errorMassage();
        } else {
            position = intent.getExtras().getInt(AppConstants.StepPositionKey);
            steps = intent.getParcelableArrayListExtra(AppConstants.ListStepsKey);
            isTwoPane = intent.getExtras().getBoolean(AppConstants.Intent_IsTwoPaneKey);
        }

    }

    public void errorMassage() {
        finish();
        Toast.makeText(getApplicationContext(), R.string.error_massage_steps, Toast.LENGTH_LONG).show();
    }

}
