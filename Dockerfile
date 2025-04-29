
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
EXPOSE 8080
# Use consistent JAR name and allow for environment variables to be passed
ENTRYPOINT ["java", "-jar", "app.jar"]