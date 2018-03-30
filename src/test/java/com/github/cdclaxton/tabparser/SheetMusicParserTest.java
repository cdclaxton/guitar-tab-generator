package com.github.cdclaxton.tabparser;

import com.github.cdclaxton.sheetmusic.SheetMusic;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SheetMusicParserTest {

    @Test
    void parseValidSheetMusic() throws IOException, ExtractionException {
        String path = this.getClass().getResource("/SheetMusic/How_great_is_our_God.txt").getFile();
        File file = new File(path);
        SheetMusic sheetMusic = SheetMusicParser.parseSheetMusic(file);
    }

    @Test
    void testExtractHeader() throws ExtractionException {
        assertTrue(new ExtractedHeader("title", "My Song").equals(SheetMusicParser.extractHeader("title = My Song")));
        assertTrue(new ExtractedHeader("artist", "Bryan Adams").equals(SheetMusicParser.extractHeader("artist = Bryan Adams")));
    }

    @Test
    void testExtractSectionHeader() throws ExtractionException {
        assertEquals("Chorus", SheetMusicParser.extractSectionHeader("[Chorus]").getName());
        assertEquals("Chorus", SheetMusicParser.extractSectionHeader(" [Chorus]").getName());
        assertEquals("Chorus", SheetMusicParser.extractSectionHeader("[Chorus] ").getName());
        assertEquals("Chorus", SheetMusicParser.extractSectionHeader(" [Chorus] ").getName());
    }

    @Test
    void testExtractText() throws ExtractionException {
        assertEquals("Light overdrive", SheetMusicParser.extractText(">Light overdrive").getText());
        assertEquals("Light overdrive", SheetMusicParser.extractText("> Light overdrive").getText());
        assertEquals("Light overdrive", SheetMusicParser.extractText(">Light overdrive ").getText());
    }

    @Test
    void testExtractBar() throws ExtractionException {
        String barWithChords = "(1/Db) 1/<a4 d6 g6>";
        ExtractedBar extractedBar = SheetMusicParser.extractBar(barWithChords);
        assertEquals("1/Db", extractedBar.getChords());
        assertEquals("1/<a4 d6 g6>", extractedBar.getTimedNotes());

        String barNoChords = "() 1/g6 1+/g6 ";
        ExtractedBar extractedBar2 = SheetMusicParser.extractBar(barNoChords);
        assertEquals("", extractedBar2.getChords());
        assertEquals("1/g6 1+/g6", extractedBar2.getTimedNotes());

        String chordsNoTab = "(1/Db)";
        ExtractedBar extractedBar3 = SheetMusicParser.extractBar(chordsNoTab);
        assertEquals("1/Db", extractedBar3.getChords());
        assertEquals("", extractedBar3.getTimedNotes());
    }
}