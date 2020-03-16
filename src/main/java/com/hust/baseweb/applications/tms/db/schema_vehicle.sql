create table vehicle_type
(
    vehicle_type_id    VARCHAR(60) NOT NULL,
    capacity           numeric,
    long               Integer,
    width              Integer,
    height             Integer,
    pallet             numeric,

    description        TEXT,
    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_vehicle_type primary key (vehicle_type_id)
);

create table vehicle
(
    vehicle_id                    VARCHAR(60) NOT NULL,
    vehicle_type_id               VARCHAR(60),
    capacity                      numeric,
    long                          Integer,
    width                         Integer,
    height                        Integer,
    pallet                        numeric,
    status_id                     VARCHAR(60),
    product_transport_category_id VARCHAR(60),
    priority                      int,
    description                   TEXT,
    last_updated_stamp            TIMESTAMP,
    created_stamp                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_vehicle_id primary key (vehicle_id),
    constraint fk_vehicle_vehicle_type foreign key (vehicle_type_id) references vehicle_type (vehicle_type_id),
    constraint fk_vehicle_status_id foreign key (status_id) references status_item (status_id),
    constraint fk_vehicle_type_product_transport_category_id foreign key (product_transport_category_id) references enumeration (enum_id)
);

create table vehicle_maintenance_history
(
    vehicle_maintenance_history_id UUID        NOT NULL default uuid_generate_v1(),
    vehicle_id                     VARCHAR(60) NOT NULL,
    maintenance_date               TIMESTAMP,
    thru_date                      TIMESTAMP,
    capacity                       numeric,
    long                           Integer,
    width                          Integer,
    height                         Integer,
    pallet                         numeric,
    description                    TEXT,
    last_updated_stamp             TIMESTAMP,
    created_stamp                  TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    constraint pk_vehicle_maintenance_history primary key (vehicle_maintenance_history_id),
    constraint fk_vehicle_maintenance_history_vehicle_id foreign key (vehicle_id) references vehicle (vehicle_id)
);

create table vehicle_forbidden_geo_point
(
    vehicle_forbidden_geo_point_id UUID NOT NULL default uuid_generate_v1(),
    vehicle_id                     VARCHAR(60),
    geo_point_id                   UUID,
    from_date                      TIMESTAMP,
    thru_date                      TIMESTAMP,
    last_updated_stamp             TIMESTAMP,
    created_stamp                  TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    constraint pk_vehicle_forbidden_geo_point primary key (vehicle_forbidden_geo_point_id),
    constraint fk_vehicle_forbidden_geo_point_vehicle_id foreign key (vehicle_id) references vehicle (vehicle_id),
    constraint fk_vehicle_forbidden_geo_point_geo_point_id foreign key (geo_point_id) references geo_point (geo_point_id)
);


create table vehicle_location_priority
(
    vehicle_location_priority_id UUID NOT NULL default uuid_generate_v1(),
    vehicle_id                   VARCHAR(60),
    contact_mech_id              UUID,
    priority                     Integer,
    from_date                    TIMESTAMP,
    thru_date                    TIMESTAMP,
    constraint pk_vehicle_location_priority primary key (vehicle_location_priority_id),
    constraint fk_vehicle_location_priority_vehicle_id foreign key (vehicle_id) references vehicle (vehicle_id),
    constraint fk_vehicle_location_priority_contact_mech_id foreign key (contact_mech_id) references postal_address (contact_mech_id)
);
