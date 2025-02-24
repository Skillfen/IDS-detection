# Use a base image with Java installed
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Spring Boot jar file into the container
COPY target/webtest-0.0.1-SNAPSHOT.jar app.jar
# Copy wait-for-it script into the container
COPY wait-for-it.sh /wait-for-it.sh

# Expose the port the app will run on
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
