#!/bin/sh
##############################################################################
## Gradle wrapper script
##############################################################################
DIR="$( cd "$( dirname "$0" )" && pwd )"
exec "$JAVA_HOME/bin/java" -jar "$DIR/gradle/wrapper/gradle-wrapper.jar" "$@"

