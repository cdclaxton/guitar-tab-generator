package com.github.cdclaxton.tabwriter;

import com.github.cdclaxton.music.Bar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LayoutEngine {

    protected static class Block {
        List<String> lines;

        public Block(List<String> lines) throws TabBuildingException {

            if (lines == null) {
                throw new TabBuildingException("Lines cannot be null");
            }

            // Check of the lines have the same length
            int width = lines.get(0).length();
            for (int i=1; i<lines.size(); i++) {
                if (lines.get(i).length() != width) {
                    throw new TabBuildingException("Lines in block have differing lengths");
                }
            }

            this.lines = lines;
        }

        public void horizontalLayout(Block block) throws TabBuildingException {

            // Check the blocks have the same number of rows
            if (this.lines.size() != block.lines.size()) {
                throw new TabBuildingException("Blocks have differing number of rows");
            }

            // Modify this block
            for (int i=0; i < this.lines.size(); i++) {
                this.lines.set(i, this.lines.get(i) + block.lines.get(i));
            }
        }

        public int getWidth() {
            return this.lines.get(0).length();
        }
    }

    public static List<String> layoutBars(List<Bar> bars, int pageWidth, int verticalSpacing) throws TabBuildingException {

        // Determine the most compact layout for each bar
        List<SingleBarTablatureBuilder.Markings> markings = bars.stream()
                .map(bar -> LayoutEngine.compactLayout(bar))
                .collect(Collectors.toList());

        // Build the tab, one bar at a time
        List<Block> verticalBlocks = new ArrayList<>();
        Block currentBlock = null;

        for (int i = 0; i < bars.size(); i++) {

            // Create the string representation of the bar
            SingleBarTablature tabBar = SingleBarTablatureBuilder.buildTabFromBar(bars.get(i), markings.get(i));
            tabBar.addLeadingSpace();
            tabBar.addTrailingSpace();
            tabBar.addBarEndLines(SingleBarTablature.BarLineType.single);

            if (currentBlock == null) {
                tabBar.addBarStartLines(SingleBarTablature.BarLineType.single);
                tabBar.addStringLetters();
                currentBlock = new Block(tabBar.getFullBar());
            } else {
                // Does the bar fit onto the current line?
                if (currentBlock.getWidth() + tabBar.getLineWidth() < pageWidth) {
                    Block temp = new Block(tabBar.getFullBar());
                    currentBlock.horizontalLayout(temp);
                } else {
                    // Finish the current block
                    verticalBlocks.add(currentBlock);

                    // Start a new block
                    tabBar.addBarStartLines(SingleBarTablature.BarLineType.single);
                    tabBar.addStringLetters();
                    currentBlock = new Block(tabBar.getFullBar());
                }
            }
        }

        // If there is a block in progress, finish it
        if (currentBlock != null) verticalBlocks.add(currentBlock);

        // Vertically layout the blocks
        return LayoutEngine.verticallyLayoutBlocks(verticalBlocks, verticalSpacing);
    }

    protected static List<String> verticallyLayoutBlocks(List<Block> blocks, int spacing) {

        List<String> lines = new ArrayList<>();

        for (Block b : blocks) {
            lines.addAll(b.lines);
            for (int emptyLine=0; emptyLine < spacing; emptyLine++) {
                lines.add("");
            }
        }

        return lines;
    }

    /**
     * Find the most compact layout for a bar.
     *
     * @param bar Bar of music.
     * @return Most compact timing layout.
     */
    public static SingleBarTablatureBuilder.Markings compactLayout(Bar bar) {

        // Get a list of all the note times
        List<Integer> noteTimes = bar.getNotes().stream()
                .map(note -> note.getTiming().getSixteenthNumber())
                .collect(Collectors.toList());

        // Get a list of all the chord times
        List<Integer> chordTimes = bar.getTimedChords().stream()
                .map(timedChord -> timedChord.getTiming().getSixteenthNumber())
                .collect(Collectors.toList());

        // Create a list of all timings (notes and chords)
        List<Integer> timings = new ArrayList<>(noteTimes);
        timings.addAll(chordTimes);

        // Find the most compact form
        SingleBarTablatureBuilder.Markings markings;
        if (timings.size() == 0) {
            markings = SingleBarTablatureBuilder.Markings.Main;
        } else {
            if (LayoutEngine.allDivisible(timings, 4)) markings = SingleBarTablatureBuilder.Markings.Main;
            else if (LayoutEngine.allDivisible(timings, 2)) markings = SingleBarTablatureBuilder.Markings.Secondary;
            else markings = SingleBarTablatureBuilder.Markings.Tertiary;
        }

        // Return the marking
        return markings;
    }

    /**
     * Are all of the values divisible by the divisor, i.e. have no remainder when performing integer division?
     *
     * @param values List of values.
     * @param divisor Divisor.
     * @return True if all values are divisible.s
     */
    protected static boolean allDivisible(List<Integer> values, int divisor) {
        return values.stream().map(v -> v % divisor).allMatch(v -> v == 0);
    }

}
