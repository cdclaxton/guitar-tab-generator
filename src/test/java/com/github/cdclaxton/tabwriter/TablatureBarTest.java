package com.github.cdclaxton.tabwriter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TablatureBarTest {

    @Test
    void addSingleBarLines() throws TabBuildingException {
        String ruler  = "1 + 2 + 3 + 4 + ";
        String chords = "Am      C       ";
        String tab0   = "----------------";
        String tab1   = "1-------1-------";
        String tab2   = "-2------0-------";
        String tab3   = "--2-----2-------";
        String tab4   = "---0----3-------";
        String tab5   = "----------------";

        // Build the bar
        TablatureBar bar = new TablatureBar(ruler, chords, tab0, tab1, tab2, tab3, tab4, tab5);

        // Add a single bar line
        bar.addBarLines(TablatureBar.BarLineType.single);

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
}