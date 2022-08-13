CREATE TABLE IF NOT EXISTS user_access_status
(
    id INT NOT NULL
        CONSTRAINT user_access_status_pkey PRIMARY KEY,
    status VARCHAR(100) NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS platform_user_id_seq
    INCREMENT 1
    START 6;

CREATE TABLE IF NOT EXISTS platform_user
(
    id INT NOT NULL DEFAULT platform_user_id_seq.nextval
        CONSTRAINT platform_user_pkey PRIMARY KEY,
    created_date DATE NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    dob DATE NOT NULL,
    email VARCHAR(100) NOT NULL
        CONSTRAINT platform_user_email_key UNIQUE,
    password VARCHAR(65) NOT NULL,
    phone_number VARCHAR(50),
    user_access_status_id INT NOT NULL
        CONSTRAINT user_access_status_id_fkey REFERENCES user_access_status
);

CREATE TABLE IF NOT EXISTS driver
(
    id INT NOT NULL
        CONSTRAINT driver_pkey PRIMARY KEY,
    license_number VARCHAR(100),
    platform_user_id INT NOT NULL
        CONSTRAINT platform_user_id_fkey REFERENCES platform_user ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS passenger
(
    id INT NOT NULL
        CONSTRAINT passenger_pkey PRIMARY KEY,
    platform_user_id INT NOT NULL
        CONSTRAINT platform_user_id_fkey_2 REFERENCES platform_user ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS location
(
    id INT NOT NULL
        CONSTRAINT location_pkey PRIMARY KEY,
    description VARCHAR(255) NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS journey_id_seq
    INCREMENT 1
    START 8;

CREATE TABLE IF NOT EXISTS journey
(
    id  INT NOT NULL DEFAULT journey_id_seq.nextval
        CONSTRAINT journey_pkey PRIMARY KEY,
    created_date TIMESTAMP NOT NULL,
    location_id_from INT NOT NULL
        CONSTRAINT location_id_from_fkey REFERENCES location,
    location_id_to INT NOT NULL
        CONSTRAINT location_id_to_fkey REFERENCES location,
    max_passengers INT NOT NULL,
    date_time TIMESTAMP NOT NULL,
    driver_id INT
);

CREATE SEQUENCE IF NOT EXISTS passenger_journey_id_seq
    INCREMENT 1
    START 13;

CREATE TABLE IF NOT EXISTS passenger_journey
(
    id INT NOT NULL DEFAULT passenger_journey_id_seq.nextval
        CONSTRAINT passenger_journey_pkey PRIMARY KEY,
    passenger_id INT NOT NULL,
    journey_id INT NOT NULL
        CONSTRAINT journey_id_fkey REFERENCES journey
);
