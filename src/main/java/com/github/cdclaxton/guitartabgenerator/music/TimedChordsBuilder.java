package com.github.cdclaxton.guitartabgenerator.music;

import java.util.ArrayList;
import java.util.List;

public final class TimedChordsBuilder {

    private final List<TimedChord> timedChords = new ArrayList<>();

    TimedChordsBuilder() {}

    /**
     * Add a timed chord.
     *
     * @param chord Chord.
     * @param timing Timing of the chord.
     * @return Builder.
     * @throws InvalidTimingException Timing is invalid.
     * @throws InvalidChordException Chord is invalid.
     */
    TimedChordsBuilder addTimedChord(String chord, int timing) throws InvalidTimingException, InvalidChordException {
        this.timedChords.add(new TimedChord(new Timing(timing), chord));
        return this;
    }

    /**
     * Construct a list of timed chords.
     *
     * @return List of timed chords.
     */
    List<TimedChord> build() {
        return this.timedChords;
    }
}
