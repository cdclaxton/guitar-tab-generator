package com.github.cdclaxton.tabwriter;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class TablatureBarBuilderTest {

    @Test
    void testConstructorNegativeNumElements() {
        assertThrows(TabBuildingException.class, () -> TablatureBarBuilder.buildTabLine(-1, Collections.emptyMap()));
    }

    @Test
    void testBuildEmptyTabLines() throws TabBuildingException {
        assertTrue(TablatureBarBuilder.buildTabLine(1, Collections.emptyMap()).equals("-"));
        assertTrue(TablatureBarBuilder.buildTabLine(2, Collections.emptyMap()).equals("--"));
        assertTrue(TablatureBarBuilder.buildTabLine(3, Collections.emptyMap()).equals("---"));
        assertTrue(TablatureBarBuilder.buildTabLine(4, Collections.emptyMap()).equals("----"));
    }

    @Test
    void testBuildTabLinesOneCharacter() throws TabBuildingException {
        assertTrue(TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(0, "0")).equals("0---"));
        assertTrue(TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(1, "0")).equals("-0--"));
        assertTrue(TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(2, "0")).equals("--0-"));
        assertTrue(TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(3, "0")).equals("---0"));
    }

    @Test
    void testBuildTabLinesTwoCharacter() throws TabBuildingException {
        assertTrue(TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(0, "12")).equals("12--"));
        assertTrue(TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(1, "12")).equals("-12-"));
        assertTrue(TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(2, "12")).equals("--12"));
        assertTrue(TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(3, "1")).equals("---1"));
    }

    @Test
    void testBuildTabLinesBeyondEnd() {
        assertThrows(TabBuildingException.class, () -> TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(4, "12")));
    }

    @Test
    void testBuildTabLinesTwoStrings() throws TabBuildingException {
        assertTrue(TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(0, "1", 1, "2")).equals("12--"));
        assertTrue(TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(0, "1", 2, "2")).equals("1-2-"));
        assertTrue(TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(0, "1", 3, "2")).equals("1--2"));
        assertTrue(TablatureBarBuilder.buildTabLine(4, ImmutableMap.of(0, "1", 2, "34")).equals("1-34"));
    }

    @Test
    void testBuildRuler() throws TabBuildingException {
        assertTrue(TablatureBarBuilder.buildRuler(TablatureBarBuilder.Markings.Main, 1, 2).equals("1  "));
        assertTrue(TablatureBarBuilder.buildRuler(TablatureBarBuilder.Markings.Main, 2, 2).equals("1  2  "));
        assertTrue(TablatureBarBuilder.buildRuler(TablatureBarBuilder.Markings.Main, 3, 2).equals("1  2  3  "));
        assertTrue(TablatureBarBuilder.buildRuler(TablatureBarBuilder.Markings.Main, 4, 2).equals("1  2  3  4  "));

        assertTrue(TablatureBarBuilder.buildRuler(TablatureBarBuilder.Markings.Secondary, 1, 2).equals("1  +  "));
        assertTrue(TablatureBarBuilder.buildRuler(TablatureBarBuilder.Markings.Secondary, 2, 2).equals("1  +  2  +  "));
        assertTrue(TablatureBarBuilder.buildRuler(TablatureBarBuilder.Markings.Secondary, 3, 2).equals("1  +  2  +  3  +  "));

        assertTrue(TablatureBarBuilder.buildRuler(TablatureBarBuilder.Markings.Tertiary, 1, 2).equals("1  .  +  .  "));
        assertTrue(TablatureBarBuilder.buildRuler(TablatureBarBuilder.Markings.Tertiary, 2, 2).equals("1  .  +  .  2  .  +  .  "));
    }

    @Test
    void testBuildEmptyChordLines() throws TabBuildingException {
        assertTrue(TablatureBarBuilder.buildChordLine(1, Collections.emptyMap()).equals(" "));
        assertTrue(TablatureBarBuilder.buildChordLine(2, Collections.emptyMap()).equals("  "));
        assertTrue(TablatureBarBuilder.buildChordLine(3, Collections.emptyMap()).equals("   "));
        assertTrue(TablatureBarBuilder.buildChordLine(4, Collections.emptyMap()).equals("    "));
    }

    @Test
    void testBuildNonEmptyChordLines() throws TabBuildingException {
        assertTrue(TablatureBarBuilder.buildChordLine(4, ImmutableMap.of(0, "Am")).equals("Am  "));
        assertTrue(TablatureBarBuilder.buildChordLine(4, ImmutableMap.of(1, "A")).equals(" A  "));
        assertTrue(TablatureBarBuilder.buildChordLine(4, ImmutableMap.of(0, "A", 2, "Bm")).equals("A Bm"));
        assertTrue(TablatureBarBuilder.buildChordLine(4, ImmutableMap.of(3, "D")).equals("   D"));
    }


}