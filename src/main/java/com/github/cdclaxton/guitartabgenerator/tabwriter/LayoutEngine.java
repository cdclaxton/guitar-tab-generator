package com.github.cdclaxton.guitartabgenerator.tabwriter;

import com.github.cdclaxton.guitartabgenerator.music.Bar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class LayoutEngine {

    /**
     * Inner class to represent a block of text.
     */
    static class Block {

        final List<String> lines;

        /**
         * Instantiate a block of text.
         *
         * @param lines Lines of text.
         * @throws TabBuildingException Lines are null or have differing lengths.
         */
        Block(final List<String> lines) throws TabBuildingException {

            if (lines == null) {
                throw new TabBuildingException("Lines cannot be null");
            }

            // Check the lines have the same length
            final int width = lines.get(0).length();
            for (int i=1; i<lines.size(); i++) {
                if (lines.get(i).length() != width) {
                    throw new TabBuildingException("Lines in block have differing lengths");
                }
            }

            this.lines = lines;
        }

        /**
         * Modify the block by laying the input block to the right of this block.
         *
         * @param block Block to layout to the right.
         * @throws TabBuildingException Blocks have differing numbers of rows.
         */
        void horizontalLayout(final Block block) throws TabBuildingException {

            // Check the blocks have the same number of rows
            if (this.lines.size() != block.lines.size()) {
                throw new TabBuildingException("Blocks have differing number of rows");
            }

            // Modify this block
            for (int i=0; i < this.lines.size(); i++) {
                this.lines.set(i, this.lines.get(i) + block.lines.get(i));
            }
        }

        /**
         * Get the width of the block (in characters).
         *
         * @return Block width.
         */
        int getWidth() {
            return this.lines.get(0).length();
        }
    }

    /**
     * Layout the bars (to generate a list of lines).
     *
     * @param bars Bars of music.
     * @param pageWidth Page width (in characters).
     * @param verticalSpacing Vertical spacing between bars.
     * @return Lines representing the bars.
     * @throws TabBuildingException
     */
    static List<String> layoutBars(final List<Bar> bars,
                                   final int pageWidth,
                                   final int verticalSpacing) throws TabBuildingException {

        // Determine the most compact layout for each bar
        final List<SingleBarTablatureBuilder.Markings> markings = bars.stream()
                .map(bar -> LayoutEngine.compactLayout(bar))
                .collect(Collectors.toList());

        // Build the tab, one bar at a time
        final List<Block> verticalBlocks = new ArrayList<>();
        Block currentBlock = null;

        for (int i = 0; i < bars.size(); i++) {

            // Create the string representation of the bar
            final SingleBarTablature tabBar = SingleBarTablatureBuilder.buildTabFromBar(bars.get(i), markings.get(i));
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

    /**
     * Layout blocks vertically.
     *
     * @param blocks Blocks to lay out.
     * @param spacing Spacing between blocks.
     * @return Lines of text generated from the blocks.
     */
    private static List<String> verticallyLayoutBlocks(final List<Block> blocks,
                                                       final int spacing) {

        final List<String> lines = new ArrayList<>();

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
    static SingleBarTablatureBuilder.Markings compactLayout(final Bar bar) {

        // Get a list of all the note times
        final List<Integer> noteTimes = bar.getNotes().stream()
                .map(note -> note.getTiming().getSixteenthNumber())
                .collect(Collectors.toList());

        // Get a list of all the chord times
        final List<Integer> chordTimes = bar.getTimedChords().stream()
                .map(timedChord -> timedChord.getTiming().getSixteenthNumber())
                .collect(Collectors.toList());

        // Create a list of all timings (notes and chords)
        final List<Integer> timings = new ArrayList<>(noteTimes);
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
    static boolean allDivisible(List<Integer> values,
                                int divisor) {
        return values.stream().map(v -> v % divisor).allMatch(v -> v == 0);
    }

}
