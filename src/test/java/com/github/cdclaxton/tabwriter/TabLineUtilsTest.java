package com.github.cdclaxton.tabwriter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TabLineUtilsTest {

    @Test
    void testPadLeft() {
        assertEquals("  ", TabLineUtils.padLeft("", 2, ' '));
        assertEquals(" A", TabLineUtils.padLeft("A", 1, ' '));
        assertEquals("  A", TabLineUtils.padLeft("A", 2, ' '));
        assertEquals("   A", TabLineUtils.padLeft(" A", 2, ' '));
    }

    @Test
    void testPadRight() {
        assertEquals("  ", TabLineUtils.padRight("", 2, ' '));
        assertEquals("A ", TabLineUtils.padRight("A", 1, ' '));
        assertEquals("A  ", TabLineUtils.padRight("A", 2, ' '));
        assertEquals(" A  ", TabLineUtils.padRight(" A", 2, ' '));
    }

}