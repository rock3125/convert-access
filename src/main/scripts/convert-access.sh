#!/bin/bash

if [ "$JAVA_HOME" == "" ]; then
  echo "JAVA_HOME not set"
  exit 1
fi

SCRIPT_HOME=`dirname "$0"`

CP=`echo $SCRIPT_HOME/lib/*.jar | tr ' ' ':'`
$JAVA_HOME/bin/java -cp $CP accessConversion.Main "$@"
