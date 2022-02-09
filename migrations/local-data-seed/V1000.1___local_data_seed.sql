insert into platform_user(id, created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    values(1, '2021-12-24', 'John', 'Smith', '1970-02-23', 'john.smith@example.com', 'Pass1234!', '0287513626', 1);
insert into platform_user(id, created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    values(2, '2022-01-04', 'Mary', 'Green', '1990-06-06', 'mary.green@example.com', 'Pass1234!', '0286579635', 1);
insert into platform_user(id, created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    values(3, '2022-01-05', 'Paul', 'Gipson', '1995-07-08', 'paul.gipson@example.com', 'Pass1234!', '0286547891', 1);
insert into platform_user(id, created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    values(4, '2022-01-28', 'Jess', 'Brown', '1986-01-04', 'jess.brown@example.com', 'Pass1234!', '0283215978', 1);
insert into platform_user(id, created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    values(5, '2022-02-01', 'Tom', 'Thompson', '2000-09-30', 'tom.thompson@example.com', 'Pass1234!', '0286782453', 1);


insert into driver(id, license_number, user_id)
    values(1, '35789654', 1);
insert into driver(id, license_number, user_id)
    values(2, '16548329', 2);
insert into driver(id, license_number, user_id)
    values(3, '35789126', 3);
insert into driver(id, license_number, user_id)
    values(4, '65782349', 4);
insert into driver(id, license_number, user_id)
    values(5, '67324982', 5);


insert into passenger(id, user_id)
    values(1, 1);
insert into passenger(id, user_id)
    values(2, 2);
insert into passenger(id, user_id)
    values(3, 3);
insert into passenger(id, user_id)
    values(4, 4);
insert into passenger(id, user_id)
    values(5, 5);


insert into journey(id, created_date, location_id_from, location_id_to, max_passengers, driver_id)
    values(1, '2021-11-30', 1, 2, 2, 1);
insert into journey(id, created_date, location_id_from, location_id_to, max_passengers, driver_id)
    values(2, '2021-12-31', 3, 4, 3, 2);
insert into journey(id, created_date, location_id_from, location_id_to, max_passengers, driver_id)
    values(3, '2022-01-04', 4, 3, 4, 3);
insert into journey(id, created_date, location_id_from, location_id_to, max_passengers, driver_id)
    values(4, '2022-01-05', 5, 2, 3, 4);
insert into journey(id, created_date, location_id_from, location_id_to, max_passengers, driver_id)
    values(5, '2022-02-09', 5, 1, 3, 5);


insert into journey_occurrence(id, date_time, journey_id)
    values(1, '2021-11-30', 1);
insert into journey_occurrence(id, date_time, journey_id)
    values(2, '2022-01-04', 2);
insert into journey_occurrence(id, date_time, journey_id)
    values(3, '2022-01-05', 2);
insert into journey_occurrence(id, date_time, journey_id)
    values(4, '2022-01-06', 2);
insert into journey_occurrence(id, date_time, journey_id)
    values(5, '2022-01-11', 3);
insert into journey_occurrence(id, date_time, journey_id)
    values(6, '2022-01-18', 3);


insert into passenger_journey_occurrence(id, passenger_id, journey_occurrence_id)
    values(1, 2, 1);
insert into passenger_journey_occurrence(id, passenger_id, journey_occurrence_id)
    values(2, 3, 1);

insert into passenger_journey_occurrence(id, passenger_id, journey_occurrence_id)
    values(3, 4, 2);
insert into passenger_journey_occurrence(id, passenger_id, journey_occurrence_id)
    values(4, 5, 2);
insert into passenger_journey_occurrence(id, passenger_id, journey_occurrence_id)
    values(5, 3, 2);

insert into passenger_journey_occurrence(id, passenger_id, journey_occurrence_id)
    values(6, 1, 3);
insert into passenger_journey_occurrence(id, passenger_id, journey_occurrence_id)
    values(7, 3, 3);
insert into passenger_journey_occurrence(id, passenger_id, journey_occurrence_id)
    values(8, 4, 3);

insert into passenger_journey_occurrence(id, passenger_id, journey_occurrence_id)
    values(9, 4, 4);
insert into passenger_journey_occurrence(id, passenger_id, journey_occurrence_id)
    values(10, 5, 4);

insert into passenger_journey_occurrence(id, passenger_id, journey_occurrence_id)
    values(11, 1, 5);
insert into passenger_journey_occurrence(id, passenger_id, journey_occurrence_id)
    values(12, 2, 5);
