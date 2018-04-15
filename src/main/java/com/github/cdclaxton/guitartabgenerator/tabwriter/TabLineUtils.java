package com.github.cdclaxton.guitartabgenerator.tabwriter;

import com.google.common.base.Strings;

final class TabLineUtils {

    /**
     * Pad a line to the left.
     *
     * @param line Line to pad.
     * @param padding Number of characters to pad.
     * @param paddingChar Character to ue to pad.
     * @return Padded line.
     */
    static String padLeft(final String line,
                          final int padding,
                          final char paddingChar) {

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
    static String padRight(final String line,
                           final int padding,
                           final char paddingChar) {

        if (line == null) {
            throw new IllegalArgumentException("Can't pad a null string");
        }

        return Strings.padEnd(line, line.length() +  padding, paddingChar);
    }

}
