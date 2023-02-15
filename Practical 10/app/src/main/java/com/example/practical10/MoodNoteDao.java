package com.example.practical10;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;
//2. Create the DAO to access to MoodNote database
@Dao
public interface MoodNoteDao {
    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    // the convenience method - insert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(MoodNote note);
// the query method
@Query("DELETE FROM MoodNote")
void deleteAll();
    // LiveData works with Room database to get instant update whenever there isany changes
    @Query("SELECT * FROM MoodNote ORDER BY mDate ASC")
    LiveData<List<MoodNote>> getAscendingNote();
}