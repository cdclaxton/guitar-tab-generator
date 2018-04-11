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
        Option transpose = Option.builder("t")
                .longOpt("transpose")
                .hasArg()
                .argName("key")
                .desc("transpose music to a different key")
                .build();
        options.addOption(transpose);

        // Open video
        Option video = Option.builder("v")
                .longOpt("video")
                .hasArg()
                .argName("url")
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

    public static void generateTab(String inputFile, String transposeKey, boolean transposeUp) {

        // Read the input file (if it exists)


        // Transpose the music

        // Write the tab to a file

    }

}
