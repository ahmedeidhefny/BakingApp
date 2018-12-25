package com.udacity.ahmed_eid.bakingapp.Fragments;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.udacity.ahmed_eid.bakingapp.Model.step;
import com.udacity.ahmed_eid.bakingapp.R;
import com.udacity.ahmed_eid.bakingapp.Utils.AppConstants;

import java.util.ArrayList;

public class StepDetailsFragment extends Fragment implements View.OnClickListener {

    private View mView;
    private TextView description, shortDescription, noStep;
    LinearLayout stepLayout;
    private Button next, previous;
    private PlayerView playerView;
    private ImageView noVideoImage;
    private SimpleExoPlayer player;
    private Uri uri = null;
    private final String TAG = "StepDetailsFragment";

    private ArrayList<step> steps;
    private int position, currentWindow = 0;
    private long playbackPosition = 0;
    private Boolean playWhenReady = true, isTwoPane;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_rstep_details, container, false);
        initializeUI();
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(AppConstants.InstanceState_playbackPositionKey);
            playbackPosition = savedInstanceState.getLong(AppConstants.InstanceState_playbackPositionKey);
            playWhenReady = savedInstanceState.getBoolean(AppConstants.InstanceState_playWhenReadyKey);
            currentWindow = savedInstanceState.getInt(AppConstants.InstanceState_currentWindowKey);
            steps = savedInstanceState.getParcelableArrayList(AppConstants.InstanceState_listOfStepsKey);
            isTwoPane = savedInstanceState.getBoolean(AppConstants.InstanceState_stepIsTwoPaneKey);
        }
        populateUI(position);
        checkOrientation();
        return mView;
    }

    private void initializeUI() {
        shortDescription = mView.findViewById(R.id.step_shortDescription);
        description = mView.findViewById(R.id.step_description);
        next = mView.findViewById(R.id.step_next);
        previous = mView.findViewById(R.id.step_previous);
        playerView = mView.findViewById(R.id.step_video);
        noVideoImage = mView.findViewById(R.id.noVideoImage);
        noStep = mView.findViewById(R.id.noStep);
        stepLayout = mView.findViewById(R.id.stepLayout);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
    }

    public void populateUI(int index) {
        if (steps != null) {
            step stepO = steps.get(index);
            if (stepO != null) {
                String shortDes = stepO.getShortDescription();
                String descript = stepO.getDescription();
                String videoPath = stepO.getVideoURL();
                String thumbURL = stepO.getThumbnailURL();
                int id = stepO.getsId();
                //if (id == 0) {
                //    getActivity().setTitle("Introduction");
                //} else {
                //   String sId = String.valueOf(id);
                //   getActivity().setTitle("Step: " + sId);
                //}
                description.setText(descript);
                shortDescription.setText(shortDes);
                if (videoPath == null || videoPath.isEmpty() || videoPath.equals("")) {
                    if (thumbURL == null || thumbURL.isEmpty() || thumbURL.equals("")) {
                        releasePlayer();
                        toggleNoHaveVideo();
                    } else {  //if (thumbURL != null || !thumbURL.equals("") || !thumbURL.isEmpty()){
                        toggleHaveVideo();
                        uri = Uri.parse(thumbURL);
                        releasePlayer();
                        initializePlayer(uri);
                        //Toast.makeText(getActivity(), "thumbURL", Toast.LENGTH_SHORT).show();
                    }
                } else { //if (videoPath != null || !videoPath.isEmpty() || !videoPath.equals("")){
                    toggleHaveVideo();
                    uri = Uri.parse(videoPath);
                    releasePlayer();
                    initializePlayer(uri);
                    // Toast.makeText(getActivity(), videoPath, Toast.LENGTH_SHORT).show();
                }
            } else {
                noStep.setVisibility(View.VISIBLE);
                stepLayout.setVisibility(View.INVISIBLE);
            }
        } else {
            Log.e(TAG, "steps List Is Empty");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer(uri);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(AppConstants.InstanceState_PositionKey, position);
        outState.putInt(AppConstants.InstanceState_currentWindowKey, currentWindow);
        outState.putBoolean(AppConstants.InstanceState_playWhenReadyKey, playWhenReady);
        outState.putLong(AppConstants.InstanceState_playbackPositionKey, playbackPosition);
        outState.putParcelableArrayList(AppConstants.InstanceState_listOfStepsKey, steps);
        outState.putBoolean(AppConstants.InstanceState_stepIsTwoPaneKey, isTwoPane);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    private void initializePlayer(Uri uri) {
        player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());
        playerView.setPlayer(player);
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                Util.getUserAgent(getActivity(), "BakingApp"));
        ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
        player.prepare(mediaSource, true, false);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.step_next:
                if (position < steps.size() - 1) {
                    position = position + 1;
                    populateUI(position);
                } else if (position == steps.size() - 1) {
                    position = 0;
                    populateUI(position);
                }
                break;
            case R.id.step_previous:
                if (position <= steps.size() - 1 && position != 0) {
                    position = position - 1;
                    populateUI(position);
                } else if (position == 0) {
                    position = steps.size() - 1;
                    populateUI(position);
                }
                break;
        }
    }

    public void setSteps(ArrayList<step> steps) {
        this.steps = steps;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void releasePlayer() {
        if (player != null) {
            currentWindow = player.getCurrentWindowIndex();
            playbackPosition = player.getCurrentPosition();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    private void toggleHaveVideo() {
        playerView.setVisibility(View.VISIBLE);
        noVideoImage.setVisibility(View.GONE);
    }

    private void toggleNoHaveVideo() {
        playerView.setVisibility(View.GONE);
        noVideoImage.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), "No video", Toast.LENGTH_SHORT).show();
    }

    public void setTwoPane(Boolean twoPane) {
        isTwoPane = twoPane;
    }

    private void checkOrientation() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE && isTwoPane == true) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) playerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            playerView.setLayoutParams(params);
            hideSystemUi();
            next.setVisibility(View.GONE);
            previous.setVisibility(View.GONE);
            shortDescription.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            toggleHaveVideo();
        }
    }


}