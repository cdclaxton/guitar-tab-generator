package com.github.cdclaxton.guitartabgenerator.music;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "Note[" + this.fret.toString() + "," + this.timing.toString() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(fret, note.fret) &&
                Objects.equals(timing, note.timing);
    }

    @Override
    public int hashCode() {

        return Objects.hash(fret, timing);
    }
}
