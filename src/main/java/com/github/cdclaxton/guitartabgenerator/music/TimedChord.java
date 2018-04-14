package com.github.cdclaxton.guitartabgenerator.music;

import java.util.Objects;
import java.util.regex.Pattern;

public final class TimedChord {

    private final Timing timing;
    private final String chord;

    private static final String validNotes = "Ab|A|A#|Bb|B|C|C#|Db|D|D#|Eb|E|F|F#|Gb|G|G#";
    private static final String chordPattern = "^(" + validNotes + ")m?(/(" + validNotes + "))?$";
    private final Pattern pattern = Pattern.compile(chordPattern);

    /**
     * Instantiate a timed chord (i.e. a chord with a time).
     *
     * @param timing Timing of the chord.
     * @param chord Chord.
     * @throws InvalidChordException Invalid chord.
     */
    public TimedChord(Timing timing, String chord) throws InvalidChordException {
        if (!isValid(chord)) {
            throw new InvalidChordException("Chord (" + chord + ") is not valid");
        }

        this.timing = Timing.newInstance(timing);
        this.chord = chord;
    }

    /**
     * Create a new instance of the timed chord.
     *
     * @param timedChord Timed chord to copy.
     * @return New timed chord.
     */
    public static TimedChord newInstance(TimedChord timedChord) {
        try {
            return new TimedChord(Timing.newInstance(timedChord.timing), timedChord.chord);
        } catch (InvalidChordException e) {
            throw new IllegalStateException("Timed chord is now invalid!");
        }
    }

    /**
     * Is the chord valid?
     *
     * @param chord Chord to check.
     * @return True if the chord is valid.
     */
    private boolean isValid(String chord) {
        return chord.length() > 0 && this.pattern.matcher(chord).find();
    }

    /**
     * Get the chord.
     *
     * @return Chord.
     */
    public String getChord() {
        return chord;
    }

    /**
     * Get the timing.
     *
     * @return Timing.
     */
    public Timing getTiming() {
        return Timing.newInstance(timing);
    }

    @Override
    public String toString() {
        return "TimedChord[chord=" + this.getChord() + ",timing=" + this.getTiming() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimedChord that = (TimedChord) o;
        return Objects.equals(timing, that.timing) &&
                Objects.equals(chord, that.chord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timing, chord);
    }
}
