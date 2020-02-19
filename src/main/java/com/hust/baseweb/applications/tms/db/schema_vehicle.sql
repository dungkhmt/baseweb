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
    vehicle_id         VARCHAR(60) NOT NULL,
    vehicle_type_id    VARCHAR(60),
    capacity           numeric,
    long               Integer,
    width              Integer,
    height             Integer,
    pallet             numeric,
    status_id          VARCHAR(60),
    description        TEXT,
    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_vehicle_id primary key (vehicle_id),
    constraint fk_vehicle_vehicle_type foreign key (vehicle_type_id) references vehicle_type (vehicle_type_id),
    constraint fk_vehicle_status_id foreign key (status_id) references status_item (status_id)
);

create table vehicle_maintenance_history
(
    vehicle_maintenance_history_id UUID        NOT NULL default uuid_generate_v1(),
    vehicle_id                     VARCHAR(60) NOT NULL,
    maintenance_date               TIMESTAMP,
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