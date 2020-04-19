package main;

import i18n.ResourceHandler;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * Represents the input option the user has on the command line
 */
public enum UserInputOption {

    /**
     * Print the command line help
     */
    HELP(Option.builder("h")
        .longOpt("help")
        .desc(ResourceHandler.getMessage("command.input.help.description"))
        .hasArg(false)
        .build()),
    /**
     * Print the version of the application
     */
    VERSION(Option.builder("v")
        .longOpt("version")
        .desc(ResourceHandler.getMessage("command.input.version.description"))
        .hasArg(false)
        .build()),
    /**
     * Show the GUI for choosing the files
     */
    GUI(Option.builder("g")
        .longOpt("gui")
        .desc(ResourceHandler.getMessage("command.input.gui.description"))
        .hasArg(false)
        .build()),
    /**
     * Specify the files in the arguments of this command
     */
    FILE(Option.builder("f")
        .longOpt("file")
        .desc(ResourceHandler.getMessage("command.input.file.description"))
        .numberOfArgs(3)
        .argName(ResourceHandler.getMessage("command.input.file.arguments"))
        .build());

    private final Option option;

    /**
     * Create a user input option
     * @param option Apache CLI option for the user input option
     */
    private UserInputOption(Option option) {
        this.option = option;
    }

    /**
     * Get the Apache CLI option of this user input option
     * @return Apache CLI option
     */
    public Option getOption() {
        return this.option;
    }

    /**
     * Get the Apache CLI options
     * @return Apache CLI options
     */
    public static Options getOptions() {
        Options options = new Options();
        for (UserInputOption uio : UserInputOption.values()) {
            options.addOption(uio.getOption());
        }
        return options;
    }

}
