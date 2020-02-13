create table vehicle_type(
	vehicle_type_id VARCHAR(60) NOT NULL,
	capacity numeric,
	long Integer,
	width Integer,
	height Integer,
	pallet numeric,
	description TEXT,
	last_updated_stamp TIMESTAMP   ,
  	created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	constraint pk_vehicle_type primary key(vehicle_type_id)
);

create table vehicle(
	vehicle_id VARCHAR(60) NOT NULL,
	vehicle_type_id VARCHAR(60),
	status_id VARCHAR(60),
	description TEXT,
	last_updated_stamp TIMESTAMP   ,
  	created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  	constraint pk_vehicle_id primary key(vehicle_id),
  	constraint fk_vehicle_vehicle_type foreign key(vehicle_type_id) references vehicle_type(vehicle_type_id),
	constraint fk_vehicle_status_id foreign key(status_id) references status_item(status_id)
);