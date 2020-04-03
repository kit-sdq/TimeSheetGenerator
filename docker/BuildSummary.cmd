SET SRC_PATH=%cd%
SET IMAGE_NAME=timesheetgenerator:latest
REM docker pull %IMAGE_NAME%
docker run --rm -it -v "%SRC_PATH%":/prod/src %IMAGE_NAME% /prod/build_timesheet.sh /prod/src/global.json /prod/src/february.json
PAUSE