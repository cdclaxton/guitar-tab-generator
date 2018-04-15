package com.github.cdclaxton.guitartabgenerator.tabparser;

import java.util.Objects;

public final class ExtractedHeader implements ExtractedComponent {

    private final String key;
    private final String value;

    /**
     * Extracted header element.
     *
     * @param key Key (e.g. artist).
     * @param value Value associated with the key.
     */
    ExtractedHeader(final String key, final String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Get the extracted header element's key.
     *
     * @return Key.
     */
    public String getKey() {
        return key;
    }

    /**
     * Get the extracted header element's value.
     *
     * @return Value.
     */
    String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtractedHeader that = (ExtractedHeader) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}
