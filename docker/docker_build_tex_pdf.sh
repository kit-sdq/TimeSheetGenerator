#!/bin/bash
# Arguments $1 global.json $2 month.json
cd /prod/
mkdir -p /prod/aux
withoutExtension=${2%.*}
filename=${withoutExtension##*/}
java -jar TimeSheetGenerator.jar --file "$1" "$2" /prod/aux/${filename%.*}.tex
cd /prod/aux/
cp /prod/Latex_Logo.pdf /prod/aux/Latex_Logo.pdf
pdflatex --output-directory=/prod/aux/ /prod/aux/${filename%.*}.tex
cp /prod/aux/${filename%.*}.pdf /prod/src/${filename%.*}.pdf