package com.github.cdclaxton.guitartabgenerator.music;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ChordTest {

    @Test
    void isChordValid() {

        // Major
        assertTrue(Chord.isChordValid("C"));
        assertTrue(Chord.isChordValid("C#"));
        assertTrue(Chord.isChordValid("B/D#"));

        // Minor
        assertTrue(Chord.isChordValid("Cm"));
        assertTrue(Chord.isChordValid("Cm/Eb"));
        assertTrue(Chord.isChordValid("C#m/E"));

        // Suspended
        assertTrue(Chord.isChordValid("Csus2"));
        assertTrue(Chord.isChordValid("Csus4"));
        assertTrue(Chord.isChordValid("Csus2/E"));
        assertTrue(Chord.isChordValid("Csus4/E"));

        // Diminished
        assertTrue(Chord.isChordValid("Cdim"));
        assertTrue(Chord.isChordValid("Cdim/E"));

        // Major seventh
        assertTrue(Chord.isChordValid("Gmaj7"));
        assertTrue(Chord.isChordValid("Gmaj7/E"));

        // Minor seventh
        assertTrue(Chord.isChordValid("Cmin7"));
        assertTrue(Chord.isChordValid("C#m7/E"));
        assertTrue(Chord.isChordValid("Dbm7/E"));

        // Extended
        assertTrue(Chord.isChordValid("C9"));
        assertTrue(Chord.isChordValid("C9/E"));
        assertTrue(Chord.isChordValid("C12"));
        assertTrue(Chord.isChordValid("C12/E"));

        // Augmented
        assertTrue(Chord.isChordValid("Aaug"));

        // Dominant seventh
        assertTrue(Chord.isChordValid("A7"));

        // Non-chords
        assertFalse(Chord.isChordValid("C/H"));
        assertFalse(Chord.isChordValid("Hi"));
    }

    @Test
    void build() throws TranspositionException {

        // Major
        assertEquals(new Chord("C", "", Optional.empty()), Chord.build("C"));
        assertEquals(new Chord("C#", "", Optional.empty()), Chord.build("C#"));
        assertEquals(new Chord("B", "", Optional.of("D#")), Chord.build("B/D#"));

        // Minor
        assertEquals(new Chord("C", "m", Optional.empty()), Chord.build("Cm"));
        assertEquals(new Chord("Db", "m", Optional.empty()), Chord.build("Dbm"));
        assertEquals(new Chord("C", "m", Optional.of("E")), Chord.build("Cm/E"));

        // Suspended
        assertEquals(new Chord("C", "sus2", Optional.empty()), Chord.build("Csus2"));
        assertEquals(new Chord("C", "sus4", Optional.empty()), Chord.build("Csus4"));
        assertEquals(new Chord("C", "sus2", Optional.of("E")), Chord.build("Csus2/E"));
        assertEquals(new Chord("C", "sus4", Optional.of("E")), Chord.build("Csus4/E"));

        // Diminished
        assertEquals(new Chord("C", "dim", Optional.empty()), Chord.build("Cdim"));
        assertEquals(new Chord("C", "dim", Optional.of("E")), Chord.build("Cdim/E"));

        // Major seventh
        assertEquals(new Chord("C", "maj7", Optional.empty()), Chord.build("Cmaj7"));
        assertEquals(new Chord("C", "maj7", Optional.of("E")), Chord.build("Cmaj7/E"));

        // Minor seventh
        assertEquals(new Chord("C#", "m7", Optional.empty()), Chord.build("C#m7"));
        assertEquals(new Chord("C#", "m7", Optional.of("E")), Chord.build("C#m7/E"));

        // Extended
        assertEquals(new Chord("C", "9", Optional.empty()), Chord.build("C9"));
        assertEquals(new Chord("C", "9", Optional.of("E")), Chord.build("C9/E"));

        // Augmented
        assertEquals(new Chord("C", "aug", Optional.empty()), Chord.build("Caug"));
        assertEquals(new Chord("C", "aug", Optional.of("E")), Chord.build("Caug/E"));

        // Dominant seventh
        assertEquals(new Chord("C", "7", Optional.empty()), Chord.build("C7"));
        assertEquals(new Chord("C", "7", Optional.of("E")), Chord.build("C7/E"));
    }

    @Test
    void getMusicNotation() {
        assertEquals("C", new Chord("C", "", Optional.empty()).getMusicNotation());
        assertEquals("C#", new Chord("C#", "", Optional.empty()).getMusicNotation());
        assertEquals("C#m", new Chord("C#", "m", Optional.empty()).getMusicNotation());
        assertEquals("C#m7/E", new Chord("C#", "m7", Optional.of("E")).getMusicNotation());
    }

}