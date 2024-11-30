# TimeSheetGenerator

TimeSheetGenerator is an application that checks and builds time sheet documents.

## UI for Timesheet Generator

This is a UI for the kit-sdq timesheet generator.
Global settings can be edited via the UI, saving files only saves the month settings.
These saved JSON files are also compatible with the original timesheet generator, so the CLI
can be used as well.

Yes, you can drag and drop your month.json files into the generator.

The files can be compiled to tex or directly compiled to a pdf.

#### To open a file with the UI from the command line, you can type `$ java -jar TimeSheetGenerator.jar /path/to/month.json` to open the specified file with the TimesheetGenerator UI.

### User Interface: Images

The User Interface:
![Image of the TimesheetGenerator open with the example month.json file](/examples/ui/ui_default.png)
<img src="/examples/ui/ui_default.png" alt="Image of the TimesheetGenerator open with the example month.json file" width="80%">

The User Interface with File options selected:
![Image of the TimesheetGenerator with File Options selected](/examples/ui/ui_file_dialog.png)
<img src="/examples/ui/ui_file_dialog.png" alt="Image of the TimesheetGenerator with File Options selected" width="80%">

Opening a JSON file with the TimeSheetGenerator:
![Image of opening a file with the TimesheetGenerator](/examples/ui/ui_open_file_dialog.png)
<img src="/examples/ui/ui_open_file_dialog.png" alt="Image of opening a file with the TimesheetGenerator" width="80%">

Adding a new entry to the timesheet:
![Image of the Add Timesheet Entry Dialog](/examples/ui/ui_add_entry.png)
<img src="/examples/ui/ui_add_entry.png" alt="Image of the Add Timesheet Entry Dialog" width="80%">

Editing the global settings:
![Image of the Edit Global Settings Dialog](/examples/ui/ui_global_settings.png)
<img src="/examples/ui/ui_global_settings.png" alt="Image of the Edit Global Settings Dialog" width="80%">

## Command Line Execution

Run TimeSheetGenerator (requires Java 21 or higher):

`$ java -jar TimeSheetGenerator.jar [--help] [--version] [--gui] [--file <global.json> <month.json> <output.tex>]`

### Command Line Options

| Option | Long Option | Arguments                               | Description                                                    |
| ------ | ----------- | --------------------------------------- | -------------------------------------------------------------- |
|  `-h`  |  `--help`   | _none_                                  | Print a help dialog.                                           |
|  `-v`  | `--version` | _none_                                  | Print the version of the application.                          |
|  `-g`  |   `--gui`   | _none_                                  | Generate an output file based on files chosen in a file dialog.|
|  `-f`  |  `--file`   |`<global.json> <month.json> <output.tex>`| Generate an output file based on the given files.              |

### Third-Party Libraries

This project uses the following third-party libraries:

- **Apache PDFBox**
    - Website: https://pdfbox.apache.org/
    - License: Apache License 2.0 (See `LICENSE` and `NOTICE` files)
