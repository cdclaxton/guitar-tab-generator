package com.github.cdclaxton.guitartabgenerator.sheetmusic;

import java.util.HashMap;
import java.util.Map;

public class Metadata {

    private final Map<String, Object> metadata = new HashMap<>();

    public Metadata() {}

    public void addMetadata(String key, Object value) {
        this.metadata.put(key, value);
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }
}
