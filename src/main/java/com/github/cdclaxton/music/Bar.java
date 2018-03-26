package com.github.cdclaxton.music;

import java.util.List;

public class Bar {

    private TimeSignature timeSignature;
    private List<Note> notes;
    private List<TimedChord> timedChords;

    public Bar(TimeSignature timeSignature, List<Note> notes, List<TimedChord> timedChords) {
        this.timeSignature = timeSignature;
        this.notes = notes;
        this.timedChords = timedChords;
    }

}
