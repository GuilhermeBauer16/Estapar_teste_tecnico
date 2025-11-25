CREATE TABLE IF NOT EXISTS spots (

    id BIGINT NOT NULL,

    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    is_occupied BOOLEAN NOT NULL DEFAULT FALSE,
    sector_fk VARCHAR(10) NOT NULL,

    PRIMARY KEY (`id`),


    INDEX fk_spots_garages_idx (sector_fk ASC) VISIBLE,
    CONSTRAINT fk_spots_garages
    FOREIGN KEY (sector_fk)
    REFERENCES `garages` (sector)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
    )