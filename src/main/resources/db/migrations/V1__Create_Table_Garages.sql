CREATE TABLE IF NOT EXISTS garages (

    sector VARCHAR(10) NOT NULL,


    base_price DECIMAL(10, 2) NOT NULL,
    max_capacity INT NOT NULL,


    current_occupancy INT NOT NULL DEFAULT 0,

    open_hour VARCHAR(5) NULL,
    close_hour VARCHAR(5) NULL,
    duration_limit_minutes INT NULL,

    PRIMARY KEY (sector)
    )