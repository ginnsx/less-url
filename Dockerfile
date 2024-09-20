# Use a base image with Java installed
FROM openjdk:17-alpine

# Set the working directory in the container
WORKDIR /app

EXPOSE 8080

# Copy the JAR file into the container at /app
COPY target/*.jar app.jar

# Specify the command to run your application
ENTRYPOINT ["java", "-jar", "app.jar"]