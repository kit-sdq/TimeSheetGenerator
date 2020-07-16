#!/bin/bash

image_name=timesheetgenerator
image_version=$1

if [[ $image_version == "" ]] ; then
    echo "Missing required image version argument"
    exit 1
fi

# check image version
image_version_parts=($(echo $image_version | sed 's/\./ /g'))

if [[ ${#image_version_parts[@]} < 3 ]] ; then
    echo "Please specify the exact version of the image"
    exit 1
fi

# apply all version tags to the specified image
# WARNING: this may overwrite tags in your docker instance
image_tags=
version_prefix=

for i in "${image_version_parts[@]}" ; do
    image_tag="$version_prefix$i"

    docker tag "$image_name:$image_version" "$image_name:$image_tag"

    image_tags+="$image_name:$image_tag "
    version_prefix+="$i."
done

docker tag "$image_name:$image_version" "$image_name:latest"
image_tags+="$image_name:latest"

# create output directory
mkdir -p ./target

# export docker image to .tar
docker save $image_tags > ./target/${image_name}_${image_version}.tar

# compress docker image
gzip -f ./target/${image_name}_${image_version}.tar

# copy docker run scripts
cp ./run_docker_build_pdf.cmd ./target/
cp ./run_docker_build_pdf.sh ./target/

# replace version in run scripts
image_version_major_minor="${image_version_parts[0]}.${image_version_parts[1]}"

sed "s/image_version=latest/image_version=$image_version_major_minor/" ./target/run_docker_build_pdf.sh -i
sed "s/image_version=latest/image_version=$image_version_major_minor/" ./target/run_docker_build_pdf.cmd -i

# create final zip file
cd ./target/

zip ${image_name}_docker_${image_version}.zip \
    ${image_name}_${image_version}.tar.gz \
    run_docker_build_pdf.sh \
    run_docker_build_pdf.cmd \

# clean up
rm -f run_docker_build_pdf.sh run_docker_build_pdf.cmd
