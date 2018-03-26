package com.github.cdclaxton.tabwriter;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class BarBuilderTest {

    @Test
    void testConstructorNegativeNumElements() {
        assertThrows(TabBuildingException.class, () -> BarBuilder.buildTabLine(-1, Collections.emptyMap()));
    }

    @Test
    void testBuildEmptyTabLines() throws TabBuildingException {
        assertTrue(BarBuilder.buildTabLine(1, Collections.emptyMap()).equals("-"));
        assertTrue(BarBuilder.buildTabLine(2, Collections.emptyMap()).equals("--"));
        assertTrue(BarBuilder.buildTabLine(3, Collections.emptyMap()).equals("---"));
        assertTrue(BarBuilder.buildTabLine(4, Collections.emptyMap()).equals("----"));
    }

    @Test
    void testBuildTabLinesOneCharacter() throws TabBuildingException {
        assertTrue(BarBuilder.buildTabLine(4, ImmutableMap.of(0, "0")).equals("0---"));
        assertTrue(BarBuilder.buildTabLine(4, ImmutableMap.of(1, "0")).equals("-0--"));
        assertTrue(BarBuilder.buildTabLine(4, ImmutableMap.of(2, "0")).equals("--0-"));
        assertTrue(BarBuilder.buildTabLine(4, ImmutableMap.of(3, "0")).equals("---0"));
    }

    @Test
    void testBuildTabLinesTwoCharacter() throws TabBuildingException {
        assertTrue(BarBuilder.buildTabLine(4, ImmutableMap.of(0, "12")).equals("12--"));
        assertTrue(BarBuilder.buildTabLine(4, ImmutableMap.of(1, "12")).equals("-12-"));
        assertTrue(BarBuilder.buildTabLine(4, ImmutableMap.of(2, "12")).equals("--12"));
        assertTrue(BarBuilder.buildTabLine(4, ImmutableMap.of(3, "1")).equals("---1"));
    }

    @Test
    void testBuildTabLinesBeyondEnd() {
        assertThrows(TabBuildingException.class, () -> BarBuilder.buildTabLine(4, ImmutableMap.of(4, "12")));
    }

    @Test
    void testBuildTabLinesTwoStrings() throws TabBuildingException {
        assertTrue(BarBuilder.buildTabLine(4, ImmutableMap.of(0, "1", 1, "2")).equals("12--"));
        assertTrue(BarBuilder.buildTabLine(4, ImmutableMap.of(0, "1", 2, "2")).equals("1-2-"));
        assertTrue(BarBuilder.buildTabLine(4, ImmutableMap.of(0, "1", 3, "2")).equals("1--2"));
        assertTrue(BarBuilder.buildTabLine(4, ImmutableMap.of(0, "1", 2, "34")).equals("1-34"));
    }

    @Test
    void testBuildRuler() throws TabBuildingException {
        assertTrue(BarBuilder.buildRuler(BarBuilder.Markings.Main, 1, 2).equals("1  "));
        assertTrue(BarBuilder.buildRuler(BarBuilder.Markings.Main, 2, 2).equals("1  2  "));
        assertTrue(BarBuilder.buildRuler(BarBuilder.Markings.Main, 3, 2).equals("1  2  3  "));
        assertTrue(BarBuilder.buildRuler(BarBuilder.Markings.Main, 4, 2).equals("1  2  3  4  "));

        assertTrue(BarBuilder.buildRuler(BarBuilder.Markings.Secondary, 1, 2).equals("1  +  "));
        assertTrue(BarBuilder.buildRuler(BarBuilder.Markings.Secondary, 2, 2).equals("1  +  2  +  "));
        assertTrue(BarBuilder.buildRuler(BarBuilder.Markings.Secondary, 3, 2).equals("1  +  2  +  3  +  "));

        assertTrue(BarBuilder.buildRuler(BarBuilder.Markings.Tertiary, 1, 2).equals("1  .  +  .  "));
        assertTrue(BarBuilder.buildRuler(BarBuilder.Markings.Tertiary, 2, 2).equals("1  .  +  .  2  .  +  .  "));
    }
}