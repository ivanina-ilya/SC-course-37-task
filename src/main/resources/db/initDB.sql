DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS
(
  id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  firstName VARCHAR(64) NOT NULL,
  lastName VARCHAR(64),
  birthday DATETIME,
  email VARCHAR(64) NOT NULL
);
CREATE UNIQUE INDEX users_email_uindex ON users (email);

DROP TABLE IF EXISTS Auditoriums;

CREATE TABLE Auditoriums
(
  id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  name VARCHAR(64) NOT NULL,
  numberOfSeats INT DEFAULT 0 NOT NULL,
  vipSeats VARCHAR(512)
);
CREATE UNIQUE INDEX Auditoriums_name_uindex ON Auditoriums (name);

DROP TABLE IF EXISTS Events;

CREATE TABLE Events
(
  id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  basePrice DOUBLE,
  eventRating VARCHAR(32),
  name VARCHAR(64) NOT NULL,
  durationMilliseconds BIGINT
);
CREATE UNIQUE INDEX Events_name_uindex ON Events (name);

DROP TABLE IF EXISTS EventToAuditorium;

CREATE TABLE EventToAuditorium
(
  event_id BIGINT NOT NULL,
  auditorium_id BIGINT NOT NULL,
  airDate DATETIME NOT NULL
);
CREATE UNIQUE INDEX EventToAuditorium_event_id_auditorium_id_airDate_uindex ON EventToAuditorium (event_id, auditorium_id, airDate);



DROP TABLE IF EXISTS Tickets;

CREATE TABLE Tickets
(
  id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  user_id BIGINT,
  event_id BIGINT NOT NULL,
  dateTime DATETIME NOT NULL,
  seat INT,
  price DOUBLE
);

DROP TABLE IF EXISTS TicketsToUser;

CREATE TABLE TicketsToUser
(
  ticket_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL
);
CREATE UNIQUE INDEX TicketsToUser_ticket_id_user_id_uindex ON TicketsToUser (ticket_id, user_id);