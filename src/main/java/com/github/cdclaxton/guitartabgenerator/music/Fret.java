package com.github.cdclaxton.guitartabgenerator.music;

import java.util.Objects;

public final class Fret {

    private final int stringNumber;
    private final int fretNumber;

    /**
     * Representation of a string and fret position.
     *
     * @param stringNumber String (1 = high E).
     * @param fretNumber Fret number (0 = open string).
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

    /**
     * Create a new instance.
     *
     * @param fret Fret object to copy.
     * @return New Fret object.
     */
    public static Fret newInstance(Fret fret) {
        try {
            return new Fret(fret.getStringNumber(), fret.getFretNumber());
        } catch (InvalidFretNumberException e) {
            throw new IllegalStateException("Fret number is now invalid!");
        } catch (InvalidStringException e) {
            throw new IllegalStateException("String number is now invalid!");
        }
    }

    /**
     * Get the string representation of the fret marking (typically a fret number).
     *
     * @return Fret marking.
     */
    public String fretMarking() {
        return String.valueOf(this.fretNumber);
    }

    /**
     * Get the fret number.
     *
     * @return Fret number.
     */
    public int getFretNumber() {
        return fretNumber;
    }

    /**
     * Get the string number (1 = high E string).
     *
     * @return String number.
     */
    public int getStringNumber() {
        return stringNumber;
    }

    /**
     * Is the guitar string number valid?
     *
     * @param stringNumber String number.
     * @return True if valid, otherwise false.
     */
    private static boolean isStringNumberValid(int stringNumber) {
        return (stringNumber >= 1) && (stringNumber <= 6);
    }

    @Override
    public String toString() {
        return "Fret[string=" + this.getStringNumber() + ",fret=" + this.getFretNumber() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fret fret = (Fret) o;
        return stringNumber == fret.stringNumber &&
                fretNumber == fret.fretNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stringNumber, fretNumber);
    }
}
