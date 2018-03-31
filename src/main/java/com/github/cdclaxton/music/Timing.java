package com.github.cdclaxton.music;

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

}
