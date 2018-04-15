package com.github.cdclaxton.guitartabgenerator.tabwriter;

import com.github.cdclaxton.guitartabgenerator.tabparser.SheetMusicParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TabSheetMusicWriter {

    private static final Logger logger = LoggerFactory.getLogger(SheetMusicParser.class);

    /**
     * Write a list of lines to a file.
     *
     * @param lines Lines to write.
     * @param filepath Path of the file to write.
     * @throws IOException Unable to write to file.
     */
    public static void writeLines(final List<String> lines,
                           final String filepath) throws IOException {

        logger.info("Writing tab to: " + filepath);

        // Open the file for writing
        BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));

        // Write each of the lines
        for (String line : lines) {
            writer.write(line + "\n");
        }

        // Close the the file
        writer.close();
    }

}
