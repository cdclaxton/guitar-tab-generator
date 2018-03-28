package com.github.cdclaxton.tabwriter;

import java.util.Arrays;
import java.util.List;

public class TablatureBar {

    private String ruler;
    private String chordLine;
    private List<String> tabLines;

    public enum BarLineType { single }

    /**
     * Instantiate a single bar of tablature (convenience constructor).
     *
     * @param ruler Ruler showing timings.
     * @param chordLine Single line of chords.
     * @param lines Varargs list of tab lines.
     * @throws TabBuildingException
     */
    public TablatureBar(String ruler, String chordLine, String... lines) throws TabBuildingException {
        this(ruler, chordLine,  Arrays.asList(lines));
    }

    /**
     * Instantiate a single bar of tablature.
     *
     * Note that each of the lines in the tab must be the same length.
     *
     * @param ruler Ruler showing timings.
     * @param chordLine Single line of chords.
     * @param tabLines Tab lines.
     * @throws TabBuildingException
     */
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

    /**
     * Add bar lines to the end of the tab lines (and pad the ruler and chords).
     *
     * @param barLineType Type of bar line to add.
     */
    public void addBarLines(BarLineType barLineType) {

        // Width (in characters) of the bar line
        int barLineWidth = getWidthOfBarSeparator(barLineType);

        // Pad the ruler with spaces
        this.ruler = TabLineUtils.padRight(this.ruler, barLineWidth, ' ');

        // Pad the chord line with spaces
        this.chordLine = TabLineUtils.padRight(this.chordLine, barLineWidth, ' ');

        // Add the barline to each of the tab lines
        for (int i = 0; i < this.tabLines.size(); i++) {
            this.tabLines.set(i, this.tabLines.get(i) + getBarSeparator(barLineType));
        }
    }

    /**
     * Get the number of characters that will be used to show the bar line.
     *
     * @param barLineType Type of bar line.
     * @return Number of characters.
     */
    private static int getWidthOfBarSeparator(BarLineType barLineType) {
        if (barLineType == BarLineType.single) {
            return 1;
        }

        throw new IllegalStateException("Shouldn't reach here.");
    }

    /**
     * Get the separator to use given the bar line type.
     *
     * @param barLineType Type of bar line.
     * @return Separator.
     */
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
