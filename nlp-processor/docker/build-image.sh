#!/bin/bash
if [[ -z ${VERSION} ]]; then
  VERSION="latest"
fi
cp ../build/libs/*.jar app.jar
docker build . -t "${REGISTRY}"nlp-processor:${VERSION} --no-cache
rm app.jar