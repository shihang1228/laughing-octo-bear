#!/bin/sh
mkdir -p $CATALINA_HOME/webapps/jdbc/
cp -r src/main/webapp/* $CATALINA_HOME/webapps/jdbc/
cp -r target/classes $CATALINA_HOME/webapps/jdbc/WEB-INF/