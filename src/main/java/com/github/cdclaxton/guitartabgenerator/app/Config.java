package com.github.cdclaxton.guitartabgenerator.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public final class Config {

    private final int pageWidth;
    private final int maxFret;

    private final String docxFontFamily;
    private final int docxFontSize;
    private final int docxPageWidth;

    /**
     * Instantiate the config a config.properties file.
     *
     * @param path Path of the config file.
     * @throws IOException Unable to read the config file.
     */
    Config(String path) throws IOException {
        Properties prop = new Properties();
        InputStream input = new FileInputStream(path);

        // Read the properties file
        prop.load(input);

        // Extract the values
        this.pageWidth = Integer.valueOf(prop.getProperty("page.width"));
        this.maxFret = Integer.valueOf(prop.getProperty("max.fret"));
        this.docxFontFamily = prop.getProperty("docx.font.family");
        this.docxFontSize = Integer.valueOf(prop.getProperty("docx.font.size"));
        this.docxPageWidth = Integer.valueOf(prop.getProperty("docx.page.width"));
    }

    /**
     * Get the page width (in characters).
     *
     * @return Page width.
     */
    int getPageWidth() { return pageWidth; }

    /**
     * Get the maximum fret number.
     *
     * @return Maximum fret number.
     */
    int getMaxFret() { return maxFret; }

    /**
     * Font family to use in the docx file.
     *
     * @return Font family.
     */
    public String getDocxFontFamily() {
        return docxFontFamily;
    }

    /**
     * Font size to use in the docx file.
     *
     * @return Font size.
     */
    public int getDocxFontSize() {
        return docxFontSize;
    }

    /**
     * Number of characters across a docx page.
     *
     * @return Page width in characters.
     */
    public int getDocxPageWidth() {
        return docxPageWidth;
    }

    @Override
    public String toString() {
        return "Config[pageWidth=" + this.pageWidth +
                ",maxFret=" + this.maxFret +
                ",docxFontFamily=" + this.docxFontFamily +
                ",docxFontSize=" + this.docxFontSize +
                ",docxPageWidth" + this.docxPageWidth + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Config config = (Config) o;
        return pageWidth == config.pageWidth &&
                maxFret == config.maxFret &&
                docxFontSize == config.docxFontSize &&
                docxPageWidth == config.docxPageWidth &&
                Objects.equals(docxFontFamily, config.docxFontFamily);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageWidth, maxFret, docxFontFamily, docxFontSize, docxPageWidth);
    }
}
