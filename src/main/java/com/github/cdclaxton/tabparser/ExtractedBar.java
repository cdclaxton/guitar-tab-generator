package com.github.cdclaxton.tabparser;

import java.util.Objects;

public class ExtractedBar implements ExtractedComponent {

    private String chords;
    private String timedNotes;

    public ExtractedBar(String chords, String timedNotes) {
        this.chords = chords;
        this.timedNotes = timedNotes;
    }


    public String getChords() {
        return chords;
    }

    public String getTimedNotes() {
        return timedNotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtractedBar that = (ExtractedBar) o;
        return Objects.equals(chords, that.chords) &&
                Objects.equals(timedNotes, that.timedNotes);
    }

    @Override
    public int hashCode() {

        return Objects.hash(chords, timedNotes);
    }
}
