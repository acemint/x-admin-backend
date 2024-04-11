# syntax=docker/dockerfile:1
FROM eclipse-temurin:21-jdk-alpine as build

# Set cwd to /app
WORKDIR /app

# Copies maven wrapper & pom.xml
COPY ./.mvn ./.mvn
COPY ./mvnw ./
COPY ./pom.xml ./

# Download project dependencies
RUN ./mvnw -X dependency:go-offline

# Copy the src folder and build the app
COPY ./src ./src
RUN ./mvnw -X package -DskipTests

FROM eclipse-temurin:21-jre-alpine as run

# Set cwd to /app
WORKDIR /app

# Set variable port
ARG PORT=8080

# Set environment variable for the port
ENV PORT=${PORT}

# Copy the executable JAR file from the build stage
COPY --from=build /app/target/x-admin-0.0.1-SNAPSHOT*.jar /app.jar

# Define the default command to run the Spring Boot application
CMD ["java", "-Dserver.port=${PORT}", "-Dspring.profiles.active=production", "-jar", "/app.jar"]