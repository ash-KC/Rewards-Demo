#!/bin/sh
# Maven Wrapper script for Unix-like systems
# Downloads Maven if not found locally

set -e

MAVEN_PROJECTBASEDIR="${MAVEN_BASEDIR:-$(cd "$(dirname "$0")" && pwd)}"
MAVEN_WRAPPER_PROPERTIES="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.properties"
MAVEN_WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"

if [ -f "$MAVEN_WRAPPER_PROPERTIES" ]; then
    distributionUrl=$(grep 'distributionUrl' "$MAVEN_WRAPPER_PROPERTIES" | cut -d'=' -f2-)
    wrapperUrl=$(grep 'wrapperUrl' "$MAVEN_WRAPPER_PROPERTIES" | cut -d'=' -f2-)
fi

MAVEN_HOME="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/dists/$(basename "$distributionUrl" .zip)"

if [ ! -d "$MAVEN_HOME" ]; then
    echo "Downloading Maven from $distributionUrl"
    mkdir -p "$MAVEN_PROJECTBASEDIR/.mvn/wrapper/dists"
    if command -v curl > /dev/null; then
        curl -fsSL "$distributionUrl" -o "$MAVEN_PROJECTBASEDIR/.mvn/wrapper/dists/maven.zip"
    elif command -v wget > /dev/null; then
        wget -q "$distributionUrl" -O "$MAVEN_PROJECTBASEDIR/.mvn/wrapper/dists/maven.zip"
    fi
    unzip -q "$MAVEN_PROJECTBASEDIR/.mvn/wrapper/dists/maven.zip" -d "$MAVEN_PROJECTBASEDIR/.mvn/wrapper/dists/"
    rm "$MAVEN_PROJECTBASEDIR/.mvn/wrapper/dists/maven.zip"
fi

exec "$MAVEN_HOME/bin/mvn" "$@"
