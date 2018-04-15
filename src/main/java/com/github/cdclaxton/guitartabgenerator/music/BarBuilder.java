package com.github.cdclaxton.guitartabgenerator.music;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for constructing a Bar.
 */
public final class BarBuilder {

    private final Bar.TimeSignature timeSignature;
    private final List<TimedChord> timedChords = new ArrayList<>();
    private final List<Note> notes = new ArrayList<>();

    /**
     * Instantiate building a bar.
     *
     * @param timeSignature Time signature of the bar.
     */
    public BarBuilder(final Bar.TimeSignature timeSignature) {
        this.timeSignature = timeSignature;
    }

    /**
     * Add a timed chord to the bar.
     *
     * @param sixteenthTiming Timing of the chord (in sixteenth notes).
     * @param chord Chord.
     * @return BarBuilder.
     * @throws InvalidTimingException Sixteenth note timing is invalid.
     * @throws InvalidChordException Chord is invalid.
     */
    public BarBuilder addTimedChord(int sixteenthTiming, String chord)
            throws InvalidTimingException, InvalidChordException {

        this.timedChords.add(new TimedChord(new Timing(sixteenthTiming), chord));
        return this;
    }

    /**
     * Add a note to the bar.
     *
     * @param stringNumber String number (1 to 6).
     * @param fret Fret number.
     * @param sixteenthTiming Timing (in sixteenth notes).
     * @return BarBuilder.
     * @throws InvalidStringException String number is invalid.
     * @throws InvalidFretNumberException Fret number is invalid.
     * @throws InvalidTimingException Timing is invalid.
     */
    public BarBuilder addNote(int stringNumber, int fret, int sixteenthTiming)
            throws InvalidStringException, InvalidFretNumberException, InvalidTimingException {

        this.notes.add(new Note(new Fret(stringNumber, fret), new Timing(sixteenthTiming)));
        return this;
    }

    /**
     * Construct a Bar object from the BarBuilder object.
     *
     * @return Constructed Bar.
     */
    public Bar build() {
        return new Bar(this.timeSignature, this.notes, this.timedChords);
    }

}
