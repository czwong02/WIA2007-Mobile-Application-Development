package com.example.practical12;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
//abstract PlaybackInfoListener to list out the possible states and changes (methods) during the playback
public abstract class PlaybackInfoListener {
    //Retention indicates how long annotations with the annotated type are to be retained
    //Set RetentionPolicy.SOURCE - Annotations are to be discarded by the compiler.
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({State.INVALID, State.PLAYING, State.PAUSED, State.RESET,
            State.COMPLETED})
            //@interface == annotation
    @interface State{
        int INVALID = -1;
        int PLAYING = 0;
        int PAUSED = 1;
        int RESET = 2;
        int COMPLETED = 3;
    }
    //Returns the string of the annotated state
    public static String convertStateToString(@State int state){
        String stateString;
        switch(state){
            case State.INVALID:
                stateString = "INVALID";
                break;
            case State.PLAYING:
                stateString = "PLAYING";
                break;
            case State.PAUSED:
                stateString = "PAUSED";
                break;
            case State.RESET:
                stateString = "RESET";
                break;
            case State.COMPLETED:
                stateString = "COMPLETED";
                break;
            default:
                stateString = "UNKNOWN STATE";
        }
        return stateString;
    }
    //to be implemented later
    void onLogUpdated(String formattedMessage){}
    void onDurationChanged(int duration){}
    void onPositionChanged(int position){}
    void onStateChanged(@State int state){}
    void onPlaybackCompleted(){}
}