#!/bin/bash
mvn install:install-file -Dfile=or-tools_Ubuntu-20.04-64bit_v9.2.9972/ortools-java-9.2.9972.jar -DpomFile=or-tools_Ubuntu-20.04-64bit_v9.2.9972/pom-local.xml
mvn install:install-file -Dfile=or-tools_Ubuntu-20.04-64bit_v9.2.9972/ortools-linux-x86-64-9.2.9972.jar -DpomFile=or-tools_Ubuntu-20.04-64bit_v9.2.9972/pom-runtime.xml
mvn dependency:go-offline
mvn install -DskipTests
