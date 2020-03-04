
create table party_driver(
	party_id UUID NOT NULL default uuid_generate_v1(),
	status_id          VARCHAR(60),
	last_updated_stamp   TIMESTAMP,
    created_stamp        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_party_driver primary key(party_id),
    constraint fk_party_driver foreign key (party_id) references party(party_id),
	constraint fk_party_driver_status_id foreign key (status_id) references status_item (status_id)
);

create table vehicle_driver(
	vehicle_driver_id UUID not null default uuid_generate_v1(),
	party_driver_id UUID not null,
	vehicle_id VARCHAR(60),
	from_date TIMESTAMP,
	thru_date TIMESTAMP,
	last_updated_stamp   TIMESTAMP,
    created_stamp        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_vehicle_driver primary key(vehicle_driver_id),
    constraint fk_vehicle_driver_vehicle_id foreign key(vehicle_id) references vehicle(vehicle_id),
    constraint fk_vehicle_driver_party_driver_id foreign key(party_driver_id) references party_driver(party_id)
);