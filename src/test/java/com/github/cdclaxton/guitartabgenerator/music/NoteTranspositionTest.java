package com.github.cdclaxton.guitartabgenerator.music;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class NoteTranspositionTest {

    private final  int NUM_RANDOM_TESTS = 100;

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

    class TempFretBuilder {
        private List<NoteTransposition.TempFret> tempFrets = new ArrayList<>();

        public TempFretBuilder() {}

        public TempFretBuilder addTempFret(int stringNumber, int fret) {
            tempFrets.add(new NoteTransposition.TempFret(stringNumber, fret));
            return this;
        }

        public List<NoteTransposition.TempFret> build() {
            return this.tempFrets;
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
    void testTransposeNotesTwoNotesFiveSemitones() throws InvalidStringException, InvalidFretNumberException,
            InvalidTimingException, TranspositionException {

        for (int stringNumber = 1; stringNumber <= 5; stringNumber++) {

            int randomFret1 = RandomGenerators.randomFret(0, 17);
            int randomFret2 = RandomGenerators.randomFret(0, 17);

            List<Note> notes1 = new NotesBuilder()
                    .addNote(stringNumber, randomFret1, 0)
                    .addNote(stringNumber+1, randomFret2, 0)
                    .build();

            List<Note> expectedTransposed1 = new NotesBuilder()
                    .addNote(stringNumber, randomFret1+5, 0)
                    .addNote(stringNumber+1, randomFret2+5, 0)
                    .build();

            assertEquals(expectedTransposed1, NoteTransposition.transposeNotes(notes1, 5));
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

    @Test
    void makeNoteNegativeFretValid() throws TranspositionException {

        // Note that doesn't need changing
        for (int i = 0; i < NUM_RANDOM_TESTS; i++) {
            int stringNumber = RandomGenerators.randomStringNumber();
            int fretNumber = RandomGenerators.randomFret(0,16);
            NoteTransposition.TempFret tempFret = new NoteTransposition.TempFret(stringNumber, fretNumber);
            assertEquals(tempFret, NoteTransposition.makeNoteNegativeFretValid(tempFret));
        }

        // Notes that need to go one string down (thicker string)
        assertEquals(new NoteTransposition.TempFret(2, 4),
                NoteTransposition.makeNoteNegativeFretValid(new NoteTransposition.TempFret(1, -1)));
        assertEquals(new NoteTransposition.TempFret(3, 3),
                NoteTransposition.makeNoteNegativeFretValid(new NoteTransposition.TempFret(2, -1)));
        assertEquals(new NoteTransposition.TempFret(4, 4),
                NoteTransposition.makeNoteNegativeFretValid(new NoteTransposition.TempFret(3, -1)));
        assertEquals(new NoteTransposition.TempFret(5, 4),
                NoteTransposition.makeNoteNegativeFretValid(new NoteTransposition.TempFret(4, -1)));
        assertEquals(new NoteTransposition.TempFret(6, 4),
                NoteTransposition.makeNoteNegativeFretValid(new NoteTransposition.TempFret(5, -1)));
    }

    @Test
    void moveToLowerStrings() throws TranspositionException {
        List<NoteTransposition.TempFret> tempFrets = new TempFretBuilder()
                .addTempFret(5, -1)
                .addTempFret(4, 1)
                .addTempFret(2, 1)
                .build();

        List<NoteTransposition.TempFret> expected = new TempFretBuilder()
                .addTempFret(6, 4)
                .addTempFret(5, 6)
                .addTempFret(3,5)
                .build();

        assertEquals(expected, NoteTransposition.moveToLowerStrings(tempFrets));
    }

    @Test
    void changeToNonConflictingStrings() {

        // One note


    }

}