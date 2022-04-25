# Car-Migo

This is my first Spring Boot application.

## What is the application?
It is a match-making system for drivers and passengers.
Drivers can create rides (a journey and a timeframe) while passengers can accept or reject.

## Why?
It is great for the environment once there will be less CO<sub>2</sub> released into the atmosphere.
Moreover, there will be less traffic in our cities thus emergency vehicles will respond to emergencies more rapidly, less noise pollution, less road accidents and you can make new friends to top it off.
The application is not about profiting but about car sharing so the passengers can pay the driver a fair amount for fuel costs.

## Requirements
- Java 11
- Docker

## How to run it?
From your Unix-based terminal, navigate to the root of this project and run:
```
./run-application.sh
```
This script will create a jar file from Maven package lifecycle. For convenience, there is no need to have Maven pre-installed because this project ships a Maven Wrapper.

The script also builds a Docker image named `car-migo` and spins the necessary containers. Once finished, the application will be available at http://localhost:8086/.

To stop the containers, run:
```
docker-compose down
```

### pgAdmin
This is the chosen database client.
To interact with it, go to http://localhost:8000/ from your browser to open the pgAdmin 4 UI and enter:
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

### restart.sh
This script restarts the containers. You are also given the option to restart the volumes.

## Swagger UI
OpenApi 3.0 Specification is implemented. Here are the endpoints:
* http://localhost:8086/swagger-ui/index.html
* http://localhost:8086/v3/api-docs
* http://localhost:8086/v3/api-docs.yaml (automatically downloads its yaml file)
* http://localhost:8086/v3/api-docs/swagger-config
