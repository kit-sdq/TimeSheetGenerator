#!/bin/bash

image_name=timesheetgenerator
image_version=latest

# create output directory
mkdir -p ./target

# export docker image to .tar
docker save $image_name:$image_version > ./target/${image_name}_${image_version}.tar

# compress docker image
gzip ./target/${image_name}_${image_version}.tar
