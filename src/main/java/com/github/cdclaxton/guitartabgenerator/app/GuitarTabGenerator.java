package com.github.cdclaxton.guitartabgenerator.app;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public final class GuitarTabGenerator {

    private static final Options options = buildOptions();
    private static final CommandLineParser parser = new DefaultParser();
    private static final String cmdLineName = "guitartabgenerator";

    private static Logger logger = LoggerFactory.getLogger(GuitarTabGenerator.class);

    static class ParsedCmdArgs {

        private boolean help;
        private String inputFile;
        private Optional<String> outputFolder = Optional.empty();
        private boolean video;
        private Optional<String> transposeKey;
        private Optional<Boolean> transposeUp;

        ParsedCmdArgs(CommandLine cmd) {

            // Help
            this.help = cmd.hasOption("help");

            // Input (specification) file -- required if help is not requested
            if (!cmd.hasOption("input") && !this.help) throw new IllegalArgumentException("Input file required");
            this.inputFile = cmd.getOptionValue("input");

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
        public String getInputFile() { return inputFile; }

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

        @Override
        public String toString() {
            return "ParsedCmdArgs[" +
                    "help=" + this.help + ","  +
                    "input=" + this.inputFile + "," +
                    "output=" + this.outputFolder + "," +
                    "transpose=" + this.transposeKey + "," +
                    "video=" + this.video + "]";
        }
    }

    /**
     * Main function for the guitar tab generator app.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {

        // Parse the command line arguments
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            logger.error("Can't parse command line arguments");
            e.printStackTrace();
        }

        // Secondary parsing stage of the command line arguments
        ParsedCmdArgs cmdLine = new ParsedCmdArgs(cmd);
        System.out.printf(cmdLine.toString());

        // Just show the help?
        if (cmdLine.showHelp()) showHelpAndExit();

        // Generate guitar tab?
        if (cmdLine.generateTab()) {
            if (cmdLine.transposeKey.isPresent()) {
                // Generate transposed tab
            } else {
                // Generate tab in the same key as the specification file
            }
        }

        // Show a video?
        if (cmdLine.showVideo()) {
            if (cmdLine.transposeKey.isPresent()) {
                // Show video in required key
            } else {
                // Show video in usual key
            }
        }

    }

    /**
     * Show the help (for the options) and exit.
     */
    private static void showHelpAndExit() {
        showHelp();
        System.exit(0);
    }

    /**
     * Build the command line interface (CLI) options.
     *
     * @return CLI options.
     */
    static Options buildOptions() {
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
     * Show the command line help.
     */
    private static void showHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineName, options);
    }

    public static void generateTab(String inputFile) {

    }

    public static void generateTab(String inputFile, String transposeKey, boolean transposeUp) {

        // Read the input file (if it exists)


        // Transpose the music

        // Write the tab to a file

    }

}
