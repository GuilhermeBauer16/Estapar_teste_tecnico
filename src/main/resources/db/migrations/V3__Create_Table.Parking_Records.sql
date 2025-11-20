CREATE TABLE parking_records
(

    id                      BIGINT PRIMARY KEY,


    license_plate           VARCHAR(10)    NOT NULL,


    sector                  VARCHAR(255)   NOT NULL,


    entry_time              TIMESTAMP      NOT NULL,

    exit_time               TIMESTAMP,

    effective_hourly_rate   DECIMAL(10, 2) NOT NULL,


    calculated_hourly_price DECIMAL(10, 2),


    spot_id                 BIGINT         NOT NULL,


    CONSTRAINT fk_parking_sector
        FOREIGN KEY (sector)
            REFERENCES garages (sector),

    CONSTRAINT fk_parking_spot
        FOREIGN KEY (spot_id)
            REFERENCES spots (id)
);