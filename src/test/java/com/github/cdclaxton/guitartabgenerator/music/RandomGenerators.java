package com.github.cdclaxton.guitartabgenerator.music;

import java.util.Random;

public class RandomGenerators {

    /**
     * Generate a random integer in the interval [min, max].
     *
     * @param min Minimum value.
     * @param max Maximum value.
     * @return Random integer in the range [min, max].
     */
    public static int randomInteger(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    /**
     * Generate a random timing value in the interval [0,15].
     *
     * @return Random timing.
     */
    public static int randomTiming() {
        return randomInteger(0, 15);
    }

    /**
     * Generate a random fret number in the range [minFret, maxFret].
     *
     * @param minFret Minimum fret number.
     * @param maxFret Maximum fret number.
     * @return Random fret.
     */
    public static int randomFret(int minFret, int maxFret) {
        return randomInteger(minFret, maxFret);
    }

    /**
     * Generate a random string number.
     *
     * @return Random string number.
     */
    public static int randomStringNumber () {
        return randomInteger(1, 6);
    }
}
