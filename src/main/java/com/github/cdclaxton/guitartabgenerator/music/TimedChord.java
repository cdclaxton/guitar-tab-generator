package com.github.cdclaxton.guitartabgenerator.music;

import java.util.Objects;
import java.util.regex.Pattern;

public final class TimedChord {

    private final Timing timing;
    private final Chord chord;

    /**
     * Instantiate a timed chord (i.e. a chord with a time).
     *
     * @param timing Timing of the chord.
     * @param chord Chord.
     */
    public TimedChord(final Timing timing,
                      final Chord chord) {

        this.timing = Timing.newInstance(timing);
        this.chord = Chord.newInstance(chord);
    }

    /**
     * Create a new instance of the timed chord.
     *
     * @param timedChord Timed chord to copy.
     * @return New timed chord.
     */
    public static TimedChord newInstance(TimedChord timedChord) {
         return new TimedChord(Timing.newInstance(timedChord.timing), timedChord.chord);
    }

    /**
     * Get the chord.
     *
     * @return Chord.
     */
    public Chord getChord() {
        return Chord.newInstance(chord);
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
