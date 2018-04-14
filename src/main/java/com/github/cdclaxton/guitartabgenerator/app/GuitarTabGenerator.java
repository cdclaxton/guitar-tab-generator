package com.github.cdclaxton.guitartabgenerator.app;

import com.github.cdclaxton.guitartabgenerator.music.*;
import com.github.cdclaxton.guitartabgenerator.sheetmusic.SheetMusic;
import com.github.cdclaxton.guitartabgenerator.tabparser.ExtractionException;
import com.github.cdclaxton.guitartabgenerator.tabparser.SheetMusicParser;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
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

        /**
         * Construct a ParsedCmdArgs object.
         *
         * @param help Show help?
         * @param inputFile Input (specification) file.
         * @param outputFolder Output folder (where the tab will be written).
         * @param video Show a video?
         * @param transposeKey Transpose to musical key.
         * @param transposeUp Transpose up?
         */
        ParsedCmdArgs(boolean help, Optional<String> inputFile, Optional<String> outputFolder, boolean video,
                      Optional<String> transposeKey, Optional<Boolean> transposeUp) {
            this.help = help;
            this.inputFile = inputFile;
            this.outputFolder = outputFolder;
            this.video = video;
            this.transposeKey = transposeKey;
            this.transposeUp = transposeUp;
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
        public boolean generateTab() { return this.outputFolder.isPresent(); }

        /**
         * Transpose the tab?
         *
         * @return True if the tab should be transposed.
         */
        public boolean transposeTab() { return this.transposeKey.isPresent(); }

        /**
         * Should the tool's help be shown?
         *
         * @return True if the help should be shown.
         */
        public boolean showHelp() { return help; }

        /**
         * Should the video be shown (if it is available)?
         *
         * @return True if the video should be shown.
         */
        public boolean showVideo() { return this.video; }

        /**
         * Get the file path of the specification file.
         *
         * @return Specification file path.
         */
        public Optional<String> getInputFile() { return inputFile; }

        /**
         * Get the location where the tab should be written to.
         *
         * @return Output location (if provided by on the CLI).
         */
        public Optional<String> getOutputFolder() { return this.outputFolder; }

        /**
         * Get the key to transpose the music to.
         *
         * @return Musical key (if present).
         */
        public Optional<String> getTransposeKey() { return this.transposeKey; }

        /**
         * Transpose up?
         *
         * @return True if the tab should be transposed up.
         */
        public Optional<Boolean> getTransposeUp() { return this.transposeUp; }

        @Override
        public String toString() {
            return "ParsedCmdArgs[" +
                    "help=" + this.help + ","  +
                    "input=" + this.inputFile + "," +
                    "output=" + this.outputFolder + "," +
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
                    Objects.equals(transposeUp, that.transposeUp);
        }

        @Override
        public int hashCode() {
            return Objects.hash(help, inputFile, outputFolder, video, transposeKey, transposeUp);
        }
    }

    /**
     * Main function for the guitar tab generator app.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {

        // Parse the command line arguments
        ParsedCmdArgs cmdLine = null;
        try {
            cmdLine = new ParsedCmdArgs(args);
        } catch (ParseException e) {
            logger.error("Failed to parse command line arguments");
            e.printStackTrace();
            System.exit(-1);
        }

        // Read the config.properties file
        Config config;
        try {
            config = new Config("config.properties");
            logger.info("Config.properties read - " + config.toString());
        } catch (IOException e) {
            logger.error("Invalid config.properties file");
            logger.error(e.getMessage());
            System.exit(-1);
        }

        // Just show the help?
        if (cmdLine.showHelp()) showHelpAndExit();

        // Read the sheet music
        logger.info("Reading specification from file: " + cmdLine.inputFile.get());
        Optional<SheetMusic> sheetMusic = parseSheetMusic(cmdLine.inputFile.get());
        if (!sheetMusic.isPresent()) {
            logger.error("Aborting due to input specification failure");
            System.exit(-1);
        }
        logger.info("Sheet music read: " + sheetMusic.get().getHeader().getTitle() +
                " - " + sheetMusic.get().getHeader().getArtist() +
                        " [" + sheetMusic.get().getHeader().getKey().getKey() + "]");

        // Generate guitar tab?
        if (cmdLine.generateTab()) {
            if (cmdLine.transposeKey.isPresent()) {
                // Generate transposed tab
                logger.info("Transposing to key: " + cmdLine.transposeKey.get());
            } else {
                // Generate tab in the same key as the specification file
                logger.info("No transposition");
            }
        }

        // Show a video?
        if (cmdLine.showVideo()) {
            if (cmdLine.transposeKey.isPresent()) {
                // Show video in required key
                logger.info("Show video in key: " + cmdLine.transposeKey.get());
            } else {
                // Show video in usual key
                logger.info("Show video in normal key");
            }
        }

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
