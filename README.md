# Car-Migo

Car-migo application is awesome! It will revolutionize the way you get around. It is eco-friendly and helps your pocket.

## What is the application?
The application is quite straightway, you can either advertise rides or query them.

It is a match-making system for drivers and passengers. You can either advertise rides or query them.
Drivers can create rides (a journey and a timeframe) while passengers can book them.

When inside the car share, you can enjoy the ride and make new friends. Come onboard and experience this new lifestyle.

There are 5 types of users:
* `Staged` users are when a user has created the account but has not confirmed the email yet.
* `Active` users have access to non-admin endpoints.
* `Suspended` users can only access their profiles and update some information.
* `Locked out` users cannot access any endpoints because they had 5 failed attempts to access their accounts.
* `Admin` users can access admin and non-admin endpoints as well as actuator endpoints.
* `Dev` users can access their profiles, update some information and access the actuator endpoints.

## Why?
It is great for the environment once there will be less CO<sub>2</sub> released into the atmosphere.
Moreover, there will be less traffic in our cities thus emergency vehicles will respond to emergencies more rapidly, 
less noise pollution, less road accidents, and you can make new friends to top it off.
The application is not about profiting but about car sharing so the passengers can pay the driver a fair amount for fuel costs.

## Requirements
- Java 17
- Docker

## How to run it?
From your Unix-based terminal, navigate to the root of this project and run:
```
./run-app.sh
```
This script will create a jar file from Maven package lifecycle. For convenience, there is no need to have Maven
pre-installed because this project ships a Maven Wrapper.

The script also builds a Docker image named `car-migo` and spins the necessary containers. Once finished, the application
will be available at http://localhost:8086/.

Visit http://localhost:8086/v1/heartbeat to ensure everything is running as expected.

To stop and remove the containers, run:
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
This script restarts the containers. You are also given the option to restart PostgreSQL and pgAdmin volumes.

## CI/CD
Git Actions is triggered everytime there is a new code push or a new pull request against the `main` branch.

The script will build the application and run tests using Apache Maven.
It will also scan the code and produce a security report using CodeQL Analysis.

Moreover, a Docker image is built and sent to [Docker Hub](#docker-hub) when the code is merged into the main branch.

## Swagger UI
OpenApi 3.0 Specification is implemented. Here are the endpoints:
* http://localhost:8086/swagger-ui/index.html
* http://localhost:8086/v3/api-docs
* http://localhost:8086/v3/api-docs.yaml (automatically downloads its yaml file)
* http://localhost:8086/v3/api-docs/swagger-config

Open endpoints, i.e., no credentials needed:
* http://localhost:8086/v1/users/create
* http://localhost:8086/v1/login
* http://localhost:8086/v1/journeys/calculateDistance
* http://localhost:8086/v1/journeys/search

## Docker Hub
The latest car-migo image is available at https://hub.docker.com/r/kleydocker/car-migo
```
docker pull kleydocker/car-migo
```
