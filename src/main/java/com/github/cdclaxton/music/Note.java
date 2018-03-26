package com.github.cdclaxton.music;

public class Note {

    private Fret fret;
    private Timing timing;

    public Note(Fret fret, Timing timing) {
        this.fret = fret;
        this.timing = timing;
    }

    public Fret getFret() {
        return fret;
    }

    public Timing getTiming() {
        return timing;
    }
}
