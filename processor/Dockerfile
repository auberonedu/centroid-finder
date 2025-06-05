# Using the latest lightweight OpenJDK image
FROM openjdk:25-slim

# Setting the working directory inside the container
WORKDIR /app

# Copying the compiled JAR into the container
COPY processor.jar .

# Command to run the JAR file
CMD ["java", "-jar", "processor.jar"]