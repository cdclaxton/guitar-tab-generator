package com.github.cdclaxton.guitartabgenerator.pdftabwriter;

import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DocxToPdf {

    /**
     * Convert a docx file to PDF.
     *
     * @param document Docx file.
     * @param filePath Location where the PDF will be written.
     * @throws IOException Unable to open or write PDF file.
     */
    public static void convertToPdf(final XWPFDocument document,
                                    final String filePath) throws IOException {

        PdfOptions options = PdfOptions.create();
        OutputStream out = new FileOutputStream(new File(filePath));
        PdfConverter.getInstance().convert(document, out, options);
    }

}
