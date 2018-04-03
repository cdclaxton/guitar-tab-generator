package com.github.cdclaxton.guitartabgenerator.tabwriter;

import com.github.cdclaxton.guitartabgenerator.music.*;
import com.github.cdclaxton.guitartabgenerator.sheetmusic.SheetMusic;
import com.github.cdclaxton.guitartabgenerator.tabparser.ExtractionException;
import com.github.cdclaxton.guitartabgenerator.tabparser.SheetMusicParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TabSheetMusicBuilderTest {

    @Test
    void testBuildTabSheetMusic() throws InvalidTimingException, InvalidStringException,
            IOException, InvalidFretNumberException, InvalidChordException, InvalidKeyException, ExtractionException,
            TabBuildingException {

        // Read the sheet music from a file
        String path = this.getClass().getResource("/SheetMusic/How_great_is_our_God.txt").getFile();
        File file = new File(path);
        SheetMusic sheetMusic = SheetMusicParser.parseSheetMusic(file);

        // Build the sheet music tab
        List<String> sheetMusicLines = TabSheetMusicBuilder.buildTabSheetMusic(sheetMusic, 100);

        //sheetMusicLines.forEach(line -> System.out.println(line));

        // Write to a file
        File outputFile = File.createTempFile("output1", ".txt");
        TabSheetMusicWriter.writeLines(sheetMusicLines, outputFile.getPath());
    }

}