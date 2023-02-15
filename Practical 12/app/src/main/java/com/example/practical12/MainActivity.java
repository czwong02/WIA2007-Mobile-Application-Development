package com.example.practical12;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final int MEDIA_RES_ID =
            R.raw.yuna_dont_blame_it_on_love;
    private TextView mTextDebug;
    private SeekBar mSeekbarAudio;
    private ScrollView mScrollContainer;
    private PlayerAdapter mPlayerAdapter;
    private boolean mUserIsSeeking = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();
        initializeSeekbar();
        initializePlaybackController();
        Log.d(TAG, ">> Completed activity initialization. [04:onCreate()]");
    }
    private void initializeSeekbar() {
        mSeekbarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int userSelectedPosition = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    userSelectedPosition = progress;
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mUserIsSeeking = true;
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mUserIsSeeking = false;
                mPlayerAdapter.seekTo(userSelectedPosition);
            }
        });
    }
    private void initializePlaybackController() {
        MediaPlayerHolder mMediaPlayerHolder = new
                MediaPlayerHolder(this);
        Log.d(TAG, ">> Created MediaPlayerHolder [04:initializePlaybackController()]");
        mMediaPlayerHolder.setPlaybackInfoListener(new
                PlaybackListener());
        mPlayerAdapter = mMediaPlayerHolder;
        Log.d(TAG, ">> MediaPlayerHolder progress callback set. [04:initializePlaybackController()]");
    }
    private void initializeUI() {
        mTextDebug = findViewById(R.id.TVDebug);
        Button mPlayButton = findViewById(R.id.BtnPlay);
        Button mPauseButton = findViewById(R.id.BtnPause);
        Button mResetButton = findViewById(R.id.BtnReset);
        mSeekbarAudio = findViewById(R.id.SBAudio);
        mScrollContainer = findViewById(R.id.SVContainer);
        mPauseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) { mPlayerAdapter.pause(); }
        });
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { mPlayerAdapter.play(); }
        });
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { mPlayerAdapter.reset(); }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mPlayerAdapter.loadMedia(MEDIA_RES_ID);
        Log.d(TAG, ">> Create MediaPlayer. [04:onStart()]");
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(isChangingConfigurations() && mPlayerAdapter.isPlaying()){
            Log.d(TAG, ">> Don't release MediaPlayer as screen is rotating and playing. [04:onStop()]");
        }
        else {
            mPlayerAdapter.release();
            Log.d(TAG, ">> Release MediaPlayer. [04:onStop()]");
        }
    }
    private class PlaybackListener extends PlaybackInfoListener {
        @Override
        void onDurationChanged(int duration) {
            mSeekbarAudio.setMax(duration);
            Log.d(TAG, String.format(">> Setting playback duration - setMax(%d). " +
                    "[04sub:onDurationChanged()]", duration));
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        void onPositionChanged(int position) {
            if(!mUserIsSeeking){
                mSeekbarAudio.setProgress(position, true);
                Log.d(TAG, String.format(">> Setting position changes - setProgress(%d). " + "[04sub:onPositionChanged()]", position));
            }
        }
        @Override
        void onStateChanged(int state) {
            String stateToString =
                    PlaybackInfoListener.convertStateToString(state);
            onLogUpdated(String.format(">> Changing state - (%s). " +
                    "[04sub:onStateChanged()]", stateToString));
        }
        @Override
        void onPlaybackCompleted() {
            onLogUpdated(">> Playback is completed. [04sub:onPlaybackCompleted()]");
        }
        @Override
        void onLogUpdated(String formattedMessage) {
            if(mTextDebug != null){
                mTextDebug.append(formattedMessage);
                mTextDebug.append("\n");
                //Moves the scrollContainer focus to the end
                mScrollContainer.post(
                        new Runnable() {
                            @Override
                            public void run() {

                                mScrollContainer.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        }
                );
            }
        }
    }
}