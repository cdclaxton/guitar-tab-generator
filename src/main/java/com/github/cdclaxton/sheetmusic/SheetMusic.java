package com.github.cdclaxton.sheetmusic;

import com.github.cdclaxton.music.Key;

import java.util.List;
import java.util.Map;

public class SheetMusic {

    private String title;
    private String artist;
    private Key key;
    private Map<String, Object> metadata;
    private List<Section> sections;

    public SheetMusic(String title, String artist, Key key, Map<String, Object> metadata, List<Section> sections) {
        this.title = title;
        this.artist = artist;
        this.key = key;
        this.metadata = metadata;
        this.sections = sections;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public List<Section> getSections() {
        return sections;
    }

    public Key getKey() {
        return key;
    }
}
