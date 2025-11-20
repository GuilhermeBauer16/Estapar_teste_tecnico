CREATE TABLE spots
(

    id                        BIGINT PRIMARY KEY,


    lat                       NUMERIC(9, 6) NOT NULL,
    lng                       NUMERIC(9, 6) NOT NULL,


    sector                    VARCHAR(255)  NOT NULL,


    is_occupied               BOOLEAN       NOT NULL DEFAULT FALSE,


    occupied_by_license_plate VARCHAR(10),


    CONSTRAINT fk_spot_sector
        FOREIGN KEY (sector)
            REFERENCES garages (sector)
);