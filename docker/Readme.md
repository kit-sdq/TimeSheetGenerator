# TimeSheetGenerator - Docker

Wraps the TimeSheetGenerator in a docker image for easier use.

## Execution

As of now (04.04.2020) there is no docker hub image available so you have to build it by yourself. The build command is part of the execution script so after you copied the global.json and month.json with your personal data into this folder and changed the names in the execution script to match your filenames you can simply execute the script.

### Script
- the generated pdf-document will be copied in this folder by the mount (-v host:container)

```bash
docker build .
SET SRC_PATH=%cd%
SET IMAGE_NAME=timesheetgenerator:latest
SET GLOBAL_JSON=global.json
SET MONTH_JSON=month.json
REM docker pull %IMAGE_NAME%
docker run --rm -it -v "%SRC_PATH%":/prod/src %IMAGE_NAME% /prod/docker_build_tex_pdf.sh /prod/src/%GLOBAL_JSON% /prod/src/%MONTH_JSON%
PAUSE
```
