@echo off

SET image_name=timesheetgenerator
SET image_version=latest

REM script arguments
SET global_json=%~dpnx1
SET month_json=%~dpnx2
SET output_pdf=%~dpnx3

REM file names
SET global_json_name=%~nx1
SET month_json_name=%~nx2
SET output_pdf_name=%~nx3

REM copy input files
if not exist %TMP%\timesheetgenerator mkdir %TMP%\timesheetgenerator

copy %global_json% %TMP%\timesheetgenerator || exit /b %errorlevel%
copy %month_json% %TMP%\timesheetgenerator || exit /b %errorlevel%

REM run docker container and remove after exit
docker run --rm -v %TMP%\timesheetgenerator:/prod/src %image_name%:%image_version% %global_json_name% %month_json_name% %output_pdf_name%

REM copy output file
copy %TMP%\timesheetgenerator\%output_pdf_name% %output_pdf%

REM clean tmp directory
del /F %TMP%\timesheetgenerator\%global_json_name% %TMP%\timesheetgenerator\%month_json_name% %TMP%\timesheetgenerator\%output_pdf_name%
