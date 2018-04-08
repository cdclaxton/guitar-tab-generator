package com.github.cdclaxton.guitartabgenerator.music;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bar bar = (Bar) o;
        return timeSignature == bar.timeSignature &&
                Objects.equals(notes, bar.notes) &&
                Objects.equals(timedChords, bar.timedChords);
    }

    @Override
    public int hashCode() {

        return Objects.hash(timeSignature, notes, timedChords);
    }

    @Override
    public String toString() {
        return "Bar[" + this.timeSignature + ",timedChords=" + this.timedChords + ",notes=" + this.notes + "]";
    }
}
