package com.github.cdclaxton.tabwriter;

import com.github.cdclaxton.music.Bar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LayoutEngine {

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
