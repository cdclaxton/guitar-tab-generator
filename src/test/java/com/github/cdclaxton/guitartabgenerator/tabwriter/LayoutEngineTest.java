package com.github.cdclaxton.guitartabgenerator.tabwriter;

import com.github.cdclaxton.guitartabgenerator.music.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LayoutEngineTest {

    @Test
    void testCompactLayout() throws InvalidStringException, InvalidFretNumberException, InvalidTimingException, InvalidChordException {
        Fret fret = new Fret(1, 0);

        List<Note> notes = Arrays.asList(new Note(fret, new Timing(0)));
        List<TimedChord> chords = Arrays.asList(new TimedChord(new Timing(0), "C#"));
        Bar bar = new Bar(Bar.TimeSignature.Four4, notes, chords);
        assertEquals(SingleBarTablatureBuilder.Markings.Main, LayoutEngine.compactLayout(bar));

        List<Note> notes2 = Arrays.asList(new Note(fret, new Timing(0)));
        List<TimedChord> chords2 = Arrays.asList(new TimedChord(new Timing(4), "C#"));
        Bar bar2 = new Bar(Bar.TimeSignature.Four4, notes2, chords2);
        assertEquals(SingleBarTablatureBuilder.Markings.Main, LayoutEngine.compactLayout(bar2));

        List<Note> notes3 = Arrays.asList(new Note(fret, new Timing(0)));
        List<TimedChord> chords3 = Arrays.asList(new TimedChord(new Timing(6), "C#"));
        Bar bar3 = new Bar(Bar.TimeSignature.Four4, notes3, chords3);
        assertEquals(SingleBarTablatureBuilder.Markings.Secondary, LayoutEngine.compactLayout(bar3));

        List<Note> notes4 = Arrays.asList(new Note(fret, new Timing(0)));
        List<TimedChord> chords4 = Arrays.asList(new TimedChord(new Timing(3), "C#"));
        Bar bar4 = new Bar(Bar.TimeSignature.Four4, notes4, chords4);
        assertEquals(SingleBarTablatureBuilder.Markings.Tertiary, LayoutEngine.compactLayout(bar4));
    }

    @Test
    void testAllDivisible() {
        assertEquals(true, LayoutEngine.allDivisible(Arrays.asList(0), 2));
        assertEquals(true, LayoutEngine.allDivisible(Arrays.asList(2, 4, 6, 8), 2));
        assertEquals(true, LayoutEngine.allDivisible(Arrays.asList(1, 2, 3, 4), 1));
        assertEquals(false, LayoutEngine.allDivisible(Arrays.asList(1, 2, 3, 4), 2));

        assertEquals(true, LayoutEngine.allDivisible(Arrays.asList(2), 2));
        assertEquals(true, LayoutEngine.allDivisible(Arrays.asList(2, 4, 6, 8), 2));
        assertEquals(false, LayoutEngine.allDivisible(Arrays.asList(2, 4, 6, 8, 9), 2));

        assertEquals(true, LayoutEngine.allDivisible(Arrays.asList(0, 3), 3));
        assertEquals(false, LayoutEngine.allDivisible(Arrays.asList(0, 3, 4), 3));

        assertEquals(true, LayoutEngine.allDivisible(Arrays.asList(0), 4));
        assertEquals(true, LayoutEngine.allDivisible(Arrays.asList(0, 4, 8), 4));
        assertEquals(false, LayoutEngine.allDivisible(Arrays.asList(0, 4, 6, 8), 4));
    }

    @Test
    void testBlockLayoutHorizontal() throws TabBuildingException {
        LayoutEngine.Block b1 = new LayoutEngine.Block(Arrays.asList("ABC", "DEF"));
        LayoutEngine.Block b2 = new LayoutEngine.Block(Arrays.asList("123", "456"));
        b1.horizontalLayout(b2);
        assertEquals(2, b1.lines.size());
        assertEquals("ABC123", b1.lines.get(0));
        assertEquals("DEF456", b1.lines.get(1));
    }
}