docker build -t timesheetgenerator:latest .
SET SRC_PATH=%cd%
SET IMAGE_NAME=timesheetgenerator:latest
SET GLOBAL_JSON=global.json
SET MONTH_JSON=month.json
REM docker pull %IMAGE_NAME%
docker run --rm -it -v "%SRC_PATH%":/prod/src %IMAGE_NAME% /prod/docker_build_tex_pdf.sh /prod/src/%GLOBAL_JSON% /prod/src/%MONTH_JSON%
PAUSE