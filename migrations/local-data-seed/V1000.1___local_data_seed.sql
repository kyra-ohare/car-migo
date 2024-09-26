insert into platform_user(created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    values('2024-09-24', 'John', 'Smith', '1991-12-12', 'john.smith@example.com', 'Pass1234!', '0287513626', 2);
insert into platform_user(created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    values('2024-01-04', 'Mary', 'Green', '1990-06-30', 'mary.green@example.com', 'Pass1234!', '0286579635', 2);
insert into platform_user(created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    values('2024-01-05', 'Paul', 'Gibson', '1995-07-08', 'paul.gibson@example.com', 'Pass1234!', '0286547891', 2);
insert into platform_user(created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    values('2024-01-28', 'Jess', 'Brown', '1986-01-04', 'jess.brown@example.com', 'Pass1234!', '0283215978', 3);
insert into platform_user(created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    values('2024-02-01', 'Tom', 'Thompson', '2000-09-30', 'tom.thompson@example.com', 'Pass1234!', '0286782453', 4);
insert into platform_user(created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    values('2024-05-11', 'Jake', 'Sully', '2009-05-30', 'jake.sully@example.com', 'Pass1234!', '02865783249', 5);
insert into platform_user(created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    values('2024-06-03', 'Kyra', 'OHare', '1986-01-04', 'kyra.ohare@example.com', 'Pass1234!', '02854723651', 6);
insert into platform_user(created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    values('2024-09-24', 'Peter', 'McDonald', '1995-03-25', 'petr@mcdonald@example.com', 'Pass1234!', '0287513626', 1);

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
    values('2024-08-30', 1, 2, 2, '2024-09-01 09:00:00', 1);
insert into journey(created_date, location_id_from, location_id_to, max_passengers, date_time, driver_id)
    values('2024-08-30', 3, 4, 3, '2024-09-02 09:00:00', 1);
insert into journey(created_date, location_id_from, location_id_to, max_passengers, date_time, driver_id)
    values('2024-01-04', 4, 3, 3, '2024-01-04 15:30:00', 2);
insert into journey(created_date, location_id_from, location_id_to, max_passengers, date_time, driver_id)
    values('2024-01-05', 5, 2, 2, '2024-01-06 08:00:00', 3);
insert into journey(created_date, location_id_from, location_id_to, max_passengers, date_time, driver_id)
    values('2024-01-05', 5, 1, 2, '2024-02-02 08:15:00', 3);
insert into journey(created_date, location_id_from, location_id_to, max_passengers, date_time, driver_id)
    values('2024-01-05', 5, 1, 1, '2024-03-03 08:00:00', 3);


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
