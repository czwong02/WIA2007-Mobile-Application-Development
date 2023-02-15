package com.example.practical10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    public static final int NEW_NOTE_ACTIVITY_REQUEST_CODE = 1;
    private MoodNoteViewModel mNoteViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.RVMoodNote);
        final MoodNoteListAdapter adapter = new MoodNoteListAdapter(new
                MoodNoteListAdapter.NoteDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNoteViewModel = new
                ViewModelProvider(this).get(MoodNoteViewModel.class);
        mNoteViewModel.getAllNotes().observe(this, notes -> {
            // Update the cached copy of the words in the adapter.
            adapter.submitList(notes);
        });
        FloatingActionButton fab = findViewById(R.id.FABAddNote);
        fab.setOnClickListener( view -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivityForResult(intent, NEW_NOTE_ACTIVITY_REQUEST_CODE);
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_NOTE_ACTIVITY_REQUEST_CODE && resultCode ==
                RESULT_OK) {
            int mood =
                    Integer.parseInt(data.getStringExtra(AddNoteActivity.ExtraMood));
            boolean daynight =
                    Boolean.parseBoolean(data.getStringExtra(AddNoteActivity.ExtraDayNight));
            MoodNote note = new
                    MoodNote(data.getStringExtra(AddNoteActivity.ExtraDate), mood,
                    daynight, data.getStringExtra(AddNoteActivity.ExtraNote));
            mNoteViewModel.insert(note);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}