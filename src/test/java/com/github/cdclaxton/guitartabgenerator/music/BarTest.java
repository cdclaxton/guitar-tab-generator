package com.github.cdclaxton.guitartabgenerator.music;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BarTest {

    @Test
    void getNotesIsImmutable() throws InvalidStringException, InvalidFretNumberException,
            InvalidTimingException, InvalidChordException {

        // Construct the bar
        Bar.TimeSignature timeSignature = Bar.TimeSignature.Four4;
        List<Note> notes = new NotesBuilder()
                .addNote(2, 10, 0)
                .addNote(2, 12, 1)
                .build();
        List<TimedChord> timedChords = new TimedChordsBuilder()
                .addTimedChord(Chord.build("C#m"), 0)
                .addTimedChord(Chord.build("E"), 8)
                .build();
        Bar bar = new Bar(timeSignature, notes, timedChords);
        assertEquals(notes, bar.getNotes());

        // Mutate the notes that were used to construct the bar
        notes.add(new Note(new Fret(1, 2), new Timing(0)));

        // Check the constructor used an immutable version of the notes
        List<Note> expectedNotes = new NotesBuilder()
                .addNote(2, 10, 0)
                .addNote(2, 12, 1)
                .build();
        assertEquals(expectedNotes, bar.getNotes());

        // Check that mutating the notes obtained from the bar doesn't modify the original bar
        List<Note> notes2 = bar.getNotes();
        notes2.add(new Note(new Fret(1, 4), new Timing(1)));

        assertEquals(expectedNotes, bar.getNotes());
    }

    @Test
    void getTimedChordsIsImmutable() throws InvalidStringException, InvalidFretNumberException,
            InvalidTimingException, InvalidChordException {

        // Construct the bar
        Bar.TimeSignature timeSignature = Bar.TimeSignature.Four4;
        List<Note> notes = new NotesBuilder()
                .addNote(2, 10, 0)
                .addNote(2, 12, 1)
                .build();
        List<TimedChord> timedChords = new TimedChordsBuilder()
                .addTimedChord(Chord.build("F#m"), 0)
                .addTimedChord(Chord.build("E"), 8)
                .build();
        Bar bar = new Bar(timeSignature, notes, timedChords);
        assertEquals(timedChords, bar.getTimedChords());

        // Mutate the timed chords that were used to construct the bar
        timedChords.add(new TimedChord(new Timing(0), Chord.build("B")));

        // Check the constructor used an immutable version of the timed chords
        List<TimedChord> expectedTimedChords = new TimedChordsBuilder()
                .addTimedChord(Chord.build("F#m"), 0)
                .addTimedChord(Chord.build("E"), 8)
                .build();
        assertEquals(expectedTimedChords, bar.getTimedChords());

        // Check that mutating the chords obtained from the bar doesn't modify the original bar
        List<TimedChord> timedChords1 = bar.getTimedChords();
        timedChords1.add(new TimedChord(new Timing(0), Chord.build("B")));

        assertEquals(expectedTimedChords, bar.getTimedChords());
    }
}