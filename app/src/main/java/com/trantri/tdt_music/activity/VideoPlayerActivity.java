package com.trantri.tdt_music.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.trantri.tdt_music.data.Constraint;
import com.trantri.tdt_music.databinding.ActivityVideoPlayerBinding;
import com.trantri.tdt_music.utils.NetworkUtils;
import com.trantri.tdt_music.utils.SharedPrefUtils;

public class VideoPlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private ActivityVideoPlayerBinding binding;
    private YouTubePlayer mCurrentYoutubePlayer;
    private String mCurrentVideoId = null;

    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // config dialog
        builder = new AlertDialog.Builder(VideoPlayerActivity.this);
        builder.setTitle("Exit")
                .setMessage("You want exit this video?")
                .setPositiveButton("OK", (dialog, which) -> onBackPressed())
                .create();

        // get data
        getData();
        if (NetworkUtils.isOnline(this)) {
            if (mCurrentVideoId != null) {
                binding.youtubePlayer.initialize(mCurrentVideoId, this);
            }
        } else {
            Log.d(TAG, "onCreate: ");
            Toast.makeText(this, "Please check your wifi", Toast.LENGTH_SHORT).show();
        }
    }

    private void getData() {
        Intent intent = getIntent();

        mCurrentVideoId = intent.getStringExtra(Constraint.YoutubeVideo.VIDEO_ID);
    }

    @Override
    protected void onPause() {
        SharedPrefUtils.getInstance().put(Constraint.SharedPref.TIME_VIDEO, mCurrentYoutubePlayer.getCurrentTimeMillis());
        Log.d(TAG, "onPause: " + mCurrentYoutubePlayer.getCurrentTimeMillis());
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        SharedPrefUtils.getInstance().put(Constraint.SharedPref.TIME_VIDEO, 0);
        super.onDestroy();
    }

    private static final String TAG = "LOG_VideoPlayerActivity";

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        mCurrentYoutubePlayer = youTubePlayer;

        int time = SharedPrefUtils.getInstance().get(Constraint.SharedPref.TIME_VIDEO, Integer.class, 0);
        Log.d(TAG, "onInitializationSuccess: " + time);
        youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);

        youTubePlayer.loadVideo(mCurrentVideoId);
        youTubePlayer.setFullscreen(true);

        youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onLoading() {
                int time = SharedPrefUtils.getInstance().get(Constraint.SharedPref.TIME_VIDEO, Integer.class);

//                Log.d(TAG, "onInitializationSuccess: " + time + " - " + time1);
            }

            @Override
            public void onLoaded(String s) {
//                youTubePlayer.seekRelativeMillis(SharedPrefUtils.getInstance().get(Constraint.SharedPref.TIME_VIDEO, Integer.class, 0));
                youTubePlayer.seekToMillis(time);
            }

            @Override
            public void onAdStarted() {
                youTubePlayer.seekToMillis(time);

            }

            @Override
            public void onVideoStarted() {
                youTubePlayer.seekToMillis(time);

            }

            @Override
            public void onVideoEnded() {
                Toast.makeText(VideoPlayerActivity.this, "video ended", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {

            }
        });

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Log.d(TAG, "onInitializationFailure: fail");
//        Toast.makeText(this, youTubeInitializationResult.toString(), Toast.LENGTH_SHORT).show();
    }
}