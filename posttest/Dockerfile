FROM gradle:7.4-jdk17 AS builder

# Set the working directory in the container
WORKDIR /app

# Copy all files from the host into the container
COPY . .

# Build the Spring Boot application using Gradle
RUN gradle clean build --no-daemon

# Use the AdoptOpenJDK image with OpenJDK 17 as the base image
FROM openjdk:17-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the Gradle build stage into the final image
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8888
# Specify the command to run the Spring Boot application
CMD ["java", "-jar", "app.jar"]