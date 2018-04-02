package com.github.cdclaxton.guitartabgenerator.music;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimedChord {

    private Timing timing;
    private String chord;

    private static String validNotes = "Ab|A|A#|Bb|B|C|C#|Db|D|D#|Eb|E|F|F#|Gb|G|G#";
    private static String chordPattern = "^(" + validNotes + ")m?(/(" + validNotes + "))?$";
    private Pattern pattern = Pattern.compile(chordPattern);

    public TimedChord(Timing timing, String chord) throws InvalidChordException {
        if (!isValid(chord)) {
            throw new InvalidChordException("Chord (" + chord + ") is not valid");
        }
        this.timing = timing;
        this.chord = chord;
    }

    private boolean isValid(String chord) {
        return chord.length() > 0 && this.pattern.matcher(chord).find();
    }

    public String getChord() {
        return chord;
    }

    public Timing getTiming() {
        return timing;
    }
}
