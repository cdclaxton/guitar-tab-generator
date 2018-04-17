package com.github.cdclaxton.guitartabgenerator.music;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;

final class ChordTransposition {

    private static final String[] notes = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};
    private static final String[] enharmonicNotes = {"A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab"};
    private static final int numberNotes = 12;
    private static final Logger logger = LoggerFactory.getLogger(ChordTransposition.class);

    /**
     * Transpose a chord.
     *
     * @param chord Chord to transpose.
     * @param oldKey Old musical key.
     * @param newKey New musical key.
     * @return Transposed chord.
     * @throws TranspositionException Can't transpose the chord (invalid key or change from major to minor).
     */
    static Chord transposeChord(final Chord chord,
                                final String oldKey,
                                final String newKey) throws TranspositionException {

        logger.debug("Transposing chord " + chord + " from key " + oldKey + " to key " + newKey);

        // Check the keys are valid
        if (!Key.isValid(oldKey)) throw new TranspositionException("Old key " + oldKey + " is not valid");
        if (!Key.isValid(newKey)) throw new TranspositionException("New key " + newKey + " is not valid");

        // Check the keys together are valid
        final boolean oldKeyIsMajor = Key.isMajorKey(oldKey);
        final boolean newKeyIsMajor = Key.isMajorKey(newKey);
        if ((oldKeyIsMajor & !newKeyIsMajor) || (!oldKeyIsMajor && newKeyIsMajor)) {
            throw new TranspositionException("Can't transpose from major to minor keys and vice versa");
        }

        // Find the number of semitones difference between the keys
        final int nSemitones = numSemitones(oldKey, newKey);

        // Transpose the base note
        final String transposedRootNote = transposeNote(chord.getRootNote(), nSemitones, newKey);

        // Transpose the bass note if required
        Optional<String> transposedBassNote = Optional.empty();
        if (chord.getBassNote().isPresent()) {
            transposedBassNote = Optional.of(transposeNote(chord.getBassNote().get(), nSemitones, newKey));
        }

        // Create and return the transposed chord
        return new Chord(transposedRootNote, chord.getSymbols(), transposedBassNote);
    }

    /**
     * Transpose a note a given number of semitones.
     *
     * @param note Note to transpose.
     * @param nSemitones Number of semitones to transpose the note.
     * @param newKey New musical key (after transposition).
     * @return Transposed note.
     * @throws TranspositionException Note transposition failed.
     */
    private static String transposeNote(final String note,
                                        final int nSemitones,
                                        final String newKey) throws TranspositionException {

        // Get the index of the note
        final int noteIndex = rootNoteToIndex(note);

        // Get the index of the transposed note
        final int transposedIndex = transposeNoteIndex(noteIndex, nSemitones);

        // Get the note letter (enharmonic where required)
        String transposedNote;
        if (inNotes(removeMinor(newKey))) transposedNote = notes[transposedIndex];
        else transposedNote = enharmonicNotes[transposedIndex];

        return transposedNote;
    }

    /**
     * Get the number of semitones different between two keys.
     *
     * @param oldKey Old musical key.
     * @param newKey New musical key.
     * @return Number of semitones different.
     * @throws TranspositionException Root note can't be found in the list of valid notes.
     */
    static int numSemitones(final String oldKey,
                            final String newKey) throws TranspositionException {

        final String oldKeyBaseNote = keyBaseNote(oldKey);
        final String newKeyBaseNote = keyBaseNote(newKey);

        int diff = rootNoteToIndex(newKeyBaseNote) - rootNoteToIndex(oldKeyBaseNote);

        if (diff > numberNotes/2) {
            diff = diff - numberNotes;
        } else if (diff < -numberNotes/2) {
            diff = diff + numberNotes;
        }

        return diff;
    }

    /**
     * Get the root note from a musical key, e.g. A# from A#m.
     *
     * @param key Musical key.
     * @return Base note.
     */
    private static String keyBaseNote(final String key) {
        if (isMinorKey(key)) return removeMinor(key);
        else return key;
    }

    /**
     * Is the musical key a minor key?
     *
     * @param key Key.
     * @return True if the key is a minor key, otherwise false.
     */
    static boolean isMinorKey(final String key) {
        return key.endsWith("m");
    }

    /**
     * Take the minor part off of the musical key, if present.
     *
     * @param key Key.
     * @return Key without the minor part.
     */
    static String removeMinor(final String key) {
        if (ChordTransposition.isMinorKey(key)) return key.substring(0, key.length()-1);
        else return key;
    }

    /**
     * Given a root note (e.g. A#, Bb), find its index.
     *
     * @param rootNote Root note.
     * @return Index.
     * @throws TranspositionException Invalid root note.
     */
    static int rootNoteToIndex(final String rootNote) throws TranspositionException {
        if (inNotes(rootNote)) return noteIndex(rootNote);
        else if (inEnharmonicNotes(rootNote)) return enharmonicNoteIndex(rootNote);
        else throw new TranspositionException("Root note is not valid: " + rootNote);
    }

    /**
     * Is the note in the list of notes?
     *
     * @param note Note.
     * @return True if the note is contained in the list, otherwise false.
     */
    private static boolean inNotes(final String note) {
        return Arrays.asList(notes).contains(note);
    }

    /**
     * Is the note in the list of enharmonic notes?
     *
     * @param note Note.
     * @return True if the note is contained in the list, otherwise false.
     */
    private static boolean inEnharmonicNotes(final String note) {
        return Arrays.asList(enharmonicNotes).contains(note);
    }

    /**
     * Get the index of a note.
     *
     * @param note Note.
     * @return Index of the note.
     */
    private static int noteIndex(final String note) {
        return Arrays.asList(notes).indexOf(note);
    }

    /**
     * Get the index of an (enharmonic) note.
     *
     * @param note Note.
     * @return Index.
     */
    private static int enharmonicNoteIndex(final String note) {
        return Arrays.asList(enharmonicNotes).indexOf(note);
    }

    /**
     * Transpose a note given its index.
     *
     * @param noteIndex Note index.
     * @param nSemitones Number of semitones to transpose the note.
     * @return Transposed note index.
     * @throws TranspositionException Invalid note index.
     */
    static int transposeNoteIndex(final int noteIndex,
                                  final int nSemitones) throws TranspositionException {

        // Check the note index is valid
        if (noteIndex < 0 || noteIndex >= numberNotes) {
            throw new TranspositionException("Note index " + noteIndex + " is not valid");
        }

        // Calculate the new index using modulo arithmetic
        int index = (noteIndex + nSemitones) % numberNotes;

        // The result of modulo arithmetic can be negative, so make the value positive
        if (index < 0) {
            index = index + numberNotes;
        }

        return index;
    }

}
