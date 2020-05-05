# TimeSheetGenerator

TimeSheetGenerator is an application that checks and builds time sheet documents.

## Execution

Run TimeSheetGenerator (requires Java 8 or higher):

`$ java -jar TimeSheetGenerator.jar [--help] [--version] [--gui] [--file <global.json> <month.json> <output.tex>]`

### Command Line Options

| Option | Long Option | Arguments                               | Description                                                    |
| ------ | ----------- | --------------------------------------- | -------------------------------------------------------------- |
|  `-h`  |  `--help`   | _none_                                  | Print a help dialog.                                           |
|  `-v`  | `--version` | _none_                                  | Print the version of the application.                          |
|  `-g`  |   `--gui`   | _none_                                  | Generate an output file based on files chosen in a file dialog.|
|  `-f`  |  `--file`   |`<global.json> <month.json> <output.tex>`| Generate an output file based on the given files.              |

## Input Files

For now only JSON files are supported.

### Global Time Sheet Definition

| Property    | Type         | Description                                                                                                |
| ----------- | ------------ | ---------------------------------------------------------------------------------------------------------- |
| name        | String       | Name of the employee.                                                                                      |
| staffId     | Integer      | Staff ID of the employee.                                                                                  |
| department  | String       | Department where the employee is employed at.                                                              |
| workingTime | TimeSpan     | Maximum allowed working time per month according to the employment contract.                               |
| wage        | Decimal      | Wage per hour according to the employment contract.                                                        |
| workingArea | {"ub", "gf"} | Working area of the employee; either "ub" (= "Universitaetsbereich") or "gf" (= "Grossforschungsbereich"). |

### Monthly Working Time Recording

| Property      | Type      | Description                                                                                  |
| ------------- | --------- | -------------------------------------------------------------------------------------------- |
| year          | Integer   | Year for which the working time is recorded.                                                 |
| month         | Integer   | Month for which the working time is recorded.                                                |
| pred_transfer | TimeSpan  | Working time transfer, in hours, from the predecessing month. *(default: 0:00)*              |
| succ_transfer | TimeSpan  | Working time transfer, in hours, to the successing month. *(default: 0:00)*                  |
| entries       | [Entries] | Array of entries, where each entry represent a continual period of working or vacation time. |

#### Monthly Working Time Entry

| Property | Type      | Description                                                                      |
| -------- | --------- | -------------------------------------------------------------------------------- |
| action   | String    | Description of the actions performed in this period of working or vacation time. |
| day      | Integer   | Day of the month.                                                                |
| start    | ClockTime | Starting time for the period of working or vacation time in 24-hour format.      |
| end      | ClockTime | Ending time for the period of working or vacation time in 24-hour format.        |
| pause    | TimeSpan  | Combined pause time during the period of working time. *(default: 0:00)*         |
| vacation | Boolean   | If the recorded period of time represents vacation time. *(default: False)*      |

*Note that the pause and the vacation property may not both be present.*
