package com.github.cdclaxton.guitartabgenerator.tabwriter;

import com.github.cdclaxton.guitartabgenerator.sheetmusic.Header;
import com.github.cdclaxton.guitartabgenerator.sheetmusic.Section;
import com.github.cdclaxton.guitartabgenerator.sheetmusic.SheetMusic;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public final class TabSheetMusicBuilder {

    private TabSheetMusicBuilder() {}

    /**
     * Build the text lines for a sheet of tab music.
     *
     * @param sheetMusic Sheet music.
     * @param pageWidth Number of characters width of the page.
     * @return Sheet music lines.
     * @throws TabBuildingException Unable to build the sheet music.
     */
    public static List<String> buildTabSheetMusic(final SheetMusic sheetMusic,
                                                  final int pageWidth) throws TabBuildingException {

        // List to hold each of the lines of sheet music
        List<String> sheetMusicLines = new ArrayList<>();

        // Add the header
        sheetMusicLines.addAll(TabSheetMusicBuilder.buildHeader(sheetMusic.getHeader(), pageWidth));

        // Build each section
        for (Section section : sheetMusic.getSections()) {
            sheetMusicLines.addAll(TabSheetMusicBuilder.buildSection(section, pageWidth));

            // Add an empty line after the section
            sheetMusicLines.add("");
        }

        return sheetMusicLines;
    }

    /**
     * Build the text lines for a single section for a sheet of tab.
     *
     * @param section Section to build.
     * @param pageWidth Page width in characters.
     * @return Fully-constructed section.
     * @throws TabBuildingException Unable to layout the bars.
     */
    static List<String> buildSection(final Section section,
                                     final int pageWidth) throws TabBuildingException {

        List<String> lines = new ArrayList<>();

        if (section.getName() != null) {
            lines.add("# " + section.getName());
        }

        if (section.getText().size() > 0) {
            lines.addAll(section.getText());
        }

        // Layout the bars using the most compact form possible
        lines.addAll(LayoutEngine.layoutBars(section.getBars(), pageWidth, 1));

        return lines;
    }

    /**
     * Build the header text for a sheet of tab.
     *
     * @param header Header.
     * @param pageWidth Width of the page in characters.
     * @return List of text lines representing the header.
     */
    private static List<String> buildHeader(final Header header,
                                            final int pageWidth) {

        final List<String> headerLines = new ArrayList<>();

        // Add the song title
        if (header.getTitle() != null) {
            headerLines.add(StringUtils.center(header.getTitle(), pageWidth));
        }

        // Add the artist
        if (header.getArtist() != null) {
            headerLines.add(StringUtils.center(header.getArtist(), pageWidth));
        }

        if (header.getKey() != null) {
            headerLines.add(StringUtils.leftPad(header.getKey().getKey(), pageWidth));
        }

        return headerLines;
    }

}
