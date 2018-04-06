package com.github.cdclaxton.guitartabgenerator.music;

public class NoteTransposition {


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

    private static int sameNoteLowerStrings(int fret, int stringNumber, int nStringsDown)
            throws TranspositionException {

        if (nStringsDown < 0) throw new TranspositionException("Number of strings down is negative");

        int newFret = fret;
        int newStringNumber = stringNumber;
        for (int i = 0; i < nStringsDown; i++) {
            newFret = sameNoteLowerString(newFret, newStringNumber);
            newStringNumber -= 1;
        }
        return newFret;
    }

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

    protected static int sameNoteLowerString(int fret, int stringNumber) throws TranspositionException {

        if (!isStringNumberValid(stringNumber))
            throw new TranspositionException("Invalid string number: " + stringNumber);
        else if (stringNumber == 6)
            throw new TranspositionException("No string below 6");

        if (stringNumber == 2) return fret + 4;
        else return fret + 5;
    }

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
