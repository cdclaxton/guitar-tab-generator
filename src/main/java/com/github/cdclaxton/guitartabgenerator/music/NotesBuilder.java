package com.github.cdclaxton.guitartabgenerator.music;

import java.util.ArrayList;
import java.util.List;

public final class NotesBuilder {

    private final List<Note> notes = new ArrayList<>();

    NotesBuilder() {}

    /**
     * Add a note.
     *
     * @param stringNumber String number of the note.
     * @param fret Fret number of the note.
     * @param timing Timing of the note.
     * @return This.
     * @throws InvalidStringException
     * @throws InvalidFretNumberException
     * @throws InvalidTimingException
     */
    NotesBuilder addNote(int stringNumber, int fret, int timing)
            throws InvalidStringException, InvalidFretNumberException, InvalidTimingException {

        notes.add(new Note(new Fret(stringNumber, fret), new Timing(timing)));
        return this;
    }

    /**
     * Return the constructed list of notes.
     *
     * @return List of notes.
     */
    public List<Note> build() {
        return this.notes;
    }

}
