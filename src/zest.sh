#!/usr/bin/env bash

#Check the java version (min java 7)
JAVAV=`java -version 2>&1 |awk 'NR==1{ gsub(/"/,""); print $3 }'`

if [[ $JAVAV == 1.7* ]]; then
    # OK
    echo "Using Java version: $JAVAV"
else
    echo "Exiting: ZAP 2.0.0 requires a minimum of Java 7 to run."
    exit 1
fi

#Dereference from link to the real directory
SCRIPTNAME=$0

#While name of this script is symbolic link
while [ -L "$SCRIPTNAME" ] ; do 
    #Dereference the link to the name of the link target 
    SCRIPTNAME=$(ls -l "$SCRIPTNAME" | awk '{ print $NF; }')
done

#Base directory where Zest is installed
BASEDIR=$(dirname "$SCRIPTNAME")

#Switch to the directory where Zest is installed
cd "$BASEDIR"

#Start Zest command line

exec java -jar "${BASEDIR}/_ZEST_JAR_" $*

