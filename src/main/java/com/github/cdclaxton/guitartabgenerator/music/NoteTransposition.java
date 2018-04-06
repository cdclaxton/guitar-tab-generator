package com.github.cdclaxton.guitartabgenerator.music;

public final class NoteTransposition {

    /**
     * Transpose a note to a different string.
     *
     * Note that the resulting fret number may be negative.
     *
     * @param fret Fret number.
     * @param stringNumber Current string number.
     * @param newStringNumber New string number.
     * @return Fret number on the required string.
     * @throws TranspositionException
     */
    public static int sameNoteDifferentString(int fret, int stringNumber, int newStringNumber)
            throws TranspositionException {

        if (!isStringNumberValid(stringNumber)) {
            throw new TranspositionException("Current string number isn't valid: " + stringNumber);
        } else if (!isStringNumberValid(newStringNumber)) {
            throw new TranspositionException("New string number isn't valid: " + newStringNumber);
        }

        int newFret = fret;
        if (newStringNumber < stringNumber) {
            newFret = sameNoteHigherStrings(fret, stringNumber, stringNumber - newStringNumber);
        } else if (newStringNumber > stringNumber) {
            newFret = sameNoteLowerStrings(fret, stringNumber, newStringNumber - stringNumber);
        }

        return newFret;
    }

    /**
     * Find the fret number for a note on a lower (thicker) string.
     *
     * @param fret Fret number.
     * @param stringNumber Current string number.
     * @param nStringsDown Number of strings down to find the note.
     * @return Fret number nStringsDown strings down.
     * @throws TranspositionException
     */
    private static int sameNoteLowerStrings(int fret, int stringNumber, int nStringsDown)
            throws TranspositionException {

        if (nStringsDown < 0) throw new TranspositionException("Number of strings down is negative");

        int newFret = fret;
        int newStringNumber = stringNumber;
        for (int i = 0; i < nStringsDown; i++) {
            newFret = sameNoteLowerString(newFret, newStringNumber);
            newStringNumber += 1;
        }
        return newFret;
    }

    /**
     * Find the fret number for a note on a higher (thinner) string.
     *
     * @param fret Fret number.
     * @param stringNumber Current string number.
     * @param nStringsUp Number of strings up to find the note.
     * @return Fret number nStringsDown strings down.
     * @return Fret number nStringsUp strings down.
     * @throws TranspositionException
     */
    private static int sameNoteHigherStrings(int fret, int stringNumber, int nStringsUp)
            throws TranspositionException {

        if (nStringsUp < 0) throw new TranspositionException("Number of strings up is negative");

        int newFret = fret;
        int newStringNumber = stringNumber;
        for (int i = 0; i < nStringsUp; i++) {
            newFret = sameNoteHigherString(newFret, newStringNumber);
            newStringNumber -= 1;
        }
        return newFret;
    }

    /**
     * Find the fret number of a note on the next lowest (thicker) string.
     *
     * @param fret Fret number.
     * @param stringNumber Current string number.
     * @return Fret number on the lower string.
     * @throws TranspositionException
     */
    protected static int sameNoteLowerString(int fret, int stringNumber) throws TranspositionException {

        if (!isStringNumberValid(stringNumber))
            throw new TranspositionException("Invalid string number: " + stringNumber);
        else if (stringNumber == 6)
            throw new TranspositionException("No string below 6");

        if (stringNumber == 2) return fret + 4;
        else return fret + 5;
    }

    /**
     * Find the fret number of a note on the next highest (thinner) string.
     *
     * @param fret Fret number.
     * @param stringNumber Current string number.
     * @return Fret number on the higher string.
     * @throws TranspositionException
     */
    protected static int sameNoteHigherString(int fret, int stringNumber) throws TranspositionException {

        if (!isStringNumberValid(stringNumber))
            throw new TranspositionException("Invalid string number: " + stringNumber);
        else if (stringNumber == 1)
            throw new TranspositionException("No string above 1");

        if (stringNumber == 3) return fret - 4;
        else return fret - 5;
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
