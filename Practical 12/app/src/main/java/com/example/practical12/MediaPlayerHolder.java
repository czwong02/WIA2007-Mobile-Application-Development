package com.example.practical12;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import androidx.annotation.RequiresApi;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
//actual implementation of media player
public class MediaPlayerHolder implements PlayerAdapter {
    //the time duration (in MS) to update the seekbar playback position
    public static final int PLAYBACK_POSITION_REFRESH_INTERVAL_MS = 1000;
    //Context - our application environment to access resources and start process
    private final Context mContext;
    private MediaPlayer mMediaPlayer;
    private int mResourceId;
    private PlaybackInfoListener mPlaybackInfoListener;
    //ScheduledExercuterService schedules command to run on particular time / interval
    private ScheduledExecutorService mExecutor;
    //Runnable to run the codes on thread
    private Runnable mSeekbarPositionUpdateTask;
    //get the calling environment and roll the tasks!
    public MediaPlayerHolder(Context context){
        mContext = context.getApplicationContext();
    }
    //Should only be called on the given API level or higher
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void loadMedia(int resourceId){
        mResourceId = resourceId;
        initializeMediaPlayer();
        AssetFileDescriptor assetFileDescriptor =
                mContext.getResources().openRawResourceFd(mResourceId);
        try{
            logToUI("> Trying to load the mp3. [03:loadMedia()]");
            mMediaPlayer.setDataSource(assetFileDescriptor);
        } catch (IOException e) {
            logToUI(e.toString());
        }
        //Transfer MediaPlayer object to the Prepared State before it can be Started. Refer to lifecycle of MediaPlayer
        try{
            logToUI("> Prepare the media player. [03:loadMedia()]");
            mMediaPlayer.prepare();
        } catch (IOException e) {
            logToUI(e.toString());
        }
        initializeProgressCallback();
        //logToUI("> Initialize ProgressCallback()");
    }
    //Create Media Player object
    private void initializeMediaPlayer() {
        if(mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                //When the playback reaches the end of the stream
                //Display the completion information and update the state.
                @Override
                public void onCompletion(MediaPlayer mediaPlayer){
                    stopUpdatingCallbackWithPosition(true);
                    logToUI("> MediaPlayer playback completed");
                    if (mPlaybackInfoListener != null) {
                        mPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.COMPLETED
                        );
                        mPlaybackInfoListener.onPlaybackCompleted();
                    }
                }
            });
            logToUI("> MediaPlayer object created and onCompletionListener setup ready. " +
                    "[03:initializeMediaPlayer()]");
        }
    }
    private void logToUI(String s) {
        if(mPlaybackInfoListener != null){
            mPlaybackInfoListener.onLogUpdated(s);
        }
    }
    @Override
    public void initializeProgressCallback() {
        //get the duration of the media file
        final int duration = mMediaPlayer.getDuration();
        if(mPlaybackInfoListener != null) {
            mPlaybackInfoListener.onDurationChanged(duration);
            mPlaybackInfoListener.onPositionChanged(0);
            logToUI(String.format("Setting the playback duration (%d sec). [03:initializeProgressCallback()]",
            TimeUnit.MILLISECONDS.toSeconds(duration)));
            logToUI("> Setting playback position to 0. [03:initializeProgressCallback()]");
        }
    }
    public void setPlaybackInfoListener(PlaybackInfoListener listener){
        mPlaybackInfoListener = listener;
    }
    @Override
    public void release() {
        if(mMediaPlayer != null) {
            logToUI("> Release and set MediaPlayer to null. [03:release()]");
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
    @Override
    public boolean isPlaying() {
        if(mMediaPlayer != null){
            logToUI("> Media player is playing. [03:isPlaying()]");
            return mMediaPlayer.isPlaying();
        }
        logToUI("> Media player is not playing. [03:isPlaying()]");
        return false;
    }
    @Override
    public void play() {
        if(mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            logToUI(String.format("> Starting to play my favorite song - %s. [03:play()]",

            mContext.getResources().getResourceEntryName(mResourceId)));
            mMediaPlayer.start();
            if(mPlaybackInfoListener != null){
                logToUI("> Media player is currently playing. [03:play()]");

                mPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.PLAYING);
            }
            startUpdatingCallbackWithPosition();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void reset() {
        if(mMediaPlayer != null) {
            logToUI("> Resetting media player. [03:reset()]");
            mMediaPlayer.reset();
            loadMedia(mResourceId);
            if(mPlaybackInfoListener != null){

                mPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.RESET);
            }
            stopUpdatingCallbackWithPosition(true);
        }
    }
    private void stopUpdatingCallbackWithPosition(boolean
                                                          resetUIPlaybackPosition) {
        logToUI("> Resetting executor. [03:stopUpdatingCallbackWithPosition()]");
        if(mExecutor != null){
            mExecutor.shutdownNow();
            mExecutor = null;
            mSeekbarPositionUpdateTask = null;
            if(resetUIPlaybackPosition && mPlaybackInfoListener != null){
                mPlaybackInfoListener.onPositionChanged(0);
            }
        }
    }
    private void startUpdatingCallbackWithPosition() {
        logToUI("> Starting executor. [03:startUpdatingCallbackWithPosition()]");
        if (mExecutor == null) {
            mExecutor = Executors.newSingleThreadScheduledExecutor();
        }
        if(mSeekbarPositionUpdateTask == null) {
            mSeekbarPositionUpdateTask = new Runnable() {
                @Override
                public void run() {
                    updateProgressCallbackTask();
                }
            };
        }
        mExecutor.scheduleAtFixedRate(
                mSeekbarPositionUpdateTask, 0,
                PLAYBACK_POSITION_REFRESH_INTERVAL_MS,
                TimeUnit.MILLISECONDS
        );
    }
    //Updating the progress of media player
    private void updateProgressCallbackTask() {
        if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
            int currentPosition = mMediaPlayer.getCurrentPosition();
            if(mPlaybackInfoListener != null){
                mPlaybackInfoListener.onPositionChanged(currentPosition);
            }
        }
    }
    @Override
    public void pause() {
        if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
            if(mPlaybackInfoListener != null){

                mPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.PAUSED);
            }
        }
        logToUI("> Pausing the playback. [03:pause()]");
    }
    @Override
    public void seekTo(int position) {
        if (mMediaPlayer != null){
            logToUI(String.format("> Changing position to %d ms. [03: seekTo()]", position));
            mMediaPlayer.seekTo(position);
        }
    }
}