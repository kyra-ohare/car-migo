create table if not exists user_access_status
(
    id serial not null
        constraint user_access_status_pkey primary key,
    status varchar(100) not null
);

create table if not exists platform_user
(
    id serial not null
        constraint platform_user_pkey primary key,
    created_date date not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    dob date not null,
    email varchar(100) not null
        constraint platform_user_email_key unique,
    password varchar(50) not null,
    phone_number varchar(50),
    user_access_status_id integer not null
        constraint user_access_status_id_fkey references user_access_status
);

create table if not exists driver
(
    id serial not null
        constraint driver_pkey primary key,
    license_number varchar(100),
    platform_user_id integer unique not null
        constraint platform_user_id_fkey references platform_user
);


create table if not exists passenger
(
    id serial not null
        constraint passenger_pkey primary key,
    platform_user_id integer unique not null
        constraint platform_user_id_fkey references platform_user
);

create table if not exists location
(
    id serial not null
        constraint location_pkey primary key,
    description varchar(255) not null
);

create table if not exists journey
(
    id serial not null
        constraint journey_pkey primary key,
    created_date date not null,
    location_id_from integer not null
        constraint location_id_from_fkey references location,
    location_id_to integer not null
        constraint location_id_to_fkey references location,
    max_passengers integer not null,
    date_time timestamp not null,
    driver_id integer
        constraint journey_driver_id_fkey references driver ON DELETE CASCADE
);

create table if not exists passenger_journey
(
    id serial not null
        constraint passenger_journey_pkey primary key,
    passenger_id integer not null
        constraint passenger_id_fkey references passenger ON DELETE CASCADE,
    journey_id integer not null
        constraint journey_id_fkey references journey ON DELETE CASCADE
);
