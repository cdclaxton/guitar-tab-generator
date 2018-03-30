package com.github.cdclaxton.tabwriter;

import com.github.cdclaxton.music.Bar;
import com.github.cdclaxton.music.Note;
import com.github.cdclaxton.music.TimedChord;

import java.util.*;

public class TablatureBarBuilder {

    private static final char lineCharacter = '-';

    public enum Markings{ Main, Secondary, Tertiary }
    private static final String secondarySymbol = "+";
    private static final String tertiarySymbol = ".";
    private static final char rulerSpacingSymbol = ' ';

    private TablatureBarBuilder() {}

    public TablatureBar buildTabFromBar(Bar bar) {
        return null;
    }

    protected static TablatureBar buildTabIn44FromBar(Bar bar, Markings markings) throws TabBuildingException {

        int spacing = 3;

        // Build the ruler
        String ruler = TablatureBarBuilder.buildRuler(markings, 4, spacing);

        int nElements = ruler.length();

        // Build the chord line
        Map<Integer, String> chordPosToMarking =
                TablatureBarBuilder.buildPositionToMarking(bar.getTimedChords(), markings, spacing);
        String chordLine = TablatureBarBuilder.buildChordLine(nElements, chordPosToMarking);

        // Build a tab line for each guitar string
        List<String> tabLines = new ArrayList<>();
        for (int guitarString = 1; guitarString <= 6; guitarString++) {
            Map<Integer, String> tabPosToMarking =
                    TablatureBarBuilder.buildPositionToMarking(bar.getNotes(), markings, spacing, guitarString);
            String tabLine = TablatureBarBuilder.buildTabLine(nElements, tabPosToMarking);
            tabLines.add(tabLine);
        }

        // Build an return the single bar of tab
        return new TablatureBar(ruler, chordLine, tabLines);
    }

    protected static Map<Integer, String> buildPositionToMarking(List<Note> notes, Markings markings,
                                                                 int spacing, int stringNumber) throws TabBuildingException {
        Map<Integer, String> pos = new HashMap<>();

        for (Note n : notes) {
            if (n.getFret().getStringNumber() == stringNumber) {
                int position = TablatureBarBuilder.getPosition(n.getTiming().getSixteenthNumber(), markings, spacing);
                String marking = n.getFret().getFretMarking();
                pos.put(position, marking);
            }
        }

        return pos;
    }

    protected static int getPosition(int sixteenthNumber, Markings markings, int spacing) throws TabBuildingException {

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

    protected static Map<Integer, String> buildPositionToMarking(List<TimedChord> timedChords, Markings markings, int spacing)
            throws TabBuildingException {

        Map<Integer, String> pos = new HashMap<>();

        for (TimedChord timedChord : timedChords) {
            int i = TablatureBarBuilder.getPosition(timedChord.getTiming().getSixteenthNumber(), markings, spacing);
            pos.put(i, timedChord.getChord());
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
     * @throws TabBuildingException
     */
    protected static String buildRuler(Markings markings, int nBeats, int spacing) throws TabBuildingException {
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
     * @throws TabBuildingException
     */
    protected static String buildLine(int nElements, Map<Integer, String> positionToMarking, char spacer)
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
            Integer position = entry.getKey();
            if (position < 0 || position >= nElements) {
                throw new TabBuildingException("Invalid position (" + position + " for a line of length " + nElements);
            }

            // Get the fret number
            String fret = entry.getValue();

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
     * @throws TabBuildingException
     */
    protected static String buildTabLine(int nElements, Map<Integer, String> positionToMarking) throws TabBuildingException {
        return TablatureBarBuilder.buildLine(nElements, positionToMarking, TablatureBarBuilder.lineCharacter);
    }

    /**
     * Build a single line of chords.
     *
     * @param nElements Number of elements.
     * @param positionToMarking Map of index to chord markings.
     * @return Single chord line.
     * @throws TabBuildingException
     */
    protected static String buildChordLine(int nElements, Map<Integer, String> positionToMarking) throws TabBuildingException {
        return TablatureBarBuilder.buildLine(nElements, positionToMarking, ' ');
    }

}
