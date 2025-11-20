CREATE TABLE garages
(
    sector            VARCHAR(255) PRIMARY KEY,
    base_price        DECIMAL(10, 2) NOT NULL,
    max_capacity      INT            NOT NULL,
    current_occupancy INT            NOT NULL
);