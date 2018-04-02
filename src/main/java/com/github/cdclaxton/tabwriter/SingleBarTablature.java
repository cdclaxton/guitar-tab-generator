package com.github.cdclaxton.tabwriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SingleBarTablature {

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
    public SingleBarTablature(String ruler, String chordLine, String... lines) throws TabBuildingException {
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
    public SingleBarTablature(String ruler, String chordLine, List<String> tabLines) throws TabBuildingException {

        // Preconditions
        if (ruler.length() != chordLine.length()) {
            throw new TabBuildingException("Ruler and chords have different lengths");
        }

        if (tabLines.size() == 0) {
            throw new TabBuildingException("There must be at least one tab line");
        }

        if (tabLines.size() != 6) {
            throw new TabBuildingException("Currently only supports six strings in standard tuning");
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
     * Add a space (or '-') to the start of each line of the bar.
     */
    public void addLeadingSpace() {

        // Pad the ruler with a space
        this.ruler = TabLineUtils.padLeft(this.ruler, 1, ' ');

        // Pad the chord line with a space
        this.chordLine = TabLineUtils.padLeft(this.chordLine, 1, ' ');

        // Pad each of the tab lines with a '-'
        for (int i = 0; i < this.tabLines.size(); i++) {
            this.tabLines.set(i, "-" + this.tabLines.get(i));
        }
    }

    /**
     * Add a space (or '-') to the end of each line of the bar.
     */
    public void addTrailingSpace() {

        // Pad the ruler with a space
        this.ruler = TabLineUtils.padRight(this.ruler, 1, ' ');

        // Pad the chord line with a space
        this.chordLine = TabLineUtils.padRight(this.chordLine, 1, ' ');

        // Pad each of the tab lines with a '-'
        for (int i = 0; i < this.tabLines.size(); i++) {
            this.tabLines.set(i, this.tabLines.get(i) + "-");
        }
    }

    /**
     * Add the guitar string letters to the tab (standard tuning).
     */
    public void addStringLetters() {

        // Pad the ruler with spaces
        this.ruler = TabLineUtils.padLeft(this.ruler, 1, ' ');

        // Pad the chord line with spaces
        this.chordLine = TabLineUtils.padLeft(this.chordLine, 1, ' ');

        // Add the string letters
        this.tabLines.set(0, "E" + this.tabLines.get(0));
        this.tabLines.set(1, "B" + this.tabLines.get(1));
        this.tabLines.set(2, "G" + this.tabLines.get(2));
        this.tabLines.set(3, "D" + this.tabLines.get(3));
        this.tabLines.set(4, "A" + this.tabLines.get(4));
        this.tabLines.set(5, "E" + this.tabLines.get(5));
    }

    /**
     * Add bar lines to the start of the tab lines (and pad the ruler and chords).
     *
     * @param barLineType Type of bar line to add.
     */
    public void addBarStartLines(BarLineType barLineType) {

        // Width (in characters) of the bar line
        int barLineWidth = getWidthOfBarSeparator(barLineType);

        // Pad the ruler with spaces
        this.ruler = TabLineUtils.padLeft(this.ruler, barLineWidth, ' ');

        // Pad the chord line with spaces
        this.chordLine = TabLineUtils.padLeft(this.chordLine, barLineWidth, ' ');

        // Add the barline to each of the tab lines
        for (int i = 0; i < this.tabLines.size(); i++) {
            this.tabLines.set(i, getBarSeparator(barLineType) + this.tabLines.get(i));
        }
    }

    /**
     * Add bar lines to the end of the tab lines (and pad the ruler and chords).
     *
     * @param barLineType Type of bar line to add.
     */
    public void addBarEndLines(BarLineType barLineType) {

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

    /**
     * Get the full bar (ruler, chords and tab lines) as a list of strings.
     *
     * @return List of strings representing the bar.
     */
    public List<String> getFullBar() {
        List<String> lines = new ArrayList<>();

        if (ruler != null) {
            lines.add(ruler);
        }

        if (chordLine != null) {
            lines.add(chordLine);
        }

        if (tabLines != null) {
            lines.addAll(tabLines);
        }

        return lines;
    }
}
