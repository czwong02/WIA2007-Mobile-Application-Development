package com.example.practical10;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
//1. Room will use these data to create a table in sqlite database
@Entity
public class MoodNote {
    @PrimaryKey(autoGenerate = true)
    public int mNoteID;
    public String mNote;
    @NonNull
    public String mDate;
    @NonNull
    public int mMood;
    @NonNull
    public boolean mDayNight;
    public MoodNote(@NonNull String date, @NonNull int mood, @NonNull boolean
            dayNight, String note) {
        this.mDate = date;
        this.mMood = mood;
        this.mDayNight = dayNight;
        this.mNote = note;
    }
    public String getmDate(){return this.mDate;}
    public int getmMood(){return this.mMood;}
    public boolean getmDayNight(){return this.mDayNight;}
    public String getmNote(){return this.mNote;}
}