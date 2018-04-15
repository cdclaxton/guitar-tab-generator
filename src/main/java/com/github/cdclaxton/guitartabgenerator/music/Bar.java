package com.github.cdclaxton.guitartabgenerator.music;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class Bar {

    private final TimeSignature timeSignature;
    private final List<Note> notes;
    private final List<TimedChord> timedChords;

    public enum TimeSignature { Four4, Six8 }

    /**
     * Instantiate a bar.
     *
     * @param timeSignature Time signature of the bar.
     * @param notes Notes in the bar.
     * @param timedChords Chords in the bar.
     */
    public Bar(final TimeSignature timeSignature,
               final List<Note> notes,
               final List<TimedChord> timedChords) {

        this.timeSignature = timeSignature;

        // Make a deep copy of the notes
        this.notes = notes.stream().map(Note::newInstance).collect(Collectors.toList());

        // Make a deep copy of the timed chords
        this.timedChords = timedChords.stream().map(TimedChord::newInstance).collect(Collectors.toList());
    }

    /**
     * Create a new instance of the bar.
     *
     * @param bar Bar.
     * @return New instance.
     */
    public static Bar newInstance(Bar bar) {
        return new Bar(bar.timeSignature, bar.getNotes(), bar.getTimedChords());
    }

    /**
     * Get the time signature of the bar.
     *
     * @return Time signature.
     */
    public TimeSignature getTimeSignature() {
        return timeSignature;
    }

    /**
     * Get the list of notes in the bar.
     *
     * @return List of notes.
     */
    public List<Note> getNotes() {
        return notes.stream().map(Note::newInstance).collect(Collectors.toList());
    }

    /**
     * Get the list of timed chords for the bar.
     *
     * @return Timed chords.
     */
    public List<TimedChord> getTimedChords() {
        return timedChords.stream().map(TimedChord::newInstance).collect(Collectors.toList());
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
