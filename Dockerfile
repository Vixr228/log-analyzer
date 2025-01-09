FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml ./
#RUN mvn dependency:go-offline -DincludeScope=test
RUN mvn dependency:go-offline -DskipTests
COPY src ./src
RUN mvn package -DskipTests

FROM openjdk:21-slim
WORKDIR /app
COPY --from=build /app/target/log-analyzer-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
