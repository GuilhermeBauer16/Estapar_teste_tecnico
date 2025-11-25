CREATE TABLE IF NOT EXISTS parking_events (

    id BIGINT NOT NULL AUTO_INCREMENT,
    license_plate VARCHAR(10) NOT NULL UNIQUE ,
    entry_time TIMESTAMP NOT NULL,
    exit_time TIMESTAMP NULL,
    final_amount DECIMAL(10, 2) NULL,
    applied_discount_rate DECIMAL(5, 2) NULL,
    lat DECIMAL(10, 8) NOT NULL,
    lnt DECIMAL(11, 8) NOT NULL,
    spot_id_fk BIGINT NOT NULL,
    sector_fk VARCHAR(10) NOT NULL,

    PRIMARY KEY (id),
    INDEX idx_license_plate (license_plate ASC) VISIBLE,
    CONSTRAINT fk_events_spot
    FOREIGN KEY (spot_id_fk)
    REFERENCES spots (id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,


    CONSTRAINT fk_events_garage
    FOREIGN KEY (sector_fk)
    REFERENCES garages (sector)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
    )