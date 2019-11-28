package main;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JFileChooser;

import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;

public class UserInput {

  private CommandLine commmandLine;
  
  private String currentDirectory = null; //caches the last used directory for the file chooser
  
  public UserInput(String[] args) throws ParseException {
    DefaultParser dp = new DefaultParser();
    this.commmandLine = dp.parse(UserInputOption.getOptions(), args, false);
    
    if (commmandLine.hasOption(UserInputOption.HELP.getOption().getOpt())) {
      printHelp();
    }
    
    //TODO Error Messages in Enum instead of magic numbers everywhere
    /*
     * Checks whether gui or file option were used.
     * Using none of them makes it impossible to get the files.
     */
    if (!commmandLine.hasOption(UserInputOption.GUI.getOption().getOpt()) 
        && !commmandLine.hasOption(UserInputOption.FILE.getOption().getOpt())) {
      throw new ParseException("Either gui or file option has to be used.");
    } else if (commmandLine.hasOption(UserInputOption.GUI.getOption().getOpt())
        && commmandLine.hasOption(UserInputOption.FILE.getOption().getOpt())) {
      throw new ParseException("Gui and file option cannot be used at the same time.");
    }
  }
  
  public static void printHelp() {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("documentation-checker", UserInputOption.getOptions());
  }
  
  public File getFile(UserInputFile userInputFile) throws IOException {
    File file = null;
    
    if (commmandLine.hasOption(UserInputOption.GUI.getOption().getOpt())) {
      JFileChooser fileChooser = new JFileChooser();
      
      if (currentDirectory == null || currentDirectory.isEmpty()) {
    	  fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
      } else {
    	  fileChooser.setCurrentDirectory(new File(currentDirectory));
      }
      
      //Configure the accept behavior of the JFileChooser
      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      fileChooser.setFileFilter(userInputFile.getFileFilter());
      fileChooser.setAcceptAllFileFilterUsed(false);
      
      //Cosmetic changes to title
      fileChooser.setDialogTitle(userInputFile.getDialogTitel());
      
      //Get allowed file extensions
      String[] exts = userInputFile.getFileFilter().getExtensions();
      
      //Show dialog and check for errors
      switch (userInputFile.getFileOperation()) {
      	case OPEN:
    	  if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
    		  throw new IOException("File could not be opened.");
    	  }
    	  file = fileChooser.getSelectedFile();
    	  
    	  if (!file.exists()) {
    		  throw new IOException("Selected file does not exist.");
    	  } else if (exts.length > 0 && !Arrays.asList(exts).contains(FilenameUtils.getExtension(file.getName()))) {
    		  throw new IOException("Selected file has an unsupported extension.");
    	  }
    	  break;
      	case SAVE:
    	  if (fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
    		  throw new IOException("File could not be saved.");
    	  }
    	  file = fileChooser.getSelectedFile();
    	  
    	  if (exts.length > 0 && !Arrays.asList(exts).contains(FilenameUtils.getExtension(file.getName()))) {
    		  String ext = exts[0].startsWith(".") ? exts[0] : "." + exts[0];
    		  file = new File(file.getParentFile(), FilenameUtils.getBaseName(file.getName()) + ext);
    	  }
    	  break;
    	default: //Never used, since OPEN and SAVE are the only enum values
    	  break;
      }
      
      currentDirectory = file.getParent();
    } else {
      String[] fileArgs = commmandLine.getOptionValues(UserInputOption.FILE.getOption().getOpt());
      
      //TODO Get rid of the magic numbers. Maybe put them into UserInputFile?
      assert(fileArgs.length == 3); //Because ParseException is thrown if there are less args
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
        default:
          break;
      }
    }
    
    return file;
  }
}
