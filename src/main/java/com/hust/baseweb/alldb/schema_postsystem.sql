create table ship_order(
	ship_order_id UUID not null uuid_generate_v1(),
	from_location_id UUID not null,
	to_location_id UUID not null,
	
	customer_id UUID,
	from_customer_name VARCHAR(100),
	from_phone_num VARCHAR(60),
	
	to_customer_name VARCHAR(100),
	to_phone_num VARCHAR(60),
	
	weight numeric,
	
	pickup_date  TIMESTAMP,
	expected_delivery_date TIMESTAMP,
	
	last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
);


create table postoffice(
	
);

create table fixed_trip_postoffices(
	fixed_trip_postoffices_id
);

create table postman(
	postman_id UUID,
	postoffice_id UUID,
	from_date 
	thru_date
);

create table fixed_trip_postoffice_execute(
	
	fixed_trip_postoffices_id UUID not null,
	postman_id UUID not null,
	departure_date_time TIMESTAMP,
	
);

create table ship_order_postman_lastmile_assignment(
	ship_order_id
	postman_id
	pickup_delivery VARCHAR(1),
	status_id
);

create table ship_order_fixed_trip_postoffice_assignment(
		

);

create table ship_order_itinerary(
	ship_order_itinerary_id UUID not null default uuid_generate_v1(),
	ship_order_id UUID not null,
	postoffice_id UUID not null,
	arrival_date_time  TIMESTAMP,
	delivery_fixed_trip_postoffice_execute_id UUID,
	
	departure_date_time TIMESTAMP
	pickup_fixed_trip_postoffice_execute_id UUID,
	
);