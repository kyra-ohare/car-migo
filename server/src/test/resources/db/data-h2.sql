INSERT INTO user_access_status(id, status) VALUES(1, 'STAGED');
INSERT INTO user_access_status(id, status) VALUES(2, 'ACTIVE');
INSERT INTO user_access_status(id, status) VALUES(3, 'SUSPENDED');
INSERT INTO user_access_status(id, status) VALUES(4, 'LOCKED_OUT');
INSERT INTO user_access_status(id, status) VALUES(5, 'ADMIN');
INSERT INTO user_access_status(id, status) VALUES(6, 'DEV');

INSERT INTO platform_user(
        id, created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    VALUES(1, '2021-12-24', 'John', 'Smith', '1970-02-23', 'staged@example.com', 'Pass1234!', '0287513626', 1);
INSERT INTO platform_user(
        id, created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    VALUES(2, '2022-01-04', 'Mary', 'Green', '1990-06-06', 'active@example.com', 'Pass1234!', '0286579635', 2);
INSERT INTO platform_user(
        id, created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    VALUES(3, '2022-01-28', 'Jess', 'Brown', '1986-01-04', 'suspended@example.com', 'Pass1234!', '0283215978', 3);
INSERT INTO platform_user(
        id, created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    VALUES(4,'2022-02-01', 'Tom', 'Thompson', '2000-09-30', 'locked_out@example.com', 'Pass1234!', '0286782453', 4);
INSERT INTO platform_user(
        id, created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    VALUES(5, '2022-01-05', 'Jake', 'Sully', '1995-07-08', 'admin@example.com', 'Pass1234!', '0286547891', 5);

INSERT INTO driver(id, license_number, platform_user_id) VALUES(1, '35789654', 1);
INSERT INTO driver(id, license_number, platform_user_id) VALUES(2, '16548329', 2);
INSERT INTO driver(id, license_number, platform_user_id) VALUES(3, '35789126', 3);
INSERT INTO driver(id, license_number, platform_user_id) VALUES(4, '65782349', 4);
INSERT INTO driver(id, license_number, platform_user_id) VALUES(5, '67324982', 5);

INSERT INTO passenger(id, platform_user_id) VALUES(1, 1);
INSERT INTO passenger(id, platform_user_id) VALUES(2, 2);
INSERT INTO passenger(id, platform_user_id) VALUES(3, 3);
INSERT INTO passenger(id, platform_user_id) VALUES(4, 4);
INSERT INTO passenger(id, platform_user_id) VALUES(5, 5);

INSERT INTO location(id, description) VALUES(1, 'Rostrevor');
INSERT INTO location(id, description) VALUES(2, 'Belfast');
INSERT INTO location(id, description) VALUES(3, 'Downpatrick');
INSERT INTO location(id, description) VALUES(4, 'Armagh');
INSERT INTO location(id, description) VALUES(5, 'Newry');

INSERT INTO journey(id, created_date, location_id_from, location_id_to, max_passengers, date_time, driver_id)
    VALUES(1, '2021-11-30', 1, 2, 2, '2021-12-01 09:00:00', 1);
INSERT INTO journey(id, created_date, location_id_from, location_id_to, max_passengers, date_time, driver_id)
    VALUES(2, '2021-11-30', 3, 4, 3, '2021-12-02 09:00:00', 1);
INSERT INTO journey(id, created_date, location_id_from, location_id_to, max_passengers, date_time, driver_id)
    VALUES(3, '2022-01-04', 4, 3, 4, '2022-01-04 15:30:00', 2);
INSERT INTO journey(id, created_date, location_id_from, location_id_to, max_passengers, date_time, driver_id)
    VALUES(4, '2022-01-05', 5, 2, 3, '2022-12-02 08:00:00', 3);
INSERT INTO journey(id, created_date, location_id_from, location_id_to, max_passengers, date_time, driver_id)
    VALUES(5, '2022-01-05', 5, 1, 3, '2022-12-02 08:15:00', 3);
INSERT INTO journey(id, created_date, location_id_from, location_id_to, max_passengers, date_time, driver_id)
    VALUES(6, '2022-01-05', 5, 1, 3, '2022-12-03 08:00:00', 2);
INSERT INTO journey(id, created_date, location_id_from, location_id_to, max_passengers, date_time, driver_id)
    VALUES(7, '2022-05-10', 2, 1, 2, '2022-12-03 08:00:00', 4);
INSERT INTO journey(id, created_date, location_id_from, location_id_to, max_passengers, date_time, driver_id)
    VALUES(8, '2022-06-11', 5, 1, 3, '2022-10-04 18:00:00', 2);
INSERT INTO journey(id, created_date, location_id_from, location_id_to, max_passengers, date_time, driver_id)
    VALUES(9, '2022-07-12', 2, 4, 2, '2022-09-05 19:00:00', 5);
INSERT INTO journey(id, created_date, location_id_from, location_id_to, max_passengers, date_time, driver_id)
    VALUES(10, '2022-08-13', 1, 5, 1, '2022-08-06 20:00:00', 3);

INSERT INTO passenger_journey(id, passenger_id, journey_id) VALUES(1, 2, 1);
INSERT INTO passenger_journey(id, passenger_id, journey_id) VALUES(2, 3, 1);
INSERT INTO passenger_journey(id, passenger_id, journey_id) VALUES(3, 4, 2);
INSERT INTO passenger_journey(id, passenger_id, journey_id) VALUES(4, 5, 2);
INSERT INTO passenger_journey(id, passenger_id, journey_id) VALUES(5, 3, 2);
INSERT INTO passenger_journey(id, passenger_id, journey_id) VALUES(6, 1, 3);
INSERT INTO passenger_journey(id, passenger_id, journey_id) VALUES(7, 3, 3);
INSERT INTO passenger_journey(id, passenger_id, journey_id) VALUES(8, 4, 3);
INSERT INTO passenger_journey(id, passenger_id, journey_id) VALUES(9, 4, 4);
INSERT INTO passenger_journey(id, passenger_id, journey_id) VALUES(10, 5, 4);
INSERT INTO passenger_journey(id, passenger_id, journey_id) VALUES(11, 1, 5);
INSERT INTO passenger_journey(id, passenger_id, journey_id) VALUES(12, 2, 5);
