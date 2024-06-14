FROM maven:3.9.6-eclipse-temurin-21-alpine as mavenBuild
copy src /app/src
copy pom.xml /app
RUN mvn -f /app/pom.xml clean package

FROM eclipse-temurin:21-jdk-alpine
EXPOSE 8080
copy --from=mavenBuild /app/target/*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]