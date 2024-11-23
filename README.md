# TimeSheetGenerator

TimeSheetGenerator is an application that checks and builds time sheet documents.

## UI for Timesheet Generator

This is a UI for the kit-sdq timesheet generator.
Global settings can be edited via the UI, saving files only saves the month settings.
These saved JSON files are also compatible with the original timesheet generator, so the CLI
can be used as well.

The files can be compiled to tex or directly compiled to a pdf.

## CLI Execution

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
