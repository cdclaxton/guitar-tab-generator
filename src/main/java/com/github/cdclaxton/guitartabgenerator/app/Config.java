package com.github.cdclaxton.guitartabgenerator.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private int pageWidth;
    private int maxFret;

    /**
     * Instantiate the config a config.properties file.
     *
     * @param path Path of the config file.
     * @throws IOException
     */
    Config(String path) throws IOException {
        Properties prop = new Properties();
        InputStream input = new FileInputStream(path);

        // Read the properties file
        prop.load(input);

        // Extract the values
        this.pageWidth = Integer.valueOf(prop.getProperty("page.width"));
        this.maxFret = Integer.valueOf(prop.getProperty("max.fret"));
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

    @Override
    public String toString() {
        return "Config[pageWidth=" + this.pageWidth + ",maxFret=" + this.maxFret + "]";
    }
}
