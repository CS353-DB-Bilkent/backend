-- noinspection SqlNoDataSourceInspectionForFile

DROP TABLE IF EXISTS users CASCADE; -- remove this once we have data to work with :)
DROP TABLE IF EXISTS tickets CASCADE; -- remove this once we have data to work with :)
DROP TABLE IF EXISTS wallets CASCADE; -- remove this once we have data to work with :)
DROP TABLE IF EXISTS transactions CASCADE; -- remove this once we have data to work with :)

-- combine all the users into one schema, differentiate via role. This way, it's easier to manage in code and we are not
-- building the next facebook, so who cares.
CREATE TABLE users (
    user_id         SERIAL PRIMARY KEY,
    email           VARCHAR(255) NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    name            VARCHAR(255),
    phone           VARCHAR(20),
    registered_date DATE,
    IBAN            VARCHAR(34),
    company_name    VARCHAR(255),
    role VARCHAR(255) NOT NULL,
    birth_date DATe NOT NULL
);

CREATE TABLE tickets (
     ticket_id SERIAL PRIMARY KEY,
     event_id INT NOT NULL,
     user_id INT NOT NULL, -- Each ticket belongs to only one user (or at least bought by)
     seat_id VARCHAR(255) NOT NULL,
     price DECIMAL(10, 2) NOT NULL,
     status VARCHAR(255) NOT NULL,
     ticket_type VARCHAR(255) NOT NULL, -- update this once fully determined, or just leave like this who cares...
     FOREIGN KEY (user_id) REFERENCES users (user_id)
);

-- merge relation into wallet entity
CREATE TABLE wallets (
    wallet_id SERIAL PRIMARY KEY,
    balance DECIMAL(10, 2), -- 2 decimals for currency
    user_id INT, -- fk
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- merge relation into transaction entity
CREATE TABLE transactions (
     transaction_id SERIAL PRIMARY KEY,
     wallet_id INT, -- fk
     transaction_amount DECIMAL(10, 2), -- Assuming 2 decimal places for currency
     transaction_type VARCHAR(100), -- Assuming a varchar to describe the type like 'debit', 'credit'
     transaction_date DATE,
     FOREIGN KEY (wallet_id) REFERENCES wallets(wallet_id)
);

INSERT INTO users (email, password, name, phone, registered_date, IBAN, company_name, role, birth_date) VALUES
     ('admin@example.com', 'securepassword', 'Admin User', '555-357-0100', '2024-01-01', NULL, NULL, 'ADMIN', '2000-01-01'),
     ('organizer@example.com', 'securepassword', 'Event Organizer', '555-333-0101', '2024-01-02', 'TR330006100519786457841326', 'EventOrg Co.', 'EVENT_ORGANIZER', '2000-01-02'),
     ('user@example.com', 'securepassword', 'Normal User', '555-111-0102', '2024-01-03', NULL, NULL, 'NORMAL', '2000-01-03');

INSERT INTO tickets (event_id, user_id, seat_id, price, status, ticket_type) VALUES
     (1, 1, 'A1', 90.00, 'SOLD', 'PREMIUM'),
     (1, 2, 'B2', 50.00, 'AVAILABLE', 'REGULAR'),
     (1, 3, 'C3', 75.00, 'RESERVED', 'PREMIUM');

INSERT INTO wallets (balance, user_id) VALUES
     (1500.00, 2),
     (2200.50, 3);

INSERT INTO transactions (wallet_id, transaction_amount, transaction_type, transaction_date) VALUES
    (1, 200.00, 'WITHDRAWAL', '2022-01-16'),
    (1, 50.00, 'DEPOSIT', '2022-01-20'),
    (1, 100.00, 'WITHDRAWAL', '2022-01-25');

INSERT INTO transactions (wallet_id, transaction_amount, transaction_type, transaction_date) VALUES
    (2, 300.00, 'DEPOSIT', '2022-02-21'),
    (2, 70.00, 'WITHDRAWAL', '2022-02-25'),
    (2, 150.00, 'DEPOSIT', '2022-03-01');
