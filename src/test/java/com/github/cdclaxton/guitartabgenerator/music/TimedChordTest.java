package com.github.cdclaxton.guitartabgenerator.music;

import static org.junit.jupiter.api.Assertions.*;

class TimedChordTest {

    // A valid timing to use for the tests
    private Timing validTiming = new Timing(0);

    TimedChordTest() throws InvalidTimingException {
    }

    @org.junit.jupiter.api.Test
    void testValidSimpleMajorChords() throws InvalidChordException {
        assertEquals("A", new TimedChord(validTiming, "A").getChord());
        assertEquals("A#", new TimedChord(validTiming, "A#").getChord());
        assertEquals("Bb", new TimedChord(validTiming, "Bb").getChord());
        assertEquals("B", new TimedChord(validTiming, "B").getChord());
        assertEquals("C", new TimedChord(validTiming, "C").getChord());
        assertEquals("C#", new TimedChord(validTiming, "C#").getChord());
        assertEquals("Db", new TimedChord(validTiming, "Db").getChord());
        assertEquals("D", new TimedChord(validTiming, "D").getChord());
        assertEquals("D#", new TimedChord(validTiming, "D#").getChord());
        assertEquals("Eb", new TimedChord(validTiming, "Eb").getChord());
        assertEquals("E", new TimedChord(validTiming, "E").getChord());
        assertEquals("F", new TimedChord(validTiming, "F").getChord());
        assertEquals("F#", new TimedChord(validTiming, "F#").getChord());
        assertEquals("Gb", new TimedChord(validTiming, "Gb").getChord());
        assertEquals("G", new TimedChord(validTiming, "G").getChord());
        assertEquals("G#", new TimedChord(validTiming, "G#").getChord());
        assertEquals("Ab", new TimedChord(validTiming, "Ab").getChord());
    }

    @org.junit.jupiter.api.Test
    void testValidSimpleMinorChords() throws InvalidChordException {
        assertEquals("Am", new TimedChord(validTiming, "Am").getChord());
        assertEquals("A#m", new TimedChord(validTiming, "A#m").getChord());
        assertEquals("Bbm", new TimedChord(validTiming, "Bbm").getChord());
        assertEquals("Bm", new TimedChord(validTiming, "Bm").getChord());
        assertEquals("Cm", new TimedChord(validTiming, "Cm").getChord());
        assertEquals("C#m", new TimedChord(validTiming, "C#m").getChord());
        assertEquals("Dbm", new TimedChord(validTiming, "Dbm").getChord());
        assertEquals("Dm", new TimedChord(validTiming, "Dm").getChord());
        assertEquals("D#m", new TimedChord(validTiming, "D#m").getChord());
        assertEquals("Ebm", new TimedChord(validTiming, "Ebm").getChord());
        assertEquals("Em", new TimedChord(validTiming, "Em").getChord());
        assertEquals("Fm", new TimedChord(validTiming, "Fm").getChord());
        assertEquals("F#m", new TimedChord(validTiming, "F#m").getChord());
        assertEquals("Gbm", new TimedChord(validTiming, "Gbm").getChord());
        assertEquals("Gm", new TimedChord(validTiming, "Gm").getChord());
        assertEquals("G#m", new TimedChord(validTiming, "G#m").getChord());
        assertEquals("Abm", new TimedChord(validTiming, "Abm").getChord());
    }

    @org.junit.jupiter.api.Test
    void testValidDifferentBassChords() throws InvalidChordException {
        assertEquals("A/C#", new TimedChord(validTiming, "A/C#").getChord());
        assertEquals("Am/C", new TimedChord(validTiming, "Am/C").getChord());
        assertEquals("C#m/E", new TimedChord(validTiming, "C#m/E").getChord());
        assertEquals("D/A", new TimedChord(validTiming, "D/A").getChord());
        assertEquals("Eb/G", new TimedChord(validTiming, "Eb/G").getChord());
    }

    @org.junit.jupiter.api.Test
    void testConstructorInvalidChords() {
        assertThrows(InvalidChordException.class, () -> new TimedChord(validTiming, "-1"));
        assertThrows(InvalidChordException.class, () -> new TimedChord(validTiming, "H"));
        assertThrows(InvalidChordException.class, () -> new TimedChord(validTiming, "H/C#"));
        assertThrows(InvalidChordException.class, () -> new TimedChord(validTiming, "a"));
    }

}