#!/bin/bash

image_name=timesheetgenerator
image_version=latest

# script arguments
global_json=$1
month_json=$2
output_pdf=$3

# file names
global_json_name=$(basename $global_json)
month_json_name=$(basename $month_json)
output_pdf_name=$(basename $output_pdf)

# copy input files
mkdir -p /tmp/timesheetgenerator

cp $global_json /tmp/timesheetgenerator || exit $?
cp $month_json /tmp/timesheetgenerator || exit $?

# run docker container and remove after exit
docker run --rm -v /tmp/timesheetgenerator:/prod/src $image_name:$image_version $global_json_name $month_json_name $output_pdf_name

# copy output file
cp /tmp/timesheetgenerator/$output_pdf_name $output_pdf

# clean tmp directory
rm -f /tmp/timesheetgenerator/$global_json_name /tmp/timesheetgenerator/$month_json_name /tmp/timesheetgenerator/$output_pdf_name
