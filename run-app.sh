#!/bin/sh

cd ./server && \
./mvnw clean package -DskipTests && \
cd .. && \
docker build -t car-migo . && \
docker-compose up -d

echo "$(tput setaf 6)-------------------- PAUSING FOR 10 SECONDS FOR COMPLETE STARTUP --------------------$(tput sgr0)"
sleep 1; echo "10\c"; sleep 1; echo ", 9\c"
sleep 1; echo ", 8\c"; sleep 1; echo ", 7\c"
sleep 1; echo ", 6\c"; sleep 1; echo ", 5\c"
sleep 1; echo ", 4\c"; sleep 1; echo ", 3\c"
sleep 1; echo ", 2\c"; sleep 1; echo ", 1"

open http://localhost:8087/home
