# Use a base Java image
FROM openjdk:17-jdk-slim-buster

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/cryptoservice-0.0.1-SNAPSHOT.jar /app/app.jar

# Set the command to run your Java application
CMD ["java", "-jar", "app.jar"]
