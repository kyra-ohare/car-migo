#!/bin/sh

carmigo="car-migo_app"
postgres="car-migo_postgres"
pgAdmin="car-migo_pgadmin"

echo "$(tput setaf 1)Removing containers:$(tput sgr0)"
docker container rm $carmigo -f
docker container rm $postgres -f
docker container rm $pgAdmin -f

echo "$(tput setaf 6)Restart volume $postgres?$(tput sgr0) (y/n)"
read postgresResponse
if [ $postgresResponse = "y" ]
then
  docker volume rm $postgres > /dev/null 2>&1
  echo "Removed volume $postgres"
fi

echo "$(tput setaf 4)Restart volume $pgAdmin?$(tput sgr0) (y/n)"
read pgAdminResponse
if [ $pgAdminResponse = "y" ]
then
  docker volume rm $pgAdmin > /dev/null 2>&1
  echo "Removed volume $pgAdmin"
fi

echo "$(tput setaf 2)Starting containers again:$(tput sgr0)"
docker compose up -d
