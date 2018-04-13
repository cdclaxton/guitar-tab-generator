package com.github.cdclaxton.guitartabgenerator.app;

import com.github.cdclaxton.guitartabgenerator.sheetmusic.SheetMusic;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GuitarTabGeneratorTest {

    @Test
    void testParsedCmdArgsShowHelp() throws ParseException {
        String[] args = new String[] { "-h" };
        GuitarTabGenerator.ParsedCmdArgs parsed = new GuitarTabGenerator.ParsedCmdArgs(args);

        GuitarTabGenerator.ParsedCmdArgs expected = new GuitarTabGenerator.ParsedCmdArgs(
                true, null, Optional.empty(), false,
                Optional.empty(), Optional.empty());
        assertEquals(expected, parsed);
    }

    @Test
    void testParsedCmdArgsShowVideo() throws ParseException {
        String[] args = "-i test.txt -v".split(" ");
        GuitarTabGenerator.ParsedCmdArgs parsed = new GuitarTabGenerator.ParsedCmdArgs(args);

        GuitarTabGenerator.ParsedCmdArgs expected = new GuitarTabGenerator.ParsedCmdArgs(
                false, Optional.of("test.txt"), Optional.empty(), true,
                Optional.empty(), Optional.empty());
        assertEquals(expected, parsed);
    }

    @Test
    void testParsedCmdArgsBuildTab() throws ParseException {
        String[] args = "-i test.txt -o folder".split(" ");
        GuitarTabGenerator.ParsedCmdArgs parsed = new GuitarTabGenerator.ParsedCmdArgs(args);

        GuitarTabGenerator.ParsedCmdArgs expected = new GuitarTabGenerator.ParsedCmdArgs(
                false, Optional.of("test.txt"), Optional.of("folder"), false,
                Optional.empty(), Optional.empty());
        assertEquals(expected, parsed);
    }

    @Test
    void testParsedCmdArgsBuildTabTransposedUp() throws ParseException {
        String[] args = "-i test.txt -o folder -u C#".split(" ");
        GuitarTabGenerator.ParsedCmdArgs parsed = new GuitarTabGenerator.ParsedCmdArgs(args);

        GuitarTabGenerator.ParsedCmdArgs expected = new GuitarTabGenerator.ParsedCmdArgs(
                false, Optional.of("test.txt"), Optional.of("folder"), false,
                Optional.of("C#"), Optional.of(true));
        assertEquals(expected, parsed);
    }

    @Test
    void testParsedCmdArgsBuildTabTransposedDown() throws ParseException {
        String[] args = "-i test.txt -o folder -d C#".split(" ");
        GuitarTabGenerator.ParsedCmdArgs parsed = new GuitarTabGenerator.ParsedCmdArgs(args);

        GuitarTabGenerator.ParsedCmdArgs expected = new GuitarTabGenerator.ParsedCmdArgs(
                false, Optional.of("test.txt"), Optional.of("folder"), false,
                Optional.of("C#"), Optional.of(false));
        assertEquals(expected, parsed);
    }

    @Test
    void testParsedCmdArgsBuildTabTransposedUpShowVideo() throws ParseException {
        String[] args = "-i test.txt -o folder -u C# -v".split(" ");
        GuitarTabGenerator.ParsedCmdArgs parsed = new GuitarTabGenerator.ParsedCmdArgs(args);

        GuitarTabGenerator.ParsedCmdArgs expected = new GuitarTabGenerator.ParsedCmdArgs(
                false, Optional.of("test.txt"), Optional.of("folder"), true,
                Optional.of("C#"), Optional.of(true));
        assertEquals(expected, parsed);
    }

    @Test
    void testParsedCmdArgsBuildTabTransposedDownShowVideo() throws ParseException {
        String[] args = "-i test.txt -o folder -d C# -v".split(" ");
        GuitarTabGenerator.ParsedCmdArgs parsed = new GuitarTabGenerator.ParsedCmdArgs(args);

        GuitarTabGenerator.ParsedCmdArgs expected = new GuitarTabGenerator.ParsedCmdArgs(
                false, Optional.of("test.txt"), Optional.of("folder"), true,
                Optional.of("C#"), Optional.of(false));
        assertEquals(expected, parsed);
    }

    @Test
    void testParseSheetMusicValidFile() {
        String path = this.getClass().getResource("/SheetMusic/How_great_is_our_God.txt").getFile();
        Optional<SheetMusic> sheetMusic = GuitarTabGenerator.parseSheetMusic(path);
        assertTrue(sheetMusic.isPresent());
    }

    @Test
    void testParseSheetMusicInvalidFile() {
        String path = this.getClass().getResource("/SheetMusic/Invalid_example.txt").getFile();
        Optional<SheetMusic> sheetMusic = GuitarTabGenerator.parseSheetMusic(path);
        assertFalse(sheetMusic.isPresent());
    }
}
