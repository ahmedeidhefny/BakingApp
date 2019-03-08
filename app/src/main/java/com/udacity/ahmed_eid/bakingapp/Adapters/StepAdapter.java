package com.udacity.ahmed_eid.bakingapp.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.udacity.ahmed_eid.bakingapp.Activities.StepDetailsActivity;
import com.udacity.ahmed_eid.bakingapp.Fragments.StepDetailsFragment;
import com.udacity.ahmed_eid.bakingapp.Utils.AppConstants;
import com.udacity.ahmed_eid.bakingapp.Model.*;
import com.udacity.ahmed_eid.bakingapp.R;

import java.util.ArrayList;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {
    private Context mContext;
    private ArrayList<step> steps;
    private Boolean isTwoPane;
    private int row_index;

    public StepAdapter(Context mContext, ArrayList<step> steps, Boolean isTwoPane) {
        this.mContext = mContext;
        this.steps = steps;
        this.isTwoPane = isTwoPane;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView;
        myView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_steps, parent, false);
        return new StepViewHolder(myView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final StepViewHolder holder, final int position) {
        final step step = steps.get(position);
        holder.stepShortDescription.setText(step.getShortDescription());
        String id = String.valueOf(step.getsId());
        holder.stepId.setText(id);
        holder.onClickText.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();
                goToStepDetails(position);
            }
        });
        if (row_index == position) {
            holder.linearLayout.setBackgroundColor(R.color.seprator_background);
        } else {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.stepShortDescription.setTextColor(Color.parseColor("#000000"));
        }
    }

    public void goToStepDetails(int position) {
        if (isTwoPane == false) {
            StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
            stepDetailsFragment.setSteps(steps);
            stepDetailsFragment.setPosition(position);
            stepDetailsFragment.setTwoPane(isTwoPane);
            FragmentManager fragmentManager = ((FragmentActivity) this.mContext).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container_detailStep, stepDetailsFragment)
                    .commit();
        } else {
            Intent intent = new Intent(mContext, StepDetailsActivity.class);
            intent.putExtra(AppConstants.ListStepsKey, steps);
            intent.putExtra(AppConstants.StepPositionKey, position);
            intent.putExtra(AppConstants.Intent_IsTwoPaneKey, isTwoPane);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }

    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {
        TextView stepId;
        TextView stepShortDescription;
        TextView onClickText;
        LinearLayout linearLayout;

        public StepViewHolder(View itemView) {
            super(itemView);
            stepShortDescription = itemView.findViewById(R.id.stepShortDescription);
            stepId = itemView.findViewById(R.id.step_id);
            onClickText = itemView.findViewById(R.id.stepTab);
            linearLayout = itemView.findViewById(R.id.item_step_root);
        }
    }
}


