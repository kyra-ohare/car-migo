FROM openjdk:11-jre-slim-buster
ARG JAR_FILE=server/target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod", "/app.jar"]
