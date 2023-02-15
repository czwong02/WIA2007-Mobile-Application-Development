package com.example.practical10;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
public class MoodNoteListAdapter extends ListAdapter<MoodNote,
        MoodNoteViewHolder> {
    public MoodNoteListAdapter(@NonNull DiffUtil.ItemCallback<MoodNote>
                                       diffCallback) {
        super(diffCallback);
    }
    @Override
    public MoodNoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return MoodNoteViewHolder.create(parent);
    }
    @Override
    public void onBindViewHolder(MoodNoteViewHolder holder, int position) {
        MoodNote current = getItem(position);
        holder.bind(current.getmDate(), current.getmMood(),
                current.getmDayNight(), current.getmNote());
    }
    static class NoteDiff extends DiffUtil.ItemCallback<MoodNote> {
        @Override
        public boolean areItemsTheSame(@NonNull MoodNote oldItem, @NonNull
                MoodNote newItem) {
            return oldItem == newItem;
        }
        @Override
        public boolean areContentsTheSame(@NonNull MoodNote oldItem, @NonNull
                MoodNote newItem) {
            return oldItem.getmNote().equals(newItem.getmNote());
        }
    }
}