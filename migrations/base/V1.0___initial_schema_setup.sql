create table if not exists public.user_access_status
(
    id serial not null
        constraint user_access_status_pkey primary key,
    status varchar(100) not null
);

create table if not exists public.user
(
    id serial not null
        constraint user_pkey primary key,
    created_date date not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    dob date not null,
    email varchar(255) not null
        constraint user_email_key unique,
    password varchar(50) not null,
    phone_number varchar(50),
    user_access_status_id integer not null
        constraint user_access_status_id_fkey references public.user_access_status
);

create table if not exists public.driver
(
    id serial not null
        constraint driver_pkey primary key,
    license_number varchar(50),
    user_id integer not null
        constraint user_id_fkey references public.user
);


create table if not exists public.passenger
(
    id serial not null
        constraint passenger_pkey primary key,
    user_id integer not null
        constraint user_id_fkey references public.user
);

create table if not exists public.location
(
    id serial not null
        constraint location_pkey primary key,
    description varchar(255) not null
);

create table if not exists public.journey
(
    id serial not null
        constraint journey_pkey primary key,
    created_date date not null,
    location_id_from integer not null
        constraint location_id_from_fkey references public.location,
    location_id_to integer not null
        constraint location_id_to_fkey references public.location,
    max_passengers integer default 0,
    driver_id integer not null
        constraint drive_id_fkey references public.driver
);


create table if not exists public.journey_occurrence
(
    id serial not null
        constraint journey_occurrence_pkey primary key,
    date_time date not null,
    journey_id integer not null
        constraint journey_id_fkey references public.journey
);

create table if not exists public.passenger_journey_occurrence
(
    id serial not null
        constraint passenger_journey_occurrence_pkey primary key,
    passenger_id integer not null
        constraint passenger_id_fkey references public.passenger,
    journey_occurrence_id integer not null
        constraint journey_occurrence_id_fkey references public.journey_occurrence
);

create table if not exists public.weight
(
    id serial not null
        constraint weight_pkey primary key,
    description varchar(255) not null
);

create table if not exists public.review
(
    id serial not null
        constraint review_pkey primary key,
    created_date date not null,
    weight integer
        constraint weight_fkey references public.weight,
    review varchar not null,
    user_id_reviewer integer not null
        constraint user_id_reviewer_fkey references public.user,
    user_id_subject integer not null
        constraint user_id_subject_fkey references public.user
);

