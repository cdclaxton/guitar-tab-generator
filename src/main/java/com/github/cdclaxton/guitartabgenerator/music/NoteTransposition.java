package com.github.cdclaxton.guitartabgenerator.music;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Note the reason this class doesn't use the Fret class to represent strings and fret numbers is that
 * that class is designed for storing valid combinations. Due to the way the transposition is performed
 * iteratively, there may be negative frets in the intermediate stages.
 */
public final class NoteTransposition {

    static class TempFret {
        private int stringNumber;
        private int fretNumber;

        TempFret(int stringNumber, int fretNumber) {
            this.stringNumber = stringNumber;
            this.fretNumber = fretNumber;
        }

        int getStringNumber() {
            return this.stringNumber;
        }

        int getFretNumber() {
            return this.fretNumber;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TempFret tempFret = (TempFret) o;
            return stringNumber == tempFret.stringNumber &&
                    fretNumber == tempFret.fretNumber;
        }

        @Override
        public int hashCode() {

            return Objects.hash(stringNumber, fretNumber);
        }

        @Override
        public String toString() {
            return "TempFret[String: " + this.getStringNumber() + ", fret: " + this.getFretNumber() + "]";
        }
    }

    /**
     * Transpose a list of notes by the required number of semitones.
     *
     * @param notes List of notes.
     * @param nSemitones Number of semitones to transpose the notes.
     * @return List of transposed notes.
     * @throws TranspositionException
     */
    public static List<Note> transposeNotes(List<Note> notes, int nSemitones) throws TranspositionException {

        // Make a list of frets
        List<Fret> frets = notes.stream().map(n -> n.getFret()).collect(Collectors.toList());

        // Transpose the frets
        List<Fret> transposedFrets = transposeFrets(frets, nSemitones);
        assert transposedFrets.size() == notes.size();

        // Create a list of transposed notes
        List<Note> transposedNotes = new ArrayList<>(frets.size());
        for (int i = 0; i < transposedNotes.size(); i++) {
            Timing timing = notes.get(i).getTiming();
            transposedNotes.add(new Note(transposedFrets.get(i), timing));
        }

        return transposedNotes;
    }

    /**
     * Transpose the list of notes by the required number of semitones.
     *
     * @param frets List of notes (strings and fret numbers).
     * @param nSemitones Number of semitones to transpose the notes.
     * @return List of transposed notes.
     * @throws TranspositionException
     */
    public static List<Fret> transposeFrets(List<Fret> frets, int nSemitones) throws TranspositionException {

        // Convert the list of frets to TempFret objects (that can have negative fret numbers)
        List<TempFret> tempFrets = fretsToTempFrets(frets);

        // Transpose each of the notes individually (but keeping them on the same string)
        tempFrets = differentNotesSameStrings(tempFrets, nSemitones);

        // Resolve any conflicts

        // Convert the temporary notes to Fret objects
        List<Fret> transposedFrets;
        try {
            transposedFrets = tempFretsToFrets(tempFrets);
        } catch (InvalidStringException e) {
            throw new TranspositionException("Transposed notes ended up with invalid strings");
        } catch (InvalidFretNumberException e) {
            throw new TranspositionException("Transposed notes ended up with invalid fret numbers");
        }

        return transposedFrets;
    }

    static List<TempFret> resolveConflicts(List<TempFret> frets) {

        return null;
    }

    static TempFret makeNoteValid(TempFret fret) {

        return null;
    }

    /**
     * Convert a list of Fret objects to a list of TempFret objects.
     *
     * @param frets List of Fret objects.
     * @return List of TempFret objects.
     */
    static List<TempFret> fretsToTempFrets(List<Fret> frets) {
        List<TempFret> tempFrets = new ArrayList<>(frets.size());

        for (Fret f : frets) {
            tempFrets.add(fretToTempFret(f));
        }

        return tempFrets;
    }

    /**
     * Convert a Fret object to a TempFret object.
     *
     * @param fret Fret object.
     * @return TempFret object.
     */
    static TempFret fretToTempFret(Fret fret) {
        return new TempFret(fret.getStringNumber(), fret.getFretNumber());
    }

    /**
     * Convert a list of TempFret objects to a list of Fret objects.
     *
     * @param tempFrets List of TempFret objects.
     * @return List of Fret objects.
     * @throws InvalidStringException
     * @throws InvalidFretNumberException
     */
    static List<Fret> tempFretsToFrets(List<TempFret> tempFrets)
            throws InvalidStringException, InvalidFretNumberException {

        List<Fret> frets = new ArrayList<>(tempFrets.size());

        for (TempFret tempFret : tempFrets) {
            frets.add(tempFretToFret(tempFret));
        }

        return frets;
    }

    /**
     * Convert a TempFret object to a Fret object.
     *
     * @param tempFret TempFret object to convert.
     * @return Fret object.
     * @throws InvalidStringException
     * @throws InvalidFretNumberException
     */
    static Fret tempFretToFret(TempFret tempFret) throws InvalidStringException, InvalidFretNumberException {
        return new Fret(tempFret.getStringNumber(), tempFret.getFretNumber());
    }

    /**
     * Transpose a list of notes such that they stay on their original guitar string (may cause invalid values).
     *
     * @param tempFrets List of notes.
     * @param nSemitones Number of semitones to transpose the notes.
     * @return List of (potentially invalid) transposed notes.
     */
    static List<TempFret> differentNotesSameStrings(List<TempFret> tempFrets, int nSemitones) {
        return tempFrets.stream()
                .map(tf -> differentNoteSameString(tf, nSemitones))
                .collect(Collectors.toList());
    }

    /**
     * Transpose a note such that the new note is on the same string.
     *
     * @param tempFret Note to transpose.
     * @param nSemitones Number of semitones to transpose (-ve means down).
     * @return Transposed note.
     */
    static TempFret differentNoteSameString(TempFret tempFret, int nSemitones) {
        return new TempFret(tempFret.getStringNumber(), tempFret.getFretNumber() + nSemitones);
    }

    /**
     * Transpose a note to a different string.
     *
     * Note that the resulting tempFret number may be negative.
     *
     * @param tempFret Fret number.
     * @param newStringNumber New string number.
     * @return Fret number on the required string.
     * @throws TranspositionException
     */
    static TempFret sameNoteDifferentString(TempFret tempFret, int newStringNumber)
            throws TranspositionException {

        if (!isStringNumberValid(newStringNumber)) {
            throw new TranspositionException("New string number isn't valid: " + newStringNumber);
        }

        TempFret newFret = tempFret;
        if (newStringNumber < tempFret.getStringNumber()) {
            newFret = sameNoteHigherStrings(newFret, tempFret.getStringNumber() - newStringNumber);
        } else if (newStringNumber > tempFret.getStringNumber()) {
            newFret = sameNoteLowerStrings(newFret, newStringNumber - tempFret.getStringNumber());
        }

        return newFret;
    }

    /**
     * Find the fret number for a note on a lower (thicker) string.
     *
     * @param tempFret Fret number.
     * @param nStringsDown Number of strings down to find the note.
     * @return Fret number nStringsDown strings down.
     * @throws TranspositionException
     */
    static TempFret sameNoteLowerStrings(TempFret tempFret, int nStringsDown)
            throws TranspositionException {

        if (nStringsDown < 0) throw new TranspositionException("Number of strings down is negative");

        TempFret newFret = tempFret;
        for (int i = 0; i < nStringsDown; i++) {
            newFret = sameNoteLowerString(newFret);
        }

        return newFret;
    }

    /**
     * Find the fret number for a note on a higher (thinner) string.
     *
     * @param tempFret Fret number.
     * @param nStringsUp Number of strings up to find the note.
     * @return Fret number nStringsDown strings down.
     * @return Fret number nStringsUp strings down.
     * @throws TranspositionException
     */
    static TempFret sameNoteHigherStrings(TempFret tempFret, int nStringsUp)
            throws TranspositionException {

        if (nStringsUp < 0) throw new TranspositionException("Number of strings up is negative");

        TempFret newFret = tempFret;
        for (int i = 0; i < nStringsUp; i++) {
            newFret = sameNoteHigherString(newFret);
        }

        return newFret;
    }

    /**
     * Find the fret number of a note on the next lowest (thicker) string.
     *
     * @param tempFret Fret number.
     * @return Fret number on the lower string.
     * @throws TranspositionException
     */
    static TempFret sameNoteLowerString(TempFret tempFret) throws TranspositionException {

        if (tempFret.getStringNumber() == 6) {
            throw new TranspositionException("No string below 6");
        }

        if (tempFret.getStringNumber() == 2) {
            return new TempFret(tempFret.getStringNumber() + 1, tempFret.getFretNumber() + 4);
        } else {
            return new TempFret(tempFret.getStringNumber() + 1, tempFret.getFretNumber() + 5);
        }
    }

    /**
     * Find the fret number of a note on the next highest (thinner) string.
     *
     * @param tempFret Fret number.
     * @return Fret number on the higher string.
     * @throws TranspositionException
     */
    static TempFret sameNoteHigherString(TempFret tempFret) throws TranspositionException {

        if (tempFret.getFretNumber() == 1) {
            throw new TranspositionException("No string above 1");
        }

        if (tempFret.getStringNumber() == 3) {
            return new TempFret(tempFret.getStringNumber()-1, tempFret.getFretNumber() - 4);
        } else {
            return new TempFret(tempFret.getStringNumber()-1, tempFret.getFretNumber() - 5);
        }
    }

    /**
     * Is the string number valid?
     *
     * @param stringNumber String number to check.
     * @return Returns true if the string number is valid, otherwise false.
     */
    private static boolean isStringNumberValid(int stringNumber) {
        return (stringNumber >= 1) && (stringNumber <= 6);
    }

}
