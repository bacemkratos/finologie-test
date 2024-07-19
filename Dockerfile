# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Add the application's jar to the container
COPY target/web-banking-api*.jar app.jar

# Add the wait-for-it script
COPY scripts/wait-for-it.sh /app/wait-for-it.sh

# Make sure the wait-for-it script has execute permissions
RUN chmod +x /app/wait-for-it.sh

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["/app/wait-for-it.sh", "postgres:5432", "--", "java", "-jar", "app.jar"]