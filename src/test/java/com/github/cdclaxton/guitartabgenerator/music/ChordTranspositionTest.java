package com.github.cdclaxton.guitartabgenerator.music;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChordTranspositionTest {

    @Test
    void testIsMinorKey() {
        assertEquals(true, ChordTransposition.isMinorKey("Dm"));
        assertEquals(true, ChordTransposition.isMinorKey("D#m"));
        assertEquals(false, ChordTransposition.isMinorKey("D"));
        assertEquals(false, ChordTransposition.isMinorKey("D#"));
    }

    @Test
    void testRemoveMinor() {
        assertEquals("D", ChordTransposition.removeMinor("D"));
        assertEquals("C#", ChordTransposition.removeMinor("C#m"));
        assertEquals("Gb", ChordTransposition.removeMinor("Gbm"));
        assertEquals("Gb", ChordTransposition.removeMinor("Gb"));
    }

    @Test
    void testBaseNoteToIndex() throws TranspositionException {
        assertEquals(0, ChordTransposition.baseNoteToIndex("A"));
        assertEquals(1, ChordTransposition.baseNoteToIndex("A#"));
        assertEquals(1, ChordTransposition.baseNoteToIndex("Bb"));
        assertEquals(11, ChordTransposition.baseNoteToIndex("G#"));
        assertEquals(11, ChordTransposition.baseNoteToIndex("Ab"));
    }

    @Test
    void testNumSemitones() throws TranspositionException {
        assertEquals(0, ChordTransposition.numSemitones("A", "A"));
        assertEquals(0, ChordTransposition.numSemitones("C#m", "C#m"));

        assertEquals(1, ChordTransposition.numSemitones("A", "A#"));
        assertEquals(1, ChordTransposition.numSemitones("C#m", "Dm"));

        assertEquals(2, ChordTransposition.numSemitones("A", "B"));
        assertEquals(2, ChordTransposition.numSemitones("C#m", "D#m"));

        assertEquals(3, ChordTransposition.numSemitones("A", "C"));
        assertEquals(3, ChordTransposition.numSemitones("C#m", "Em"));

        assertEquals(-1, ChordTransposition.numSemitones("G", "F#"));
        assertEquals(-1, ChordTransposition.numSemitones("F", "E"));
        assertEquals(-1, ChordTransposition.numSemitones("A", "Ab"));

        assertEquals(-2, ChordTransposition.numSemitones("A", "G"));
        assertEquals(-2, ChordTransposition.numSemitones("G", "F"));
        assertEquals(-2, ChordTransposition.numSemitones("F", "Eb"));
    }

    @Test
    void testSplitChord() throws TranspositionException {
        assertEquals("C", ChordTransposition.splitChord("C").getBase());
        assertEquals("", ChordTransposition.splitChord("C").getRest());

        assertEquals("C#", ChordTransposition.splitChord("C#m").getBase());
        assertEquals("m", ChordTransposition.splitChord("C#m").getRest());

        assertEquals("C#", ChordTransposition.splitChord("C#m7").getBase());
        assertEquals("m7", ChordTransposition.splitChord("C#m7").getRest());

        assertEquals("Bb", ChordTransposition.splitChord("Bb").getBase());
        assertEquals("", ChordTransposition.splitChord("Bb").getRest());

        assertEquals("Bb", ChordTransposition.splitChord("Bbm").getBase());
        assertEquals("m", ChordTransposition.splitChord("Bbm").getRest());

        assertEquals("Bb", ChordTransposition.splitChord("Bbm7").getBase());
        assertEquals("m7", ChordTransposition.splitChord("Bbm7").getRest());
    }

    @Test
    void testTransposeChord() throws TranspositionException {
        assertEquals("A", ChordTransposition.transposeChord("A", "A", "A"));
        assertEquals("C#m", ChordTransposition.transposeChord("C#m", "E", "E"));
        assertEquals("C#m7", ChordTransposition.transposeChord("C#m7", "E","E"));
        assertEquals("D", ChordTransposition.transposeChord("D", "A", "A"));

        assertEquals("Bb", ChordTransposition.transposeChord("A", "A", "Bb"));
        assertEquals("C", ChordTransposition.transposeChord("A", "A", "C"));
        assertEquals("G", ChordTransposition.transposeChord("F", "C", "D"));
        assertEquals("G#m", ChordTransposition.transposeChord("F#m", "G", "A"));

        assertEquals("A/C#", ChordTransposition.transposeChord("A/C#", "A", "A"));
        assertEquals("B/D#", ChordTransposition.transposeChord("A/C#", "A", "B"));
        assertEquals("C/E", ChordTransposition.transposeChord("A/C#", "A", "C"));
        assertEquals("B/D#", ChordTransposition.transposeChord("A/C#", "D", "E"));
        assertEquals("C#m/B", ChordTransposition.transposeChord("Bm/A", "G", "A"));
    }

    @Test
    void testTransposeNoteIndex() throws TranspositionException {
        assertEquals(0, ChordTransposition.transposeNoteIndex(0, 0));
        assertEquals(1, ChordTransposition.transposeNoteIndex(0, 1));
        assertEquals(2, ChordTransposition.transposeNoteIndex(0, 2));
        assertEquals(5, ChordTransposition.transposeNoteIndex(3, 2));
        assertEquals(11, ChordTransposition.transposeNoteIndex(10, 1));

        assertEquals(0, ChordTransposition.transposeNoteIndex(2, -2));
        assertEquals(0, ChordTransposition.transposeNoteIndex(3, -3));
        assertEquals(6, ChordTransposition.transposeNoteIndex(9, -3));
        assertEquals(11, ChordTransposition.transposeNoteIndex(0, -1));
        assertEquals(10, ChordTransposition.transposeNoteIndex(0, -2));
        assertEquals(9, ChordTransposition.transposeNoteIndex(0, -3));
    }

}