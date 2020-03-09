create table shipment_type
(
    shipment_type_id   VARCHAR(60) NOT NULL,
    parent_type_id     VARCHAR(60),
    description        TEXT,
    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_shipment_type primary key (shipment_type_id),
    constraint fk_shipment foreign key (parent_type_id) references shipment_type (shipment_type_id)
);
create table shipment
(
    shipment_id        UUID NOT NULL default uuid_generate_v1(),
    shipment_type_id   VARCHAR(60),
    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    constraint pk_shipment primary key (shipment_id),
    constraint fk_shipment_shipment_type_id foreign key (shipment_type_id) references shipment_type (shipment_type_id)
);

create table shipment_item
(
    shipment_item_id UUID NOT NULL default uuid_generate_v1(),
    shipment_id          UUID NOT NULL,
    product_id           VARCHAR(60),
    quantity             Integer,
    pallet               numeric,
    party_customer_id    UUID,
    ship_to_location_id  UUID,
    last_updated_stamp   TIMESTAMP,
    created_stamp        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_shipment_item primary key (shipment_item_id),
    constraint fk_shipment_item_shipment_id foreign key (shipment_id) references shipment (shipment_id),
    constraint fk_shipment_item_product_id foreign key (product_id) references product (product_id),
    constraint fk_shipment_item_ship_to_location_id foreign key (ship_to_location_id) references postal_address (contact_mech_id),
    constraint fk_shipment_item_party_customer_id foreign key (party_customer_id) references party_customer (party_id)
);

create table order_shipment
(
	order_shipment_id  UUID NOT NULL default uuid_generate_v1(),
    order_id             VARCHAR(60),
    order_item_seq_id    VARCHAR(60),
    shipment_item_id UUID,
    last_updated_stamp   TIMESTAMP,
    created_stamp        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_order_shipment primary key (order_shipment_id),
    constraint fk_order_shipment_order_item foreign key (order_id, order_item_seq_id) references order_item (order_id, order_item_seq_id),
    constraint fk_order_shipment_shipment_item foreign key (shipment_item_id) references shipment_item (shipment_item_id)
);



create table delivery_plan
(
    delivery_plan_id   UUID NOT NULL default uuid_generate_v1(),
    delivery_date      TIMESTAMP,
    description        TEXT,
    created_by         VARCHAR(60),
    status_id          VARCHAR(60),
    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    constraint pk_delivery_plan primary key (delivery_plan_id),
    constraint fk_delivery_plan_created_by foreign key (created_by) references user_login (user_login_id),
    constraint fk_delivery_plan_status_id foreign key (status_id) references status_item (status_id)
);



create table vehicle_delivery_plan
(
    delivery_plan_id   UUID,
    vehicle_id         VARCHAR(60),
    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_vehicle_delivery_plan primary key (delivery_plan_id, vehicle_id)
);

create table shipment_item_delivery_plan
(
	
    delivery_plan_id     UUID,
    shipment_item_id          UUID,
    
    last_updated_stamp   TIMESTAMP,
    created_stamp        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_shipment_item_delivery_plan primary key (delivery_plan_id, shipment_item_id),
    constraint fk_shipment_item_delivery_plan_delivery_plan foreign key(delivery_plan_id) references delivery_plan(delivery_plan_id),
    constraint fk_shipment_item_delivery_plan_shipment_item foreign key(shipment_item_id) references shipment_item(shipment_item_id)
);

create table delivery_plan_solution
(
    delivery_plan_id              UUID,
    delivery_plan_solution_seq_id VARCHAR(60),
    status_id                     VARCHAR(60),
    last_updated_stamp            TIMESTAMP,
    created_stamp                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_delivery_plan_solution primary key (delivery_plan_id, delivery_plan_solution_seq_id),
    constraint fk_devlivery_plan_solution_status foreign key (status_id) references status_item (status_id),
    constraint fk_delivery_plan_solution_delivery_plan foreign key (delivery_plan_id) references delivery_plan (delivery_plan_id)
);







create table delivery_trip
(
    delivery_trip_id                 UUID NOT NULL default uuid_generate_v1(),
    delivery_plan_id                 UUID,
    delivery_plan_solution_seq_id    VARCHAR(60),
    vehicle_id                       VARCHAR(60),
    driver_id                        UUID,
    execute_date                     TIMESTAMP,
    distance                         numeric,
    totalWeight                      numeric,
    execute_external_vehicle_type_id VARCHAR(60),
    status_id                        VARCHAR(60),
    last_updated_stamp               TIMESTAMP,
    created_stamp                    TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    constraint pk_delivery_trip primary key (delivery_trip_id),
    constraint fk_delivery_trip_status foreign key (status_id) references status_item (status_id),
    constraint fk_delivery_trip_external_vehicle_type_id foreign key (execute_external_vehicle_type_id) references vehicle_type (vehicle_type_id),
    constraint fk_delivery_trip_plan_solution foreign key (delivery_plan_id, delivery_plan_solution_seq_id) references delivery_plan_solution (delivery_plan_id, delivery_plan_solution_seq_id),
    constraint fk_delivery_trip_vehicle_id foreign key (vehicle_id) references vehicle (vehicle_id),
    constraint fk_delivery_trip_driver_id foreign key (driver_id) references party (party_id)
);


create table delivery_trip_detail
(
    delivery_trip_detail_id UUID NOT NULL default uuid_generate_v1(),
    delivery_trip_id        UUID,
    sequence                Integer,
    shipment_item_id             UUID,
    delivery_quantity       Integer,
    status_id               VARCHAR(60),
    last_updated_stamp      TIMESTAMP,
    created_stamp           TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    constraint pk_delivery_trip_detail primary key (delivery_trip_detail_id),
    constraint fk_delivery_trip_detail_status foreign key (status_id) references status_item (status_id),
    constraint fk_delivery_trip_detail_delivery_trip foreign key (delivery_trip_id) references delivery_trip (delivery_trip_id),
    constraint fk_delivery_trip_detail_shipment foreign key (shipment_item_id) references shipment_item (shipment_item_id)
);



