package main;

import javax.swing.filechooser.FileNameExtensionFilter;

public enum UserInputFile {
  
  JSON_GLOBAL("Global JSON File", new FileNameExtensionFilter("JSON-File", "json"), FileOperation.OPEN),
  JSON_MONTH("Month JSON File", new FileNameExtensionFilter("JSON-File", "json"), FileOperation.OPEN),
  OUTPUT("Output Tex File", new FileNameExtensionFilter("Tex-File", "tex"), FileOperation.SAVE);
  
  private String dialogTitel;
  private FileNameExtensionFilter fileFilter;
  private FileOperation operation;
  
  private UserInputFile(String dialogTitel, FileNameExtensionFilter fileFilter, FileOperation operation) {
    this.dialogTitel = dialogTitel;
    this.fileFilter = fileFilter;
    this.operation = operation;
  }
  
  public FileNameExtensionFilter getFileFilter() {
    return this.fileFilter;
  }
  
  public String getDialogTitel() {
    return this.dialogTitel;
  }
  
  public FileOperation getFileOperation() {
	  return this.operation;
  }
  
  public enum FileOperation {
	  OPEN,
	  SAVE
  }
  
}
