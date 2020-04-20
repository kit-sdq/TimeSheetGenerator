package main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import javax.swing.JFileChooser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;

import i18n.ResourceHandler;

/**
 * Represents the user input from the command line and the GUI
 */
public class UserInput {

    private CommandLine commandLine;
    private final String[] args;

    private String currentDirectory = null; // caches the last used directory for the file chooser

    /**
     * Create a new UserInput object with the input command line arguments
     * @param args Command line arguments that the application was started with
     */
    public UserInput(String[] args) {
        this.args = args;
    }

    /**
     * Parse the command line arguments
     * @return The user request read from the command line arguments
     * @throws ParseException Thrown in case the command line arguments are invalid
     */
    public Request parse() throws ParseException {
        CommandLineParser dp = new DefaultParser();
        commandLine = dp.parse(UserInputOption.getOptions(), args, false);

        if (commandLine.hasOption(UserInputOption.HELP.getOption().getOpt())) {
            return Request.HELP;
        }
        if (commandLine.hasOption(UserInputOption.VERSION.getOption().getOpt())) {
            return Request.VERSION;
        }

        // "gui" and "file" options are mutually exclusive
        if (commandLine.hasOption(UserInputOption.GUI.getOption().getOpt())
            && commandLine.hasOption(UserInputOption.FILE.getOption().getOpt())) {
            throw new ParseException(ResourceHandler.getMessage("error.userinput.mutuallyExclusiveOptionsGuiFile"));
        } else {
            return Request.GENERATE;
        }
    }

    /**
     * Get if the files are chosen from a GUI.
     * @return True if the files are chosen from a GUI.
     */
    public boolean isGui() {
        return !commandLine.hasOption(UserInputOption.FILE.getOption().getOpt());
    }

    /**
     * Print the command line help
     */
    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(ResourceHandler.getMessage("application.name"), UserInputOption.getOptions());
    }

    /**
     * Print the version of the application.
     * If possible additional build information is printed as well.
     */
    public void printVersion() {
        String version = null;
        String buildTime = null;
        String branch = null;
        String commit = null;

        // read the project properties from the resources
        try {
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("project.properties");

            if (inputStream != null) {
                // parse the properties
                Properties properties = new Properties();
                properties.load(inputStream);

                // the build properties are encoded in the buildInfo property
                String[] buildProperties = properties.getProperty("buildInfo").split(";");
                if (buildProperties.length >= 4) {
                    version = buildProperties[0];
                    // if a string starts with a "$" the placeholder has not been replaced by the actual value
                    buildTime = buildProperties[1];
                    if (buildTime.startsWith("$")) {
                        buildTime = null;
                    }
                    branch = buildProperties[2];
                    if (branch.startsWith("$")) {
                        branch = null;
                    }
                    commit = buildProperties[3];
                    if (commit.startsWith("$")) {
                        commit = null;
                    }
                }
            }
        } catch (IOException e) {}

        if (version == null) { // general error
            System.out.println(ResourceHandler.getMessage("error.userinput.versionNotFound"));
        } else {
            System.out.println(ResourceHandler.getMessage("command.output.version", version));

            // false when the buildnumber-maven-plugin is not executed (usually when the code is not run directly from the jar)
            if (buildTime != null && branch != null && commit != null) {
                System.out.println();
                System.out.println(ResourceHandler.getMessage("command.output.buildInfo", branch, commit, buildTime));
            }
        }
    }

    /**
     * Get an input file from the user, either from the command line args or by showing a GUI
     * @param userInputFile The type of user input file to return
     * @return The user input file
     * @throws IOException Thrown during the file loading/saving
     */
    public File getFile(UserInputFile userInputFile) throws IOException {
        File file = null;

        // file is read from the command line
        if (commandLine.hasOption(UserInputOption.FILE.getOption().getOpt())) {
            String[] fileArgs = commandLine.getOptionValues(UserInputOption.FILE.getOption().getOpt());

            //TODO Get rid of the magic numbers. Maybe put them into UserInputFile?
            assert(fileArgs.length == 3); // ParseException is thrown if there are less args
            switch (userInputFile) {
            case JSON_GLOBAL:
                file = new File(fileArgs[0]);
                break;
            case JSON_MONTH:
                file = new File(fileArgs[1]);
                break;
            case OUTPUT:
                file = new File(fileArgs[2]);
                break;
            default: // never used
                break;
            }
        } else { // file is chosen in a GUI
            JFileChooser fileChooser = new JFileChooser();

            if (currentDirectory == null || currentDirectory.isEmpty()) {
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            } else {
                fileChooser.setCurrentDirectory(new File(currentDirectory));
            }

            // configure the accept behavior of the JFileChooser
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(userInputFile.getFileFilter());
            fileChooser.setAcceptAllFileFilterUsed(false);

            // cosmetic changes to title
            fileChooser.setDialogTitle(userInputFile.getDialogTitel());

            // get allowed file extensions
            String[] exts = userInputFile.getFileFilter().getExtensions();

            // show dialog and check for errors
            switch (userInputFile.getFileOperation()) {
            case OPEN:
                if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
                    throw new IOException(ResourceHandler.getMessage("error.userinput.fileCouldNotBeOpened"));
                }
                file = fileChooser.getSelectedFile();

                if (!file.exists()) {
                    throw new IOException(ResourceHandler.getMessage("error.userinput.fileDoesNotExist"));
                } else if (exts.length > 0 && !Arrays.asList(exts).contains(FilenameUtils.getExtension(file.getName()))) {
                    throw new IOException(ResourceHandler.getMessage("error.userinput.unsupportedExtension"));
                }
                break;
            case SAVE:
                if (fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
                    throw new IOException(ResourceHandler.getMessage("error.userinput.fileCouldNotBeSaved"));
                }
                file = fileChooser.getSelectedFile();

                if (exts.length > 0 && !Arrays.asList(exts).contains(FilenameUtils.getExtension(file.getName()))) {
                    String ext = exts[0].startsWith(".") ? exts[0] : "." + exts[0];
                    file = new File(file.getParentFile(), FilenameUtils.getBaseName(file.getName()) + ext);
                }
                break;
            default: // never used
                break;
            }

            currentDirectory = file.getParent();
        }

        return file;
    }

    /**
     * Action a user requested through the command line arguments
     */
    public enum Request {
        HELP,
        VERSION,
        GENERATE;
    }

}
