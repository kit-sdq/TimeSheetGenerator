package main;

import org.apache.commons.cli.HelpFormatter;

import i18n.ResourceHandler;

/**
 * Represents a user interaction where the command line help is shown.
 */
public class UserInteractionHelp extends UserInteraction {

    @Override
    public boolean isPrintRequest() {
        return true;
    }

    @Override
    public boolean isGui() {
        return false;
    }

    @Override
    public void print() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(ResourceHandler.getMessage("application.name"), CommandLineOption.getOptions());
    }
    
}
