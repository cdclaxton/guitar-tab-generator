package com.github.cdclaxton.guitartabgenerator.music;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class NoteTranspositionTest {

    @Test
    void sameNoteDifferentString() throws TranspositionException {

        // Open strings - down
        assertEquals(5, NoteTransposition.sameNoteDifferentString(0, 1, 2));
        assertEquals(4, NoteTransposition.sameNoteDifferentString(0, 2, 3));
        assertEquals(5, NoteTransposition.sameNoteDifferentString(0, 3, 4));
        assertEquals(5, NoteTransposition.sameNoteDifferentString(0, 4, 5));
        assertEquals(5, NoteTransposition.sameNoteDifferentString(0, 5, 6));

        // Open strings - up
        assertEquals(0, NoteTransposition.sameNoteDifferentString(5, 6, 5));
        assertEquals(0, NoteTransposition.sameNoteDifferentString(5, 5, 4));
        assertEquals(0, NoteTransposition.sameNoteDifferentString(5, 4, 3));
        assertEquals(0, NoteTransposition.sameNoteDifferentString(4, 3, 2));
        assertEquals(0, NoteTransposition.sameNoteDifferentString(5, 2, 1));

        // Non-open strings - down
        assertEquals(12, NoteTransposition.sameNoteDifferentString(7, 1, 2));
        assertEquals(16, NoteTransposition.sameNoteDifferentString(7, 1, 3));
        assertEquals(11, NoteTransposition.sameNoteDifferentString(7, 2, 3));
        assertEquals(16, NoteTransposition.sameNoteDifferentString(7, 1, 3));
        assertEquals(10, NoteTransposition.sameNoteDifferentString(1, 2, 4));

        // Non-open strings - up
        assertEquals(0, NoteTransposition.sameNoteDifferentString(5, 6, 5));
        assertEquals(10, NoteTransposition.sameNoteDifferentString(1, 2, 4));

        // No change in string number
        Random random = new Random();
        int randomFret = random.nextInt(22 + 1);
        assertEquals(randomFret, NoteTransposition.sameNoteDifferentString(randomFret, 6, 6));
        assertEquals(randomFret, NoteTransposition.sameNoteDifferentString(randomFret, 5, 5));
        assertEquals(randomFret, NoteTransposition.sameNoteDifferentString(randomFret, 4, 4));
        assertEquals(randomFret, NoteTransposition.sameNoteDifferentString(randomFret, 3, 3));
        assertEquals(randomFret, NoteTransposition.sameNoteDifferentString(randomFret, 2, 2));
        assertEquals(randomFret, NoteTransposition.sameNoteDifferentString(randomFret, 1, 1));

        // Negative frets
        assertEquals(-1, NoteTransposition.sameNoteDifferentString(3, 3, 2));

    }

    @Test
    void sameNoteLowerString() throws TranspositionException {
        assertEquals(5, NoteTransposition.sameNoteLowerString(0, 1));
        assertEquals(4, NoteTransposition.sameNoteLowerString(0, 2));
        assertEquals(5, NoteTransposition.sameNoteLowerString(0, 3));
        assertEquals(5, NoteTransposition.sameNoteLowerString(0, 4));
        assertEquals(5, NoteTransposition.sameNoteLowerString(0, 5));
    }

    @Test
    void sameNoteHigherString() throws TranspositionException {
        assertEquals(0, NoteTransposition.sameNoteHigherString(5, 6));
        assertEquals(0, NoteTransposition.sameNoteHigherString(5, 5));
        assertEquals(0, NoteTransposition.sameNoteHigherString(5, 4));
        assertEquals(0, NoteTransposition.sameNoteHigherString(4, 3));
        assertEquals(0, NoteTransposition.sameNoteHigherString(5, 2));
    }
}