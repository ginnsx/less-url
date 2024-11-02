# Use a base image with Java installed
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory in the container
WORKDIR /app

EXPOSE 8080

# Declare environment variable with default value
ENV SPRING_PROFILES_ACTIVE=dev

# Copy files to the container image
COPY . ./

# Build the app.
RUN ./mvnw -DoutputFile=target/mvn-dependency-list.log -B -DskipTests clean dependency:list install

# Run the app by dynamically finding the JAR file in the target directory
CMD ["sh", "-c", "java -jar target/*.jar"]
CMD ["sh", "-c", "java -jar -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} target/*.jar"]
