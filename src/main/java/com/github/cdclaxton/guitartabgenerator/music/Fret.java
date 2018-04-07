package com.github.cdclaxton.guitartabgenerator.music;

public class Fret {

    private int stringNumber;
    private int fretNumber;

    /**
     * Fret number.
     *
     * @param stringNumber String (1 = high E).
     * @param fretNumber Fret number (0 = no fretNumber).
     */
    public Fret(int stringNumber, int fretNumber) throws InvalidFretNumberException, InvalidStringException {
        // Check the stringNumber is valid for a standard guitar
        if (!isStringNumberValid(stringNumber)) {
            throw new InvalidStringException("Invalid guitar stringNumber");
        }

        // Check the fretNumber number is valid for a standard guitar
        if (fretNumber < 0) {
            throw new InvalidFretNumberException("Fret number (" + fretNumber + ") cannot be negative");
        } else if (fretNumber > 22) {
            throw new InvalidFretNumberException("Fret number (" + fretNumber + ") cannot be greater than 22");
        }

        this.stringNumber = stringNumber;
        this.fretNumber = fretNumber;
    }

    public String getFretMarking() {
        return String.valueOf(this.fretNumber);
    }

    public int getFretNumber() {
        return fretNumber;
    }

    public int getStringNumber() {
        return stringNumber;
    }

    /**
     * Is the guitar string number valid?
     *
     * @param stringNumber String number.
     * @return True if valid, otherwise false.
     */
    public static boolean isStringNumberValid(int stringNumber) {
        return (stringNumber >= 1) && (stringNumber <= 6);
    }

    @Override
    public String toString() {
        return "Fret[string=" + this.getStringNumber() + ",fret=" + this.getFretNumber() + "]";
    }
}
