#!/bin/sh
rm -r -f $CATALINA_HOME/webapps/jdbc

mkdir -p $CATALINA_HOME/webapps/jdbc/
cp -r src/main/webapp/* $CATALINA_HOME/webapps/jdbc/
cp -r target/classes $CATALINA_HOME/webapps/jdbc/WEB-INF/

mkdir -p $CATALINA_HOME/webapps/jdbc/WEB-INF/lib/
cp -r lib/mysql-connector-java-3.1.14-bin.jar $CATALINA_HOME/webapps/jdbc/WEB-INF/lib/
