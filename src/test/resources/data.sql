INSERT INTO addresses (country, postalCode, region, city, streetAddress)
VALUES ('UK', '12345', 'City-Of-Edinburgh', 'Edinburgh', 'Panmure House'),
       ('Poland', '54321', 'Central region', 'Warsaw', 'Urszuli Ledochowskiej 3'),
       ('Russia', '450080', 'Permskiy kray', 'Perm', 'Lenina 5'),
       ('USA', '90210', 'California', 'LA', 'Grove St. 15'),
       ('France', '21012', 'Central', 'Paris', 'Rue 15'),
       ('China', '20121', 'Guangdung', 'Beijin', 'Main St. 125');

INSERT INTO subjects (name, description)
VALUES ('Economics', 'Base economics'),
       ('Philosophy', 'Base philosophy'),
       ('Chemistry', 'Base chemistry'),
       ('Radiology', 'Explore radiation');

INSERT INTO teachers (first_name, last_name, gender, degree, email, phone, address_id)
VALUES ('Adam', 'Smith', 'MALE', 'DOCTOR', 'adam@smith.com', '+223322', 1),
       ('Marie', 'Curie', 'FEMALE', 'MASTER', 'marie@curie.com', '+322223', 2);

INSERT INTO vacations (teacher_id, start_date, end_date)
VALUES (1, '2000-01-01', '2000-02-01'),
       (1, '2000-05-01', '2000-06-01'),
       (2, '2000-01-15', '2000-02-15'),
       (2, '2000-06-01', '2000-07-01');

TRUNCATE TABLE teachers_subjects;

INSERT INTO teachers_subjects(teacher_id, subject_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (2, 4);

INSERT INTO groups (name)
VALUES ('AB-11'),
       ('ZI-08');

INSERT INTO students (first_name, last_name, gender, birth_date, email, phone, address_id, group_id)
VALUES ('Ivan', 'Petrov', 'MALE', '1980-11-1', 'qwe@rty.com', '123123123', 3, 1),
       ('John', 'Doe', 'MALE', '1981-11-1', 'qwe@qwe.com', '1231223', 4, 2),
       ('Janna', 'DArk', 'FEMALE', '1881-11-1', 'qwe@no.fr', '1231223', 5, 1),
       ('Mao', 'Zedun', 'MALE', '1921-9-14', 'qwe@no.cn', '1145223', 6, 2);


INSERT INTO locations (building, floor, room_number)
VALUES ('Phys building', 2, 22),
       ('Chem building', 1, 12),
       ('Chem building', 2, 12);

INSERT INTO classrooms (location_id, name, capacity)
VALUES (1, 'Big physics auditory', 500),
       (2, 'Small chemistry auditory', 30),
       (3, 'Chemistry laboratory', 15);

INSERT INTO timeslots (begin_time, end_time)
VALUES ('09:00:00', '9:45:00'),
       ('10:00:00', '10:45:00'),
       ('11:00:00', '11:45:00');

INSERT INTO lectures (date, timeslot_id, subject_id, teacher_id, classroom_id)
VALUES ('2020-1-1', 1, 1, 1, 1),
       ('2020-1-2', 2, 2, 2, 2);

INSERT INTO holidays (date, name)
VALUES ('2000-12-25', 'Christmas'),
       ('2000-10-30', 'Halloween'),
       ('2000-3-8', 'International womens day');

TRUNCATE TABLE lectures_groups;
INSERT INTO lectures_groups (lecture_id, group_id)
VALUES (1, 1),
       (1, 2),
       (2, 1);