@echo off

SET image_name=timesheetgenerator
SET image_version=latest

REM options can be given before any other arguments
SET flags=

for %%A in (%*) do (
    if "%%A" == "-v" (
        SET flags=%flags% -v
        SHIFT
    ) else (
        GOTO :break
    )
)
:break

REM script arguments
SET global_json=%~dpnx1
SET month_json=%~dpnx2
SET output_pdf=%~dpnx3

SET input_error=
if not defined global_json SET input_error=1
if not defined month_json SET input_error=1
if not defined output_pdf SET input_error=1

if defined input_error (
    echo Not all required arguments were given
    exit 1
)

REM file names
SET global_json_name=%~nx1
SET month_json_name=%~nx2
SET output_pdf_name=%~nx3

REM copy input files
if not exist %TMP%\timesheetgenerator mkdir %TMP%\timesheetgenerator

copy %global_json% %TMP%\timesheetgenerator > NUL
if not %errorlevel% == 0 (
    echo Cannot copy the global json file
    exit /b %errorlevel%
)

copy %month_json% %TMP%\timesheetgenerator > NUL
if not %errorlevel% == 0 (
    echo Cannot copy the month json file
    exit /b %errorlevel%
)

REM run docker container and remove after exit
docker run --rm -v %TMP%\timesheetgenerator:/prod/src %image_name%:%image_version% %flags% %global_json_name% %month_json_name% %output_pdf_name%

REM copy output file
copy %TMP%\timesheetgenerator\%output_pdf_name% %output_pdf% > NUL
if not %errorlevel% == 0 (
    echo Cannot copy the output file
    exit /b %errorlevel%
)

REM clean tmp directory
del /F %TMP%\timesheetgenerator\%global_json_name% %TMP%\timesheetgenerator\%month_json_name% %TMP%\timesheetgenerator\%output_pdf_name%
