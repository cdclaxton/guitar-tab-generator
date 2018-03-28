package com.github.cdclaxton.tabwriter;

import java.util.Arrays;
import java.util.List;

public class TablatureBar {

    private String ruler;
    private String chordLine;
    private List<String> tabLines;

    public enum BarLineType { single }

    public TablatureBar(String ruler, String chordLine, String... lines) throws TabBuildingException {
        this(ruler, chordLine,  Arrays.asList(lines));
    }

    public TablatureBar(String ruler, String chordLine, List<String> tabLines) throws TabBuildingException {

        // Preconditions
        if (ruler.length() != chordLine.length()) {
            throw new TabBuildingException("Ruler and chords have different lengths");
        }

        if (tabLines.size() == 0) {
            throw new TabBuildingException("There must be at least one tab line");
        }

        for (int i = 0; i < tabLines.size() - 1; i++) {
            if (tabLines.get(i).length() != tabLines.get(i+1).length()) {
                throw new TabBuildingException("Tab lines " + i + " and " + (i+1) + " have differing lengths");
            }
        }

        if (tabLines.get(0).length() != ruler.length()) {
            throw new TabBuildingException("Length of tab lines doesn't match that of chords and ruler");
        }

        // Store the values
        this.ruler = ruler;
        this.chordLine = chordLine;
        this.tabLines = tabLines;
    }

    public void addBarLines(BarLineType barLineType) {

        // Width of the bar line
        int barLineWidth = getWidthOfBarSeparator(barLineType);

        // Pad the ruler
        this.ruler = TabLineUtils.padRight(this.ruler, barLineWidth, ' ');

        // Pad the chord line
        this.chordLine = TabLineUtils.padRight(this.chordLine, barLineWidth, ' ');

        // Add the barline to each of the tab lines
        for (int i = 0; i < this.tabLines.size(); i++) {
            this.tabLines.set(i, this.tabLines.get(i) + getBarSeparator(barLineType));
        }
    }

    private static int getWidthOfBarSeparator(BarLineType barLineType) {
        if (barLineType == BarLineType.single) {
            return 1;
        }

        throw new IllegalStateException("Shouldn't reach here.");
    }

    private static String getBarSeparator(BarLineType barLineType) {
        if (barLineType == BarLineType.single) {
            return "|";
        }

        // Execution shouldn't reach here. Must be a cleaner way of doing this.
        throw new IllegalStateException("Shouldn't reach here.");
    }

    public String getRuler() {
        return ruler;
    }

    public String getChordLine() {
        return chordLine;
    }

    public List<String> getTabLines() {
        return tabLines;
    }
}
