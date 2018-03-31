package com.github.cdclaxton.tabparser;

import com.github.cdclaxton.music.*;
import com.github.cdclaxton.sheetmusic.Header;
import com.github.cdclaxton.sheetmusic.Metadata;
import com.github.cdclaxton.sheetmusic.Section;
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
    public static SheetMusic parseSheetMusic(File file)
            throws IOException, ExtractionException, InvalidKeyException, InvalidStringException, InvalidFretNumberException, InvalidTimingException, InvalidChordException {
        logger.info("Reading sheet music from file: " + file);

        // Read the file into a list of components
        List<ExtractedComponent> extractedComponents = SheetMusicParser.readSheetMusic(file);

        // Turn the components into sheet music
        return componentsToSheetMusic(extractedComponents);
    }

    protected static SheetMusic componentsToSheetMusic(List<ExtractedComponent> components)
            throws InvalidKeyException, ExtractionException, InvalidStringException, InvalidFretNumberException, InvalidTimingException, InvalidChordException {

        Header header = SheetMusicParser.componentsToHeader(components);
        Metadata metadata = SheetMusicParser.componentsToMetadata(components);
        List<Section> sections = SheetMusicParser.componentsToSections(components, header.getTimeSignature());

        return new SheetMusic(header, metadata, sections);
    }

    protected static List<Section> componentsToSections(List<ExtractedComponent> components, Bar.TimeSignature timeSignature)
            throws ExtractionException, InvalidStringException, InvalidFretNumberException, InvalidTimingException, InvalidChordException {
        final List<Section> sections = new ArrayList<>();

        boolean sectionInProgress = false;
        Section section = null;

        for (ExtractedComponent component : components) {

            // Section headers denote the start of a new section
            if (component instanceof ExtractedSectionHeader) {
               if (sectionInProgress) {
                   sections.add(section);
               }
               section = new Section();
               sectionInProgress = true;

               ExtractedSectionHeader sectionHeader = (ExtractedSectionHeader) component;
               section.setName(sectionHeader.getName());

            }

            // A section can contain zero or more lines of text
            if (component instanceof ExtractedText) {
                if (!sectionInProgress) {
                    section = new Section();
                    sectionInProgress = true;
                }
                ExtractedText text = (ExtractedText) component;
                section.addText(text.getText());
            }

            // A section can contain zero or more bars
            if (component instanceof ExtractedBar) {
                if (!sectionInProgress) {
                    section = new Section();
                    sectionInProgress = true;
                }
                ExtractedBar extractedBar = (ExtractedBar) component;
                Bar bar = extractedBar.toBar(timeSignature);
                section.addBar(bar);
            }
        }

        // Check if there is a final section to add
        if (sectionInProgress) sections.add(section);

        return sections;
    }

    protected static Header componentsToHeader(List<ExtractedComponent> components) throws InvalidKeyException, ExtractionException {
        final Header header = new Header();

        for (ExtractedComponent component : components) {
            if (component instanceof ExtractedHeader) {
                ExtractedHeader extractedHeader = (ExtractedHeader) component;
                switch (extractedHeader.getKey().toLowerCase()) {
                    case "title":
                        header.setTitle(extractedHeader.getValue());
                        break;
                    case "artist":
                        header.setArtist(extractedHeader.getValue());
                        break;
                    case "key":
                        Key key = new Key(extractedHeader.getValue());
                        header.setKey(key);
                        break;
                    case "time.signature":
                        Bar.TimeSignature timeSignature = SheetMusicParser.parseTimeSignature(extractedHeader.getValue());
                        header.setTimeSignature(timeSignature);
                        break;
                }
            }
        }

        return header;
    }

    protected static Bar.TimeSignature parseTimeSignature(String value) throws ExtractionException {
        final String trimmedValue = value.trim();
        switch (trimmedValue) {
            case "4/4":
                return Bar.TimeSignature.Four4;
            case "6/8":
                return Bar.TimeSignature.Six8;
            default:
                throw new ExtractionException("Don't recognise time signature: " + value);
        }
    }

    protected static Metadata componentsToMetadata(List<ExtractedComponent> components) {
        final Metadata metadata = new Metadata();

        for (ExtractedComponent component : components) {
            if (component instanceof ExtractedHeader) {
                ExtractedHeader extractedHeader = (ExtractedHeader) component;
                if (extractedHeader.getKey().startsWith("metadata")) {
                    metadata.addMetadata(extractedHeader.getKey(), extractedHeader.getValue());
                }
            }
        }

        return metadata;
    }

    protected static List<ExtractedComponent> readSheetMusic(File file) throws IOException, ExtractionException {
        // Read each of the lines from the sheet music
        final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line;
        final List<ExtractedComponent> extractedComponents = new ArrayList<>();

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
