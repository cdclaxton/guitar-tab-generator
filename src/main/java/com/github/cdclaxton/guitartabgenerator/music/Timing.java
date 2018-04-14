package com.github.cdclaxton.guitartabgenerator.music;

import java.util.Objects;

public final class Timing {

    private final int sixteenthNumber;

    /**
     * Construct an object to represent the timing of a note or chord.
     *
     * @param sixteenthNumber Timing in sixteenths.
     * @throws InvalidTimingException Time is invalid.
     */
    public Timing(int sixteenthNumber) throws InvalidTimingException {

        if (sixteenthNumber < 0) {
            throw new InvalidTimingException("Invalid timing: " + sixteenthNumber);
        } else if (sixteenthNumber > 15) {
            throw new InvalidTimingException("Invalid timing: " + sixteenthNumber);
        }

        this.sixteenthNumber = sixteenthNumber;
    }

    /**
     * Get a new instance of the Timing object.
     *
     * @param timing Timing.
     * @return New Timing object.
     */
    public static Timing newInstance(Timing timing) {
        try {
            return new Timing(timing.sixteenthNumber);
        } catch (InvalidTimingException e) {
            throw new IllegalStateException("Timing is now invalid!");
        }
    }

    /**
     * Get the timing.
     *
     * @return Timing.
     */
    public int getSixteenthNumber() {
        return sixteenthNumber;
    }

    @Override
    public String toString() {
        return "Timing[sixteenthNumber=" + this.getSixteenthNumber() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Timing timing = (Timing) o;
        return sixteenthNumber == timing.sixteenthNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sixteenthNumber);
    }
}
