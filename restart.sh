#!/bin/sh

carmigo_server="car-migo_server"
carmigo_ui="car-migo_ui"
postgres="car-migo_postgres"
pgAdmin="car-migo_pgadmin"

echo "$(tput setaf 1)Removing containers:$(tput sgr0)"
docker container rm $carmigo_server -f
docker container rm $carmigo_ui -f
docker container rm $postgres -f
docker container rm $pgAdmin -f

echo "$(tput setaf 6)Restart volume $postgres? (y/n)$(tput sgr0) \c"
read postgresResponse
if [ $postgresResponse = "y" ]
then
  docker volume rm $postgres > /dev/null 2>&1
  echo "Removed volume $postgres"
fi

echo "$(tput setaf 4)Restart volume $pgAdmin? (y/n)$(tput sgr0) \c"
read pgAdminResponse
if [ $pgAdminResponse = "y" ]
then
  docker volume rm $pgAdmin > /dev/null 2>&1
  echo "Removed volume $pgAdmin"
fi

echo "$(tput setaf 2)Starting containers again:$(tput sgr0)"
docker compose up -d
