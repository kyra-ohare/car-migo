#!/bin/sh

cd ./server && \
./mvnw clean package -DskipTests && \
cd .. && \
docker build -t car-migo . && \
docker-compose up -d
