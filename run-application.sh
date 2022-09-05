#!/bin/sh

cd ./server && \
./mvnw clean package && \
cd .. && \
docker build -t car-migo . && \
docker-compose up -d
