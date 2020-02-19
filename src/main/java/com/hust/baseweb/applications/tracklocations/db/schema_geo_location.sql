CREATE TABLE track_locations
(
    track_location_id  UUID NOT NULL default uuid_generate_v1(),
    party_id           UUID,
    location           VARCHAR(255),
    time_point         TIMESTAMP,
    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_track_locations PRIMARY KEY (track_location_id),
    CONSTRAINT track_location_party FOREIGN KEY (party_id) REFERENCES party (party_id)
);

CREATE TABLE current_locations
(
    party_id           UUID NOT NULL,
    location           VARCHAR(255),
    time_point         TIMESTAMP,
    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_current_locations_party_id PRIMARY KEY (party_id)
);
