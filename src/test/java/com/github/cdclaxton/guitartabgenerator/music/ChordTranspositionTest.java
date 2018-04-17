package com.github.cdclaxton.guitartabgenerator.music;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ChordTranspositionTest {

    @Test
    void testIsMinorKey() {
        assertTrue(ChordTransposition.isMinorKey("Dm"));
        assertTrue(ChordTransposition.isMinorKey("D#m"));
        assertFalse(ChordTransposition.isMinorKey("D"));
        assertFalse(ChordTransposition.isMinorKey("D#"));
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
        assertEquals(0, ChordTransposition.rootNoteToIndex("A"));
        assertEquals(1, ChordTransposition.rootNoteToIndex("A#"));
        assertEquals(1, ChordTransposition.rootNoteToIndex("Bb"));
        assertEquals(11, ChordTransposition.rootNoteToIndex("G#"));
        assertEquals(11, ChordTransposition.rootNoteToIndex("Ab"));
    }

    @Test
    void testNumSemitones() throws TranspositionException {
        assertEquals(0, ChordTransposition.numSemitones("A", "A"));
        assertEquals(0, ChordTransposition.numSemitones("C#m", "C#m"));

        assertEquals(1, ChordTransposition.numSemitones("A", "A#"));
        assertEquals(1, ChordTransposition.numSemitones("C#m", "Dm"));

        assertEquals(2, ChordTransposition.numSemitones("A", "B"));
        assertEquals(2, ChordTransposition.numSemitones("C#m", "D#m"));
        assertEquals(2, ChordTransposition.numSemitones("F#", "G#"));
        assertEquals(2, ChordTransposition.numSemitones("G", "A"));

        assertEquals(3, ChordTransposition.numSemitones("A", "C"));
        assertEquals(3, ChordTransposition.numSemitones("C#m", "Em"));

        assertEquals(-1, ChordTransposition.numSemitones("G", "F#"));
        assertEquals(-1, ChordTransposition.numSemitones("F", "E"));
        assertEquals(-1, ChordTransposition.numSemitones("A", "Ab"));

        assertEquals(-2, ChordTransposition.numSemitones("A", "G"));
        assertEquals(-2, ChordTransposition.numSemitones("G", "F"));
        assertEquals(-2, ChordTransposition.numSemitones("F", "Eb"));
        assertEquals(-2, ChordTransposition.numSemitones("Bb", "Ab"));
    }

    @Test
    void transposeChord() throws InvalidChordException, TranspositionException {

        // No transposition
        assertEquals(Chord.build("A"),
                ChordTransposition.transposeChord(Chord.build("A"), "A", "A"));

        assertEquals(Chord.build("D"),
                ChordTransposition.transposeChord(Chord.build("D"), "A", "A"));

        assertEquals(Chord.build("C#m"),
                ChordTransposition.transposeChord(Chord.build("C#m"), "C#m", "C#m"));

        assertEquals(Chord.build("C#m7"),
                ChordTransposition.transposeChord(Chord.build("C#m7"), "C#m", "C#m"));

        // Transpose
        assertEquals(Chord.build("Bb"),
                ChordTransposition.transposeChord(Chord.build("A"), "A", "Bb"));

        assertEquals(Chord.build("Bb"),
                ChordTransposition.transposeChord(Chord.build("A"), "A", "Bb"));

        assertEquals(Chord.build("C"),
                ChordTransposition.transposeChord(Chord.build("A"), "A", "C"));

        assertEquals(Chord.build("G"),
                ChordTransposition.transposeChord(Chord.build("F"), "C", "D"));

        assertEquals(Chord.build("G#m"),
                ChordTransposition.transposeChord(Chord.build("F#m"), "G", "A"));

        // Transpose with slash chords
        assertEquals(Chord.build("A/C#"),
                ChordTransposition.transposeChord(Chord.build("A/C#"), "A", "A"));

        assertEquals(Chord.build("B/D#"),
                ChordTransposition.transposeChord(Chord.build("A/C#"), "A", "B"));

        assertEquals(Chord.build("C/E"),
                ChordTransposition.transposeChord(Chord.build("A/C#"), "A", "C"));

        assertEquals(Chord.build("B/D#"),
                ChordTransposition.transposeChord(Chord.build("A/C#"), "D", "E"));

        assertEquals(Chord.build("C#m/B"),
                ChordTransposition.transposeChord(Chord.build("Bm/A"), "G", "A"));
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