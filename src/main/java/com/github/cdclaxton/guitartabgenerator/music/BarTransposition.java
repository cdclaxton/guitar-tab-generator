package com.github.cdclaxton.guitartabgenerator.music;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public final class BarTransposition {

    public static Bar transposeBar(Bar bar, String currentKey, String newKey, boolean up)
            throws TranspositionException, InvalidChordException {

        // Transpose the chords
        List<TimedChord> timedChords = BarTransposition.transposeChords(bar.getTimedChords(), currentKey, newKey);

        // Transpose the notes
        int numSemitones = BarTransposition.numSemitonesDifferent(currentKey, newKey, up);
        List<Note> notes = BarTransposition.transposeNotes(bar.getNotes(), numSemitones);

        // Construct and return the new bar
        return new Bar(bar.getTimeSignature(), notes, timedChords);
    }

    /**
     * Find the number of semitones different between two keys, dependent on direction.
     *
     * @param currentKey Current musical key.
     * @param newKey New musical key.
     * @param up Transpose up?
     * @return Number of semitones different between the keys.
     * @throws TranspositionException
     */
    protected static int numSemitonesDifferent(String currentKey, String newKey, boolean up)
            throws TranspositionException {

        // Always returns a number between -6 and 6
        int nSemitones = ChordTransposition.numSemitones(currentKey, newKey);

        if (up && nSemitones < 0) nSemitones = 12 - nSemitones;
        else if (!up && nSemitones > 0) nSemitones = -nSemitones;

        return nSemitones;
    }

    protected static List<Note> transposeNotes(List<Note> notes, int numSemitones) {

        List<Note> transposedNotes = new ArrayList<>();

        for (Note note : notes) {
            transposedNotes.add(BarTransposition.transposeNote(note, numSemitones));
        }

        return transposedNotes;
    }

    protected static Note transposeNote(Note note, int numSemitones) {

        // TODO -- transpose
        Fret currentFret = note.getFret();


        // Construct and return the transposed note
        return new Note(currentFret, note.getTiming());
    }

    protected static List<TimedChord> transposeChords(List<TimedChord> timedChords, String currentKey, String newKey)
            throws InvalidChordException, TranspositionException {

        List<TimedChord> transposedTimedChords = new ArrayList<>();
        for (TimedChord tc : timedChords) {
            transposedTimedChords.add(BarTransposition.transposeChord(tc, currentKey, newKey));
        }
        return transposedTimedChords;
    }

    protected static TimedChord transposeChord(TimedChord timedChord, String currentKey, String newKey)
            throws TranspositionException, InvalidChordException {

        return new TimedChord(timedChord.getTiming(),
                ChordTransposition.transposeChord(timedChord.getChord(), currentKey, newKey));
    }

}
