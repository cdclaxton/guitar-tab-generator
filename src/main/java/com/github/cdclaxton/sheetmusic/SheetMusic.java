package com.github.cdclaxton.sheetmusic;

import java.util.List;
import java.util.Map;

public class SheetMusic {

    private Header header;
    private Map<String, Object> metadata;
    private List<Section> sections;

    public SheetMusic(Header header, Map<String, Object> metadata, List<Section> sections) {
        this.header = header;
        this.metadata = metadata;
        this.sections = sections;
    }

    public Header getHeader() {
        return header;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public List<Section> getSections() {
        return sections;
    }

}
