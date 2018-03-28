package com.github.cdclaxton.music;

import java.util.List;

public class Bar {

    private TimeSignature timeSignature;
    private List<Note> notes;
    private List<TimedChord> timedChords;

    public enum TimeSignature { Four4, Six8 }

    public Bar(TimeSignature timeSignature, List<Note> notes, List<TimedChord> timedChords) {
        this.timeSignature = timeSignature;
        this.notes = notes;
        this.timedChords = timedChords;
    }

    public TimeSignature getTimeSignature() {
        return timeSignature;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public List<TimedChord> getTimedChords() {
        return timedChords;
    }
}
