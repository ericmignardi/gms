FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
COPY src /gms/src
COPY pom.xml /gms
RUN mvn -f /gms/pom.xml clean package -DskipTests

FROM openjdk:17
COPY --from=build /gms/target/*.jar gms.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","gms.jar"]