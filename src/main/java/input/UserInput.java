package input;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import org.apache.commons.cli.*;

public class UserInput {

  private CommandLine commmandLine;
  
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
      
      //TODO Should this line be used? What dir? Home maybe?
      fileChooser.setCurrentDirectory(new File("./"));
      
      //Configure the accept behavior of the JFileChooser
      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      fileChooser.setFileFilter(userInputFile.getFileFilter());
      fileChooser.setAcceptAllFileFilterUsed(false);
      
      //Cosmetic changes to title and button
      fileChooser.setDialogTitle(userInputFile.getDialogTitel());
      fileChooser.setApproveButtonText(userInputFile.getDialogButton());
      
      //Show dialog and check for errors
      if (fileChooser.showDialog(null, null) != JFileChooser.APPROVE_OPTION) {
        throw new IOException("File could not be selected.");
      }
      file = fileChooser.getSelectedFile();
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
