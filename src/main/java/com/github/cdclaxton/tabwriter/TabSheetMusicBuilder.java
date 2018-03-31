package com.github.cdclaxton.tabwriter;

import com.github.cdclaxton.music.Bar;
import com.github.cdclaxton.sheetmusic.Header;
import com.github.cdclaxton.sheetmusic.Section;
import com.github.cdclaxton.sheetmusic.SheetMusic;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TabSheetMusicBuilder {

    private TabSheetMusicBuilder() {}

    public static List<String> buildTabSheetMusic(SheetMusic sheetMusic, int pageWidth) throws TabBuildingException {

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

        for (String line: sheetMusicLines) {
            System.out.println(line);
        }

        return sheetMusicLines;
    }

    protected static List<String> buildSection(Section section, int pageWidth) throws TabBuildingException {
        List<String> lines = new ArrayList<>();

        if (section.getName() != null) {
            lines.add("# " + section.getName());
        }

        if (section.getText().size() > 0) {
            for (String text : section.getText()) {
                lines.add(text);
            }
        }

        if (section.getBars().size() > 0) {
            for (Bar b : section.getBars()) {
                SingleBarTablature tab = SingleBarTablatureBuilder.buildTabFromBar(b, SingleBarTablatureBuilder.Markings.Tertiary);
                tab.addBarEndLines(SingleBarTablature.BarLineType.single);
                tab.addBarStartLines(SingleBarTablature.BarLineType.single);
                tab.addStringLetters();
                List<String> fullBar = tab.getFullBar();
                lines.addAll(fullBar);
                lines.add("");
            }
        }

        return lines;
    }

    /**
     * Build the header text.
     *
     * @param header Header.
     * @param pageWidth Width of the page in characters.
     * @return Header.
     */
    protected static List<String> buildHeader(Header header, int pageWidth) {
        List<String> headerLines = new ArrayList<>();

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
