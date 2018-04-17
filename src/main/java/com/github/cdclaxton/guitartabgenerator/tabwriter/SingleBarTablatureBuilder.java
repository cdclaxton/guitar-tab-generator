package com.github.cdclaxton.guitartabgenerator.tabwriter;

import com.github.cdclaxton.guitartabgenerator.music.Bar;
import com.github.cdclaxton.guitartabgenerator.music.Note;
import com.github.cdclaxton.guitartabgenerator.music.TimedChord;

import java.util.*;

class SingleBarTablatureBuilder {

    private static final char lineCharacter = '-';

    public enum Markings{ Main, Secondary, Tertiary }
    private static final String secondarySymbol = "+";
    private static final String tertiarySymbol = ".";
    private static final char rulerSpacingSymbol = ' ';

    private SingleBarTablatureBuilder() {}

    /**
     * Build a single bar of tab.
     *
     * @param bar Bar.
     * @param markings Time markings to show.
     * @return Tab.
     * @throws TabBuildingException Unable to build tab for the bar.
     */
    static SingleBarTablature buildTabFromBar(Bar bar,
                                              Markings markings) throws TabBuildingException {

        if (bar.getTimeSignature() == Bar.TimeSignature.Four4) {
            return buildTabIn44FromBar(bar, markings);
        } else {
            throw new UnsupportedOperationException("6/8 not implemented.");
        }
    }

    /**
     * Build tab in 4/4 timing.
     *
     * @param bar Bar.
     * @param markings Markings to show.
     * @return Tablature for the bar.
     * @throws TabBuildingException Unable to build tab.
     */
    private static SingleBarTablature buildTabIn44FromBar(final Bar bar,
                                                          final Markings markings) throws TabBuildingException {

        final int spacing = 3;

        // Build the ruler
        final String ruler = SingleBarTablatureBuilder.buildRuler(markings, 4, spacing);

        final int nElements = ruler.length();

        // Build the chord line
        final Map<Integer, String> chordPosToMarking =
                SingleBarTablatureBuilder.buildPositionToMarking(bar.getTimedChords(), markings, spacing);
        final String chordLine = SingleBarTablatureBuilder.buildChordLine(nElements, chordPosToMarking);

        // Build a tab line for each guitar string
        final List<String> tabLines = new ArrayList<>();
        for (int guitarString = 1; guitarString <= 6; guitarString++) {
            Map<Integer, String> tabPosToMarking =
                    SingleBarTablatureBuilder.buildPositionToMarking(bar.getNotes(), markings, spacing, guitarString);
            String tabLine = SingleBarTablatureBuilder.buildTabLine(nElements, tabPosToMarking);
            tabLines.add(tabLine);
        }

        // Build an return the single bar of tab
        return new SingleBarTablature(ruler, chordLine, tabLines);
    }

    /**
     * Build a map of the character position and marking for a single string.
     *
     * @param notes List of notes.
     * @param markings Markings to use.
     * @param spacing Spacing between markings.
     * @param stringNumber String number.
     * @return Map of character position to marking.
     * @throws TabBuildingException Unable to represent the markings.
     */
    private static Map<Integer, String> buildPositionToMarking(final List<Note> notes,
                                                               final Markings markings,
                                                               final int spacing,
                                                               final int stringNumber) throws TabBuildingException {

        final Map<Integer, String> pos = new HashMap<>();

        for (Note n : notes) {
            if (n.getFret().getStringNumber() == stringNumber) {
                int position = SingleBarTablatureBuilder.getPosition(n.getTiming().getSixteenthNumber(), markings, spacing);
                String marking = n.getFret().fretMarking();
                pos.put(position, marking);
            }
        }

        return pos;
    }

    /**
     * Get the character position of a timing.
     *
     * @param sixteenthNumber Timing.
     * @param markings Markings to show on the tab.
     * @param spacing Spacing between markings.
     * @return Character position.
     * @throws TabBuildingException Unable to represent timing given the markings.
     */
    private static int getPosition(int sixteenthNumber,
                                   Markings markings,
                                   int spacing) throws TabBuildingException {

        if (markings == Markings.Tertiary) {
            return (spacing + 1) * sixteenthNumber;
        } else if (markings == Markings.Secondary) {
            if (sixteenthNumber % 2 != 0) {
                throw new TabBuildingException("Note can't be represented giving markings");
            }
            return (spacing + 1) * (sixteenthNumber / 2);
        } else if (markings == Markings.Main) {
            if (sixteenthNumber % 4 != 0) {
                throw new TabBuildingException("Note can't be represented giving markings");
            }
            return (spacing + 1) * (sixteenthNumber / 4);
        }

        return -1;
    }

    /**
     * Build a map of character position to markings.
     *
     * @param timedChords Timed chords.
     * @param markings Markings to show.
     * @param spacing Spacing between markings.
     * @return Map of character position to markings.
     * @throws TabBuildingException Unable to build tab.
     */
    static Map<Integer, String> buildPositionToMarking(final List<TimedChord> timedChords,
                                                       final Markings markings,
                                                       final int spacing) throws TabBuildingException {

        final Map<Integer, String> pos = new HashMap<>();

        for (TimedChord timedChord : timedChords) {
            int i = SingleBarTablatureBuilder.getPosition(timedChord.getTiming().getSixteenthNumber(), markings, spacing);
            pos.put(i, timedChord.getChord().musicNotation());
        }

        return pos;
    }

    /**
     * Build the ruler that shows the timing.
     *
     * @param markings Type of markings to display.
     * @param nBeats Number of main beats, e.g. 4 for 4/4.
     * @param spacing Number of spaces between markings.
     * @return Ruler.
     * @throws TabBuildingException Invalid number of beats or spacing.
     */
    static String buildRuler(final Markings markings,
                             final int nBeats,
                             final int spacing) throws TabBuildingException {

        // Preconditions
        if (nBeats < 0) {
            throw new TabBuildingException("Invalid number of beats: " + nBeats);
        }
        if (spacing <= 0) {
            throw new TabBuildingException("Invalid spacing: " + spacing);
        }

        // Build the spacing string
        final char[] spacingString = new char[spacing];
        Arrays.fill(spacingString, rulerSpacingSymbol);

        // Build the rule
        StringBuilder line = new StringBuilder();
        for (int beat = 1; beat <= nBeats; beat++) {
            line.append(beat);
            line.append(spacingString);

            if (markings == Markings.Tertiary) {
                line.append(tertiarySymbol);
                line.append(spacingString);
            }

            if (markings == Markings.Secondary || markings == Markings.Tertiary) {
                line.append(secondarySymbol);
                line.append(spacingString);
            }

            if (markings == Markings.Tertiary) {
                line.append(tertiarySymbol);
                line.append(spacingString);
            }
        }

        return line.toString();
    }

    /**
     * Build a single line (tab or chords).
     *
     * @param nElements Number of elements (characters) in the line.
     * @param positionToMarking Map of index to marking, e.g. fret number, tilde, etc.
     * @param spacer Character to use to create the 'background' of the line.
     * @return Single line.
     * @throws TabBuildingException Invalid number of elements, position or text.
     */
    private static String buildLine(final int nElements,
                                    final Map<Integer, String> positionToMarking,
                                    final char spacer)
            throws TabBuildingException {

        // Precondition
        if (nElements <= 0) {
            throw new TabBuildingException("Invalid number of elements: " + nElements);
        }

        // Construct an empty line
        final char[] line = new char[nElements];
        Arrays.fill(line, spacer);

        // Add the frets at the required positions
        for (Map.Entry<Integer, String> entry : positionToMarking.entrySet()) {

            // Get the position of the fret number
            final Integer position = entry.getKey();
            if (position < 0 || position >= nElements) {
                throw new TabBuildingException("Invalid position (" + position + " for a line of length " + nElements);
            }

            // Get the fret number
            final String fret = entry.getValue();

            // Check the characters will fit onto the line
            if (position + fret.length() > nElements) {
                throw new TabBuildingException("Text '" + fret + "' will go beyond the end of the line");
            }

            // Insert the characters onto the line
            for (int i = 0; i < fret.length(); i++ ) {
                line[position + i] = fret.charAt(i);
            }

        }

        return new String(line);
    }

    /**
     * Build a single line of guitar tab.
     *
     * @param nElements Number of elements (typically dashes).
     * @param positionToMarking Map of index to marking, e.g. fret number, tilde, etc.
     * @return Single tab line.
     * @throws TabBuildingException Unable to build line of tab.
     */
    static String buildTabLine(final int nElements,
                               final Map<Integer, String> positionToMarking) throws TabBuildingException {
        return SingleBarTablatureBuilder.buildLine(nElements, positionToMarking, SingleBarTablatureBuilder.lineCharacter);
    }

    /**
     * Build a single line of chords.
     *
     * @param nElements Number of elements.
     * @param positionToMarking Map of index to chord markings.
     * @return Single chord line.
     * @throws TabBuildingException Unable to build line of chords.
     */
    static String buildChordLine(final int nElements,
                                 final Map<Integer, String> positionToMarking) throws TabBuildingException {
        return SingleBarTablatureBuilder.buildLine(nElements, positionToMarking, ' ');
    }

}
