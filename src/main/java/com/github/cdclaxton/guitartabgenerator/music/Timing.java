package com.github.cdclaxton.guitartabgenerator.music;

import java.util.Objects;

public class Timing {

    private int sixteenthNumber;

    public Timing(int sixteenthNumber) throws InvalidTimingException {

        if (sixteenthNumber < 0) {
            throw new InvalidTimingException("Invalid timing: " + sixteenthNumber);
        } else if (sixteenthNumber > 15) {
            throw new InvalidTimingException("Invalid timing: " + sixteenthNumber);
        }

        this.sixteenthNumber = sixteenthNumber;
    }

    public int getSixteenthNumber() {
        return sixteenthNumber;
    }

    @Override
    public String toString() {
        return "Timing[" + this.getSixteenthNumber() + "]";
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
