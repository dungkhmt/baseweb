FROM maven:3.3.9-jdk-8-alpine
WORKDIR /app
COPY target/baseweb-0.0.1-SNAPSHOT.jar /app/
CMD java -jar /app/baseweb-0.0.1-SNAPSHOT.jar
