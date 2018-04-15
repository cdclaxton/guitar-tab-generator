package com.github.cdclaxton.guitartabgenerator.tabparser;

import java.util.Objects;

public final class ExtractedText implements ExtractedComponent {

    private final String text;

    /**
     * Instantiate an object to represent a line of text in the sheet music.
     *
     * @param text Line of text.
     */
    ExtractedText(final String text) {
        this.text = text;
    }

    /**
     * Get the text.
     *
     * @return Text.
     */
    String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtractedText that = (ExtractedText) o;
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
