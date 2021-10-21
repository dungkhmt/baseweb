#!/bin/bash
mvn install:install-file -Dfile=or-tools_Ubuntu-20.04-64bit_v8.0.8283/ortools-java-8.0.8283.jar -DpomFile=or-tools_Ubuntu-20.04-64bit_v8.0.8283/pom-local.xml
mvn install:install-file -Dfile=or-tools_Ubuntu-20.04-64bit_v8.0.8283/ortools-linux-x86-64-8.0.8283.jar -DpomFile=or-tools_Ubuntu-20.04-64bit_v8.0.8283/pom-runtime.xml
mvn dependency:go-offline
mvn install -DskipTests
