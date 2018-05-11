package com.example.beatrice.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.beatrice.bakingapp.models.Recipe;
import com.example.beatrice.bakingapp.models.Step;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class RecipeStepDetailFragment extends Fragment implements ExoPlayer.EventListener {
    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private ArrayList<Step> steps = new ArrayList<>();
    private int selectedIndex;
    ArrayList<Recipe> recipe;
    String recipeName;

    public RecipeStepDetailFragment() {

    }

    private ListItemClickListener itemClickListener;

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    public interface ListItemClickListener {
        void onListItemClick(List<Step> allSteps, int Index, String recipeName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView textView;

        itemClickListener = (RecipeDetailsActivity) getActivity();

        recipe = new ArrayList<>();

        if (savedInstanceState != null) {
            steps = savedInstanceState.getParcelableArrayList("selected_steps");
            selectedIndex = savedInstanceState.getInt("index");
            recipeName = savedInstanceState.getString("Title");


        } else {
            steps = getArguments().getParcelableArrayList("selected_steps");
            if (steps != null) {
                steps = getArguments().getParcelableArrayList("selected_steps");
                selectedIndex = getArguments().getInt("index");
                recipeName = getArguments().getString("Title");
            } else {
                recipe = getArguments().getParcelableArrayList("selected_recipes");
                steps = (ArrayList<Step>) recipe.get(0).getSteps();
                selectedIndex = 0;
            }

        }


        View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        textView = rootView.findViewById(R.id.recipe_step_detail_text);
        textView.setText(steps.get(selectedIndex).getDescription());
        textView.setVisibility(View.VISIBLE);

        mPlayerView = rootView.findViewById(R.id.playerView);
        mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        String videoURL = steps.get(selectedIndex).getVideoURL();

        if (rootView.findViewWithTag("sw600dp-port-recipe_step_detail") != null) {
            recipeName = ((RecipeDetailsActivity) Objects.requireNonNull(getActivity())).recipeName;
            Objects.requireNonNull(((RecipeDetailsActivity) getActivity()).getSupportActionBar()).setTitle(recipeName);
        }

        String imageUrl = steps.get(selectedIndex).getThumbnailURL();
        if (!imageUrl.equals("")) {
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            ImageView thumbImage = rootView.findViewById(R.id.thumbImage);
            Glide.with(Objects.requireNonNull(getContext())).load(builtUri).into(thumbImage);
        }

        if (!videoURL.isEmpty()) {


            initializePlayer(Uri.parse(steps.get(selectedIndex).getVideoURL()));

            if (rootView.findViewWithTag("sw600dp-land-recipe_step_detail") != null) {
                getActivity().findViewById(R.id.fragment_container2).setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
                mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            } else if (isInLandscapeMode(getContext())) {
                textView.setVisibility(View.GONE);
            }
        } else {
            mExoPlayer = null;
            mPlayerView.setLayoutParams(new ConstraintLayout.LayoutParams(0, 0));
        }


        Button mPrevStep = rootView.findViewById(R.id.previousStep);
        Button mNextStep = rootView.findViewById(R.id.nextStep);

        mPrevStep.setOnClickListener(view -> {
            mPrevStep.setVisibility(View.VISIBLE);
            if (steps.get(selectedIndex).getId() > 0) {
                if (mExoPlayer != null) {
                    mExoPlayer.stop();
                }
                itemClickListener.onListItemClick(steps, steps.get(selectedIndex).getId() - 1, recipeName);
            } else {
                mPrevStep.setVisibility(View.GONE);

            }
        });

        mNextStep.setOnClickListener(view -> {
            mNextStep.setVisibility(View.VISIBLE);
            int lastIndex = steps.size() - 1;
            if (steps.get(selectedIndex).getId() < steps.get(lastIndex).getId()) {
                if (mExoPlayer != null) {
                    mExoPlayer.stop();
                }
                itemClickListener.onListItemClick(steps, steps.get(selectedIndex).getId() + 1, recipeName);
            } else {
                mNextStep.setVisibility(View.GONE);

            }
        });


        return rootView;
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(getContext(), "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    Objects.requireNonNull(getContext()), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList("selected_steps", steps);
        currentState.putParcelableArrayList("selected_recipes", recipe);
        currentState.putInt("index", selectedIndex);
        currentState.putString("Title", recipeName);
    }

    public boolean isInLandscapeMode(Context context) {
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
    }
}
