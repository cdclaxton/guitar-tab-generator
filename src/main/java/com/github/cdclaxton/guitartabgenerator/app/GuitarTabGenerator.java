package com.github.cdclaxton.guitartabgenerator.app;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GuitarTabGenerator {

    private static final Options options = buildOptions();
    private static final CommandLineParser parser = new DefaultParser();
    private static final String cmdLineName = "guitartabgenerator";

    private static Logger logger = LoggerFactory.getLogger(GuitarTabGenerator.class);

    /**
     * Main function for the guitar tab generator app.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {

        // Parse the command line arguments
        CommandLine cmd = null;
        try {
            cmd = parser.parse( options, args);
        } catch (ParseException e) {
            logger.error("Can't parse command line arguments");
            e.printStackTrace();
        }

        if (cmd.hasOption("help")) {
            showHelp();
        }

    }

    /**
     * Build the command line interface (CLI) options.
     *
     * @return CLI options.
     */
    private static Options buildOptions() {
        Options options = new Options();

        // Specification file
        Option specification = Option.builder()
                .argName("spec")
                .hasArg()
                .desc("tab specification file")
                .required()
                .build();
        options.addOption(specification);

        // Tab (output) file
        Option tab = Option.builder()
                .argName("tab")
                .hasArg()
                .desc("tab (output) file")
                .build();
        options.addOption(tab);

        // Transpose
        Option transpose = Option.builder()
                .argName("transpose")
                .hasArg()
                .desc("transpose music to a different key")
                .build();
        options.addOption(transpose);

        // Open video
        Option video = new Option("video", "open video in web browser");
        options.addOption(video);

        // Help
        Option help = new Option("help", "print this message");
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

    public static void generateTab(String inputFile, String transposeKey, boolean transposeUp) {

        // Read the input file (if it exists)


        // Transpose the music

        // Write the tab to a file

    }

}
