#!/bin/bash

image_name=timesheetgenerator
image_version=latest

# copy source files
mkdir -p ./src

cp ../examples/Latex_Logo.pdf ./src/Latex_Logo.pdf || exit $?
cp $(find ../target/TimeSheetGenerator-v*-jar-with-dependencies.jar) ./src/TimeSheetGenerator.jar || exit $?

# build docker image
docker build -t $image_name:$image_version .

# clean source files
rm -f ./src/Latex_Logo.pdf ./src/TimeSheetGenerator.jar
