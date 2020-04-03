#!/bin/ash
# Arguments $1 global.json $2 month.json
Xvfb :99 -screen 0 1920x1080x16 &
export DISPLAY=:99
cd /prod/
mkdir -p /prod/aux
withoutExtension=${2%.*}
filename=${withoutExtension##*/}
java -jar TimeSheetGenerator.jar --file "$1" "$2" /prod/aux/${filename%.*}.tex
cd /prod/src/
pdflatex --output-directory=/prod/aux/ /prod/aux/${filename%.*}.tex
cp /prod/aux/${filename%.*}.pdf /prod/src/${filename%.*}.pdf