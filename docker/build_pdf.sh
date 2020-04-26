#!/bin/bash

# script arguments
global_json=$1
month_json=$2
output_pdf=$3

# setup working directory
cd /prod

# generate .tex file
output_tex=${output_pdf%.*}.tex

java -jar TimeSheetGenerator.jar --file ./src/$global_json ./src/$month_json $output_tex || exit $?

# generate .pdf file
pdflatex $output_tex || exit $?
pdflatex $output_tex || exit $?
pdflatex $output_tex || exit $?

# copy .pdf file
cp ${output_tex%.*}.pdf ./src/$output_pdf
