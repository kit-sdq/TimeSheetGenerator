# TimeSheetGenerator

TimeSheetGenerator is an application that checks and builds time sheet documents.

## Execution

Run TimeSheetGenerator (requires Java 21 or higher):

`$ java -jar TimeSheetGenerator.jar [--help] [--version] [--gui] [--file <global.json> <month.json> <output.tex>]`

### Command Line Options

| Option | Long Option | Arguments                               | Description                                                    |
| ------ | ----------- | --------------------------------------- | -------------------------------------------------------------- |
|  `-h`  |  `--help`   | _none_                                  | Print a help dialog.                                           |
|  `-v`  | `--version` | _none_                                  | Print the version of the application.                          |
|  `-g`  |   `--gui`   | _none_                                  | Generate an output file based on files chosen in a file dialog.|
|  `-f`  |  `--file`   |`<global.json> <month.json> <output.tex>`| Generate an output file based on the given files.              |
