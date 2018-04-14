package com.github.cdclaxton.guitartabgenerator.sheetmusic;

import com.github.cdclaxton.guitartabgenerator.music.*;
import com.github.cdclaxton.guitartabgenerator.tabparser.ExtractionException;
import com.github.cdclaxton.guitartabgenerator.tabparser.SheetMusicParser;
import com.github.cdclaxton.guitartabgenerator.tabwriter.TabBuildingException;
import com.github.cdclaxton.guitartabgenerator.tabwriter.TabSheetMusicBuilder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SheetMusicTranspositionTest {

    @Test
    void transpose() throws InvalidTimingException, InvalidStringException, IOException,
            InvalidFretNumberException, InvalidChordException, InvalidKeyException, ExtractionException,
            TranspositionException, TabBuildingException {

        // Read the sheet music from file
        String path = this.getClass().getResource("/SheetMusic/How_great_is_our_God.txt").getFile();
        File file = new File(path);
        SheetMusic sheetMusic = SheetMusicParser.parseSheetMusic(file);
        assertEquals("Db", sheetMusic.getHeader().getKey().getKey());

        // Transpose the sheet music
        SheetMusic transposed = SheetMusicTransposition.transpose(sheetMusic, "E", true, 22);

        // Check the old and new keys
        //assertEquals("Db", sheetMusic.getHeader().getKey().getKey());
        //assertEquals("E", transposed.getHeader().getKey().getKey());

        List<String> x = TabSheetMusicBuilder.buildTabSheetMusic(transposed, 80);
        for (String line : x) {
            System.out.println(line);
        }
    }
}