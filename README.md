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

## Docker Image

The TimeSheetGenerator application is also provided in a Docker image including all runtime dependencies. This allows running the TimeSheetGenerator without having to install a LaTeX distribution. The Docker image is provided as part of the regular releases in this GitHub repository. Additionally we provide a script to simplify the use of the Docker image, for example copying the necessary files in a temporary directory, which is then mounted in the Docker container. This allows for the input and output files to be placed anywhere on your file system, as you would expect.

### Installation

- download the `docker.zip` file from the latest release
- unzip the archive
- find the extracted Docker image and the script to run it
- install the Docker image with `docker load -i <image_name.tar.gz>`
- place the script anywhere you like

### Execution with Script

`sh ./run_docker_build_pdf.sh <global json> <month json> <output pdf>`

The input and output arguments are full paths and the files do not need to be placed in a special directory.

### Execution without Script

`docker run --rm -v <directory>:/prod/src timesheetgenerator:latest <global json> <month json> <output pdf>`

Without the script the input and output paths are relative to the specified directory. Remember that the specified directory will be mounted in the Docker container.
