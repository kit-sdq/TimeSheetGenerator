package main;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;

import i18n.ResourceHandler;
import io.FileController;

/**
 * Represents a user interaction where the user specifies the input and output in a file chooser.
 */
public class UserInteractionGui extends UserInteraction {

    /**
     * Create a new {@link UserInteractionGui} instance.
     */
    public UserInteractionGui() {
        currentDirectory = System.getProperty("user.home");
    }

    private String currentDirectory;

    @Override
    public boolean isPrintRequest() {
        return false;
    }

    @Override
    public boolean isGui() {
        return true;
    }

    @Override
    public String getInput(UserInputType inputType) throws IOException {
        switch (inputType) {
            case GLOBAL:
                return FileController.readFileToString(showFileChooser(UserInteractionFileType.JSON_GLOBAL));
            case MONTH:
                return FileController.readFileToString(showFileChooser(UserInteractionFileType.JSON_MONTH));
            default:
                throw new RuntimeException(); // never occurs
        }
    }

    @Override
    public void setOutput(String output) throws IOException {
        FileController.saveStringToFile(output, showFileChooser(UserInteractionFileType.OUTPUT));
    }

    /**
     * Show a file chooser for the given {@link UserInteractionFileType}.
     * 
     * @param fileType file type to request
     * @return chosen file
     * @throws IOException throws IOException
     */
    private File showFileChooser(UserInteractionFileType fileType) throws IOException {
        JFileChooser fileChooser = new JFileChooser();

        // set the current directory
        fileChooser.setCurrentDirectory(new File(currentDirectory));

        // configure the accept behavior of the JFileChooser
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(fileType.getFileFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);

        // cosmetic changes to title
        fileChooser.setDialogTitle(fileType.getDialogTitel());

        // get allowed file extensions
        String[] exts = fileType.getFileFilter().getExtensions();

        // show file chooser
        File file;
        switch (fileType.getFileOperation()) {
            case OPEN:
                file = showFileChooserOpen(fileChooser, exts);
                break;
            case SAVE:
                file = showFileChooserSave(fileChooser, exts);
                break;
            default:
                throw new RuntimeException(); // never occurs
        }

        // remember current directory
        currentDirectory = file.getParent();

        return file;
    }

    /**
     * Show the open dialog for a given file chooser.
     * 
     * @param fileChooser file chooser
     * @param exts allowed file extensions
     * @return chosen file
     */
    private File showFileChooserOpen(JFileChooser fileChooser, String[] exts) throws IOException {
        if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
            throw new IOException(ResourceHandler.getMessage("error.userinput.fileCouldNotBeOpened"));
        }
        File file = fileChooser.getSelectedFile();

        if (!file.exists()) {
            throw new IOException(ResourceHandler.getMessage("error.userinput.fileDoesNotExist"));
        } else if (exts.length > 0 && !Arrays.asList(exts).contains(FilenameUtils.getExtension(file.getName()))) {
            throw new IOException(ResourceHandler.getMessage("error.userinput.unsupportedExtension"));
        }

        return file;
    }

    /**
     * Show the save dialog for a given file chooser.
     * The default file extension is automatically added if none is given by the user.
     * 
     * @param fileChooser file chooser
     * @param exts allowed file extensions (default is at index 0)
     * @return chosen file
     */
    private File showFileChooserSave(JFileChooser fileChooser, String[] exts) throws IOException {
        if (fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
            throw new IOException(ResourceHandler.getMessage("error.userinput.fileCouldNotBeSaved"));
        }
        File file = fileChooser.getSelectedFile();

        if (exts.length > 0 && !Arrays.asList(exts).contains(FilenameUtils.getExtension(file.getName()))) {
            String ext = exts[0].startsWith(".") ? exts[0] : "." + exts[0];
            
            file = new File(file.getParentFile(), FilenameUtils.getBaseName(file.getName()) + ext);
        }

        return file;
    }
    
    /**
     * Represents types of files that can be requested from the user through the file chooser.
     */
    public enum UserInteractionFileType {

        /**
         * [INPUT] Global JSON file containing information that are the same on all time sheets
         */
        JSON_GLOBAL("file.json.global.description", "file.json.description", "file.json.extension", FileOperation.OPEN),
        /**
         * [INPUT] JSON file containing information that is specific to one time sheet (= one month)
         */
        JSON_MONTH("file.json.month.description", "file.json.description", "file.json.extension", FileOperation.OPEN),
        /**
         * [OUTPUT] TeX file in which the generated time sheet is written.
         */
        OUTPUT("file.tex.output.description", "file.tex.description", "file.tex.extension", FileOperation.SAVE);
    
        private final String dialogTitel;
        private final FileNameExtensionFilter fileFilter;
        private final FileOperation operation;

        /**
         * Create a new type of user input file
         * @param dialogTitel Title of the dialog that is used to request the file from the user
         * @param fileFilter Filter for allowed file extensions
         * @param operation Operation associated with this user file type (OPEN or SAVE)
         */
        private UserInteractionFileType(String dialogTitelKey, String fileDescriptionKey, String fileExtensionKey, FileOperation operation) {
            this.dialogTitel = ResourceHandler.getMessage(dialogTitelKey);
            this.fileFilter = new FileNameExtensionFilter(
                ResourceHandler.getMessage(fileDescriptionKey),
                ResourceHandler.getMessage(fileExtensionKey)
            );
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

}
