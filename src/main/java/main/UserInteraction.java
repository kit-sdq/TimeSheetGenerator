package main;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

import i18n.ResourceHandler;

/**
 * Represents the user interaction
 */
public abstract class UserInteraction {

    /**
     * Return if the user interaction is a request to print information
     * @return if the user interaction is a request to print information
     */
    public abstract boolean isPrintRequest();

    /**
     * Return if the GUI needs to be shown to handle the request
     * @return if the GUI needs to be shown to handle the request
     */
    public abstract boolean isGui();

    /**
     * Print the requested information.
     * 
     * This function is only defined if {@link UserInteraction#isPrintRequest()} returns true.
     */
    public void print() {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the user input.
     * 
     * This function is only defined if {@link UserInteraction#isPrintRequest()} returns false.
     * 
     * @param inputType type of user input
     * @return user input
     * @throws IOException throws IOException
     */
    public String getInput(UserInputType inputType) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Set the output/result of the program.
     * 
     * This function is only defined if {@link UserInteraction#isPrintRequest()} returns false.
     * 
     * @param output output/result
     * @throws IOException throws IOException
     */
    public void setOutput(String output) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Parse the command line arguments
     * 
     * @return The {@link UserInteraction} representing the user interaction with the program
     * @throws ParseException Thrown in case the command line arguments are invalid
     */
    public static UserInteraction parse(final String[] args) throws ParseException {
        CommandLineParser dp = new DefaultParser();
        CommandLine commandLine = dp.parse(CommandLineOption.getOptions(), args, false);

        // show the command line help
        if (commandLine.hasOption(CommandLineOption.HELP.getOption().getOpt())) {
            return new UserInteractionHelp();
        }
        // show version information
        if (commandLine.hasOption(CommandLineOption.VERSION.getOption().getOpt())) {
            return new UserInteractionVersion();
        }

        // "gui", "file" and "json" options are mutually exclusive
        int conflictingArguments = Arrays.stream(new Boolean[] {
            commandLine.hasOption(CommandLineOption.GUI.getOption().getOpt()),
            commandLine.hasOption(CommandLineOption.FILE.getOption().getOpt()),
            commandLine.hasOption(CommandLineOption.JSON.getOption().getOpt())
        }).mapToInt(b -> b ? 1 : 0).sum();

        if (conflictingArguments > 1) {
            throw new ParseException(ResourceHandler.getMessage("error.userinput.mutuallyExclusiveOptions"));
        }

        // create the correct user interaction instance
        if (commandLine.hasOption(CommandLineOption.GUI.getOption().getOpt())) {
            return new UserInteractionGui();
        } else if (commandLine.hasOption(CommandLineOption.FILE.getOption().getOpt())) {
            String[] fileArgs = commandLine.getOptionValues(CommandLineOption.FILE.getOption().getOpt());
            assert fileArgs.length == CommandLineOption.FILE.getOption().getArgs(); // == 3

            return new UserInteractionFile(fileArgs[0], fileArgs[1], fileArgs[2]);
        } else if (commandLine.hasOption(CommandLineOption.JSON.getOption().getOpt())) {
            String[] jsonArgs = commandLine.getOptionValues(CommandLineOption.JSON.getOption().getOpt());
            assert jsonArgs.length == CommandLineOption.JSON.getOption().getArgs(); // == 2

            return new UserInteractionJson(jsonArgs[0], jsonArgs[1]);
        } else {
            throw new ParseException(ResourceHandler.getMessage("error.userinput.noRequestDetected"));
        }
    }

    /**
     * Represents a type of input file that is specified by the user
     */
    public enum UserInputType {
        GLOBAL,
        MONTH;
    }

}
