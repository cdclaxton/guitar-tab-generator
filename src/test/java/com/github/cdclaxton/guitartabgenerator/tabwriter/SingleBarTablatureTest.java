package com.github.cdclaxton.guitartabgenerator.tabwriter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SingleBarTablatureTest {

    @Test
    void testAddSingleStartBarLines() throws TabBuildingException {
        String ruler  = "1 + 2 + 3 + 4 + ";
        String chords = "Am      C       ";
        String tab0   = "----------------";
        String tab1   = "1-------1-------";
        String tab2   = "-2------0-------";
        String tab3   = "--2-----2-------";
        String tab4   = "---0----3-------";
        String tab5   = "----------------";

        // Build the bar
        SingleBarTablature bar = new SingleBarTablature(ruler, chords, tab0, tab1, tab2, tab3, tab4, tab5);

        // Add a single bar line
        bar.addBarStartLines(SingleBarTablature.BarLineType.single);

        // Check
        assertEquals(" 1 + 2 + 3 + 4 + ", bar.getRuler());
        assertEquals(" Am      C       ", bar.getChordLine());
        assertEquals("|----------------", bar.getTabLines().get(0));
        assertEquals("|1-------1-------", bar.getTabLines().get(1));
        assertEquals("|-2------0-------", bar.getTabLines().get(2));
        assertEquals("|--2-----2-------", bar.getTabLines().get(3));
        assertEquals("|---0----3-------", bar.getTabLines().get(4));
        assertEquals("|----------------", bar.getTabLines().get(5));
    }

    @Test
    void testAddSingleEndBarLines() throws TabBuildingException {
        String ruler  = "1 + 2 + 3 + 4 + ";
        String chords = "Am      C       ";
        String tab0   = "----------------";
        String tab1   = "1-------1-------";
        String tab2   = "-2------0-------";
        String tab3   = "--2-----2-------";
        String tab4   = "---0----3-------";
        String tab5   = "----------------";

        // Build the bar
        SingleBarTablature bar = new SingleBarTablature(ruler, chords, tab0, tab1, tab2, tab3, tab4, tab5);

        // Add a single bar line
        bar.addBarEndLines(SingleBarTablature.BarLineType.single);

        // Check
        assertEquals("1 + 2 + 3 + 4 +  ", bar.getRuler());
        assertEquals("Am      C        ", bar.getChordLine());
        assertEquals("----------------|", bar.getTabLines().get(0));
        assertEquals("1-------1-------|", bar.getTabLines().get(1));
        assertEquals("-2------0-------|", bar.getTabLines().get(2));
        assertEquals("--2-----2-------|", bar.getTabLines().get(3));
        assertEquals("---0----3-------|", bar.getTabLines().get(4));
        assertEquals("----------------|", bar.getTabLines().get(5));
    }

    @Test
    void testAddStringLetters() throws TabBuildingException {
        String ruler  = " 1 + 2 + 3 + 4 +  ";
        String chords = " Am      C        ";
        String tab0   = "|----------------|";
        String tab1   = "|1-------1-------|";
        String tab2   = "|-2------0-------|";
        String tab3   = "|--2-----2-------|";
        String tab4   = "|---0----3-------|";
        String tab5   = "|----------------|";

        // Build the bar
        SingleBarTablature bar = new SingleBarTablature(ruler, chords, tab0, tab1, tab2, tab3, tab4, tab5);

        // Add a single bar line
        bar.addStringLetters();

        // Check
        assertEquals("  1 + 2 + 3 + 4 +  ", bar.getRuler());
        assertEquals("  Am      C        ", bar.getChordLine());
        assertEquals("E|----------------|", bar.getTabLines().get(0));
        assertEquals("B|1-------1-------|", bar.getTabLines().get(1));
        assertEquals("G|-2------0-------|", bar.getTabLines().get(2));
        assertEquals("D|--2-----2-------|", bar.getTabLines().get(3));
        assertEquals("A|---0----3-------|", bar.getTabLines().get(4));
        assertEquals("E|----------------|", bar.getTabLines().get(5));
    }

    @Test
    void testAddLeadingSpace() throws TabBuildingException {
        String ruler  = "1 + 2 + 3 + 4 + ";
        String chords = "Am      C       ";
        String tab0   = "----------------";
        String tab1   = "1-------1-------";
        String tab2   = "-2------0-------";
        String tab3   = "--2-----2-------";
        String tab4   = "---0----3-------";
        String tab5   = "----------------";

        // Build the bar
        SingleBarTablature bar = new SingleBarTablature(ruler, chords, tab0, tab1, tab2, tab3, tab4, tab5);

        // Add a leading space
        bar.addLeadingSpace();

        // Check
        assertEquals(" 1 + 2 + 3 + 4 + ", bar.getRuler());
        assertEquals(" Am      C       ", bar.getChordLine());
        assertEquals("-----------------", bar.getTabLines().get(0));
        assertEquals("-1-------1-------", bar.getTabLines().get(1));
        assertEquals("--2------0-------", bar.getTabLines().get(2));
        assertEquals("---2-----2-------", bar.getTabLines().get(3));
        assertEquals("----0----3-------", bar.getTabLines().get(4));
        assertEquals("-----------------", bar.getTabLines().get(5));
    }

    @Test
    void testAddTrailingSpace() throws TabBuildingException {
        String ruler  = "1 + 2 + 3 + 4 + ";
        String chords = "Am      C       ";
        String tab0   = "----------------";
        String tab1   = "1-------1-------";
        String tab2   = "-2------0-------";
        String tab3   = "--2-----2-------";
        String tab4   = "---0----3-------";
        String tab5   = "----------------";

        // Build the bar
        SingleBarTablature bar = new SingleBarTablature(ruler, chords, tab0, tab1, tab2, tab3, tab4, tab5);

        // Add a leading space
        bar.addTrailingSpace();

        // Check
        assertEquals("1 + 2 + 3 + 4 +  ", bar.getRuler());
        assertEquals("Am      C        ", bar.getChordLine());
        assertEquals("-----------------", bar.getTabLines().get(0));
        assertEquals("1-------1--------", bar.getTabLines().get(1));
        assertEquals("-2------0--------", bar.getTabLines().get(2));
        assertEquals("--2-----2--------", bar.getTabLines().get(3));
        assertEquals("---0----3--------", bar.getTabLines().get(4));
        assertEquals("-----------------", bar.getTabLines().get(5));
    }

}