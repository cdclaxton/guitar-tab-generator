package com.github.cdclaxton.guitartabgenerator.music;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class NoteTranspositionTest {

    class NotesBuilder {
        private List<Note> notes = new ArrayList<>();

        public NotesBuilder() {}

        public NotesBuilder addNote(int stringNumber, int fret, int timing)
                throws InvalidStringException, InvalidFretNumberException, InvalidTimingException {

            notes.add(new Note(new Fret(stringNumber, fret), new Timing(timing)));
            return this;
        }

        public List<Note> build() {
            return this.notes;
        }
    }

    @Test
    void testTransposeNotesSingleNoteOneSemitone() throws InvalidStringException, InvalidFretNumberException,
            InvalidTimingException, TranspositionException {

        for (int stringNumber = 1; stringNumber <= 6; stringNumber++) {

            int randomFret = RandomGenerators.randomFret(0, 21);

            List<Note> notes1 = new NotesBuilder()
                    .addNote(stringNumber, randomFret, 0)
                    .build();

            List<Note> expectedTransposed1 = new NotesBuilder()
                    .addNote(stringNumber, randomFret+1, 0)
                    .build();

            assertEquals(expectedTransposed1, NoteTransposition.transposeNotes(notes1, 1));
        }

    }



    @Test
    void sameNoteDifferentString() throws TranspositionException {

        // Open strings -- down
        assertEquals(new NoteTransposition.TempFret(2, 5),
                NoteTransposition.sameNoteDifferentString(new NoteTransposition.TempFret(1, 0), 2));

        assertEquals(new NoteTransposition.TempFret(3, 4),
                NoteTransposition.sameNoteDifferentString(new NoteTransposition.TempFret(2, 0), 3));

        assertEquals(new NoteTransposition.TempFret(4, 5),
                NoteTransposition.sameNoteDifferentString(new NoteTransposition.TempFret(3, 0), 4));

        assertEquals(new NoteTransposition.TempFret(5, 5),
                NoteTransposition.sameNoteDifferentString(new NoteTransposition.TempFret(4, 0), 5));

        assertEquals(new NoteTransposition.TempFret(6, 5),
                NoteTransposition.sameNoteDifferentString(new NoteTransposition.TempFret(5, 0), 6));

        // Open strings -- up
        assertEquals(new NoteTransposition.TempFret(5, 0),
                NoteTransposition.sameNoteDifferentString(new NoteTransposition.TempFret(6, 5), 5));

        assertEquals(new NoteTransposition.TempFret(4, 0),
                NoteTransposition.sameNoteDifferentString(new NoteTransposition.TempFret(5, 5), 4));

        assertEquals(new NoteTransposition.TempFret(3, 0),
                NoteTransposition.sameNoteDifferentString(new NoteTransposition.TempFret(4, 5), 3));

        assertEquals(new NoteTransposition.TempFret(2, 0),
                NoteTransposition.sameNoteDifferentString(new NoteTransposition.TempFret(3, 4), 2));

        assertEquals(new NoteTransposition.TempFret(1, 0),
                NoteTransposition.sameNoteDifferentString(new NoteTransposition.TempFret(2, 5), 1));

        // Non-open strings - down
        assertEquals(new NoteTransposition.TempFret(2, 12),
                NoteTransposition.sameNoteDifferentString(new NoteTransposition.TempFret(1, 7), 2));

        assertEquals(new NoteTransposition.TempFret(3, 16),
                NoteTransposition.sameNoteDifferentString(new NoteTransposition.TempFret(1, 7), 3));

        assertEquals(new NoteTransposition.TempFret(3, 11),
                NoteTransposition.sameNoteDifferentString(new NoteTransposition.TempFret(2, 7), 3));

        assertEquals(new NoteTransposition.TempFret(3, 16),
                NoteTransposition.sameNoteDifferentString(new NoteTransposition.TempFret(1, 7), 3));

        assertEquals(new NoteTransposition.TempFret(4, 10),
                NoteTransposition.sameNoteDifferentString(new NoteTransposition.TempFret(2, 1), 4));

        // Non-open strings -- up
        assertEquals(new NoteTransposition.TempFret(5, 0),
                NoteTransposition.sameNoteDifferentString(new NoteTransposition.TempFret(6, 5), 5));

        assertEquals(new NoteTransposition.TempFret(4, 0),
                NoteTransposition.sameNoteDifferentString(new NoteTransposition.TempFret(6, 10), 4));


        // No change in string number
        Random random = new Random();
        int randomFret = random.nextInt(22 + 1);

        for (int stringNumber = 1; stringNumber <= 6; stringNumber++) {
            assertEquals(new NoteTransposition.TempFret(stringNumber, randomFret),
                    NoteTransposition.sameNoteDifferentString(new NoteTransposition.TempFret(stringNumber, randomFret), stringNumber));


        }

        // Negative frets
        assertEquals(new NoteTransposition.TempFret(2, -1),
                NoteTransposition.sameNoteDifferentString(new NoteTransposition.TempFret(3, 3), 2));

    }

    @Test
    void sameNoteLowerString() throws TranspositionException {

        assertEquals(new NoteTransposition.TempFret(2, 5),
                NoteTransposition.sameNoteLowerString(new NoteTransposition.TempFret(1, 0)));

        assertEquals(new NoteTransposition.TempFret(3, 4),
                NoteTransposition.sameNoteLowerString(new NoteTransposition.TempFret(2, 0)));

        assertEquals(new NoteTransposition.TempFret(4, 5),
                NoteTransposition.sameNoteLowerString(new NoteTransposition.TempFret(3, 0)));

        assertEquals(new NoteTransposition.TempFret(5, 5),
                NoteTransposition.sameNoteLowerString(new NoteTransposition.TempFret(4, 0)));

        assertEquals(new NoteTransposition.TempFret(6, 5),
                NoteTransposition.sameNoteLowerString(new NoteTransposition.TempFret(5, 0)));
    }

    @Test
    void sameNoteHigherString() throws TranspositionException {
        assertEquals(new NoteTransposition.TempFret(5, 0),
                NoteTransposition.sameNoteHigherString(new NoteTransposition.TempFret(6, 5)));

        assertEquals(new NoteTransposition.TempFret(4, 0),
                NoteTransposition.sameNoteHigherString(new NoteTransposition.TempFret(5, 5)));

        assertEquals(new NoteTransposition.TempFret(3, 0),
                NoteTransposition.sameNoteHigherString(new NoteTransposition.TempFret(4, 5)));

        assertEquals(new NoteTransposition.TempFret(2, 0),
                NoteTransposition.sameNoteHigherString(new NoteTransposition.TempFret(3, 4)));

        assertEquals(new NoteTransposition.TempFret(1, 0),
                NoteTransposition.sameNoteHigherString(new NoteTransposition.TempFret(2, 5)));
    }
}