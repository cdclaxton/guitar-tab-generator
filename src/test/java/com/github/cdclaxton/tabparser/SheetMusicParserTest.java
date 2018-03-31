package com.github.cdclaxton.tabparser;

import com.github.cdclaxton.music.*;
import com.github.cdclaxton.sheetmusic.SheetMusic;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SheetMusicParserTest {

    @Test
    void parseValidSheetMusic() throws IOException, ExtractionException, InvalidKeyException,
            InvalidStringException, InvalidFretNumberException, InvalidTimingException, InvalidChordException {
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

    @Test
    void testParseValidLine() throws ExtractionException {
        // Section header
        ExtractedComponent component1 = SheetMusicParser.parseLine("[Chorus]");
        assertTrue(component1 instanceof ExtractedSectionHeader);
        ExtractedSectionHeader sectionHeader = (ExtractedSectionHeader) component1;
        assertEquals("Chorus", sectionHeader.getName());

        // Bar
        ExtractedComponent component2 = SheetMusicParser.parseLine("(1/Db) 1/<a4 d6 g6>");
        assertTrue(component2 instanceof ExtractedBar);
        ExtractedBar bar = (ExtractedBar) component2;
        assertEquals("1/Db", bar.getChords());
        assertEquals("1/<a4 d6 g6>", bar.getTimedNotes());

        // Header
        ExtractedComponent component3 = SheetMusicParser.parseLine("title = My Song");
        assertTrue(component3 instanceof ExtractedHeader);
        ExtractedHeader header = (ExtractedHeader) component3;
        assertEquals("title", header.getKey());
        assertEquals("My Song", header.getValue());

        // Text
        ExtractedComponent component4 = SheetMusicParser.parseLine("> Light overdrive");
        assertTrue(component4 instanceof ExtractedText);
        ExtractedText text = (ExtractedText) component4;
        assertEquals("Light overdrive", text.getText());
    }

}