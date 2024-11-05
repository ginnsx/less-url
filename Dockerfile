# Step 1: 构建自定义 JRE
FROM eclipse-temurin:17-jdk-alpine AS jre-builder

WORKDIR /build

# Copy the project source code
COPY . ./

# Build the package
RUN ./mvnw -DoutputFile=target/mvn-dependency-list.log -B -DskipTests clean dependency:list install

# Install the necessary packages
RUN apk update && apk add binutils

RUN jdeps --ignore-missing-deps -q  \
    --recursive  \
    --multi-release 17  \
    --print-module-deps  \
    --class-path 'BOOT-INF/lib/*'  \
    /build/application/target/*.jar > modules.txt

# Build the JRE
RUN ${JAVA_HOME}/bin/jlink \
  --verbose \
  --add-modules $(cat modules.txt) \
  --no-header-files \
  --no-man-pages \
  --compress=2 \
  --strip-debug \
  --output /optimized-jdk-17

# Stage 2: Create a minimal image
FROM alpine:latest
ENV JAVA_HOME=/opt/jdk/jre-17
ENV PATH=$PATH:$JAVA_HOME/bin

COPY --from=jre-builder /build/application/target/*.jar /app/app.jar
COPY --from=jre-builder /optimized-jdk-17 $JAVA_HOME

# Declare environment variable with default value
ENV SPRING_PROFILES_ACTIVE=prod

# Set the working directory in the container
WORKDIR /app

EXPOSE 8080

ENV JAVA_OPTS="\
    -XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=75.0 \
    -Xss512k \
    -XX:MetaspaceSize=64m \
    -XX:+UseG1GC \
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:HeapDumpPath=/dumps/heap-dump.hprof"

# Run the app by dynamically finding the JAR file in the target directory
CMD ["sh", "-c", "java $JAVA_OPTS -jar \
    -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} \
    app.jar"]
