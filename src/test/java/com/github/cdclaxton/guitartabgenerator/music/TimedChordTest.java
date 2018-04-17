package com.github.cdclaxton.guitartabgenerator.music;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TimedChordTest {

    // A valid timing to use for the tests
    private Timing validTiming = new Timing(0);

    TimedChordTest() throws InvalidTimingException {
    }

    @Test
    void testValidSimpleMajorChords() throws InvalidChordException {
        assertEquals("A", new TimedChord(validTiming, Chord.build("A")).getChord().musicNotation());
        assertEquals("A#", new TimedChord(validTiming, Chord.build("A#")).getChord().musicNotation());
        assertEquals("Bb", new TimedChord(validTiming, Chord.build("Bb")).getChord().musicNotation());
        assertEquals("B", new TimedChord(validTiming, Chord.build("B")).getChord().musicNotation());
        assertEquals("C", new TimedChord(validTiming, Chord.build("C")).getChord().musicNotation());
        assertEquals("C#", new TimedChord(validTiming, Chord.build("C#")).getChord().musicNotation());
        assertEquals("Db", new TimedChord(validTiming, Chord.build("Db")).getChord().musicNotation());
        assertEquals("D", new TimedChord(validTiming, Chord.build("D")).getChord().musicNotation());
        assertEquals("D#", new TimedChord(validTiming, Chord.build("D#")).getChord().musicNotation());
        assertEquals("Eb", new TimedChord(validTiming, Chord.build("Eb")).getChord().musicNotation());
        assertEquals("E", new TimedChord(validTiming, Chord.build("E")).getChord().musicNotation());
        assertEquals("F", new TimedChord(validTiming, Chord.build("F")).getChord().musicNotation());
        assertEquals("F#", new TimedChord(validTiming, Chord.build("F#")).getChord().musicNotation());
        assertEquals("Gb", new TimedChord(validTiming, Chord.build("Gb")).getChord().musicNotation());
        assertEquals("G", new TimedChord(validTiming, Chord.build("G")).getChord().musicNotation());
        assertEquals("G#", new TimedChord(validTiming, Chord.build("G#")).getChord().musicNotation());
        assertEquals("Ab", new TimedChord(validTiming, Chord.build("Ab")).getChord().musicNotation());
    }

    @Test
    void testValidSimpleMinorChords() throws InvalidChordException {
        assertEquals("Am", new TimedChord(validTiming, Chord.build("Am")).getChord().musicNotation());
        assertEquals("A#m", new TimedChord(validTiming, Chord.build("A#m")).getChord().musicNotation());
        assertEquals("Bbm", new TimedChord(validTiming, Chord.build("Bbm")).getChord().musicNotation());
        assertEquals("Bm", new TimedChord(validTiming, Chord.build("Bm")).getChord().musicNotation());
        assertEquals("Cm", new TimedChord(validTiming, Chord.build("Cm")).getChord().musicNotation());
        assertEquals("C#m", new TimedChord(validTiming, Chord.build("C#m")).getChord().musicNotation());
        assertEquals("Dbm", new TimedChord(validTiming, Chord.build("Dbm")).getChord().musicNotation());
        assertEquals("Dm", new TimedChord(validTiming, Chord.build("Dm")).getChord().musicNotation());
        assertEquals("D#m", new TimedChord(validTiming, Chord.build("D#m")).getChord().musicNotation());
        assertEquals("Ebm", new TimedChord(validTiming, Chord.build("Ebm")).getChord().musicNotation());
        assertEquals("Em", new TimedChord(validTiming, Chord.build("Em")).getChord().musicNotation());
        assertEquals("Fm", new TimedChord(validTiming, Chord.build("Fm")).getChord().musicNotation());
        assertEquals("F#m", new TimedChord(validTiming, Chord.build("F#m")).getChord().musicNotation());
        assertEquals("Gbm", new TimedChord(validTiming, Chord.build("Gbm")).getChord().musicNotation());
        assertEquals("Gm", new TimedChord(validTiming, Chord.build("Gm")).getChord().musicNotation());
        assertEquals("G#m", new TimedChord(validTiming, Chord.build("G#m")).getChord().musicNotation());
        assertEquals("Abm", new TimedChord(validTiming, Chord.build("Abm")).getChord().musicNotation());
    }

    @Test
    void testValidDifferentBassChords() throws InvalidChordException {
        assertEquals("A/C#", new TimedChord(validTiming, Chord.build("A/C#")).getChord().musicNotation());
        assertEquals("Am/C", new TimedChord(validTiming, Chord.build("Am/C")).getChord().musicNotation());
        assertEquals("C#m/E", new TimedChord(validTiming, Chord.build("C#m/E")).getChord().musicNotation());
        assertEquals("D/A", new TimedChord(validTiming, Chord.build("D/A")).getChord().musicNotation());
        assertEquals("Eb/G", new TimedChord(validTiming, Chord.build("Eb/G")).getChord().musicNotation());
    }

    @Test
    void testConstructorInvalidChords() {
        assertThrows(InvalidChordException.class, () -> new TimedChord(validTiming, Chord.build("-1")));
        assertThrows(InvalidChordException.class, () -> new TimedChord(validTiming, Chord.build("H")));
        assertThrows(InvalidChordException.class, () -> new TimedChord(validTiming, Chord.build("H/C#")));
        assertThrows(InvalidChordException.class, () -> new TimedChord(validTiming, Chord.build("a")));
    }

}