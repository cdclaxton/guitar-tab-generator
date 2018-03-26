package com.github.cdclaxton.music;

import static org.junit.jupiter.api.Assertions.*;

class TimingTest {

    @org.junit.jupiter.api.Test
    void testConstructorNegativeTime() {
        assertThrows(InvalidTimingException.class, () -> {
            new Timing(-1);
        });
    }

    @org.junit.jupiter.api.Test
    void testConstructorTooLargeTime() {
        assertThrows(InvalidTimingException.class, () -> {
            new Timing(16);
        });
    }

}