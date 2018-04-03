package com.github.cdclaxton.guitartabgenerator.music;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Transposition {

    private static String[] notes = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};
    private static String[] enharmonicNotes = {"A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab"};
    private static int numberNotes = 12;

    public static class ChordParts {
        private String base;
        private String rest;

        public ChordParts(String base, String rest) {
            this.base = base;
            this.rest = rest;
        }

        public String getBase() {
            return base;
        }

        public String getRest() {
            return rest;
        }
    }

    public static String transposeChord(String chord, String oldKey, String newKey) throws TranspositionException {

        // Check the keys are valid
        if (!Key.isValid(oldKey)) throw new TranspositionException("Key " + oldKey + " is not valid");
        if (!Key.isValid(newKey)) throw new TranspositionException("Key " + newKey + " is not valid");

        // Check the keys are valid together
        boolean oldKeyIsMajor = Key.isMajorKey(oldKey);
        boolean newKeyIsMajor = Key.isMajorKey(newKey);
        if ((oldKeyIsMajor & !newKeyIsMajor) || (!oldKeyIsMajor && newKeyIsMajor)) {
            throw new TranspositionException("Can't transpose from major to minor keys and vice versa");
        }

        // Find the number of semitones difference between the keys
        int nSemitones = numSemitones(oldKey, newKey);

        // Split the chord into two parts
        ChordParts parts = splitChord(chord);

        // Get the index of the base note
        int noteIndex = baseNoteToIndex(parts.base);

        // Get the index of the transposed note
        int transposedIndex = transposeNoteIndex(noteIndex, nSemitones);

        // Get the transposed base note
        String transposedNote;
        if (inNotes(removeMinor(newKey))) transposedNote = notes[transposedIndex];
        else transposedNote = enharmonicNotes[transposedIndex];

        // Rebuild the chord
        return transposedNote + parts.rest;
    }

    protected static ChordParts splitChord(String chord) throws TranspositionException {
        final String pattern = "([ABCDEFG](?:#|b)?)(.*)";
        final Pattern compiledPattern = Pattern.compile(pattern);
        final Matcher matcher = compiledPattern.matcher(chord);
        if (matcher.find()) {
            String base = matcher.group(1);
            String rest = matcher.group(2);
            return new ChordParts(base, rest);
        } else {
            throw new TranspositionException("Can't split chord: " + chord);
        }
    }

    protected static int numSemitones(String oldKey, String newKey) throws TranspositionException {
        String oldKeyBaseNote = keyBaseNote(oldKey);
        String newKeyBaseNote = keyBaseNote(newKey);

        int diff = baseNoteToIndex(newKeyBaseNote) - baseNoteToIndex(oldKeyBaseNote);

        if (diff > numberNotes/2) {
            diff = diff - numberNotes;
        }

        return diff;
    }

    protected static String keyBaseNote(String key) {
        if (isMinorKey(key)) return removeMinor(key);
        else return key;
    }

    protected static boolean isMinorKey(String key) {
        return key.endsWith("m");
    }

    protected static String removeMinor(String key) {
        if (Transposition.isMinorKey(key)) return key.substring(0, key.length()-1);
        else return key;
    }

    protected static int baseNoteToIndex(String baseNote) throws TranspositionException {
        if (inNotes(baseNote)) return noteIndex(baseNote);
        else if (inEnharmonicNotes(baseNote)) return enharmonicNoteIndex(baseNote);
        else throw new TranspositionException("Base note is not valid: " + baseNote);
    }

    private static boolean inNotes(String note) {
        return Arrays.asList(notes).contains(note);
    }

    private static boolean inEnharmonicNotes(String note) {
        return Arrays.asList(enharmonicNotes).contains(note);
    }

    private static int noteIndex(String note) {
        return Arrays.asList(notes).indexOf(note);
    }

    private static int enharmonicNoteIndex(String note) {
        return Arrays.asList(enharmonicNotes).indexOf(note);
    }

    protected static int transposeNoteIndex(int noteIndex, int nSemitones) throws TranspositionException {

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
