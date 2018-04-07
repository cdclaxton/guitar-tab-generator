package com.github.cdclaxton.guitartabgenerator.music;

import java.util.ArrayList;
import java.util.List;

public final class BarBuilder {

    private Bar.TimeSignature timeSignature;
    private List<TimedChord> timedChords = new ArrayList<>();
    private List<Note> notes = new ArrayList<>();

    /**
     * Instantiate building a bar.
     *
     * @param timeSignature Time signature of the bar.
     */
    public BarBuilder(Bar.TimeSignature timeSignature) {
        this.timeSignature = timeSignature;
    }

    /**
     * Add a timed chord to the bar.
     *
     * @param sixteenthTiming Timing of the chord (in sixteenth notes).
     * @param chord Chord.
     * @return BarBuilder.
     * @throws InvalidTimingException
     * @throws InvalidChordException
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
     * @throws InvalidStringException
     * @throws InvalidFretNumberException
     * @throws InvalidTimingException
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
