#!/bin/bash

image_name=timesheetgenerator

# get application jar file and version
jar=$(find ../target/TimeSheetGenerator-v*-jar-with-dependencies.jar)
version=$(echo $jar | sed -E 's/.*-v([^-]*)-.*/\1/')

# extract version parts
version_parts=($(echo $version | sed 's/\./ /g'))

# copy source files
mkdir -p ./src

cp ../examples/Latex_Logo.pdf ./src/Latex_Logo.pdf || exit $?
cp $jar ./src/TimeSheetGenerator.jar || exit $?

# create docker tags
tag_args=
version_prefix=

for i in "${version_parts[@]}" ; do
    tag_args+="-t $image_name:$version_prefix$i "
    version_prefix+="$i."
done
tag_args+="-t $image_name:latest"

# build docker image
docker build $tag_args .

# clean source files
rm -f ./src/Latex_Logo.pdf ./src/TimeSheetGenerator.jar
