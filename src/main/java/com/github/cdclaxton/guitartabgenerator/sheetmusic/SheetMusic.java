package com.github.cdclaxton.guitartabgenerator.sheetmusic;

import java.util.List;

public class SheetMusic {

    private Header header;
    private Metadata metadata;
    private List<Section> sections;

    public SheetMusic(Header header, Metadata metadata, List<Section> sections) {
        this.header = header;
        this.metadata = metadata;
        this.sections = sections;
    }

    public Header getHeader() {
        return header;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public List<Section> getSections() {
        return sections;
    }

    public Section getSection(int section) {
        return this.sections.get(section);
    }

}
