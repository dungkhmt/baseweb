create post_package_type(
    post_package_type_id VARCHAR(60),
    post_package_type_name VARCHAR(200),
    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_post_package_type primary key (post_package_type_id)
);

create table post_customer(
    post_customer_id UUID default uuid_generate_v1(),
    post_customer_name VARCHAR(200),
    contact_mech_id   UUID,
    phone_num VARCHAR(20),
    party_id    UUID,
    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_post_customer primary key(post_customer_id),
    constraint fk_post_customer_party_id foreign key(party_id) references party(party_id),
    constraint fk_post_customer_contact_mech_id foreign key(contact_mech_id) references postal_address(contact_mech_id)

);

create table post_ship_order(
	post_ship_order_id UUID not null uuid_generate_v1(),
    from_customer_id UUID not null,
    to_customer_id UUID not null,


	package_name VARCHAR(200),
	package_type_id VARCHAR(60),
	weight numeric,
	description text,

	pickup_date  TIMESTAMP,
	expected_delivery_date TIMESTAMP,

	status_id VARCHAR(60),

	last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_ship_order primary key (post_ship_order_id),
    constraint fk_ship_order_package_type_id foreign key(package_type_id) references package_type(package_type_id),
    constraint fk_post_ship_order_status foreign key(status_id) references status_item(status_id),
    constraint fk_ship_order_from_customer_id foreign key(from_customer_id) references post_customer(post_customer_id),
    constraint fk_ship_order_to_customer_id foreign key(to_customer_id) references post_customer(post_customer_id)
);


create table post_office(
	post_office_id  VARCHAR(60),
	post_office_name VARCHAR(200),
    contact_mech_id UUID not null,
    post_office_level int,
	last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	constraint pk_post_office_id primary key(post_office_id)
);

create table post_office_relationship(
    post_office_relationship_id UUID not null default uuid_generate_v1(),
    post_office_id VARCHAR(60),
    parent_post_office_id VARCHAR(60),
    from_date TIMESTAMP,
    thru_date TIMESTAMP,

	last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	constraint pk_post_office_relationship_id primary key(post_office_relationship_id)
    constraint fk_post_office_relationship_post_office_id foreign key(post_office_id) references post_office(post_office_id),
    constraint fk_post_office_relationship_parent_post_office_id foreign key(parent_post_office_id) references post_office(post_office_id)
);
create table post_office_fixed_trip(
	post_office_fixed_trip_id UUID default uuid_generate_v1(),
	from_post_office_id VARCHAR(60),
	to_post_office_id VARCHAR(60),

    schedule_departure_time VARCHAR(20),

    from_date TIMESTAMP,
    thru_date TIMESTAMP,

	last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_post_office_fixed_trip_id primary key(post_office_fixed_trip),
    constraint fk_post_office_fixed_trip_from_post_office_id foreign key(from_post_office_id) references post_office(post_office_id),
    constraint fk_post_office_fixed_trip_to_post_office_id foreign key(to_post_office_id) references post_office(post_office_id)
);

create table postman(
	postman_id UUID,
	postman_name VARCHAR(200),
	post_office_id UUID,

	last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_postman_id primary key (postman_id),
    constraint fk_postman_id foreign key(postman_id) references party(party_id),
    constraint fk_postman_post_office_id foreign key(post_office_id) references post_office(post_office_id)
);

create table post_office_fixed_trip_execute(
	post_office_fixed_trip_execute_id UUID default uuid_generate_v1(),
	post_office_fixed_trip_id UUID not null,
	postman_id UUID not null,
	departure_date_time TIMESTAMP,
	last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    constraint pk_post_office_fixed_trip_execute_id primary key(post_office_fixed_trip_execute_id),
    constraint fk_post_office_fixed_trip_execute_post_office_fixed_trip foreign key(post_office_fixed_trip_id) references
                        post_office_fixed_trip(post_office_fixed_trip_id),
    constraint fk_post_office_fixed_trip_execute_postman_id foreign key(postman_id) references postman(postman_id)
);

create table post_ship_order_post_office_assignment(
    post_ship_order_post_office_assignment_id UUID not null default uuid_generate_v1(),
    post_ship_order_id uuid not null,
    post_office_id VARCHAR(60) not null,

    assigned_date TIMESTAMP,

	last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    constraint pk_post_ship_order_post_office_assignment_id primary key(post_ship_order_post_office_assignment_id),
    constraint fk_post_ship_order_post_office_assignment_order foreign key(post_ship_order_id) references post_ship_order(post_ship_order_id),
    constraint fk_post_ship_order_post_office_assignment_post_office foreign key(post_office_id) references post_office(post_office_id)
);

create table post_ship_order_postman_last_mile_assignment(
    post_ship_order_postman_last_mile_assignment_id UUID default uuid_generate_v1(),
	post_ship_order_id UUID not null,
	postman_id UUID not null,
	pickup_delivery VARCHAR(1),
	status_id VARCHAR(60),
	last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    constraint pk_post_ship_order_postman_last_mile_assignment_id primary key(post_ship_order_fixed_trip_post_office_assignment_id),
    constraint fk_post_ship_order_postman_last_mile_assignment_post_ship_order_id foreign key(post_ship_order_id) references post_ship_order(post_ship_order_id),
    constraint fk_post_ship_order_postman_last_mile_assignment_postman_id foreign key(postman_id) references postman(postman_id),
    constraint fk_post_ship_order_postman_last_mile_assignment_staus_id foreign key(status_id) references status_item(status_id)

);

create table post_ship_order_fixed_trip_post_office_assignment(
	post_ship_order_fixed_trip_post_office_assignment_id UUID not null default uuid_generate_v1(),
    post_ship_order_id UUID not null,
    post_office_fixed_trip_execute_id UUID not null,
	last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_post_ship_order_fixed_trip_post_office_assignment primary key(post_ship_order_fixed_trip_post_office_assignment_id),
    constraint fk_post_ship_order_fixed_trip_post_office_assignment_fixed_trip_id foreign key(post_office_fixed_trip_execute_id)
                references post_office_fixed_trip_execute(post_office_fixed_trip_execute_id),
    constraint fk_post_ship_order_fixed_trip_post_office_assignment_order foreign key(post_ship_order_id) references post_ship_order(post_ship_order_id)
);

create table post_ship_order_itinerary(
	post_ship_order_itinerary_id UUID not null default uuid_generate_v1(),
	post_ship_order_id UUID not null,
	post_office_id UUID not null,
	arrival_date_time  TIMESTAMP,
	delivery_fixed_trip_post_office_execute_id UUID,
	
	departure_date_time TIMESTAMP
	pickup_fixed_trip_post_office_execute_id UUID,

	last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_post_ship_order_itinerary_id primary key(post_ship_order_itinerary_id),
    constraint fk_post_ship_order_itinerary_order_id foreign key(post_ship_order_id) references post_ship_order(post_ship_order_id),
    constraint fk_post_ship_order_itinerary_post_office_id foreign key(post_office_id) references post_office(post_office_id)
);