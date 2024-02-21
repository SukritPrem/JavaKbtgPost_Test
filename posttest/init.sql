-- Drop tables if they exist
DROP TABLE IF EXISTS lottery CASCADE;
DROP TABLE IF EXISTS user_profile CASCADE;
DROP TABLE IF EXISTS user_ticket CASCADE;
DROP TABLE IF EXISTS user_lottery CASCADE;

CREATE TABLE lottery (
      id SERIAL PRIMARY KEY,
      price VARCHAR(255) NOT NULL,
      amount VARCHAR(255) NOT NULL,
      ticket VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE user_profile (
      id SERIAL PRIMARY KEY,
      userId VARCHAR(255) UNIQUE NOT NULL,
      roles VARCHAR(255) NOT NULL,
      encoderPassword VARCHAR(255) NOT NULL
);

CREATE TABLE user_ticket (
    id SERIAL PRIMARY KEY,
    userId VARCHAR(255) REFERENCES user_profile(userId) ON DELETE CASCADE,
    ticket VARCHAR(255) NOT NULL REFERENCES lottery(ticket) ON DELETE CASCADE,
    amount VARCHAR(255) NOT NULL,
    price VARCHAR(255) NOT NULL
);


-- Initial data
INSERT INTO lottery(price,amount,ticket) VALUES('199', '10', '000001');
--password=password1
INSERT INTO user_profile(userId, roles, encoderPassword) VALUES('0123456789','USER','$2a$10$vfDwXoqEU2jQp84JITQO7OjYVarypOxwt74H60Zo4RX2NCbE/hWHa');
--password=password2
INSERT INTO user_profile(userid, roles, encoderPassword) VALUES ('9999999999','ADMIN','$2a$10$hSUyAUw9YKvUoyH4TZdRxOBfEqqTNlQJlStR570s2Krp9euM7kDPi');

INSERT INTO user_ticket(userId, ticket, amount, price) VALUES('0123456789','000001','1','199');
--password=password3
INSERT INTO user_profile(userid, roles, encoderPassword) VALUES ('0123456781','USER','$2a$10$gsWN3aRlWJMK1pl0rd/8KuPXI7pwWhA9VLD5rhjqARIdbauop9332');

INSERT INTO user_ticket(userId, ticket, amount, price) VALUES('0123456781','000001','1', '199');
