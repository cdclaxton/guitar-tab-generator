package com.github.cdclaxton.tabparser;

import java.util.Objects;

public class ExtractedSectionHeader implements ExtractedComponent {

    private String name;

    public ExtractedSectionHeader(String name) {
        this.name = name;
    }

    public String getName() {
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
