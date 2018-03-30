package com.github.cdclaxton.tabparser;

import java.util.Objects;

public class ExtractedText implements ExtractedComponent {

    private String text;

    public ExtractedText(String text) {
        this.text = text;
    }


    public String getText() {
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
