CREATE DATABASE IF NOT EXISTS task;

USE task;
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    password VARCHAR(50)
);

INSERT INTO users (username, password) VALUES
    ('random', 'random');

CREATE TABLE Redacted_TABLENAME (
    Redacted_COLUMN VARCHAR(100)
);
INSERT INTO Redacted_TABLENAME (Redacted_COLUMN) VALUES
    ('');
