#!/bin/sh
mkdir -p target/classes/
javac -encoding "utf-8" -classpath $CATALINA_HOME/lib/servlet-api.jar -d target/classes/ src/main/java/com/bodejidi/*.java
