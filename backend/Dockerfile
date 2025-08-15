# ===== Build stage =====
FROM maven:3.9.9-eclipse-temurin-8 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -e -B -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -e -B -DskipTests clean package
# ===== Run stage =====
FROM eclipse-temurin:8-jre
WORKDIR /app
COPY --from=build /app/target/people-api-java8-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh","-c","java -jar app.jar"]
