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
final class NoteTransposition {

    static class TempFret {
        private final int stringNumber;
        private final int fretNumber;

        /**
         * Construct a object to represent a fret and string number.
         *
         * @param stringNumber String number (1 = high E string).
         * @param fretNumber Fret number.
         */
        TempFret(final int stringNumber,
                 final int fretNumber) {
            this.stringNumber = stringNumber;
            this.fretNumber = fretNumber;
        }

        /**
         * Get the string number.
         *
         * @return String number.
         */
        int getStringNumber() {
            return this.stringNumber;
        }

        /**
         * Get the fret number.
         *
         * @return Fret number.
         */
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
     * @param maxFretNumber Maximum fret number.
     * @return List of transposed notes.
     * @throws TranspositionException Unable to transpose the list of notes.
     */
    static List<Note> transposeNotes(final List<Note> notes,
                                     final int nSemitones,
                                     final int maxFretNumber) throws TranspositionException {

        // Make a list of frets
        final List<Fret> frets = notes.stream().map(Note::getFret).collect(Collectors.toList());

        // Transpose the frets
        final List<Fret> transposedFrets = transposeFrets(frets, nSemitones, maxFretNumber);
        assert transposedFrets.size() == notes.size();

        // Create a list of transposed notes
        final List<Note> transposedNotes = new ArrayList<>(frets.size());
        for (int i = 0; i < notes.size(); i++) {
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
     * @throws TranspositionException Can't transpose due to invalid strings or fret numbers.
     */
    private static List<Fret> transposeFrets(final List<Fret> frets,
                                             final int nSemitones,
                                             final int maxFretNumber) throws TranspositionException {

        // Convert the list of frets to TempFret objects (that can have negative fret numbers)
        List<TempFret> tempFrets = fretsToTempFrets(frets);

        // Transpose each of the notes individually (but keeping them on the same string)
        tempFrets = differentNotesSameStrings(tempFrets, nSemitones);

        // Resolve any conflicts
        tempFrets = resolveConflicts(tempFrets, nSemitones > 0, maxFretNumber);

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

    /**
     * Resolve fret conflicts, e.g. negative frets, frets too high.
     *
     * @param tempFrets List of frets.
     * @param up Transposed up?
     * @param maxFretNumber Maximum fret number.
     * @return List of (potentially) resolved notes.
     * @throws TranspositionException Unable to resolve conflicts.
     */
    private static List<TempFret> resolveConflicts(List<TempFret> tempFrets,
                                                   final boolean up,
                                                   final int maxFretNumber) throws TranspositionException {

        // If there are no frets, there are no conflicts to resolve
        if (tempFrets.size() == 0) return tempFrets;

        if (up) {
            // Transposed up, therefore the notes could be difficult or impossible to play
            if (minimumStringNumber(tempFrets) > 1) {
                tempFrets = moveToHigherStrings(tempFrets, maxFretNumber);
            }

            // Move down the octave if the maximum fret number is too high
            if (maximumFretNumber(tempFrets) > maxFretNumber) {
                tempFrets = dropDownAnOctave(tempFrets);
            }

        } else {
            // Could be negative frets, so move to lower strings if that's the case
            tempFrets = moveToLowerStrings(tempFrets);
        }

        return tempFrets;
    }

    /**
     * Drop all notes down one octave.
     *
     * @param tempFrets List of notes to transpose.
     * @return Transposed notes.
     */
    private static List<TempFret> dropDownAnOctave(final List<TempFret> tempFrets) {
        return tempFrets.stream()
                .map(tf -> new TempFret(tf.getStringNumber(), tf.getFretNumber()-12))
                .collect(Collectors.toList());
    }

    /**
     * Make a note with a negative fret number valid by changing strings.
     *
     * @param fret (Potentially) invalid fret.
     * @return Valid fret.
     * @throws TranspositionException Unable to a make a negative fret valid.
     */
    static TempFret makeNoteNegativeFretValid(TempFret fret) throws TranspositionException {

        if (fret.getFretNumber() < 0) {
            while (fret.getStringNumber() < 6 && fret.getFretNumber() < 0) {
                fret = NoteTransposition.sameNoteLowerString(fret);
            }
        }

        // Check that the above logic was able to make the note valid
        if (fret.getFretNumber() < 0) {
            throw new TranspositionException("Can't make note valid: " + fret);
        }

        return fret;
    }

    /**
     * Move the notes to higher (thinner) strings such that the maximum fret number <= maxFretNumber.
     *
     * @param tempFrets List of notes to move.
     * @param maxFretNumber Maximum fret number.
     * @return Moved notes.
     * @throws TranspositionException Can't move the notes to higher strings.
     */
    static List<TempFret> moveToHigherStrings(List<TempFret> tempFrets,
                                              final int maxFretNumber)
            throws TranspositionException {

        // Don't continue if the list is empty
        if (tempFrets.size() == 0) return tempFrets;

        // While there are frets above the maximum number, move the notes to higher (thinner) strings
        while (maximumFretNumber(tempFrets) > maxFretNumber) {
            for (int i = 0; i < tempFrets.size(); i++) {
                tempFrets.set(i, sameNoteHigherString(tempFrets.get(i)));
            }
        }

        return tempFrets;
    }

    /**
     * Move the notes to lower (thicker) strings such that the minimum fret number >= 0.
     *
     * @param tempFrets List of notes to move.
     * @return Moved notes.
     * @throws TranspositionException Can't move the notes to lower strings.
     */
    static List<TempFret> moveToLowerStrings(List<TempFret> tempFrets) throws TranspositionException {

        // Don't continue if the list is empty
        if (tempFrets.size() == 0) return tempFrets;

        // While there are invalid fret numbers present, move the notes to lower (thicker) strings
        while (minimumFretNumber(tempFrets) < 0) {
            for (int i = 0; i < tempFrets.size(); i++) {
                tempFrets.set(i, sameNoteLowerString(tempFrets.get(i)));
            }
        }

        return tempFrets;
    }

    /**
     * Find the minimum string number.
     *
     * @param tempFrets List of temp fret objects.
     * @return Minimum string number.
     */
    private static int minimumStringNumber(final List<TempFret> tempFrets) {

        // Precondition
        assert tempFrets.size() > 0;

        return tempFrets.stream().mapToInt(TempFret::getStringNumber).min().getAsInt();
    }

    /**
     * Find the minimum fret number.
     *
     * @param tempFrets List of temp fret objects.
     * @return Minimum fret number.
     */
    private static int minimumFretNumber(final List<TempFret> tempFrets) {

        // Precondition
        assert tempFrets.size() > 0;

        return tempFrets.stream().mapToInt(TempFret::getFretNumber).min().getAsInt();
    }

    /**
     * Find the maximum fret number.
     *
     * @param tempFrets List of temp fret objects.
     * @return Maximum fret number.
     */
    private static int maximumFretNumber(final List<TempFret> tempFrets) {

        // Precondition
        assert tempFrets.size() > 0;

        return tempFrets.stream().mapToInt(TempFret::getFretNumber).max().getAsInt();
    }

    /**
     * Convert a list of Fret objects to a list of TempFret objects.
     *
     * @param frets List of Fret objects.
     * @return List of TempFret objects.
     */
    private static List<TempFret> fretsToTempFrets(List<Fret> frets) {
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
    private static TempFret fretToTempFret(final Fret fret) {
        return new TempFret(fret.getStringNumber(), fret.getFretNumber());
    }

    /**
     * Convert a list of TempFret objects to a list of Fret objects.
     *
     * @param tempFrets List of TempFret objects.
     * @return List of Fret objects.
     * @throws InvalidStringException String number is invalid.
     * @throws InvalidFretNumberException Fret number is invalid.
     */
    private static List<Fret> tempFretsToFrets(final List<TempFret> tempFrets)
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
     * @throws InvalidStringException String number is invalid.
     * @throws InvalidFretNumberException Fret number is invalid.
     */
    private static Fret tempFretToFret(final TempFret tempFret) throws InvalidStringException, InvalidFretNumberException {
        return new Fret(tempFret.getStringNumber(), tempFret.getFretNumber());
    }

    /**
     * Transpose a list of notes such that they stay on their original guitar string (may cause invalid values).
     *
     * @param tempFrets List of notes.
     * @param nSemitones Number of semitones to transpose the notes.
     * @return List of (potentially invalid) transposed notes.
     */
    private static List<TempFret> differentNotesSameStrings(final List<TempFret> tempFrets, final int nSemitones) {
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
    private static TempFret differentNoteSameString(final TempFret tempFret,
                                                    final int nSemitones) {

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
     * @throws TranspositionException Either new string number is invalid or the notes can't be moved.
     */
    static TempFret sameNoteDifferentString(final TempFret tempFret,
                                            final int newStringNumber) throws TranspositionException {

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
     * @throws TranspositionException Either new string number is invalid or the notes can't be moved.
     */
    private static TempFret sameNoteLowerStrings(final TempFret tempFret,
                                                 final int nStringsDown) throws TranspositionException {

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
     * @return Fret number nStringsUp strings down.
     * @throws TranspositionException Either new string number is invalid or the notes can't be moved.
     */
    private static TempFret sameNoteHigherStrings(final TempFret tempFret,
                                                  final int nStringsUp) throws TranspositionException {

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
     * @throws TranspositionException Either new string number is invalid or the notes can't be moved.
     */
    static TempFret sameNoteLowerString(final TempFret tempFret) throws TranspositionException {

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
     * @throws TranspositionException Either new string number is invalid or the notes can't be moved.
     */
    static TempFret sameNoteHigherString(final TempFret tempFret) throws TranspositionException {

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
    private static boolean isStringNumberValid(final int stringNumber) {
        return (stringNumber >= 1) && (stringNumber <= 6);
    }

}
