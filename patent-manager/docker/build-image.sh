#!/bin/bash
if [[ -z ${VERSION} ]]; then
  VERSION="latest"
fi
cp ../build/libs/*-executable.jar app.jar
docker build . -t "${REGISTRY}"patent-manager:${VERSION} --no-cache
rm app.jar