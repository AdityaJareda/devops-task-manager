FROM maven:3.9-eclipse-temurin-17 AS build


WORKDIR /app

COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .


COPY pom.xml .


RUN ./mvnw dependency:go-offline -B


COPY src ./src


RUN ./mvnw clean package -DskipTests


FROM eclipse-temurin:17-jre-alpine


WORKDIR /app


COPY --from=build /app/target/spring-boot-app-1.0.0.jar app.jar


RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring


EXPOSE 8080


ENTRYPOINT ["java", "-jar", "app.jar"]
