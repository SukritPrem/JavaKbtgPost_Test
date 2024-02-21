# FROM gradle:7.3.3-jdk11 AS builder
# WORKDIR /home/gradle/project
# COPY . .
# RUN gradle build --no-daemon
#
# # Stage 2: Create the final image
# FROM openjdk:17
# WORKDIR /app
# COPY --from=builder /home/gradle/project/build/libs/posttest-0.0.1-SNAPSHOT.jar ./app.jar
# EXPOSE 8081
#
# # Command to run the Spring Boot application when the container starts
# CMD ["java", "-jar", "app.jar"]


# # Use the official Gradle image as a build stage
# FROM gradle:7.3-jdk17 AS builder
#
# # Set the working directory in the container
# WORKDIR /home/gradle/project
#
# # Copy Gradle project files into the container
# COPY . .
#
# # Build the Spring Boot application using Gradle
# RUN gradle build
#
# # Use a lightweight base image for the application
# FROM openjdk:17-jdk
#
# # Set the working directory in the container
# WORKDIR /app
#
# # Copy the built JAR file from the Gradle build stage into the final image
# COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar
#
# # Specify the command to run the Spring Boot application
# CMD ["java", "-jar", "app.jar"]

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

EXPOSE 8081
# Specify the command to run the Spring Boot application
CMD ["java", "-jar", "app.jar"]