package com.timsimonhughes.a04_bakingapp.controller.fragments;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.PlayerView;

import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.timsimonhughes.a04_bakingapp.R;
import com.timsimonhughes.a04_bakingapp.model.Step;
import com.timsimonhughes.a04_bakingapp.utils.Constants;
import com.timsimonhughes.a04_bakingapp.utils.ImageUtils;

public class StepFragment extends Fragment {

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    private String shortDescription, description, thumbnailUrl, videoUrl;
    private int stepId;
    private View mView;

    private TextView textViewStepOverview, textViewStepShortDescription;
    private ImageView mImageView, mFullScreenIcon;
    private Step mStep;
    private FrameLayout mFullScreenButton;

    private boolean mExoPlayerFullscreen;
    private Dialog mFullScreenDialog;
    private SimpleExoPlayer simpleExoPlayer;
    private PlayerView exoPlayerView;

    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;

    public StepFragment() {}

    public static StepFragment newInstance(Step step) {
        StepFragment stepFragment = new StepFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.EXTRA_STEP_ITEM, step);
        stepFragment.setArguments(bundle);
        return stepFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(Constants.KEY_PLAYER_POSITION, 0);
            playWhenReady = savedInstanceState.getBoolean(Constants.KEY_PLAY_WHEN_READY, true);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(Constants.KEY_PLAYER_POSITION, 0);
            playWhenReady = savedInstanceState.getBoolean(Constants.KEY_PLAY_WHEN_READY, true);
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (simpleExoPlayer != null) {
            playbackPosition = simpleExoPlayer.getCurrentPosition();
            playWhenReady = simpleExoPlayer.getPlayWhenReady();
            outState.putLong(Constants.KEY_PLAYER_POSITION, playbackPosition);
            outState.putBoolean(Constants.KEY_PLAY_WHEN_READY, playWhenReady);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_recipe_step, container, false);

        initViews();
        initFullScreenDialog();
        initFullscreenButton();


        if (getArguments() != null) {

            mStep = getArguments().getParcelable(Constants.EXTRA_STEP_ITEM);
            if (mStep != null) {

                stepId = mStep.getId();
                shortDescription = mStep.getShortDescription();
                description = mStep.getDescription();
                thumbnailUrl = mStep.getThumbnailUrl();
                videoUrl = mStep.getVideoUrl();

                if (videoUrl.isEmpty()) {
                    exoPlayerView.setVisibility(View.GONE);
                    ImageUtils.loadImage(getContext(), thumbnailUrl, mImageView);
                    mImageView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    initializePlayer();
                    prepareMediaSource(videoUrl);
                }
            }
        }

        textViewStepOverview.setText(description);
        textViewStepShortDescription.setText(shortDescription);

        return mView;
    }



    private void initViews() {
        textViewStepOverview = mView.findViewById(R.id.tv_step_overview);
        textViewStepShortDescription = mView.findViewById(R.id.tv_step_short_description);
        mImageView = mView.findViewById(R.id.iv_step_thumbnail);
        exoPlayerView = mView.findViewById(R.id.sepv_step_video);
    }

    private void initializePlayer() {
        if (simpleExoPlayer == null) {
            TrackSelection.Factory adaptiveTrackSelectionFactory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(adaptiveTrackSelectionFactory), new DefaultLoadControl());
            exoPlayerView.setPlayer(simpleExoPlayer);
            simpleExoPlayer.setPlayWhenReady(playWhenReady);
            simpleExoPlayer.seekTo(currentWindow, playbackPosition);
        }
        MediaSource mediaSource = buildMediaSourceFromUrl(Uri.parse(videoUrl));
        simpleExoPlayer.prepare(mediaSource, true, false);
    }

    private void prepareMediaSource(String mediaUrl) {
        MediaSource mediaSource = buildMediaSourceFromUrl(Uri.parse(mediaUrl));
        simpleExoPlayer.prepare(mediaSource, true, false);
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            playbackPosition = simpleExoPlayer.getCurrentPosition();
            currentWindow = simpleExoPlayer.getCurrentWindowIndex();
            playWhenReady = simpleExoPlayer.getPlayWhenReady();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    private MediaSource buildMediaSourceFromUrl(Uri uri) {
        return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(Constants.APP_PACKAGE_NAME))
                .createMediaSource(uri);
    }

    private void initFullScreenDialog() {
        if (getContext() != null) {
            mFullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
                @Override
                public void onBackPressed() {
                    if (mExoPlayerFullscreen) {
                        closeFullscreenDialog();
                    }
                    super.onBackPressed();
                }
            };
        }
    }

    private void openFullscreenDialog() {
        if (getContext() != null) {

            ((ViewGroup) exoPlayerView.getParent()).removeView(exoPlayerView);
            mFullScreenDialog.addContentView(exoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_exit));
            mExoPlayerFullscreen = true;
            mFullScreenDialog.show();
        }
    }

    private void closeFullscreenDialog() {
        if (getContext() != null) {
            ((ViewGroup) exoPlayerView.getParent()).removeView(exoPlayerView);
            ((FrameLayout) mView.findViewById(R.id.main_media_frame)).addView(exoPlayerView);
            mExoPlayerFullscreen = false;
            mFullScreenDialog.dismiss();
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen));
        }
    }

    private void initFullscreenButton() {

        PlaybackControlView controlView = exoPlayerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen)
                    openFullscreenDialog();
                else
                    closeFullscreenDialog();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        hideSystemUi();
        if ((Util.SDK_INT <= 23 || simpleExoPlayer == null)) {
            initializePlayer();
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


}


//    @SuppressLint("InlinedApi")
//    private void hideSystemUi() {
//        exoPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//    }
