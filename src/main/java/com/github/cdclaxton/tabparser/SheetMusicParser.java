package com.github.cdclaxton.tabparser;

import com.github.cdclaxton.sheetmusic.SheetMusic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SheetMusicParser {

    private static Logger logger = LoggerFactory.getLogger(SheetMusicParser.class);

    private SheetMusicParser() {}

    /**
     * Parse sheet music from a text file.
     *
     * Very simple parser in this MVP.
     *
     * @param file File containing the sheet music.
     * @return Parsed sheet music.
     * @throws IOException
     */
    public static SheetMusic parseSheetMusic(File file) throws IOException, ExtractionException {
        logger.info("Reading sheet music from file: " + file);

        // Read the file into a list of components
        List<ExtractedComponent> extractedComponents = SheetMusicParser.readSheetMusic(file);

        // Turn the components into sheet music
        return componentsToSheetMusic(extractedComponents);
    }

    protected static SheetMusic componentsToSheetMusic(List<ExtractedComponent> components) {
        return null;
    }

    protected static List<ExtractedComponent> readSheetMusic(File file) throws IOException, ExtractionException {
        // Read each of the lines from the sheet music
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line;
        List<ExtractedComponent> extractedComponents = new ArrayList<>();

        while ((line = bufferedReader.readLine()) != null) {
            logger.debug("Processing line: " + line);
            ExtractedComponent component = SheetMusicParser.parseLine(line);
            if (component != null) {
                extractedComponents.add(component);
            }
        }

        return extractedComponents;
    }

    protected static ExtractedComponent parseLine(String line) throws ExtractionException {
        final String trimmedLine = line.trim();
        ExtractedComponent component;

        if (trimmedLine.length() == 0) {
            component = null;
        } else if (SheetMusicParser.isLineHeader(line)) {
            component = SheetMusicParser.extractHeader(line);
        } else if (SheetMusicParser.isLineSectionHeader(line)) {
            component = SheetMusicParser.extractSectionHeader(line);
        } else if (SheetMusicParser.isLineBar(line)) {
            component = SheetMusicParser.extractBar(line);
        } else if (SheetMusicParser.isLineText(line)) {
            component = SheetMusicParser.extractText(line);
        } else {
            throw new ExtractionException("Can't determine type of component to extract: " + line);
        }

        return component;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Header
    // -----------------------------------------------------------------------------------------------------------------

    protected static boolean isLineHeader(String line) {
        return line.contains("=");
    }

    protected static ExtractedHeader extractHeader(String line) throws ExtractionException {
        logger.debug("Extracting header from line: " + line);
        if (!SheetMusicParser.isLineHeader(line)) {
            throw new ExtractionException("Invalid header: " + line);
        }
        final String[] parts = line.split("=");
        return new ExtractedHeader(parts[0].trim(), parts[1].trim());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Section header
    // -----------------------------------------------------------------------------------------------------------------

    protected static boolean isLineSectionHeader(String line) {
        return line.contains("[") && line.contains("]");
    }

    protected static ExtractedSectionHeader extractSectionHeader(String line) throws ExtractionException {
        logger.debug("Extracting section header from line: " + line);
        if (!SheetMusicParser.isLineSectionHeader(line)) {
            throw new ExtractionException("Invalid section header: " + line);
        }

        final String pattern = "\\[(.*)]";
        final Pattern compiledPattern = Pattern.compile(pattern);
        final Matcher matcher = compiledPattern.matcher(line);
        ExtractedSectionHeader sectionHeader;
        if (matcher.find()) {
            sectionHeader = new ExtractedSectionHeader(matcher.group(1));
        } else {
            throw new ExtractionException("Can't extract section name from: " + line);
        }
        return sectionHeader;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Bar
    // -----------------------------------------------------------------------------------------------------------------

    protected static boolean isLineBar(String line) {
        return line.startsWith("(") && line.contains(")");
    }

    protected static ExtractedBar extractBar(String line) throws ExtractionException {
        logger.debug("Extracting bar from line: " + line);
        if (!SheetMusicParser.isLineBar(line)) {
            throw new ExtractionException("Invalid bar: " + line);
        }

        final String pattern = "\\((.*)\\)\\s*(.*)";
        final Pattern compiledPattern = Pattern.compile(pattern);
        final Matcher matcher = compiledPattern.matcher(line);
        ExtractedBar extractedBar;
        if (matcher.find()) {
            extractedBar = new ExtractedBar(matcher.group(1).trim(), matcher.group(2).trim());
        } else {
            throw new ExtractionException("Can't extract bar from: " + line);
        }

        return extractedBar;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Text
    // -----------------------------------------------------------------------------------------------------------------

    protected static boolean isLineText(String line) {
        return line.trim().startsWith(">");
    }

    protected static ExtractedText extractText(String line) throws ExtractionException {
        logger.debug("Extracting text from line: " + line);
        if (!SheetMusicParser.isLineText(line)) {
            throw new ExtractionException("Invalid text: " + line);
        }

        final String pattern = ">\\s*(.*)";
        final Pattern compiledPattern = Pattern.compile(pattern);
        final Matcher matcher = compiledPattern.matcher(line);
        ExtractedText extractedText;
        if (matcher.find()) {
            extractedText = new ExtractedText(matcher.group(1).trim());
        } else {
            throw new ExtractionException("Can't extract section name from: " + line);
        }

        return extractedText;
    }
}
