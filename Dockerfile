FROM openjdk:17.0.2-jdk AS server
ARG JAR_FILE=server/car-migo/target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8086
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod", "/app.jar"]

FROM node:20-buster-slim AS ui
WORKDIR /ui
COPY ./ui .
RUN npm ci
EXPOSE 8087
CMD [ "npm", "run", "dev" ]
