FROM openjdk:11-jdk-slim-buster

# Set JAR_FILE
ARG JAR_FILE=build/libs/\*.jar

# Copy JAR
COPY ${JAR_FILE} app.jar

# RUN APPLICATION
ENTRYPOINT [ "java", "-jar", "/app.jar" ]