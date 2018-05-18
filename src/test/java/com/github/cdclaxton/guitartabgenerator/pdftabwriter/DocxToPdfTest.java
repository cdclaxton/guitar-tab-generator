package com.github.cdclaxton.guitartabgenerator.pdftabwriter;

import com.github.cdclaxton.guitartabgenerator.music.*;
import com.github.cdclaxton.guitartabgenerator.sheetmusic.SheetMusic;
import com.github.cdclaxton.guitartabgenerator.tabparser.ExtractionException;
import com.github.cdclaxton.guitartabgenerator.tabparser.SheetMusicParser;
import com.github.cdclaxton.guitartabgenerator.tabwriter.TabBuildingException;
import com.github.cdclaxton.guitartabgenerator.tabwriter.TabSheetMusicBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;

public class DocxToPdfTest {

    @Test
    void test() throws InvalidTimingException, InvalidStringException, IOException, InvalidFretNumberException,
            InvalidChordException, InvalidKeyException, ExtractionException, TabBuildingException {

        // Read the sheet music from a file
        String path = this.getClass().getResource("/SheetMusic/How_great_is_our_God.txt").getFile();
        File file = new File(path);
        SheetMusic sheetMusic = SheetMusicParser.parseSheetMusic(file);

        // Build the sheet music tab
        List<String> sheetMusicLines = TabSheetMusicBuilder.buildTabSheetMusic(sheetMusic, 100);

        // Build the docx file
        XWPFDocument doc = DocxWriter.buildDocx(sheetMusicLines, "Consolas", 9);

        // Create the PDF file
        String filePath = Files.createTempFile("build_pdf", ".pdf").toFile().toString();
        DocxToPdf.convertToPdf(doc, filePath);
    }

}
