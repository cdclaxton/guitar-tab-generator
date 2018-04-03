package com.github.cdclaxton.guitartabgenerator.music;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TranspositionTest {

    @Test
    void testIsMinorKey() {
        assertEquals(true, Transposition.isMinorKey("Dm"));
        assertEquals(true, Transposition.isMinorKey("D#m"));
        assertEquals(false, Transposition.isMinorKey("D"));
        assertEquals(false, Transposition.isMinorKey("D#"));
    }

    @Test
    void testRemoveMinor() {
        assertEquals("D", Transposition.removeMinor("D"));
        assertEquals("C#", Transposition.removeMinor("C#m"));
        assertEquals("Gb", Transposition.removeMinor("Gbm"));
        assertEquals("Gb", Transposition.removeMinor("Gb"));
    }

    @Test
    void testBaseNoteToIndex() throws TranspositionException {
        assertEquals(0, Transposition.baseNoteToIndex("A"));
        assertEquals(1, Transposition.baseNoteToIndex("A#"));
        assertEquals(1, Transposition.baseNoteToIndex("Bb"));
        assertEquals(11, Transposition.baseNoteToIndex("G#"));
        assertEquals(11, Transposition.baseNoteToIndex("Ab"));
    }

    @Test
    void testNumSemitones() throws TranspositionException {
        assertEquals(0, Transposition.numSemitones("A", "A"));
        assertEquals(0, Transposition.numSemitones("C#m", "C#m"));

        assertEquals(1, Transposition.numSemitones("A", "A#"));
        assertEquals(1, Transposition.numSemitones("C#m", "Dm"));

        assertEquals(2, Transposition.numSemitones("A", "B"));
        assertEquals(2, Transposition.numSemitones("C#m", "D#m"));

        assertEquals(3, Transposition.numSemitones("A", "C"));
        assertEquals(3, Transposition.numSemitones("C#m", "Em"));

        assertEquals(-1, Transposition.numSemitones("G", "F#"));
        assertEquals(-1, Transposition.numSemitones("F", "E"));
        assertEquals(-1, Transposition.numSemitones("A", "Ab"));

        assertEquals(-2, Transposition.numSemitones("A", "G"));
        assertEquals(-2, Transposition.numSemitones("G", "F"));
        assertEquals(-2, Transposition.numSemitones("F", "Eb"));
    }

    @Test
    void testSplitChord() throws TranspositionException {
        assertEquals("C", Transposition.splitChord("C").getBase());
        assertEquals("", Transposition.splitChord("C").getRest());

        assertEquals("C#", Transposition.splitChord("C#m").getBase());
        assertEquals("m", Transposition.splitChord("C#m").getRest());

        assertEquals("C#", Transposition.splitChord("C#m7").getBase());
        assertEquals("m7", Transposition.splitChord("C#m7").getRest());

        assertEquals("Bb", Transposition.splitChord("Bb").getBase());
        assertEquals("", Transposition.splitChord("Bb").getRest());

        assertEquals("Bb", Transposition.splitChord("Bbm").getBase());
        assertEquals("m", Transposition.splitChord("Bbm").getRest());

        assertEquals("Bb", Transposition.splitChord("Bbm7").getBase());
        assertEquals("m7", Transposition.splitChord("Bbm7").getRest());
    }

    @Test
    void testTransposeChord() throws TranspositionException {
        assertEquals("A", Transposition.transposeChord("A", "A", "A"));
        assertEquals("C#m", Transposition.transposeChord("C#m", "E", "E"));
        assertEquals("C#m7", Transposition.transposeChord("C#m7", "E","E"));
        assertEquals("D", Transposition.transposeChord("D", "A", "A"));

        assertEquals("Bb", Transposition.transposeChord("A", "A", "Bb"));
        assertEquals("C", Transposition.transposeChord("A", "A", "C"));
        assertEquals("G", Transposition.transposeChord("F", "C", "D"));
        assertEquals("G#m", Transposition.transposeChord("F#m", "G", "A"));

        assertEquals("A/C#", Transposition.transposeChord("A/C#", "A", "A"));
        assertEquals("B/D#", Transposition.transposeChord("A/C#", "A", "B"));
        assertEquals("C/E", Transposition.transposeChord("A/C#", "A", "C"));
        assertEquals("B/D#", Transposition.transposeChord("A/C#", "D", "E"));
        assertEquals("C#m/B", Transposition.transposeChord("Bm/A", "G", "A"));
    }

    @Test
    void testTransposeNoteIndex() throws TranspositionException {
        assertEquals(0, Transposition.transposeNoteIndex(0, 0));
        assertEquals(1, Transposition.transposeNoteIndex(0, 1));
        assertEquals(2, Transposition.transposeNoteIndex(0, 2));
        assertEquals(5, Transposition.transposeNoteIndex(3, 2));
        assertEquals(11, Transposition.transposeNoteIndex(10, 1));

        assertEquals(0, Transposition.transposeNoteIndex(2, -2));
        assertEquals(0, Transposition.transposeNoteIndex(3, -3));
        assertEquals(6, Transposition.transposeNoteIndex(9, -3));
        assertEquals(11, Transposition.transposeNoteIndex(0, -1));
        assertEquals(10, Transposition.transposeNoteIndex(0, -2));
        assertEquals(9, Transposition.transposeNoteIndex(0, -3));
    }

}