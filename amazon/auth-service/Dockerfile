# Use the official Maven image as the base image
FROM maven:3.9-eclipse-temurin-17-alpine AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and any other necessary configuration files
COPY ./pom.xml /app

# Build the application
RUN mvn dependency:go-offline -B

COPY ./src /app/src

RUN mvn package -DskipTests


# Create a new image for running the application
FROM amazoncorretto:17-alpine3.16

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Command to run the application
CMD ["java", "-jar", "app.jar"]