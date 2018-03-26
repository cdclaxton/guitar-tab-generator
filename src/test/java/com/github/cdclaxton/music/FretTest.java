package com.github.cdclaxton.music;

import static org.junit.jupiter.api.Assertions.*;

class FretTest {

    @org.junit.jupiter.api.Test
    void testConstructorNegativeFretNumber() {
        assertThrows(InvalidFretNumberException.class, () -> new Fret(1,-2));
    }

    @org.junit.jupiter.api.Test
    void testConstructorTooLargeFretNumber() {
        assertThrows(InvalidFretNumberException.class, () -> new Fret(1,25));
    }

    @org.junit.jupiter.api.Test
    void testConstructorZeroStringNumber() {
        assertThrows(InvalidStringException.class, () -> new Fret(0,1));
    }

    @org.junit.jupiter.api.Test
    void testConstructorTooLargeStringNumber() {
        assertThrows(InvalidStringException.class, () -> new Fret(7,1));
    }

    @org.junit.jupiter.api.Test
    void getFret() throws InvalidFretNumberException, InvalidStringException {
        assertEquals(0, new Fret(1,0).getFretNumber());
        assertEquals(5, new Fret(1,5).getFretNumber());
        assertEquals(22, new Fret(1,22).getFretNumber());
    }
}
