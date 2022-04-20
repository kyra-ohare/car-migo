# Car-Migo

This is my first Spring Boot application.

## What is the application?
It is a match-making system for drivers and passengers.
Drivers can offer rides (a journey and a timeframe) while passengers can accept or reject.

## Why?
It is great for the environment once there will be less CO<sub>2</sub> released into the atmosphere.
Moreover, there will be less traffic in our cities thus emergency vehicles will respond to emergencies more rapidly, less noise pollution, less road accidents and make new friends.
The application is not about profiting but about car sharing so the passengers can pay the driver a fair amount for fuel costs.

## Requirements
- Java 11
- Docker

## How to run it?
### Database
From your terminal, navigate to the root of this project and run
```
docker-compose up -d
```

This will spin up three containers: flyway, postgres and pgadmin.
To interact with the database, go to `localhost:8000` from your browser to open the pgAdmin 4 UI and enter:
- Email = `admin@car-migo.com`
- Password = `password`

Once inside the pgAdmin 4, click on `Add New Server`. From the dialog box, enter:
- From the `General` tab, give it any name, maybe `car-migo`.
- From the `Connection` tab:
  - Host name/address = `host.docker.internal`
  - Port = `5432`
  - Maintenance database = `postgres`
  - Username = `admin`
  - Password = `password`
- Leave the rest as it is and `Save`.

Then, from the left panel, navigate to Servers > car-migo > Databases > carmigo > Schemas > public > Tables.

### Run a different Spring Boot profile
```
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### restart.sh
This script restarts the containers. You also have the option to restart the volumes.
