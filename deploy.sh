#!/bin/sh
mkdir -p $CATALINA_HOME/webapps/jdbc/
cp -r src/main/webapp/* $CATALINA_HOME/webapps/jdbc/
cp -r target/classes $CATALINA_HOME/webapps/jdbc/WEB-INF/

mkdir $CATALINA_HOME/webapps/jdbc/WEB-INF/lib/
cp -r lib/mysql-connector-java-3.1.14-bin.jar $CATALINA_HOME/webapps/jdbc/WEB-INF/lib/
