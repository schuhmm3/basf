#!/bin/bash
if [[ -z ${VERSION} ]]; then
  VERSION="latest"
fi
cp ../build/libs/*.jar app.jar
echo docker build . -t ${REGISTRY}nlp-processor:${VERSION}