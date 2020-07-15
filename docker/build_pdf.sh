#!/bin/bash

# options can be given before any other arguments
verbose=false
prefix=''

while getopts 'v' flag ; do
    case "${flag}" in
        v)  verbose=true
            prefix='[TimeSheetGenerator] '
            shift ;;
        *)  exit 1 ;;
    esac
done

# script arguments
global_json=$1
month_json=$2
output_pdf=$3

# setup working directory
cd /prod

# generate .tex file
echo $prefix 'Generating timesheet ...'

output_tex=${output_pdf%.*}.tex

java -jar TimeSheetGenerator.jar --file ./src/$global_json ./src/$month_json $output_tex || exit $?

# generate .pdf file
echo $prefix 'Making pdf file ...'

if [ $verbose == true ] ; then
    echo
    latexmk -pdf $output_tex || exit $?
    echo
else
    latexmk -pdf $output_tex > /dev/null 2>&1 || exit $?
fi

# copy .pdf file
cp ${output_tex%.*}.pdf ./src/$output_pdf

echo $prefix 'Finished successfully!'
