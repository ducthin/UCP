
FROM maven:3-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
# Download dependencies first (for better caching)
RUN mvn dependency:go-offline
COPY src ./src
# Fix the maven command (was missing 'mvn')
RUN mvn clean package -DskipTests

# Run stage - use minimal, frequently updated base image
FROM openjdk:17-jdk-slim
WORKDIR /app
# Fix the JAR name mismatch between COPY and ENTRYPOINT
COPY --from=build /app/target/UserCasePoint-0.0.1-SNAPSHOT.jar app.jar
# Make port dynamic for Render - use Render's default 10000 if not specified
ENV PORT=10000
EXPOSE ${PORT}
# Use consistent JAR name and allow for environment variables to be passed
# Ensure server.port is set to the same value as the exposed port
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]