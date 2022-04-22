#!/bin/sh

carmigo="car-migo_app"
postgres="car-migo_postgres"
pgAdmin="car-migo_pgadmin"

echo "Removing containers:"
docker container rm $carmigo -f
docker container rm $postgres -f
docker container rm $pgAdmin -f

echo "Restart volume $postgres? (y/n)"
read postgresResponse
if [ $postgresResponse = "y" ]
then
  docker volume rm $postgres > /dev/null 2>&1
  echo "Removed volume $postgres"
fi

echo "Restart volume $pgAdmin? (y/n)"
read pgAdminResponse
if [ $pgAdminResponse = "y" ]
then
  docker volume rm $pgAdmin > /dev/null 2>&1
  echo "Removed volume $pgAdmin"
fi

echo "Starting containers again:"
docker-compose up -d
