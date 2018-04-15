package com.github.cdclaxton.guitartabgenerator.sheetmusic;

import java.util.ArrayList;
import java.util.List;

public final class SheetMusic {

    private final Header header;
    private final Metadata metadata;
    private final List<Section> sections;

    /**
     * Construct an object to represent the sheet music.
     *
     * @param header Header (title, artist, etc.)
     * @param metadata Metadata.
     * @param sections Sections (each with title, text and musical bars).
     */
    public SheetMusic(final Header header, final Metadata metadata, final List<Section> sections) {
        this.header = header;
        this.metadata = metadata;
        this.sections = sections;
    }

    /**
     * Get the header of the sheet music.
     *
     * @return Header.
     */
    public Header getHeader() {
        return Header.newInstance(header);
    }

    /**
     * Get the metadata.
     *
     * @return Metadata.
     */
    public Metadata getMetadata() {
        return metadata;
    }

    /**
     * Get the sections.
     *
     * @return Sections.
     */
    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }

    /**
     * Get a specific section by index.
     *
     * @param index Index of the section.
     * @return Section.
     */
    public Section getSection(int index) {
        return this.sections.get(index);
    }

}
