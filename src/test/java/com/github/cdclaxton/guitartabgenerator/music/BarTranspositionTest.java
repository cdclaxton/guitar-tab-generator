package com.github.cdclaxton.guitartabgenerator.music;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BarTranspositionTest {

    /**
     * Generate a random integer in the interval [min, max].
     *
     * @param min Minimum value.
     * @param max Maximum value.
     * @return Random integer in the range [min, max].
     */
    private int randomInteger(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    /**
     * Generate a random timing value in the interval [0,15].
     *
     * @return Random timing.
     */
    private int randomTiming() {
        return randomInteger(0, 15);
    }

    /**
     * Generate a random fret number in the range [minFret, maxFret].
     *
     * @param minFret Minimum fret number.
     * @param maxFret Maximum fret number.
     * @return Random fret.
     */
    private int randomFret(int minFret, int maxFret) {
        return randomInteger(minFret, maxFret);
    }

    @Test
    void testTransposeBarSingleNote() throws InvalidTimingException, InvalidChordException,
            InvalidStringException, InvalidFretNumberException, TranspositionException {

        // Single note - same string before and after transposition
        for (int stringNumber = 1; stringNumber <= 6; stringNumber++) {

            int chordTiming = randomTiming();
            int noteTiming = randomTiming();
            int initialFret = randomFret(1,22);

            Bar bar = new BarBuilder(Bar.TimeSignature.Four4)
                    .addTimedChord(chordTiming, "C")
                    .addNote(1, initialFret, noteTiming)
                    .build();

            Bar barExpected = new BarBuilder(Bar.TimeSignature.Four4)
                    .addTimedChord(chordTiming, "B")
                    .addNote(1,initialFret-1, noteTiming)
                    .build();

            Bar barTransposed = BarTransposition.transposeBar(bar, "C", "B", false);
            assertEquals(barExpected, barTransposed);
        }

    }

    @Test
    void testTransposeBarTwoNotes() throws InvalidTimingException, InvalidChordException,
            InvalidStringException, InvalidFretNumberException, TranspositionException {

        for (int lowerString = 6; lowerString > 1; lowerString--) {

            int upperString = lowerString - 1;
            int chordTiming = randomTiming();
            int noteTiming = randomTiming();

            Bar bar = new BarBuilder(Bar.TimeSignature.Four4)
                    .addTimedChord(chordTiming, "C")
                    .addNote(lowerString, 2, noteTiming)
                    .addNote(upperString, 2, noteTiming)
                    .build();

            Bar barExpected = new BarBuilder(Bar.TimeSignature.Four4)
                    .addTimedChord(chordTiming, "D")
                    .addNote(upperString, 4, noteTiming)
                    .addNote(upperString, 4, noteTiming)
                    .build();

            Bar barTransposed = BarTransposition.transposeBar(bar, "C", "D", false);
            assertEquals(barExpected, barTransposed);
        }

    }

    @Test
    void testTransposeBarSingleNoteStringUp() {

    }

    @Test
    void testTransposeBarSingleNoteStringDown() {

    }

    @Test
    void testTransposeBarAbove22ndFret() {

    }

    @Test
    void testTransposeBarBelow0thFret() {

    }

    @Test
    void testTransposeBarTwoNotesSameString() throws InvalidTimingException, InvalidChordException,
            InvalidStringException, InvalidFretNumberException, TranspositionException {

        // Notes that would fall on the same string after transposition
        Bar bar1 = new BarBuilder(Bar.TimeSignature.Four4)
                .addTimedChord(0, "C")
                .addNote(1,1,0)
                .addNote(2,3,4)
                .build();
        Bar bar1Expected = new BarBuilder(Bar.TimeSignature.Four4)
                .addTimedChord(0, "Bb")
                .addNote(2,4,0)
                .addNote(4,6,4)
                .build();
        Bar bar1Transposed = BarTransposition.transposeBar(bar1, "C", "Bb", false);
        assertEquals(bar1Expected, bar1Transposed);
    }

    @Test
    void testNumSemitonesDifferent() throws TranspositionException {
        // Same key
        assertEquals(0, BarTransposition.numSemitonesDifferent("B", "B", true));
        assertEquals(0, BarTransposition.numSemitonesDifferent("C#m", "C#m", true));
        assertEquals(0, BarTransposition.numSemitonesDifferent("Bb", "Bb", true));
        assertEquals(0, BarTransposition.numSemitonesDifferent("C#", "C#", true));

        // Down
        assertEquals(-1, BarTransposition.numSemitonesDifferent("B", "A#", false));
        assertEquals(-2, BarTransposition.numSemitonesDifferent("B", "A", false));
        assertEquals(-3, BarTransposition.numSemitonesDifferent("B", "Ab", false));
        assertEquals(-3, BarTransposition.numSemitonesDifferent("B", "G#", false));
        assertEquals(-1, BarTransposition.numSemitonesDifferent("Cm", "Bm", false));
        assertEquals(-2, BarTransposition.numSemitonesDifferent("C#m", "Bm", false));
        assertEquals(-2, BarTransposition.numSemitonesDifferent("A", "G", false));
        assertEquals(-5, BarTransposition.numSemitonesDifferent("A", "D", false));

        // Up
        assertEquals(1, BarTransposition.numSemitonesDifferent("B", "C", true));
        assertEquals(2, BarTransposition.numSemitonesDifferent("B", "C#", true));
        assertEquals(3, BarTransposition.numSemitonesDifferent("B", "D", true));
        assertEquals(4, BarTransposition.numSemitonesDifferent("B", "D#", true));
        assertEquals(4, BarTransposition.numSemitonesDifferent("B", "Eb", true));
        assertEquals(2, BarTransposition.numSemitonesDifferent("G", "A", true));
        assertEquals(5, BarTransposition.numSemitonesDifferent("E", "A", true));
    }

    @Test
    void transposeNotes() {
    }

    @Test
    void transposeChords() {
    }
}