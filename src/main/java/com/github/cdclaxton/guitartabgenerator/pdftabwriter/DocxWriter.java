package com.github.cdclaxton.guitartabgenerator.pdftabwriter;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public class DocxWriter {

    /**
     * Build a docx file (all lines in the same font and size).
     *
     * @param lines Lines to write to the file.
     * @param fontFamily Font family.
     * @param fontSize Font size.
     * @return Apache POI docx file.
     */
    public static XWPFDocument buildDocx(final List<String> lines,
                                         final String fontFamily,
                                         final int fontSize) {

        // Create a blank Word document
        XWPFDocument document = new XWPFDocument();

        // Create styles
        XWPFStyles styles = document.createStyles();
        //styles.setSpellingLanguage("English");

        // Set the page margins
        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        CTPageMar pageMar = sectPr.addNewPgMar();
        Long oneCentimetre = 567L;
        Long spacing = oneCentimetre / 2;
        pageMar.setLeft(BigInteger.valueOf(spacing));
        pageMar.setTop(BigInteger.valueOf(spacing));
        pageMar.setRight(BigInteger.valueOf(spacing));
        pageMar.setBottom(BigInteger.valueOf(spacing));

        // Set the page size
        if(!sectPr.isSetPgSz()) {
            sectPr.addNewPgSz();
        }
        CTPageSz pageSize = sectPr.getPgSz();
        pageSize.setW(BigInteger.valueOf(11900));
        pageSize.setH(BigInteger.valueOf(16840));

        // Add each line to the document
        for (String line : lines) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setFontSize(fontSize);
            run.setFontFamily(fontFamily);
            run.setText(line);
        }

        return document;
    }

    /**
     * Write the lines to a docx file.
     *
     * @param lines Lines to write.
     * @param filePath Path of the docx file to write.
     * @param fontFamily Font family, e.g. Consolas.
     * @param fontSize Font size, e.g. 9.
     * @throws IOException Unable to write the file.
     */
    public static void buildAndWriteDocx(List<String> lines,
                                         String filePath,
                                         String fontFamily,
                                         int fontSize) throws IOException {

        // Build the docx file
        XWPFDocument document = buildDocx(lines, fontFamily, fontSize);

        // Write the document
        FileOutputStream out = new FileOutputStream(new File(filePath));
        document.write(out);
        out.close();
    }

}
