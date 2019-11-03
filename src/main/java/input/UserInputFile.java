package input;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public enum UserInputFile {
  
  JSON_GLOBAL("Global JSON File", "Load json", new FileNameExtensionFilter("JSON-File", "json")),
  JSON_MONTH("Month JSON File", "Load json", new FileNameExtensionFilter("JSON-File", "json")),
  OUTPUT("Output Tex File", "Save", new FileNameExtensionFilter("Tex-File", "tex"));
  
  private String dialogTitel;
  private String dialogButton;
  private FileFilter fileFilter;
  
  private UserInputFile(String dialogTitel, String dialogButton, FileFilter fileFilter) {
    this.dialogTitel = dialogTitel;
    this.dialogButton = dialogButton;
    this.fileFilter = fileFilter;
  }
  
  public FileFilter getFileFilter() {
    return this.fileFilter;
  }
  
  public String getDialogTitel() {
    return this.dialogTitel;
  }
  
  public String getDialogButton() {
    return this.dialogButton;
  }
}
