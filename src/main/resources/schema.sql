DROP TABLE IF EXISTS users; -- remove this once we have data to work with :)

CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

INSERT INTO users (email, password, role, name) VALUES
    ('user1@example.com', 'password1', 'USER', 'Name 1'),
    ('user2@example.com', 'password2', 'ADMIN', 'Name 2'),
    ('user3@example.com', 'password3', 'USER', 'Name 3'),
    ('user4@example.com', 'password4', 'USER', 'Name 4'),
    ('user5@example.com', 'password5', 'ADMIN', 'Name 5'),
    ('user6@example.com', 'password6', 'USER', 'Name 6'),
    ('user7@example.com', 'password7', 'USER', 'Name 7'),
    ('user8@example.com', 'password8', 'ADMIN', 'Name 8'),
    ('user9@example.com', 'password9', 'USER', 'Name 9'),
    ('user10@example.com', 'password10', 'USER', 'Name 10');
