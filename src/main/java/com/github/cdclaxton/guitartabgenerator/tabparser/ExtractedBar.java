package com.github.cdclaxton.guitartabgenerator.tabparser;

import com.github.cdclaxton.guitartabgenerator.music.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class ExtractedBar implements ExtractedComponent {

    private final String timedChords;
    private final String notes;
    private final static Logger logger = LoggerFactory.getLogger(SheetMusicParser.class);

    /**
     * Instantiate an immutable object that represents a bar.
     *
     * @param timedChords Chords.
     * @param notes Notes.
     */
    ExtractedBar(final String timedChords,
                 final String notes) {
        this.timedChords = timedChords;
        this.notes = notes;
    }

    /**
     * Get the timed chords.
     *
     * @return Timed chords.
     */
    String getTimedChords() {
        return timedChords;
    }

    /**
     * Get the notes.
     *
     * @return Notes.
     */
    public String getNotes() {
        return notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtractedBar that = (ExtractedBar) o;
        return Objects.equals(timedChords, that.timedChords) &&
                Objects.equals(notes, that.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timedChords, notes);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Parsers
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Convert the extracted bar to a Bar.
     *
     * @param timeSignature Time signature of the bar.
     * @return Bar.
     * @throws ExtractionException Unable to parse notes or chords.
     * @throws InvalidStringException Invalid string number.
     * @throws InvalidFretNumberException Invalid fret number.
     * @throws InvalidTimingException Invalid timing.
     * @throws InvalidChordException Invalid chord.
     */
    Bar toBar(final Bar.TimeSignature timeSignature)
            throws ExtractionException, InvalidStringException, InvalidFretNumberException,
            InvalidTimingException, InvalidChordException {

        // Parse the notes
        logger.debug("Parsing notes: " + this.notes);
        List<Note> notes = ExtractedBar.parseNotes(this.notes);

        // Parse the timed chords
        logger.debug("Parsing timed chords: " + this.timedChords);
        List<TimedChord> chords = ExtractedBar.parseChords(this.timedChords);

        return new Bar(timeSignature, notes, chords);
    }

    /**
     * Parse a list of timed chords, e.g. 1/Db 2+/F.
     *
     * @param chords List of timedChords.
     * @return List of timed timedChords.
     * @throws ExtractionException Unable to extract chord and timing.
     * @throws InvalidTimingException Invalid timing.
     * @throws InvalidChordException Invalid chord.
     */
    static List<TimedChord> parseChords(final String chords)
            throws ExtractionException, InvalidTimingException, InvalidChordException {

        // Separate timed chords into individual elements
        List<String> separateChordsStrings = ExtractedBar.separateChords(chords.trim());

        // Remove any white space
        List<String> chordStrings = separateChordsStrings.stream().filter(s -> s.length() > 0).collect(Collectors.toList());

        // Parse each of the string representation of the timedChords
        List<TimedChord> timedChords = new ArrayList<>();
        for (String c : chordStrings) {
            logger.debug("About to parse chord: " + c);
            timedChords.add(ExtractedBar.notationToChord(c));
        }

        return timedChords;
    }

    /**
     * Separate a list of timed chords, e.g. 1/Db 2+/F into [1/Db, 2+/F].
     *
     * @param chords Chords to separate.
     * @return Separate timedChords.
     */
    static List<String> separateChords(final String chords) {
        String[] individualChords = chords.trim().split(" ");
        return Arrays.asList(individualChords);
    }

    /**
     * Convert the string representation of a timed chord, e.g. 1/Eb, into a TimedChord.
     *
     * @param chord Timed chord to parse.
     * @return Timed chord.
     * @throws ExtractionException Unable to extract chord or timing.
     * @throws InvalidTimingException Invalid timing.
     * @throws InvalidChordException Invalid chord.
     */
    static TimedChord notationToChord(final String chord)
            throws ExtractionException, InvalidTimingException, InvalidChordException {

        logger.debug("Parsing timed chord: " + chord);

        final String pattern = "([1-6][+ea]?)/(.*)";
        final Pattern compiledPattern = Pattern.compile(pattern);
        final Matcher matcher = compiledPattern.matcher(chord);
        if (matcher.find()) {
            // Extract the timing of the chord
            String timingString = matcher.group(1);
            Timing timing = new Timing(ExtractedBar.timingNotationToSixteenth(timingString));

            // Extract the chord and parse
            String chordPart = matcher.group(2);
            Chord parsedChord = Chord.build(chordPart);

            // Create and return a timed chord
            return new TimedChord(timing, parsedChord);

        } else {
            throw new ExtractionException("Can't extract chord and timing from: " + chord);
        }
    }

    /**
     * Parse a list of notes, e.g. 1/g8 2+/<g9 b11>, into separate notes.
     *
     * @param listNotes String representation of a list of notes.
     * @return List of notes.
     * @throws InvalidStringException Invalid string number.
     * @throws InvalidFretNumberException Invalid fret number.
     * @throws InvalidTimingException Invalid timing.
     * @throws ExtractionException Unable to parse note.
     */
    static List<Note> parseNotes(final String listNotes)
            throws InvalidStringException, InvalidFretNumberException, InvalidTimingException, ExtractionException {

        // Perform an initial separation
        // e.g. 2+/g6 3/<g7, b8> -> [2+/g6, 3/<g7, b8>]
        final List<String> parts = ExtractedBar.splitNotesByTime(listNotes);

        // Walk through the string representation of the notes and convert to Notes
        final List<Note> notes = new ArrayList<>();

        for (String n : parts) {
            if (n.contains("<")) {
                List<String> separateNotes = ExtractedBar.parseSimultaneousNotes(n);
                for (String m : separateNotes) {
                    notes.add(ExtractedBar.notationToNote(m));
                }
            } else {
                notes.add(ExtractedBar.notationToNote(n));
            }
        }

        return notes;
    }

    /**
     * Split simultaneous notes into separate notes, e.g. 1/<g8 b9> into [1/g8, 1/b9].
     *
     * @param notes Notes to split.
     * @return Separate notes.
     * @throws ExtractionException Unable to parse simultaneous notes.
     */
    static List<String> parseSimultaneousNotes(final String notes) throws ExtractionException {

        logger.debug("Extracting simultaneous notes from: " + notes);

        // Extract the time
        final String timePattern = "([1-6][+ae]?)/.*";
        final Pattern compiledTimePattern = Pattern.compile(timePattern);
        final Matcher timeMatcher = compiledTimePattern.matcher(notes);
        String time;
        if (timeMatcher.find()) {
            time = timeMatcher.group(1);
            logger.debug("Extracted time: " + time);
        } else {
            throw new ExtractionException("Can't extract time from: " + notes);
        }

        // Extract each of the separate notes
        final List<String> simultaneousNotes = new ArrayList<>();

        final String notePattern = "([A-Za-z][0-9]{1,2})";
        final Pattern compiledNotePattern = Pattern.compile(notePattern);
        final Matcher noteMatcher = compiledNotePattern.matcher(notes);
        while (noteMatcher.find()) {
            String n = time + "/" + noteMatcher.group(1).trim();
            simultaneousNotes.add(n);
            logger.debug("Extracted note: " + n);
        }

        if (simultaneousNotes.size() == 0) {
            throw new ExtractionException("Couldn't extract any notes from: " + notes);
        }

        return simultaneousNotes;
    }

    /**
     * Split a list of notes into time-separated notes, e.g. 2+/g6 3/<g7, b8> into 2+/g6 and 3/<g7, b8>.
     *
     * @param listNotes List of notes.
     * @return Time-separated notes.
     */
    static List<String> splitNotesByTime(final String listNotes) {
        final String pattern = "([1-6][+ae]?/(<([A-Za-z][0-9]{1,2}\\s*)+>|[A-Za-z][0-9]{1,2}\\s*))";
        final Pattern compiledPattern = Pattern.compile(pattern);
        final Matcher matcher = compiledPattern.matcher(listNotes);
        List<String> parts = new ArrayList<>();
        while (matcher.find()) {
            parts.add(matcher.group(1).trim());
        }
        return parts;
    }

    /**
     * Convert a note from its notation (e.g. 4e/g7) into a Note object.
     *
     * @param notation String representation of the note.
     * @return Note.
     * @throws InvalidStringException String letter is invalid.
     * @throws ExtractionException Unable to extract note.
     * @throws InvalidFretNumberException Fret number is invalid.
     * @throws InvalidTimingException Timing is invalid.
     */
    static Note notationToNote(final String notation)
            throws InvalidStringException, ExtractionException, InvalidFretNumberException, InvalidTimingException {

        final String pattern = "([1-6][+ea]?)/([A-Za-z])([0-9]{1,2})";
        final Pattern compiledPattern = Pattern.compile(pattern);
        final Matcher matcher = compiledPattern.matcher(notation);
        if (matcher.find()) {

            // Extract the timing of the note
            final String timingString = matcher.group(1);
            final Timing timing = new Timing(ExtractedBar.timingNotationToSixteenth(timingString));

            // Extract the note
            final int stringNumber = ExtractedBar.stringLetterToNumber(matcher.group(2));
            final int fretNumber = Integer.valueOf(matcher.group(3));
            final Fret fret = new Fret(stringNumber, fretNumber);

            // Build and return the note
            return new Note(fret, timing);
        } else {
            throw new ExtractionException("Can't extract note and timing from: " + notation);
        }
    }

    /**
     * Convert a string representation of time (e.g. 4e) into a sixteenth note time.
     *
     * @param timingNotation String representation of the time to convert.
     * @return Sixteenth note time.
     * @throws ExtractionException Unable to extract timing.
     */
    static int timingNotationToSixteenth(final String timingNotation) throws ExtractionException {
        final String pattern = "([1-6])([+ea]?)";
        final Pattern compiledPattern = Pattern.compile(pattern);
        final Matcher matcher = compiledPattern.matcher(timingNotation);
        if (matcher.find()) {
            int sixteenth = (Integer.valueOf(matcher.group(1))-1) * 4;
            switch (matcher.group(2)) {
                case "":
                    break;
                case "+":
                    sixteenth += 2;
                    break;
                case "e":
                    sixteenth += 1;
                    break;
                case "a":
                    sixteenth += 3;
                    break;
            }
            return sixteenth;
        } else {
            throw new ExtractionException("Can't extract timing from: " + timingNotation);
        }
    }

    /**
     * Convert a string letter (e.g. E) to a number (e.g. 6).
     *
     * @param letter String letter.
     * @return String number.
     * @throws InvalidStringException Invalid string letter.
     */
    static int stringLetterToNumber(String letter) throws InvalidStringException {
        switch (letter) {
            case "E":
                return 6;
            case "a":
                return 5;
            case "d":
                return 4;
            case "g":
                return 3;
            case "b":
                return 2;
            case "e":
                return 1;
        }
        throw new InvalidStringException("Can't determine string number for: " + letter);
    }

}
