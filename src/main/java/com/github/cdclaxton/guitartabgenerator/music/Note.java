package com.github.cdclaxton.guitartabgenerator.music;

import java.util.Objects;

public final class Note {

    private final Fret fret;
    private final Timing timing;

    /**
     * Construct a representation of a note with time and position.
     *
     * @param fret Fret and string representation.
     * @param timing Timing of the note or chord.
     */
    public Note(Fret fret, Timing timing) {
        this.fret = Fret.newInstance(fret);
        this.timing = Timing.newInstance(timing);
    }

    /**
     * Get a new instance of the Note object.
     *
     * @param note Note.
     * @return New instance.
     */
    public static Note newInstance(Note note) {
        return new Note(Fret.newInstance(note.fret), Timing.newInstance(note.timing));
    }

    /**
     * Get the representation of the string and fret number.
     *
     * @return String and fret number.
     */
    public Fret getFret() {
        return Fret.newInstance(fret);
    }

    /**
     * Get the timing of the note or chord.
     *
     * @return Timing.
     */
    public Timing getTiming() {
        return Timing.newInstance(timing);
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
