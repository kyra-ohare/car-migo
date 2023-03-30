insert into platform_user(created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    values('2022-05-11', 'Jake', 'Sully', '2009-05-30', 'jake.sully@example.com', 'Pass1234!', '02865783249', 5);
insert into platform_user(created_date, first_name, last_name, dob, email, password, phone_number, user_access_status_id)
    values('2022-06-03', 'Kyra', 'OHare', '1986-01-04', 'kyra.ohare@example.com', 'Pass1234!', '02854723651', 6);

update platform_user set password='$2a$10$ewrEAQF8Gg5cRIb.AHkDluYJbZjclet3CTB1WJva2y1wXpqdKcAc6' WHERE id = 1;
update platform_user set password='$2a$10$ewrEAQF8Gg5cRIb.AHkDluYJbZjclet3CTB1WJva2y1wXpqdKcAc6' WHERE id = 2;
update platform_user set password='$2a$10$ewrEAQF8Gg5cRIb.AHkDluYJbZjclet3CTB1WJva2y1wXpqdKcAc6' WHERE id = 3;
update platform_user set password='$2a$10$ewrEAQF8Gg5cRIb.AHkDluYJbZjclet3CTB1WJva2y1wXpqdKcAc6' WHERE id = 4;
update platform_user set password='$2a$10$ewrEAQF8Gg5cRIb.AHkDluYJbZjclet3CTB1WJva2y1wXpqdKcAc6' WHERE id = 5;
update platform_user set password='$2a$10$ewrEAQF8Gg5cRIb.AHkDluYJbZjclet3CTB1WJva2y1wXpqdKcAc6' WHERE id = 6;
update platform_user set password='$2a$10$ewrEAQF8Gg5cRIb.AHkDluYJbZjclet3CTB1WJva2y1wXpqdKcAc6' WHERE id = 7;
