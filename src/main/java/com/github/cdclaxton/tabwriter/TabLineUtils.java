package com.github.cdclaxton.tabwriter;

import com.google.common.base.Strings;

public class TabLineUtils {

    /**
     * Pad a line to the left.
     *
     * @param line Line to pad.
     * @param padding Number of characters to pad.
     * @param paddingChar Character to ue to pad.
     * @return Padded line.
     */
    public static String padLeft(String line, int padding, char paddingChar) {
        if (line == null) {
            throw new IllegalArgumentException("Can't pad a null string");
        }

        return Strings.padStart(line, line.length() +  padding, paddingChar);
    }

    /**
     * Pad a line to the right.
     *
     * @param line Line to pad.
     * @param padding Number of characters to pad.
     * @param paddingChar Character to ue to pad.
     * @return Padded line.
     */
    public static String padRight(String line, int padding, char paddingChar) {
        if (line == null) {
            throw new IllegalArgumentException("Can't pad a null string");
        }

        return Strings.padEnd(line, line.length() +  padding, paddingChar);
    }

}
