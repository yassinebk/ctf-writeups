SET GLOBAL event_scheduler = ON;

CREATE DATABASE IF NOT EXISTS intelDB;


USE intelDB;

CREATE TABLE IF NOT EXISTS soldiers (
    username CHAR(20),
    password CHAR(20)
);

INSERT INTO soldiers VALUES ('admin', 'not_that_easy ;)');
