package com.github.cdclaxton.guitartabgenerator.tabparser;

import com.github.cdclaxton.guitartabgenerator.music.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExtractedBarTest {

    @Test
    void testTimingNotationToSixteenth() throws ExtractionException {
        assertEquals(0, ExtractedBar.timingNotationToSixteenth("1"));
        assertEquals(1, ExtractedBar.timingNotationToSixteenth("1e"));
        assertEquals(2, ExtractedBar.timingNotationToSixteenth("1+"));
        assertEquals(3, ExtractedBar.timingNotationToSixteenth("1a"));
        assertEquals(4, ExtractedBar.timingNotationToSixteenth("2"));
        assertEquals(5, ExtractedBar.timingNotationToSixteenth("2e"));
        assertEquals(6, ExtractedBar.timingNotationToSixteenth("2+"));
        assertEquals(7, ExtractedBar.timingNotationToSixteenth("2a"));
        assertEquals(8, ExtractedBar.timingNotationToSixteenth("3"));
        assertEquals(12, ExtractedBar.timingNotationToSixteenth("4"));
    }

    @Test
    void testStringLetterToNumber() throws InvalidStringException {
        assertEquals(6, ExtractedBar.stringLetterToNumber("E"));
        assertEquals(2, ExtractedBar.stringLetterToNumber("b"));
        assertEquals(1, ExtractedBar.stringLetterToNumber("e"));
    }

    @Test
    void testNotationToNote() throws InvalidStringException, InvalidFretNumberException, InvalidTimingException, ExtractionException {
        Note note = ExtractedBar.notationToNote("1/g2");
        assertEquals(0, note.getTiming().getSixteenthNumber());
        assertEquals(2, note.getFret().getFretNumber());
        assertEquals(3, note.getFret().getStringNumber());

        Note note2 = ExtractedBar.notationToNote("4a/b0");
        assertEquals(15, note2.getTiming().getSixteenthNumber());
        assertEquals(0, note2.getFret().getFretNumber());
        assertEquals(2, note2.getFret().getStringNumber());
    }

    @Test
    void testSplitNotesByTime() {
        List<String> notes = ExtractedBar.splitNotesByTime("1/g7");
        assertEquals(1, notes.size());
        assertEquals("1/g7", notes.get(0));

        List<String> notes2 = ExtractedBar.splitNotesByTime("1e/g7");
        assertEquals(1, notes2.size());
        assertEquals("1e/g7", notes2.get(0));

        List<String> notes3 = ExtractedBar.splitNotesByTime("1+/g7");
        assertEquals(1, notes3.size());
        assertEquals("1+/g7", notes3.get(0));

        List<String> notes4 = ExtractedBar.splitNotesByTime("1a/g7");
        assertEquals(1, notes4.size());
        assertEquals("1a/g7", notes4.get(0));

        List<String> notes5 = ExtractedBar.splitNotesByTime("1/g7 2e/g9");
        assertEquals(2, notes5.size());
        assertEquals("1/g7", notes5.get(0));
        assertEquals("2e/g9", notes5.get(1));

        List<String> notes6 = ExtractedBar.splitNotesByTime("1/<g7 e9>");
        assertEquals(1, notes6.size());
        assertEquals("1/<g7 e9>", notes6.get(0));

        List<String> notes7 = ExtractedBar.splitNotesByTime("1/<g7 e9> 2/g8");
        assertEquals(2, notes7.size());
        assertEquals("1/<g7 e9>", notes7.get(0));
        assertEquals("2/g8", notes7.get(1));

        List<String> notes8 = ExtractedBar.splitNotesByTime("1/<g7 e9> 2e/g8");
        assertEquals(2, notes8.size());
        assertEquals("1/<g7 e9>", notes8.get(0));
        assertEquals("2e/g8", notes8.get(1));

        List<String> notes9 = ExtractedBar.splitNotesByTime("1/<g7 e9> 2e/<g8 b8>");
        assertEquals(2, notes9.size());
        assertEquals("1/<g7 e9>", notes9.get(0));
        assertEquals("2e/<g8 b8>", notes9.get(1));
    }

    @Test
    void testParseSimulataneousNotes() throws ExtractionException {
        List<String> notes = ExtractedBar.parseSimultaneousNotes("1/<g8 b9>");
        assertEquals(2, notes.size());
        assertEquals("1/g8", notes.get(0));
        assertEquals("1/b9", notes.get(1));

        List<String> notes2 = ExtractedBar.parseSimultaneousNotes("1/<g8 b9 e9>");
        assertEquals(3, notes2.size());
        assertEquals("1/g8", notes2.get(0));
        assertEquals("1/b9", notes2.get(1));
        assertEquals("1/e9", notes2.get(2));

        List<String> notes3 = ExtractedBar.parseSimultaneousNotes("1/<g8>");
        assertEquals(1, notes3.size());
        assertEquals("1/g8", notes3.get(0));
    }

    @Test
    void testSeparateNotes() throws InvalidStringException, ExtractionException, InvalidTimingException, InvalidFretNumberException {
        List<Note> notes = ExtractedBar.parseNotes("2+/g6");
        assertEquals(1, notes.size());

        List<Note> notes1 = ExtractedBar.parseNotes("2+/g6 3/g7");
        assertEquals(2, notes1.size());

        List<Note> notes2 = ExtractedBar.parseNotes("2+/g6 3/g7 4/b0  ");
        assertEquals(3, notes2.size());

        List<Note> notes3 = ExtractedBar.parseNotes("2+/<g6 b4>");
        assertEquals(2, notes3.size());

        List<Note> notes4 = ExtractedBar.parseNotes("2+/<g6 b4> 3/b0");
        assertEquals(3, notes4.size());

        List<Note> notes5 = ExtractedBar.parseNotes("2+/<g6 b4> 3/<b0 e0>");
        assertEquals(4, notes5.size());
    }

    @Test
    void testParseNotes() throws InvalidStringException, ExtractionException, InvalidTimingException, InvalidFretNumberException {
        List<Note> notes = ExtractedBar.parseNotes("1/g8");
        assertEquals(1, notes.size());

        List<Note> notes2 = ExtractedBar.parseNotes("1/g8 1e/g9");
        assertEquals(2, notes2.size());

        List<Note> notes3 = ExtractedBar.parseNotes("1/g8 1e/g9 2+/g11");
        assertEquals(3, notes3.size());

        List<Note> notes4 = ExtractedBar.parseNotes("1/<g8 b9>");
        assertEquals(2, notes4.size());

        List<Note> notes5 = ExtractedBar.parseNotes("1/<g8 b9> 2+/b12");
        assertEquals(3, notes5.size());

        List<Note> notes6 = ExtractedBar.parseNotes("1/<g8 b9> 2+/<b12 e0>");
        assertEquals(4, notes6.size());
    }

    @Test
    void testNotationToChord() throws ExtractionException, InvalidTimingException, InvalidChordException {

        // Major
        assertEquals(new TimedChord(new Timing(0), "D"),
                ExtractedBar.notationToChord("1/D"));

        assertEquals(new TimedChord(new Timing(0), "D/F#"),
                ExtractedBar.notationToChord("1/D/F#"));

        assertEquals(new TimedChord(new Timing(0), "Db"),
                ExtractedBar.notationToChord("1/Db"));

        assertEquals(new TimedChord(new Timing(0), "Db/A"),
                ExtractedBar.notationToChord("1/Db/A"));

        assertEquals(new TimedChord(new Timing(0), "D#"),
                ExtractedBar.notationToChord("1/D#"));

        assertEquals(new TimedChord(new Timing(0), "D#/F#"),
                ExtractedBar.notationToChord("1/D#/F#"));

        assertEquals(new TimedChord(new Timing(15), "D"),
                ExtractedBar.notationToChord("4a/D"));

        assertEquals(new TimedChord(new Timing(15), "D/F#"),
                ExtractedBar.notationToChord("4a/D/F#"));

        assertEquals(new TimedChord(new Timing(15), "D#"),
                ExtractedBar.notationToChord("4a/D#"));

        assertEquals(new TimedChord(new Timing(15), "D#/F#"),
                ExtractedBar.notationToChord("4a/D#/F#"));

        // Minor
        assertEquals(new TimedChord(new Timing(0), "Dbm"),
                ExtractedBar.notationToChord("1/Dbm"));

        assertEquals(new TimedChord(new Timing(0), "Dbm/C"),
                ExtractedBar.notationToChord("1/Dbm/C"));

        assertEquals(new TimedChord(new Timing(0), "G#m7"),
                ExtractedBar.notationToChord("1/G#m7"));

        assertEquals(new TimedChord(new Timing(0), "G#m7/A"),
                ExtractedBar.notationToChord("1/G#m7/A"));

        // Diminished
        assertEquals(new TimedChord(new Timing(0), "Cdim"),
                ExtractedBar.notationToChord("1/Cdim"));

        assertEquals(new TimedChord(new Timing(0), "Cdim/B"),
                ExtractedBar.notationToChord("1/Cdim/B"));

        assertEquals(new TimedChord(new Timing(0), "C#dim"),
                ExtractedBar.notationToChord("1/C#dim"));

        assertEquals(new TimedChord(new Timing(0), "C#dim/B"),
                ExtractedBar.notationToChord("1/Cdim/B"));

        assertEquals(new TimedChord(new Timing(0), "Dbdim"),
                ExtractedBar.notationToChord("1/Dbdim"));

        assertEquals(new TimedChord(new Timing(0), "Dbdim/A"),
                ExtractedBar.notationToChord("1/Dbdim/A"));

        // Suspended
        assertEquals(new TimedChord(new Timing(0), "Fsus2"),
                ExtractedBar.notationToChord("1/Fsus2"));

        assertEquals(new TimedChord(new Timing(0), "Fsus2/E"),
                ExtractedBar.notationToChord("1/Fsus2/E"));

        assertEquals(new TimedChord(new Timing(0), "Fsus4"),
                ExtractedBar.notationToChord("1/Fsus4"));

        assertEquals(new TimedChord(new Timing(0), "Fsus4/E"),
                ExtractedBar.notationToChord("1/Fsus4/E"));

        assertEquals(new TimedChord(new Timing(0), "F2"),
                ExtractedBar.notationToChord("1/F2"));

        assertEquals(new TimedChord(new Timing(0), "F2/A"),
                ExtractedBar.notationToChord("1/F2/A"));

        assertEquals(new TimedChord(new Timing(0), "F4"),
                ExtractedBar.notationToChord("1/F4"));

        assertEquals(new TimedChord(new Timing(0), "F4/G"),
                ExtractedBar.notationToChord("1/F4/G"));

        assertEquals(new TimedChord(new Timing(0), "Db2"),
                ExtractedBar.notationToChord("1/Db2"));

        assertEquals(new TimedChord(new Timing(0), "Db2/Bb"),
                ExtractedBar.notationToChord("1/Db2/Bb"));

        assertEquals(new TimedChord(new Timing(0), "D#2"),
                ExtractedBar.notationToChord("1/D#2"));

        assertEquals(new TimedChord(new Timing(0), "D#2/F#"),
                ExtractedBar.notationToChord("1/D#2/F#"));

        // Major seventh
        assertEquals(new TimedChord(new Timing(0), "Gmaj7"),
                ExtractedBar.notationToChord("1/Gmaj7"));

        assertEquals(new TimedChord(new Timing(0), "Gmaj7/D"),
                ExtractedBar.notationToChord("1/Gmaj7/D"));

        assertEquals(new TimedChord(new Timing(0), "G#maj7"),
                ExtractedBar.notationToChord("1/G#maj7"));

        assertEquals(new TimedChord(new Timing(0), "G#maj7/D#"),
                ExtractedBar.notationToChord("1/G#maj7/D#"));

        // Minor seventh
        assertEquals(new TimedChord(new Timing(0), "Gm7"),
                ExtractedBar.notationToChord("1/Gm7"));

        assertEquals(new TimedChord(new Timing(0), "Gm7/B"),
                ExtractedBar.notationToChord("1/Gm7/B"));

        assertEquals(new TimedChord(new Timing(0), "Gbm7"),
                ExtractedBar.notationToChord("1/Gbm7"));

        assertEquals(new TimedChord(new Timing(0), "Gbm7/B"),
                ExtractedBar.notationToChord("1/Gbm7/B"));

        // Extended
        assertEquals(new TimedChord(new Timing(0), "A9"),
                ExtractedBar.notationToChord("1/A9"));

        assertEquals(new TimedChord(new Timing(0), "A9/E"),
                ExtractedBar.notationToChord("1/A9/E"));

        // Augmented
        assertEquals(new TimedChord(new Timing(0), "Aaug"),
                ExtractedBar.notationToChord("1/Aaug"));

        assertEquals(new TimedChord(new Timing(0), "Aaug/C"),
                ExtractedBar.notationToChord("1/Aaug/C"));

        // Dominant seventh
        assertEquals(new TimedChord(new Timing(0), "G7"),
                ExtractedBar.notationToChord("1/G7"));

        assertEquals(new TimedChord(new Timing(0), "G7/F"),
                ExtractedBar.notationToChord("1/G7/F"));
    }

    @Test
    void testSeparateChords() {
        List<String> chords = ExtractedBar.separateChords("1/Db");
        assertEquals(1, chords.size());
        assertEquals("1/Db", chords.get(0));

        List<String> chords2 = ExtractedBar.separateChords("1/Db 1+/E");
        assertEquals(2, chords2.size());
        assertEquals("1/Db", chords2.get(0));
        assertEquals("1+/E", chords2.get(1));

        List<String> chords3 = ExtractedBar.separateChords("1/Db 1+/E ");
        assertEquals(2, chords3.size());
        assertEquals("1/Db", chords3.get(0));
        assertEquals("1+/E", chords3.get(1));

        List<String> chords4 = ExtractedBar.separateChords("  1/Db 1+/E ");
        assertEquals(2, chords4.size());
        assertEquals("1/Db", chords4.get(0));
        assertEquals("1+/E", chords4.get(1));

        List<String> chords5 = ExtractedBar.separateChords("  1/Db 2/Dbm ");
        assertEquals(2, chords5.size());
        assertEquals("1/Db", chords5.get(0));
        assertEquals("2/Dbm", chords5.get(1));

        List<String> chords6 = ExtractedBar.separateChords("  1/Fsus2 ");
        assertEquals(1, chords6.size());
        assertEquals("1/Fsus2", chords6.get(0));

        List<String> chords7 = ExtractedBar.separateChords("  1/Fsus2 3/D4 ");
        assertEquals(2, chords7.size());
        assertEquals("1/Fsus2", chords7.get(0));
        assertEquals("3/D4", chords7.get(1));
    }

    @Test
    void testParseChords() throws ExtractionException, InvalidTimingException, InvalidChordException {
        List<TimedChord> chords = ExtractedBar.parseChords("1/Db");
        assertEquals(1, chords.size());
        assertEquals("Db", chords.get(0).getChord());
        assertEquals(0, chords.get(0).getTiming().getSixteenthNumber());

        List<TimedChord> chords2 = ExtractedBar.parseChords("1/Db 2+/F");
        assertEquals(2, chords2.size());
        assertEquals("Db", chords2.get(0).getChord());
        assertEquals(0, chords2.get(0).getTiming().getSixteenthNumber());
        assertEquals("F", chords2.get(1).getChord());
        assertEquals(6, chords2.get(1).getTiming().getSixteenthNumber());

        List<TimedChord> chords3 = ExtractedBar.parseChords("1/Dbm");
        assertEquals(1, chords3.size());
        assertEquals("Dbm", chords3.get(0).getChord());
        assertEquals(0, chords3.get(0).getTiming().getSixteenthNumber());

        List<TimedChord> chords4 = ExtractedBar.parseChords("");
        assertEquals(0, chords4.size());
    }

    @Test
    void testToBar() throws InvalidStringException, InvalidFretNumberException, InvalidChordException,
            InvalidTimingException, ExtractionException {
        String chords = "1/A 3+/G#m";
        String notes = "1/<g2 b2> 3+/b0";
        ExtractedBar extractedBar = new ExtractedBar(chords, notes);

        Bar bar = extractedBar.toBar(Bar.TimeSignature.Four4);
        assertEquals(2, bar.getTimedChords().size());
        assertEquals(3, bar.getNotes().size());
    }

}