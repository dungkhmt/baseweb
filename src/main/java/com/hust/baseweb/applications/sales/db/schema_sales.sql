create table party_salesman(
	party_id UUID NOT NULL default uuid_generate_v1(),
	status_id VARCHAR(60),
	last_updated_stamp          TIMESTAMP   ,
    created_stamp               TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	constraint pk_party_salesman primary key(party_id),
	constraint fk_party_salesman_status_id foreign key(status_id) references status_item(status_id)
);

CREATE TABLE party_customer(
	party_id UUID NOT NULL,
	customer_name VARCHAR(100),
	status_id VARCHAR(60),
	party_type_id VARCHAR(60),
	description TEXT,
	last_updated_stamp TIMESTAMP   ,
	created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT pk_party_customer_party_id PRIMARY KEY(party_id),
	CONSTRAINT fp_party_customer_party_id FOREIGN KEY(party_id) REFERENCES party(party_id),
	CONSTRAINT fp_party_customer_party_type_id FOREIGN KEY(party_type_id) REFERENCES party_type(party_type_id),
	CONSTRAINT fp_party_customer_status_id FOREIGN KEY(status_id) REFERENCES status_item(status_id)
);

create table customer_salesman(
	customer_salesman_id UUID NOT NULL default uuid_generate_v1(),
	party_customer_id UUID NOT NULL,
	party_salesman_id UUID NOT NULL,
	from_date TIMESTAMP,
	thru_date TIMESTAMP,
	constraint pk_customer_salesman primary key(customer_salesman_id),
	constraint fk_customer_salesman_customer foreign key(party_customer_id) references party_customer(party_id),
	constraint fk_customer_salesman_salesman foreign key(party_salesman_id) references party_salesman(party_id)
);