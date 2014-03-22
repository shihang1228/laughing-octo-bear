#!/bin/sh
mkdir -p target/classes/
javac -classpath $CATALINA_HOME/lib/servlet-api.jar -d target/classes/ src/main/java/com/bodejidi/Manager.java