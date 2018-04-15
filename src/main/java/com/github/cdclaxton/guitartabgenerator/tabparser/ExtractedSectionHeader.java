package com.github.cdclaxton.guitartabgenerator.tabparser;

import java.util.Objects;

public final class ExtractedSectionHeader implements ExtractedComponent {

    private final String name;

    /**
     * Instantiate an object to represent a musical section's header.
     *
     * @param name Name or title of the section.
     */
    ExtractedSectionHeader(String name) {
        this.name = name;
    }

    /**
     * Get the section name.
     *
     * @return Name.
     */
    String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtractedSectionHeader that = (ExtractedSectionHeader) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
