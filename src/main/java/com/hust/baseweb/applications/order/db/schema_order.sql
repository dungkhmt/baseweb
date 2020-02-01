CREATE TABLE role_type(
	role_type_id	VARCHAR(60) NOT NULL,
	parent_type_id	VARCHAR(60),
	description TEXT,
	last_updated_stamp TIMESTAMP   ,
	created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT pk_role_type_id PRIMARY KEY (role_type_id),
	CONSTRAINT fk_parent_type_id FOREIGN KEY (parent_type_id) REFERENCES role_type (role_type_id)
);
CREATE TABLE sales_channel(
	sales_channel_id VARCHAR(60) NOT NULL,
	sales_channel_name VARCHAR(100),
	last_updated_stamp TIMESTAMP,
	created_stamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT pk_sales_channel_id PRIMARY KEY (sales_channel_id)
);




CREATE TABLE status_type (
  status_type_id     VARCHAR(60) NOT NULL,
  parent_type_id VARCHAR(60),
  description        TEXT,
  last_updated_stamp TIMESTAMP   ,
  created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_status_type_id PRIMARY KEY (status_type_id),
  CONSTRAINT fk_parent_type_id FOREIGN KEY (parent_type_id) REFERENCES status_type (status_type_id)
);

CREATE TABLE status_item (
  status_id     VARCHAR(60) NOT NULL,
  status_type_id			VARCHAR(60),
  status_code			VARCHAR(60),
  description        TEXT,
  last_updated_stamp TIMESTAMP   ,
  created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_status_id PRIMARY KEY (status_id),
  CONSTRAINT fk_status_type_id FOREIGN KEY (status_type_id) REFERENCES status_type (status_type_id)
);

CREATE TABLE order_type (
  order_type_id     VARCHAR(60) NOT NULL,
  parent_type_id     VARCHAR(60),
  description        TEXT,
  last_updated_stamp TIMESTAMP   ,
  created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_order_type_id PRIMARY KEY (order_type_id),
  CONSTRAINT fk_parent_type FOREIGN KEY (parent_type_id) REFERENCES order_type (order_type_id)
);
CREATE TABLE order_header (
  order_id     VARCHAR(60) NOT NULL,
  order_type_id     VARCHAR(60),
  original_facility_id		VARCHAR(60),
  product_store_id	VARCHAR(60),
  sales_channel_id VARCHAR(60),
  created_by		VARCHAR(60),
  currency_uom_id	VARCHAR(60),
  grand_total		DECIMAL(18,2),
  description        TEXT,
  last_updated_stamp TIMESTAMP   ,
  created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_order PRIMARY KEY (order_id),
  CONSTRAINT fk_order_type_id FOREIGN KEY (order_type_id) REFERENCES order_type (order_type_id),
  CONSTRAINT fk_original_facility_id FOREIGN KEY (original_facility_id) REFERENCES facility (facility_id),
  CONSTRAINT fk_product_store_id FOREIGN KEY (product_store_id) REFERENCES facility (facility_id)
   	
 );
CREATE TABLE order_item_type(
	order_item_type_id VARCHAR(60) NOT NULL,
	parent_type_id VARCHAR(60) NOT NULL,
	description	TEXT,
	last_updated_stamp TIMESTAMP   ,
	created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_order_item_type_id PRIMARY KEY (order_item_type_id),
  CONSTRAINT fk_parent_type_id FOREIGN KEY (parent_type_id) REFERENCES order_item_type (order_item_type_id)		
);

CREATE TABLE order_item(
	order_id VARCHAR(60) NOT NULL,
	order_item_seq_id VARCHAR(60),
	order_item_type_id VARCHAR(60),
	product_id VARCHAR(60),
	unit_price DECIMAL(18,2),
	quantity	DECIMAL(18,6),
	status_id VARCHAR(60),
	last_updated_stamp TIMESTAMP   ,
	created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_order_item_id PRIMARY KEY (order_id,order_item_seq_id),
  CONSTRAINT fk_order_item_type_id FOREIGN KEY (order_item_type_id) REFERENCES order_item_type (order_item_type_id),
  CONSTRAINT fk_order_item_product_id FOREIGN KEY (product_id) REFERENCES product (product_id),
  
  CONSTRAINT fk_status_id FOREIGN KEY (status_id) REFERENCES status_item (status_id)	
);

CREATE TABLE order_role(
	order_id VARCHAR(60),
	party_id	UUID,
	role_type_id VARCHAR(60),
	last_updated_stamp TIMESTAMP   ,
	created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT pk_order_role PRIMARY KEY(order_id,party_id,role_type_id),
	CONSTRAINT fk_order_role_order_id FOREIGN KEY (order_id) REFERENCES order_header(order_id),
	CONSTRAINT fk_order_role_party_id FOREIGN KEY (party_id) REFERENCES party (party_id),
	CONSTRAINT fk_order_role_role_type_id FOREIGN KEY (role_type_id) REFERENCES role_type (role_type_id)
);


CREATE TABLE order_status(
	order_status_id	VARCHAR(60),
	status_id VARCHAR(60),
	order_id	VARCHAR(60),
	status_datetime  TIMESTAMP,
	status_user_login_id	VARCHAR(60),
	last_updated_stamp TIMESTAMP   ,
	created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT pk_order_status_id PRIMARY KEY (order_status_id),
	CONSTRAINT fk_order_status_status_id FOREIGN KEY (status_id) REFERENCES status_item (status_id),
	CONSTRAINT fk_order_status_order_id FOREIGN KEY (order_id) REFERENCES order_header (order_id),
	CONSTRAINT fk_status_user_login_id FOREIGN KEY (status_user_login_id) REFERENCES user_login (user_login_id)
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









