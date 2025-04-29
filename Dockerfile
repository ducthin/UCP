
FROM maven:3-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .

RUN mvn dependency:go-offline
COPY src ./src

RUN mvn clean package -DskipTests


FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /app/target/UserCasePoint-0.0.1-SNAPSHOT.jar app.jar

EXPOSE ${10000}
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${10000} -jar app.jar"]