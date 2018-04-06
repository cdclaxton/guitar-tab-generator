package com.github.cdclaxton.guitartabgenerator.music;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BarTranspositionTest {

    @Test
    void transposeBar() {
    }

    @Test
    void numSemitonesDifferent() throws TranspositionException {
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