package main;

import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Represents a type of file that is specified by the user
 */
public enum UserInputFile {

    /**
     * [INPUT] Global JSON file containing information that are the same on all time sheets
     */
    JSON_GLOBAL("Global JSON File", new FileNameExtensionFilter("JSON-File", "json"), FileOperation.OPEN),
    /**
     * [INPUT] JSON file containing information that is specific to one time sheet (= one month)
     */
    JSON_MONTH("Month JSON File", new FileNameExtensionFilter("JSON-File", "json"), FileOperation.OPEN),
    /**
     * [OUTPUT] TeX file in which the generated time sheet is written.
     */
    OUTPUT("Output Tex File", new FileNameExtensionFilter("Tex-File", "tex"), FileOperation.SAVE);

    private final String dialogTitel;
    private final FileNameExtensionFilter fileFilter;
    private final FileOperation operation;

    /**
     * Create a new type of user input file
     * @param dialogTitel Title of the dialog that is used to request the file from the user
     * @param fileFilter Filter for allowed file extensions
     * @param operation Operation associated with this user file type (OPEN or SAVE)
     */
    private UserInputFile(String dialogTitel, FileNameExtensionFilter fileFilter, FileOperation operation) {
        this.dialogTitel = dialogTitel;
        this.fileFilter = fileFilter;
        this.operation = operation;
    }

    /**
     * Get the title of the dialog that is used to request the file from the user
     * @return Dialog title
     */
    public String getDialogTitel() {
        return this.dialogTitel;
    }

    /**
     * Get the filter for allowed file extensions
     * @return File extension filter
     */
    public FileNameExtensionFilter getFileFilter() {
        return this.fileFilter;
    }

    /**
     * Get the operation associated with this user file type
     * @return File operation (OPEN or SAVE)
     */
    public FileOperation getFileOperation() {
        return this.operation;
    }

    /**
     * Represents the operation that is executed with a file type (either OPEN or SAVE)
     */
    public enum FileOperation {
        OPEN,
        SAVE
    }

}
