package com.github.cdclaxton.tabwriter;

import com.github.cdclaxton.music.*;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TablatureBarBuilderTest {

    @Test
    void testConstructorNegativeNumElements() {
        assertThrows(TabBuildingException.class, () -> TablatureBarBuilder.buildTabLine(-1, Collections.emptyMap()));
    }

    @Test
    void testBuildEmptyTabLines() throws TabBuildingException {
        assertTrue(TablatureBarBuilder.buildTabLine(1, Collections.emptyMap()).equals("-"));
        assertTrue(TablatureBarBuilder.buildTabLine(2, Collections.emptyMap()).equals("--"));
        assertTrue(TablatureBarBuilder.buildTabLine(3, Collections.emptyMap()).equals("---"));
        assertTrue(TablatureBarBuilder.buildTabLine(4, Collections.emptyMap()).equals("----"));
    }

    @Test
    void testBuildTabLinesOneCharacter() throws TabBuildingException {
        assertTrue(TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(0, "0")).equals("0---"));
        assertTrue(TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(1, "0")).equals("-0--"));
        assertTrue(TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(2, "0")).equals("--0-"));
        assertTrue(TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(3, "0")).equals("---0"));
    }

    @Test
    void testBuildTabLinesTwoCharacter() throws TabBuildingException {
        assertTrue(TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(0, "12")).equals("12--"));
        assertTrue(TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(1, "12")).equals("-12-"));
        assertTrue(TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(2, "12")).equals("--12"));
        assertTrue(TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(3, "1")).equals("---1"));
    }

    @Test
    void testBuildTabLinesBeyondEnd() {
        assertThrows(TabBuildingException.class, () -> TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(4, "12")));
    }

    @Test
    void testBuildTabLinesTwoStrings() throws TabBuildingException {
        assertTrue(TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(0, "1", 1, "2")).equals("12--"));
        assertTrue(TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(0, "1", 2, "2")).equals("1-2-"));
        assertTrue(TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(0, "1", 3, "2")).equals("1--2"));
        assertTrue(TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(0, "1", 2, "34")).equals("1-34"));
    }

    @Test
    void testBuildRuler() throws TabBuildingException {
        assertTrue(TablatureBarBuilder.buildRuler(TablatureBarBuilder.Markings.Main, 1, 2).equals("1  "));
        assertTrue(TablatureBarBuilder.buildRuler(TablatureBarBuilder.Markings.Main, 2, 2).equals("1  2  "));
        assertTrue(TablatureBarBuilder.buildRuler(TablatureBarBuilder.Markings.Main, 3, 2).equals("1  2  3  "));
        assertTrue(TablatureBarBuilder.buildRuler(TablatureBarBuilder.Markings.Main, 4, 2).equals("1  2  3  4  "));

        assertTrue(TablatureBarBuilder.buildRuler(TablatureBarBuilder.Markings.Secondary, 1, 2).equals("1  +  "));
        assertTrue(TablatureBarBuilder.buildRuler(TablatureBarBuilder.Markings.Secondary, 2, 2).equals("1  +  2  +  "));
        assertTrue(TablatureBarBuilder.buildRuler(TablatureBarBuilder.Markings.Secondary, 3, 2).equals("1  +  2  +  3  +  "));

        assertTrue(TablatureBarBuilder.buildRuler(TablatureBarBuilder.Markings.Tertiary, 1, 2).equals("1  .  +  .  "));
        assertTrue(TablatureBarBuilder.buildRuler(TablatureBarBuilder.Markings.Tertiary, 2, 2).equals("1  .  +  .  2  .  +  .  "));
    }

    @Test
    void testBuildEmptyChordLines() throws TabBuildingException {
        assertTrue(TablatureBarBuilder.buildChordLine(1, Collections.emptyMap()).equals(" "));
        assertTrue(TablatureBarBuilder.buildChordLine(2, Collections.emptyMap()).equals("  "));
        assertTrue(TablatureBarBuilder.buildChordLine(3, Collections.emptyMap()).equals("   "));
        assertTrue(TablatureBarBuilder.buildChordLine(4, Collections.emptyMap()).equals("    "));
    }

    @Test
    void testBuildNonEmptyChordLines() throws TabBuildingException {
        assertTrue(TablatureBarBuilder.buildChordLine(4, ImmutableMap.of(0, "Am")).equals("Am  "));
        assertTrue(TablatureBarBuilder.buildChordLine(4, ImmutableMap.of(1, "A")).equals(" A  "));
        assertTrue(TablatureBarBuilder.buildChordLine(4, ImmutableMap.of(0, "A", 2, "Bm")).equals("A Bm"));
        assertTrue(TablatureBarBuilder.buildChordLine(4, ImmutableMap.of(3, "D")).equals("   D"));
    }

    @Test
    void testBuildPositionToMarkingSingleChord() throws InvalidTimingException, InvalidChordException {
//        TimedChord timedChord1 = new TimedChord(new Timing(0), "Am");
//        Map<Integer, String> posToMarking = TablatureBarBuilder.buildPositionToMarking(Arrays.asList(timedChord1));
//
//        assertEquals(1, posToMarking.size());
//        assertEquals(posToMarking.get(0), "Am");
    }

    @Test
    void testBuildPositionToMarkingTwoChords() throws InvalidTimingException, InvalidChordException {
//        TimedChord timedChord1 = new TimedChord(new Timing(0), "Am");
//        TimedChord timedChord2 = new TimedChord(new Timing(7), "C");
//        Map<Integer, String> posToMarking = TablatureBarBuilder.buildPositionToMarking(Arrays.asList(timedChord1, timedChord2));
//
//        assertEquals(2, posToMarking.size());
//        assertEquals(posToMarking.get(0), "Am");
//        assertEquals(posToMarking.get(7), "C");
    }

    @Test
    void testBuildSixteenthTabIn44FromBar() throws InvalidStringException, InvalidFretNumberException,
            InvalidTimingException, InvalidChordException, TabBuildingException {

        // Construct the bar
        List<Note> notes = Arrays.asList(
                new Note(new Fret(1, 2), new Timing(0)),
                new Note(new Fret(2, 3), new Timing(2)),
                new Note(new Fret(3, 4), new Timing(4)),
                new Note(new Fret(3, 7), new Timing(5))
        );
        List<TimedChord> timedChords = Arrays.asList(
                new TimedChord(new Timing(0), "D"),
                new TimedChord(new Timing(4), "Bm"),
                new TimedChord(new Timing(8), "F#m")
        );
        Bar bar = new Bar(Bar.TimeSignature.Four4, notes, timedChords);

        TablatureBar tab = TablatureBarBuilder.buildTabIn44FromBar(bar, TablatureBarBuilder.Markings.Tertiary);

        assertEquals("1   .   +   .   2   .   +   .   3   .   +   .   4   .   +   .   ", tab.getRuler());
        assertEquals("D               Bm              F#m                             ", tab.getChordLine());
        assertEquals("2---------------------------------------------------------------", tab.getTabLines().get(0));
        assertEquals("--------3-------------------------------------------------------", tab.getTabLines().get(1));
        assertEquals("----------------4---7-------------------------------------------", tab.getTabLines().get(2));
    }

    @Test
    void testBuildEighthTabIn44FromBar() throws InvalidStringException, InvalidFretNumberException,
            InvalidTimingException, InvalidChordException, TabBuildingException {

        // Construct the bar
        List<Note> notes = Arrays.asList(
                new Note(new Fret(1, 2), new Timing(0)),
                new Note(new Fret(2, 3), new Timing(4)),
                new Note(new Fret(3, 4), new Timing(6))
        );
        List<TimedChord> timedChords = Arrays.asList(
                new TimedChord(new Timing(0), "D"),
                new TimedChord(new Timing(4), "Bm"),
                new TimedChord(new Timing(8), "F#m")
        );
        Bar bar = new Bar(Bar.TimeSignature.Four4, notes, timedChords);

        TablatureBar tab = TablatureBarBuilder.buildTabIn44FromBar(bar, TablatureBarBuilder.Markings.Secondary);

        assertEquals("1   +   2   +   3   +   4   +   ", tab.getRuler());
        assertEquals("D       Bm      F#m             ", tab.getChordLine());
        assertEquals("2-------------------------------", tab.getTabLines().get(0));
        assertEquals("--------3-----------------------", tab.getTabLines().get(1));
        assertEquals("------------4-------------------", tab.getTabLines().get(2));
    }

    @Test
    void testBuildQuarterTabIn44FromBar() throws InvalidStringException, InvalidFretNumberException,
            InvalidTimingException, InvalidChordException, TabBuildingException {

        // Construct the bar
        List<Note> notes = Arrays.asList(
                new Note(new Fret(1, 2), new Timing(0)),
                new Note(new Fret(2, 3), new Timing(4)),
                new Note(new Fret(3, 4), new Timing(8))
        );
        List<TimedChord> timedChords = Arrays.asList(
                new TimedChord(new Timing(0), "D"),
                new TimedChord(new Timing(4), "Bm"),
                new TimedChord(new Timing(8), "F#m")
        );
        Bar bar = new Bar(Bar.TimeSignature.Four4, notes, timedChords);

        TablatureBar tab = TablatureBarBuilder.buildTabIn44FromBar(bar, TablatureBarBuilder.Markings.Main);

        assertEquals("1   2   3   4   ", tab.getRuler());
        assertEquals("D   Bm  F#m     ", tab.getChordLine());
        assertEquals("2---------------", tab.getTabLines().get(0));
        assertEquals("----3-----------", tab.getTabLines().get(1));
        assertEquals("--------4-------", tab.getTabLines().get(2));
    }
}