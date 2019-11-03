package input;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public enum UserInputOption {
  
  HELP(Option.builder("h")
      .longOpt("help")
      .desc("Prints helping information")
      .hasArg(false)
      .build()),
  GUI(Option.builder("g")
      .longOpt("gui")
      .desc("Enables load/save dialogs")
      .hasArg(false)
      .build()),
  FILE(Option.builder("f")
      .longOpt("file")
      .desc("Passes file paths via console")
      .numberOfArgs(3)
      .argName("global.json> <month.json> <output-file.tex")
      .build());
  
  private Option option;
  
  private UserInputOption (Option option) {
    this.option = option;
  }
  
  public Option getOption() {
    return this.option;
  }
  
  public static Options getOptions() {
    Options options = new Options();
    for(UserInputOption uio : UserInputOption.values()) {
      options.addOption(uio.getOption());
    }
    return options;
  } 
}
