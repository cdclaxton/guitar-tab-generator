package com.github.cdclaxton.guitartabgenerator.sheetmusic;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Metadata {

    private final Map<String, Object> metadata = new HashMap<>();

    public Metadata() {}

    public void addMetadata(String key, Object value) {
        this.metadata.put(key, value);
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    /**
     * Find the URL for the song in the normal key.
     *
     * @return URL (if present).
     */
    public Optional<String> findUrl() {

        // Construct the key
        final String key = "metadata.video";

        Optional<String> url = Optional.empty();
        if (metadata.containsKey(key)) {
            url = Optional.of((String) metadata.get(key));
        }

        return url;
    }

    /**
     * Find the URL for the song a given transpose key.
     *
     * @param musicalKey Musical key to find the song in.
     * @return URL (if present).
     */
    public Optional<String> findUrl(final String musicalKey) {

        // Construct the expected key
        final String key = "metadata.video." + musicalKey;

        Optional<String> url = Optional.empty();
        if (metadata.containsKey(key)) {
            url = Optional.of((String) metadata.get(key));
        }

        return url;
    }
}
