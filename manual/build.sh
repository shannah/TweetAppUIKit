#!/bin/bash
mkdir ../docs/manual
mkdir ../docs/manual/images
asciidoctor -D ../docs/manual/ index.adoc 
cp -r images/* ../docs/manual/images/