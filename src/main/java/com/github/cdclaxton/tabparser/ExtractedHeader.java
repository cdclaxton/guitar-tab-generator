package com.github.cdclaxton.tabparser;

import java.util.Objects;

public class ExtractedHeader implements ExtractedComponent {

    private String key;
    private String value;

    public ExtractedHeader(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
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

}
