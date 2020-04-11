CREATE TABLE facility_type
(
    facility_type_id   VARCHAR(60) NOT NULL,
    parent_type_id     VARCHAR(60),
    description        TEXT,
    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_facility_type_id PRIMARY KEY (facility_type_id),
    CONSTRAINT fk_parent_type_id FOREIGN KEY (parent_type_id) REFERENCES facility_type (facility_type_id)

);

CREATE TABLE facility
(
    facility_id        VARCHAR(60) NOT NULL,
    facility_type_id   VARCHAR(60),
    parent_facility_id VARCHAR(60),
    facility_name      VARCHAR(100),
    contact_mech_id    UUID,
    product_store_id   VARCHAR(60),
    opened_date        TIMESTAMP,
    closed_date        TIMESTAMP,
    description        TEXT,
    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_facility_id PRIMARY KEY (facility_id),
    constraint fk_facility_contact_mech_id foreign key (contact_mech_id) references postal_address (contact_mech_id),
    CONSTRAINT fk_facility_type_id FOREIGN KEY (facility_type_id) REFERENCES facility_type (facility_type_id),
    CONSTRAINT fk_parent_facility_id FOREIGN KEY (parent_facility_id) REFERENCES facility (facility_id),
    CONSTRAINT fk_product_store_id FOREIGN KEY (product_store_id) REFERENCES product_store (product_store_id)
);
