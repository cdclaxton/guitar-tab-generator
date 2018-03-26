package com.github.cdclaxton.tabwriter;

import com.github.cdclaxton.music.Bar;

import java.util.Arrays;
import java.util.Map;

public class BarBuilder {

    private static final char lineCharacter = '-';

    public BarBuilder(Bar bar) {

    }

    protected static String buildTabLine(int nElements, Map<Integer, String> positionToMarking) throws TabBuildingException {

        // Precondition
        if (nElements <= 0) {
            throw new TabBuildingException("Invalid number of elements: " + nElements);
        }

        // Construct an empty line
        final char[] line = new char[nElements];
        Arrays.fill(line, lineCharacter);

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
                throw new TabBuildingException("Text '" + fret + "' will go beyond the end of the tab line");
            }

            // Insert the characters onto the line
            for (int i = 0; i < fret.length(); i++ ) {
                line[position + i] = fret.charAt(i);
            }

        }

        return new String(line);
    }

}
