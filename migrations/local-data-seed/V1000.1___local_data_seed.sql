insert into platform_user(created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    values('2021-12-24', 'John', 'Smith', '1970-02-23', 'john.smith@example.com', 'Pass1234!', '0287513626', 1);
insert into platform_user(created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    values('2022-01-04', 'Mary', 'Green', '1990-06-06', 'mary.green@example.com', 'Pass1234!', '0286579635', 2);
insert into platform_user(created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    values('2022-01-05', 'Paul', 'Gibson', '1995-07-08', 'paul.gipson@example.com', 'Pass1234!', '0286547891', 2);
insert into platform_user(created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    values('2022-01-28', 'Jess', 'Brown', '1986-01-04', 'jess.brown@example.com', 'Pass1234!', '0283215978', 3);
insert into platform_user(created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    values('2022-02-01', 'Tom', 'Thompson', '2000-09-30', 'tom.thompson@example.com', 'Pass1234!', '0286782453', 4);


insert into driver(license_number, platform_user_id)
    values('35789654', 1);
insert into driver(license_number, platform_user_id)
    values('16548329', 2);
insert into driver(license_number, platform_user_id)
    values('35789126', 3);
insert into driver(license_number, platform_user_id)
    values('65782349', 4);
insert into driver(license_number, platform_user_id)
    values('67324982', 5);


insert into passenger(platform_user_id)
    values(1);
insert into passenger(platform_user_id)
    values(2);
insert into passenger(platform_user_id)
    values(3);
insert into passenger(platform_user_id)
    values(4);
insert into passenger(platform_user_id)
    values(5);


insert into journey(created_date, location_id_from, location_id_to, max_passengers, date_time, driver_id)
    values('2021-11-30', 1, 2, 2, '2021-12-01 09:00:00', 1);
insert into journey(created_date, location_id_from, location_id_to, max_passengers, date_time, driver_id)
    values('2021-11-30', 3, 4, 3, '2021-12-02 09:00:00', 1);
insert into journey(created_date, location_id_from, location_id_to, max_passengers, date_time, driver_id)
    values('2022-01-04', 4, 3, 4, '2022-01-04 15:30:00', 2);
insert into journey(created_date, location_id_from, location_id_to, max_passengers, date_time, driver_id)
    values('2022-01-05', 5, 2, 3, '2022-12-02 08:00:00', 3);
insert into journey(created_date, location_id_from, location_id_to, max_passengers, date_time, driver_id)
    values('2022-01-05', 5, 1, 3, '2022-12-02 08:15:00', 3);
insert into journey(created_date, location_id_from, location_id_to, max_passengers, date_time, driver_id)
    values('2022-01-05', 5, 1, 3, '2022-12-03 08:00:00', 3);


insert into passenger_journey(passenger_id, journey_id)
    values(2, 1);
insert into passenger_journey(passenger_id, journey_id)
    values(3, 1);

insert into passenger_journey(passenger_id, journey_id)
    values(4, 2);
insert into passenger_journey(passenger_id, journey_id)
    values(5, 2);
insert into passenger_journey(passenger_id, journey_id)
    values(3, 2);

insert into passenger_journey(passenger_id, journey_id)
    values(1, 3);
insert into passenger_journey(passenger_id, journey_id)
    values(3, 3);
insert into passenger_journey(passenger_id, journey_id)
    values(4, 3);

insert into passenger_journey(passenger_id, journey_id)
    values(4, 4);
insert into passenger_journey(passenger_id, journey_id)
    values(5, 4);

insert into passenger_journey(passenger_id, journey_id)
    values(1, 5);
insert into passenger_journey(passenger_id, journey_id)
    values(2, 5);
