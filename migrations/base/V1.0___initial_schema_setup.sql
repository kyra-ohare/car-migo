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
        constraint user_email_key unique,
    password varchar(50) not null,
    phone_number varchar(50),
    user_access_status_id integer not null
        constraint user_access_status_id_fkey references user_access_status
);

create table if not exists driver
(
    id serial not null
        constraint driver_pkey primary key,
    license_number varchar(50),
    user_id integer not null
        constraint user_id_fkey references platform_user
);


create table if not exists passenger
(
    id serial not null
        constraint passenger_pkey primary key,
    user_id integer not null
        constraint user_id_fkey references platform_user
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
    max_passengers integer default 0,
    driver_id integer not null
        constraint drive_id_fkey references driver
);


create table if not exists journey_occurrence
(
    id serial not null
        constraint journey_occurrence_pkey primary key,
    date_time date not null,
    journey_id integer not null
        constraint journey_id_fkey references journey
);

create table if not exists passenger_journey_occurrence
(
    id serial not null
        constraint passenger_journey_occurrence_pkey primary key,
    passenger_id integer not null
        constraint passenger_id_fkey references passenger,
    journey_occurrence_id integer not null
        constraint journey_occurrence_id_fkey references journey_occurrence
);

create table if not exists weight
(
    id serial not null
        constraint weight_pkey primary key,
    description varchar(255) not null
);

create table if not exists review
(
    id serial not null
        constraint review_pkey primary key,
    created_date date not null,
    weight integer
        constraint weight_fkey references weight,
    review varchar(1000) not null,
    user_id_reviewer integer not null
        constraint user_id_reviewer_fkey references platform_user,
    user_id_subject integer not null
        constraint user_id_subject_fkey references platform_user
);

