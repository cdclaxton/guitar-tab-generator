package com.github.cdclaxton.guitartabgenerator.app;

import com.github.cdclaxton.guitartabgenerator.music.*;
import com.github.cdclaxton.guitartabgenerator.pdftabwriter.DocxToPdf;
import com.github.cdclaxton.guitartabgenerator.pdftabwriter.DocxWriter;
import com.github.cdclaxton.guitartabgenerator.sheetmusic.SheetMusic;
import com.github.cdclaxton.guitartabgenerator.sheetmusic.SheetMusicTransposition;
import com.github.cdclaxton.guitartabgenerator.tabparser.ExtractionException;
import com.github.cdclaxton.guitartabgenerator.tabparser.SheetMusicParser;
import com.github.cdclaxton.guitartabgenerator.tabwriter.TabBuildingException;
import com.github.cdclaxton.guitartabgenerator.tabwriter.TabSheetMusicBuilder;
import com.github.cdclaxton.guitartabgenerator.tabwriter.TabSheetMusicWriter;
import org.apache.commons.cli.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class GuitarTabGenerator {

    static final Options options = buildOptions();
    private static final CommandLineParser parser = new DefaultParser();
    private static final String cmdLineName = "guitartabgenerator";

    private static Logger logger = LoggerFactory.getLogger(GuitarTabGenerator.class);

    static class ParsedCmdArgs {

        private boolean help;
        private Optional<String> inputFile;
        private Optional<String> outputFolder = Optional.empty();
        private boolean video;
        private Optional<String> transposeKey;
        private Optional<Boolean> transposeUp;
        private Optional<String> format;

        /**
         * Construct a ParsedCmdArgs object.
         *
         * @param help Show help?
         * @param inputFile Input (specification) file.
         * @param outputFolder Output folder (where the tab will be written).
         * @param video Show a video?
         * @param transposeKey Transpose to musical key.
         * @param transposeUp Transpose up?
         * @param format Output format.
         */
        ParsedCmdArgs(final boolean help,
                      final Optional<String> inputFile,
                      final Optional<String> outputFolder,
                      final boolean video,
                      final Optional<String> transposeKey,
                      final Optional<Boolean> transposeUp,
                      final Optional<String> format) {
            this.help = help;
            this.inputFile = inputFile;
            this.outputFolder = outputFolder;
            this.video = video;
            this.transposeKey = transposeKey;
            this.transposeUp = transposeUp;
            this.format = format;
        }

        /**
         * Construct a ParsedCmdArgs from command line arguments.
         *
         * @param args Command line arguments.
         * @throws ParseException
         */
        ParsedCmdArgs(String[] args) throws ParseException {

            // Parse the command line arguments
            CommandLine cmd = parser.parse(options, args);

            // Help
            this.help = cmd.hasOption("help");

            // Input (specification) file -- required if help is not requested
            if (!cmd.hasOption("input") && !this.help) throw new IllegalArgumentException("Input file required");
            if (cmd.hasOption("input")) this.inputFile = Optional.of(cmd.getOptionValue("input"));

            // Output folder
            if (cmd.hasOption("output")) this.outputFolder = Optional.of(cmd.getOptionValue("output"));

            // Output format
            if (cmd.hasOption("format")) this.format = Optional.of(cmd.getOptionValue("format"));
            else this.format = Optional.empty();

            // Check that both transpose up and transpose down haven't been defined
            if (cmd.hasOption("up") && cmd.hasOption("down")) throw new IllegalArgumentException("Only one transpose per run");

            // Transpose
            if (cmd.hasOption("up")) {
                this.transposeKey = Optional.of(cmd.getOptionValue("up"));
                this.transposeUp = Optional.of(true);
            } else if (cmd.hasOption("down")) {
                this.transposeKey = Optional.of(cmd.getOptionValue("down"));
                this.transposeUp = Optional.of(false);
            } else {
                this.transposeKey = Optional.empty();
                this.transposeUp = Optional.empty();
            }

            // Video
            this.video = cmd.hasOption("video");
        }

        /**
         * Should tab be generated?
         *
         * @return True if tab should be generated.
         */
        boolean generateTab() { return this.outputFolder.isPresent(); }

        /**
         * Transpose the tab?
         *
         * @return True if the tab should be transposed.
         */
        boolean transposeTab() { return this.transposeKey.isPresent(); }

        /**
         * Should the tool's help be shown?
         *
         * @return True if the help should be shown.
         */
        boolean showHelp() { return help; }

        /**
         * Should the video be shown (if it is available)?
         *
         * @return True if the video should be shown.
         */
        boolean showVideo() { return this.video; }

        /**
         * Get the file path of the specification file.
         *
         * @return Specification file path.
         */
        Optional<String> getInputFile() { return inputFile; }

        /**
         * Get the location where the tab should be written to.
         *
         * @return Output location (if provided by on the CLI).
         */
        Optional<String> getOutputFolder() { return this.outputFolder; }

        /**
         * Get the key to transpose the music to.
         *
         * @return Musical key (if present).
         */
        Optional<String> getTransposeKey() { return this.transposeKey; }

        /**
         * Transpose up?
         *
         * @return True if the tab should be transposed up.
         */
        Optional<Boolean> getTransposeUp() { return this.transposeUp; }

        /**
         * Output format.
         *
         * @return Output format.
         */
        public Optional<String> getFormat() {
            return format;
        }

        @Override
        public String toString() {
            return "ParsedCmdArgs[" +
                    "help=" + this.help + ","  +
                    "input=" + this.inputFile + "," +
                    "output=" + this.outputFolder + "," +
                    "format=" + this.format + "," +
                    "transpose=" + this.transposeKey + "," +
                    "video=" + this.video + "]";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ParsedCmdArgs that = (ParsedCmdArgs) o;
            return help == that.help &&
                    video == that.video &&
                    Objects.equals(inputFile, that.inputFile) &&
                    Objects.equals(outputFolder, that.outputFolder) &&
                    Objects.equals(transposeKey, that.transposeKey) &&
                    Objects.equals(transposeUp, that.transposeUp) &&
                    Objects.equals(format, that.format);
        }

        @Override
        public int hashCode() {
            return Objects.hash(help, inputFile, outputFolder, video, transposeKey, transposeUp, format);
        }
    }

    /**
     * Main function for the guitar tab generator app.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {

        // Parse the command line arguments
        ParsedCmdArgs cmdLine = parseCommandLineArgs(args);

        // Read the config.properties file
        Config config = readConfig();

        // Just show the help?
        if (cmdLine.showHelp()) showHelpAndExit();

        // Read the sheet music
        if (!cmdLine.inputFile.isPresent()) {
            throw new IllegalStateException("No sheet music to process. Can't continue.");
        }
        SheetMusic sheetMusic = readSheetMusic(cmdLine.inputFile.get());

        // Generate guitar tab?
        if (cmdLine.generateTab()) {

            // Transpose the sheet music (if required)
            SheetMusic sheetMusicInRequiredKey = transposeSheetMusic(sheetMusic,
                    cmdLine.transposeKey, cmdLine.transposeUp, config.getMaxFret());

            // Determine the output format
            String format = config.getDefaultFormat();
            if (cmdLine.getFormat().isPresent()) {
                format = cmdLine.getFormat().get();
            }

            // Determine the page width
            int pageWidth = 0;
            switch (format) {
                case "txt":
                    pageWidth = config.getPageWidth();
                    break;
                case "docx":
                    pageWidth = config.getDocxPageWidth();
                    break;
                case "pdf":
                    pageWidth = config.getDocxPageWidth();
                    break;
                default:
                    logger.error("Unknown output format: " + format);
                    System.exit(-1);
            }

            // Write the sheet music
            writeSheetMusic(sheetMusicInRequiredKey,
                    format,
                    cmdLine.getOutputFolder().get(),
                    pageWidth,
                    config.getDocxFontFamily(),
                    config.getDocxFontSize());
        }

        // Show a video?
        if (cmdLine.showVideo()) showVideo(sheetMusic, cmdLine.transposeKey);

    }

    private static SheetMusic transposeSheetMusic(final SheetMusic sheetMusic,
                                                  Optional<String> transposeKey,
                                                  Optional<Boolean> transposeUp,
                                                  int maxFret) {

        SheetMusic sheetMusicInRequiredKey = null;

        if (transposeKey.isPresent() && transposeUp.isPresent()) {
            // Generate transposed tab
            logger.info("Transposing to key: " + transposeKey.get());
            try {
                sheetMusicInRequiredKey = SheetMusicTransposition.transpose(sheetMusic,
                        transposeKey.get(), transposeUp.get(), maxFret);
            } catch (InvalidKeyException e) {
                logger.error("Invalid key: " + e.getMessage());
            } catch (InvalidChordException e) {
                logger.error("Invalid chord: " + e.getMessage());
            } catch (TranspositionException e) {
                logger.error("Unable to transpose: " + e.getMessage());
            }
        } else {
            // Generate tab in the same key as the specification file
            logger.info("No transposition");
            sheetMusicInRequiredKey = sheetMusic;
        }

        return sheetMusicInRequiredKey;
    }

    private static void showVideo(final SheetMusic sheetMusic,
                                  final Optional<String> transposeKey) {

        Optional<String> url;

        if (transposeKey.isPresent()) {
            // Show video in required key
            logger.info("Show video in key: " + transposeKey.get());
            url = sheetMusic.getMetadata().findUrl(transposeKey.get());
        } else {
            // Show video in usual key
            logger.info("Show video in normal key");
            url = sheetMusic.getMetadata().findUrl();
        }

        logger.info("URL is: " + url);
        url.ifPresent(WebBrowserLauncher::launch);
    }

    private static ParsedCmdArgs parseCommandLineArgs(final String[] args) {
        ParsedCmdArgs cmdLine = null;
        try {
            cmdLine = new ParsedCmdArgs(args);
        } catch (ParseException e) {
            logger.error("Failed to parse command line arguments");
            System.exit(-1);
        }

        return cmdLine;
    }

    private static Config readConfig() {
        Config config = null;
        try {
            config = new Config("config.properties");
            logger.info("Config.properties read - " + config.toString());
        } catch (IOException e) {
            logger.error("Invalid config.properties file: " + e.getMessage());
            System.exit(-1);
        }
        return config;
    }

    private static SheetMusic readSheetMusic(final String filePath) {
        logger.info("Reading specification from file: " + filePath);
        final Optional<SheetMusic> sheetMusic = parseSheetMusic(filePath);
        if (!sheetMusic.isPresent()) {
            logger.error("Aborting due to input specification failure");
            System.exit(-1);
        }
        logger.info("Sheet music read: " + sheetMusic.get().getHeader().getTitle() +
                " - " + sheetMusic.get().getHeader().getArtist() +
                " [" + sheetMusic.get().getHeader().getKey().getKey() + "]");

        return sheetMusic.get();
    }

    private static void writeSheetMusic(final SheetMusic sheetMusic,
                                        final String outputFormat,
                                        final String outputFolder,
                                        final int pageWidth,
                                        final String docxFontFamily,
                                        final int docxFontSize) {

        // Create the tab
        List<String> tab = null;
        try {
            tab = TabSheetMusicBuilder.buildTabSheetMusic(sheetMusic, pageWidth);
            logger.info("Tab built");
        } catch (TabBuildingException e) {
            logger.error("Unable to build guitar tab: " + e.getMessage());
            System.exit(-1);
        }

        // Build the output filename
        String filePath = buildTabFilename(sheetMusic.getHeader().getTitle(),
                sheetMusic.getHeader().getArtist(),
                sheetMusic.getHeader().getKey().getKey(),
                outputFolder,
                outputFormat);

        // Write to file
        switch (outputFormat) {
            case "docx":
                logger.info("Writing docx file: " + filePath);
                writeDocxFile(tab, filePath, docxFontFamily, docxFontSize);
                break;
            case "pdf":
                logger.info("Writing PDF file: " + filePath);
                writePdfFile(tab, filePath, docxFontFamily, docxFontSize);
                break;
            case "txt":
                logger.info("Writing txt file: " + filePath);
                writeTextFile(tab, filePath);
                break;
            default:
                logger.error("Unknown file type: " + outputFormat);
                break;
        }
    }

    /**
     * Write a PDF file containing the lines.
     *
     * @param lines Data to write.
     * @param filePath Path of the file to write.
     * @param fontFamily Font family to use for all text, e.g. Consolas.
     * @param fontSize Font size, e.g. 9.
     */
    private static void writePdfFile(final List<String> lines,
                                     final String filePath,
                                     final String fontFamily,
                                     final int fontSize) {

        // Build the docx file
        XWPFDocument document = DocxWriter.buildDocx(lines, fontFamily, fontSize);

        // Write to PDF
        try {
            DocxToPdf.convertToPdf(document, filePath);
        } catch (IOException e) {
            logger.error("Unable to write PDF: " + e.getMessage());
        }
    }

    /**
     * Write a docx file containing the lines.
     *
     * @param lines Data to write.
     * @param filePath Path of the file to write.
     * @param fontFamily Font family to use for all text, e.g. Consolas.
     * @param fontSize Font size, e.g. 9.
     */
    private static void writeDocxFile(final List<String> lines,
                                      final String filePath,
                                      final String fontFamily,
                                      final int fontSize) {

        try {
            DocxWriter.buildAndWriteDocx(lines, filePath, fontFamily, fontSize);
        } catch (IOException e) {
            logger.error("Unable to write docx file: " + e.getMessage());
        }
    }

    /**
     * Write a text file containing the lines.
     *
     * @param lines Data to write.
     * @param filePath Path of the file to write.
     */
    private static void writeTextFile(final List<String> lines,
                                      final String filePath) {
        try {
            TabSheetMusicWriter.writeLines(lines, filePath);
        } catch (IOException e) {
            logger.error("Can't write file to: " + filePath);
            logger.error(e.getMessage());
        }
    }

    /**
     * Build the filename and path.
     *
     * @param title Song title.
     * @param artist Song artist.
     * @param key Musical key.
     * @param folder Folder where the tab will be stored.
     * @param extension File extension (without the dot), e.g. txt.
     * @return Filename with path.
     */
    static String buildTabFilename(final String title,
                                   final String artist,
                                   final String key,
                                   final String folder,
                                   final String extension) {

        String fullFolder = folder;
        if (!folder.endsWith("/"))  fullFolder = fullFolder + "/";

        return fullFolder + title + " (" + artist + ") - " + key + "." + extension;
    }

    /**
     * Parse the sheet music (if possible).
     *
     * @param filePath Path of the tab specification file.
     * @return Sheet music (if parsable), otherwise empty.
     */
    static Optional<SheetMusic> parseSheetMusic(String filePath) {
        File inputFile = new File(filePath);
        SheetMusic sheetMusic = null;

        // Try to parse the sheet music from the file
        try {
            sheetMusic = SheetMusicParser.parseSheetMusic(inputFile);
        } catch (IOException e) {
            logger.error("Can't read specification file: " + inputFile);
        } catch (ExtractionException e) {
            logger.error("Extraction exception: " + e.getMessage());
        } catch (InvalidKeyException e) {
            logger.error("Invalid key: " + e.getMessage());
        } catch (InvalidStringException e) {
            logger.error("Invalid string: " + e.getMessage());
        } catch (InvalidFretNumberException e) {
            logger.error("Invalid fret number: " + e.getMessage());
        } catch (InvalidTimingException e) {
            logger.error("Invalid timing: " + e.getMessage());
        } catch (InvalidChordException e) {
            logger.error("Invalid chord: " + e.getMessage());
        }

        // Return the parsed sheet music
        return sheetMusic != null ? Optional.of(sheetMusic) : Optional.empty();
    }

    /**
     * Build the command line interface (CLI) options.
     *
     * @return CLI options.
     */
    private static Options buildOptions() {
        Options options = new Options();

        // Specification (input) file
        Option spec = Option.builder("i")
                .longOpt("input")
                .hasArg()
                .argName("file")
                .desc("input (specification) file")
                .build();
        options.addOption(spec);

        // Tab (output) file
        Option tab = Option.builder("o")
                .longOpt("output")
                .hasArg()
                .argName("folder")
                .desc("folder where the tab will be written to")
                .build();
        options.addOption(tab);

        // Output format
        Option format = Option.builder("f")
                .longOpt("format")
                .hasArg()
                .argName("format")
                .desc("tab format")
                .build();
        options.addOption(format);

        // Transpose
        Option transposeUp = Option.builder("u")
                .longOpt("up")
                .hasArg()
                .argName("key")
                .desc("transpose up to musical key")
                .build();
        options.addOption(transposeUp);

        Option transposeDown = Option.builder("d")
                .longOpt("down")
                .hasArg()
                .argName("key")
                .desc("transpose down to a musical key")
                .build();
        options.addOption(transposeDown);

        // Open video
        Option video = Option.builder("v")
                .longOpt("video")
                .desc("open video in the associated key in a web browser (if available)")
                .build();
        options.addOption(video);

        // Help
        Option help = Option.builder("h")
                .desc("print this message")
                .longOpt("help")
                .hasArg(false)
                .build();
        options.addOption(help);

        return options;
    }

    /**
     * Show the help (for the options) and exit.
     */
    private static void showHelpAndExit() {
        showHelp();
        System.exit(0);
    }

    /**
     * Show the command line help.
     */
    private static void showHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineName, options);
    }

}
