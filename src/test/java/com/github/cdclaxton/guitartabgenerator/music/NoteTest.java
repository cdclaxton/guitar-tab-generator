package com.github.cdclaxton.guitartabgenerator.music;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoteTest {

    @Test
    void getFret() throws InvalidStringException, InvalidFretNumberException, InvalidTimingException {
        Fret fret = new Fret( 1, 10);
        Timing timing = new Timing(0);
        Note note = new Note(fret, timing);

        assertEquals(fret, note.getFret());
    }

    @Test
    void getTiming() throws InvalidStringException, InvalidFretNumberException, InvalidTimingException {
        Fret fret = new Fret( 1, 10);
        Timing timing = new Timing(0);
        Note note = new Note(fret, timing);

        assertEquals(timing, note.getTiming());
    }

}