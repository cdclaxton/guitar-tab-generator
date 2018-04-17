package com.github.cdclaxton.guitartabgenerator.music;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Chord {

    private final String rootNote;
    private final String symbols;
    private final Optional<String> bassNote;

    private final static Logger logger = LoggerFactory.getLogger(Chord.class);

    /**
     * Instantiate a chord.
     *
     * E.g. a C#m7/E chord is represented as rootNote=C#, symbols=m7, bassNote=E.
     *
     * @param rootNote Root note of the chord.
     * @param symbols Type of the chord.
     * @param bassNote Bass note (if different from the root note).
     */
    public Chord(final String rootNote,
                 final String symbols,
                 final Optional<String> bassNote) {
        this.rootNote = rootNote;
        this.symbols = symbols;
        this.bassNote = bassNote;
    }

    /**
     * Create a new instance of the chord.
     *
     * @param chord Chord to make a new instance of.
     * @return New instance of the chord.
     */
    public static Chord newInstance(final Chord chord) {
        return new Chord(chord.rootNote, chord.symbols, chord.bassNote);
    }

    /**
     * Performs a basic test to determine if the chord is valid.
     *
     * @param chord Chord to test.
     * @return True if the chord is valid.
     */
    public static boolean isChordValid(final String chord) {
        boolean parsable = true;
        try {
            Chord.build(chord);
        } catch (TranspositionException e) {
            parsable = false;
        }
        return parsable;
    }

    /**
     * Parse a string representation of a chord.
     *
     * @param chord Chord to parse.
     * @return Parsed chord.
     * @throws TranspositionException Unable to parse chord.
     */
    public static Chord build(final String chord) throws TranspositionException {

        final String pattern = "^([ABCDEFG][#b]?)([A-Za-z0-9]*)(/[ABCDEFG][#b]?)?$";
        final Pattern compiledPattern = Pattern.compile(pattern);
        final Matcher matcher = compiledPattern.matcher(chord);
        final Chord parsedChord;
        if (matcher.find()) {
            final String rootNote = matcher.group(1);
            final String symbols = matcher.group(2);
            final Optional<String> bassNote;
            if (matcher.group(3) != null) bassNote = Optional.of(matcher.group(3).substring(1));
            else bassNote = Optional.empty();
            parsedChord = new Chord(rootNote, symbols, bassNote);
        } else {
            logger.error("Can't parse chord: " + chord);
            throw new TranspositionException("Can't parse chord: " + chord);
        }

        logger.debug("Chord " + chord + " --> " + parsedChord);
        return parsedChord;
    }

    @Override
    public String toString() {
        return "Chord[rootNote=" + rootNote + ",symbols=" + symbols + ",bassNote=" + bassNote + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chord chord = (Chord) o;
        return Objects.equals(rootNote, chord.rootNote) &&
                Objects.equals(symbols, chord.symbols) &&
                Objects.equals(bassNote, chord.bassNote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rootNote, symbols, bassNote);
    }
}
