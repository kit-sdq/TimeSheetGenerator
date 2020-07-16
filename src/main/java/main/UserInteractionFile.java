package main;

import java.io.File;
import java.io.IOException;

import io.FileController;

/**
 * Represents a user interaction where the user specifies the input and output as file paths.
 */
public class UserInteractionFile extends UserInteraction {

    /**
     * Create a new {@link UserInteractionFile} instance with the specified file paths.
     * 
     * @param globalFilePath Path to the global file
     * @param monthFilePath Path to the month file
     * @param outputFilePath Path to the output file
     */
    public UserInteractionFile(String globalFilePath, String monthFilePath, String outputFilePath) {
        this.globalFilePath = globalFilePath;
        this.monthFilePath = monthFilePath;
        this.outputFilePath = outputFilePath;
    }

    private final String globalFilePath;
    private final String monthFilePath;
    private final String outputFilePath;

    @Override
    public boolean isPrintRequest() {
        return false;
    }

    @Override
    public boolean isGui() {
        return false;
    }

    @Override
    public String getInput(UserInputType inputType) throws IOException {
        switch (inputType) {
            case GLOBAL:
                return FileController.readFileToString(new File(globalFilePath));
            case MONTH:
                return FileController.readFileToString(new File(monthFilePath));
            default:
                throw new RuntimeException(); // never occurs
        }
    }

    @Override
    public void setOutput(String output) throws IOException {
        FileController.saveStringToFile(output, new File(outputFilePath));
    }

}
