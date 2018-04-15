package com.github.cdclaxton.guitartabgenerator.sheetmusic;

import com.github.cdclaxton.guitartabgenerator.music.Bar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Section {

    private final String name;
    private final List<String> text;
    private final List<Bar> bars;

    /**
     * Instantiate a section within sheet music.
     *
     * @param name Name (or title) of the section.
     * @param text Lines of text.
     * @param bars Bars in the section.
     */
    public Section(final String name, final List<String> text, final List<Bar> bars) {
        this.name = name;

        // Make a deep copy of the text
        this.text = new ArrayList<>(text);

        // Make a deep copy of the bars
        this.bars = new ArrayList<>(bars);
    }

    /**
     * Get the name (or title) of the section.
     *
     * @return Name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the lines of text.
     *
     * @return Lines of text.
     */
    public List<String> getText() {
        return new ArrayList<>(this.text);
    }

    /**
     * Get the bars in the section.
     *
     * @return List of bars in the section.
     */
    public List<Bar> getBars() {
        return bars.stream().map(Bar::newInstance).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(name, section.name) &&
                Objects.equals(text, section.text) &&
                Objects.equals(bars, section.bars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, text, bars);
    }
}
