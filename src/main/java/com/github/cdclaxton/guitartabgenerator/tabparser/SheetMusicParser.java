package com.github.cdclaxton.guitartabgenerator.tabparser;

import com.github.cdclaxton.guitartabgenerator.music.*;
import com.github.cdclaxton.guitartabgenerator.sheetmusic.Header;
import com.github.cdclaxton.guitartabgenerator.sheetmusic.Metadata;
import com.github.cdclaxton.guitartabgenerator.sheetmusic.Section;
import com.github.cdclaxton.guitartabgenerator.sheetmusic.SheetMusic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SheetMusicParser {

    private static final Logger logger = LoggerFactory.getLogger(SheetMusicParser.class);

    private SheetMusicParser() {}

    /**
     * Parse sheet music from a text file.
     *
     * Very simple parser in this MVP.
     *
     * @param file File containing the sheet music.
     * @return Parsed sheet music.
     * @throws IOException Unable to read the file.
     */
    public static SheetMusic parseSheetMusic(final File file)
            throws IOException, ExtractionException, InvalidKeyException, InvalidStringException,
            InvalidFretNumberException, InvalidTimingException, InvalidChordException {

        logger.info("Reading sheet music from file: " + file);

        // Read the file into a list of components
        List<ExtractedComponent> extractedComponents = SheetMusicParser.readSheetMusic(file);

        // Turn the components into sheet music
        return componentsToSheetMusic(extractedComponents);
    }

    /**
     * Convert the extracted components into (parsed) sheet music.
     *
     * @param components List of components.
     * @return Parsed sheet music.
     * @throws InvalidKeyException Musical key is invalid.
     * @throws ExtractionException Unable to perform extraction.
     * @throws InvalidStringException String number is invalid.
     * @throws InvalidFretNumberException Fret number is invalid.
     * @throws InvalidTimingException Invalid timing.
     * @throws InvalidChordException Invalid chord.
     */
    private static SheetMusic componentsToSheetMusic(final List<ExtractedComponent> components)
            throws InvalidKeyException, ExtractionException, InvalidStringException, InvalidFretNumberException,
            InvalidTimingException, InvalidChordException {

        final Header header = SheetMusicParser.componentsToHeader(components);
        final Metadata metadata = SheetMusicParser.componentsToMetadata(components);
        final List<Section> sections = SheetMusicParser.componentsToSections(components, header.getTimeSignature());

        return new SheetMusic(header, metadata, sections);
    }

    /**
     * Parse the extracted components into sections (title, text, bars) of the sheet music.
     *
     * @param components List of extracted components from the sheet music.
     * @param timeSignature Time signature of the music.
     * @return List of sections.
     * @throws ExtractionException Component was probably misidentified.
     * @throws InvalidStringException String number is invalid.
     * @throws InvalidFretNumberException Fret number is invalid.
     * @throws InvalidTimingException Timing of a note is invalid.
     * @throws InvalidChordException Chord is invalid.
     */
    private static List<Section> componentsToSections(final List<ExtractedComponent> components,
                                                      final Bar.TimeSignature timeSignature)
            throws ExtractionException, InvalidStringException, InvalidFretNumberException, InvalidTimingException,
            InvalidChordException {

        final List<Section> sections = new ArrayList<>();
        boolean sectionInProgress = false;

        // The Section class is immutable, so create temporary variables to hold the extracted values
        String name = null;
        List<String> text = new ArrayList<>();
        List<Bar> bars = new ArrayList<>();

        for (ExtractedComponent component : components) {

            // Section headers denote the start of a new section
            if (component instanceof ExtractedSectionHeader) {

                // As there is a section in progress, but a new section has started, add the previous section to list
               if (sectionInProgress) {
                   Section section = new Section(name, text, bars);
                   sections.add(section);

                   // Clear out the temporary variables
                   text = new ArrayList<>();
                   bars = new ArrayList<>();
               }
               sectionInProgress = true;

               ExtractedSectionHeader sectionHeader = (ExtractedSectionHeader) component;
               name = sectionHeader.getName();
            }

            // A section can contain zero or more lines of text
            if (component instanceof ExtractedText) {
                if (!sectionInProgress) {
                    sectionInProgress = true;
                }
                ExtractedText extractedText = (ExtractedText) component;
                text.add(extractedText.getText());
            }

            // A section can contain zero or more bars
            if (component instanceof ExtractedBar) {
                if (!sectionInProgress) {
                    sectionInProgress = true;
                }
                ExtractedBar extractedBar = (ExtractedBar) component;
                Bar bar = extractedBar.toBar(timeSignature);
                bars.add(bar);
            }
        }

        // Check if there is a final section to add
        if (sectionInProgress) {
            Section section = new Section(name, text, bars);
            sections.add(section);
        }

        return sections;
    }

    /**
     * Parse the extracted components of the sheet music into a header.
     *
     * @param components List of extracted components.
     * @return Header.
     * @throws InvalidKeyException Musical key is invalid.
     * @throws ExtractionException Invalid time signature.
     */
    private static Header componentsToHeader(final List<ExtractedComponent> components)
            throws InvalidKeyException, ExtractionException {

        // As Header is immutable, create temporary variables for the member variables
        String title = null;
        String artist = null;
        Key key = null;
        Bar.TimeSignature timeSignature = null;

        // Find the parts of the header
        for (ExtractedComponent component : components) {
            if (component instanceof ExtractedHeader) {
                ExtractedHeader extractedHeader = (ExtractedHeader) component;
                switch (extractedHeader.getKey().toLowerCase()) {
                    case "title":
                        title = extractedHeader.getValue();
                        break;
                    case "artist":
                        artist = extractedHeader.getValue();
                        break;
                    case "key":
                        key = new Key(extractedHeader.getValue());
                        break;
                    case "time.signature":
                        timeSignature = SheetMusicParser.parseTimeSignature(extractedHeader.getValue());
                        break;
                }
            }
        }

        return new Header(title, artist, key, timeSignature);
    }

    /**
     * Parse the string representation of the time signature.
     *
     * @param value Time signature to parse.
     * @return Time signature.
     * @throws ExtractionException Time signature is not one of the recognised ones.
     */
    private static Bar.TimeSignature parseTimeSignature(final String value) throws ExtractionException {
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

    /**
     * Find the components to do with metadata.
     *
     * @param components List of components extracted from the sheet music.
     * @return Metadata.
     */
    private static Metadata componentsToMetadata(final List<ExtractedComponent> components) {
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

    /**
     * Parse the sheet music from a file.
     *
     * @param file File to parse.
     * @return List of extracted components of the sheet music.
     * @throws IOException Unable to read the file.
     * @throws ExtractionException Unable to parse a line from the file.
     */
    private static List<ExtractedComponent> readSheetMusic(final File file) throws IOException, ExtractionException {

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

    /**
     * Parse a line into a 'component' of the sheet music.
     *
     * @param line Line to parse.
     * @return Extracted component.
     * @throws ExtractionException Unable to detemine the type of the line.
     */
    static ExtractedComponent parseLine(final String line) throws ExtractionException {
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

    /**
     * Is the line a header?
     *
     * @param line Line to process.
     * @return True if the line is a header.
     */
    private static boolean isLineHeader(final String line) {
        return line.contains("=");
    }

    /**
     * Extract header from a line.
     *
     * @param line Line to process.
     * @return Extracted header.
     * @throws ExtractionException Header isn't present on the line.
     */
    static ExtractedHeader extractHeader(final String line) throws ExtractionException {
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

    /**
     * Does the line represent a section header?
     *
     * @param line Line to check.
     * @return True if the line is a section header.
     */
    private static boolean isLineSectionHeader(final String line) {
        return line.contains("[") && line.contains("]");
    }

    /**
     * Extract a section header from a line (from a file).
     *
     * @param line Line to process.
     * @return Section header.
     * @throws ExtractionException Section header can't be extracted from the line.
     */
    static ExtractedSectionHeader extractSectionHeader(final String line) throws ExtractionException {
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

    /**
     * Does the line represent a bar of music?
     *
     * @param line Line to check.
     * @return True if the line is a bar of music.
     */
    private static boolean isLineBar(final String line) {
        return line.startsWith("(") && line.contains(")");
    }

    /**
     * Extract the bar from a line of text (from a file).
     *
     * @param line Line to process.
     * @return Extracted bar of music.
     * @throws ExtractionException A valid bar isn't present on the line.
     */
    static ExtractedBar extractBar(final String line) throws ExtractionException {
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

    /**
     * Is the line (from a file) text?
     *
     * @param line Line to check.
     * @return True if the line is text.
     */
    private static boolean isLineText(final String line) {
        return line.trim().startsWith(">");
    }

    /**
     * Extract the text from a line from a specification file.
     *
     * @param line Line to process.
     * @return Extracted text.
     * @throws ExtractionException Line doesn't contain text.
     */
    static ExtractedText extractText(final String line) throws ExtractionException {
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
