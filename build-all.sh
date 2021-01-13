#!/bin/bash
if [[ -z ${CHART_VERSION} ]]; then
  CHART_VERSION="1.0.0"
fi
if [[ -z ${BUILD_IMAGES} ]]; then
  BUILD_IMAGES=false
fi
if [[ -z ${PACKAGE} ]]; then
  PACKAGE=false
fi
if [ "$BUILD_IMAGES" = true ] ; then
  home=$PWD
  echo "Building NLP Processor"
  cd nlp-processor || exit
  ./gradlew clean build -PskipTests
  cd docker || exit
  bash build-image.sh
  cd $home || exit

  echo "Building Patent manager"
  cd patent-manager || exit
  ./gradlew clean build -PskipTests
  cd docker || exit
  bash build-image.sh
  cd $home || exit
fi

echo "Building chart"
cp docker-compose.yml basf-test.yml
kompose convert -f basf-test.yml --chart
if [ "$PACKAGE" = true ] ; then
  helm package basf-test --version "${CHART_VERSION}-local" --app-version "${CHART_VERSION}"
  rm -rf basf-test
fi
rm basf-test.yml
