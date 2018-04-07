package com.github.cdclaxton.guitartabgenerator.music;

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
}
