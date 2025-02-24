FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Spring Boot JAR file into the container
COPY target/webtest-0.0.1-SNAPSHOT.jar /app/app.jar

# Copy the wait-for-it.sh script into the container and give it execute permissions
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Expose the port the app will run on
EXPOSE 8080

# Run the application with wait-for-it to ensure the database is ready
ENTRYPOINT ["/wait-for-it.sh", "h2database:1521", "--", "java", "-jar", "/app.jar"]
