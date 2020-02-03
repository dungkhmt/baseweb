CREATE TABLE enumeration_type (
  enumeration_type_id     VARCHAR(60) NOT NULL,
  parent_type_id     VARCHAR(60),
  description        TEXT,
  last_updated_stamp TIMESTAMP   ,
  created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_enumeration_type_id PRIMARY KEY (enumeration_type_id),
  CONSTRAINT fk_parent_type_id FOREIGN KEY (parent_type_id) REFERENCES enumeration_type (enumeration_type_id)
);
CREATE TABLE enumeration (
  enum_id     VARCHAR(60) NOT NULL,
  enum_type_id     VARCHAR(60),
  enum_code			VARCHAR(60),
  sequence_id		VARCHAR(60),
  description        TEXT,
  last_updated_stamp TIMESTAMP   ,
  created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_enum_id PRIMARY KEY (enum_id),
  CONSTRAINT fk_enum_type_id FOREIGN KEY (enum_type_id) REFERENCES enumeration_type (enumeration_type_id)
);


CREATE TABLE uom_type (
  uom_type_id     VARCHAR(60) NOT NULL,
  parent_type_id     VARCHAR(60),
  description        TEXT,
  last_updated_stamp TIMESTAMP   ,
  created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_oum_type_id PRIMARY KEY (uom_type_id),
  CONSTRAINT fk_parent_type_id FOREIGN KEY (parent_type_id) REFERENCES uom_type (uom_type_id)
);
CREATE TABLE uom (
  uom_id     VARCHAR(60) NOT NULL,
  uom_type_id     VARCHAR(60),
  abbreviation	VARCHAR(60),
  description        TEXT,
  last_updated_stamp TIMESTAMP   ,
  created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_oum PRIMARY KEY (uom_id),
  CONSTRAINT fk_uom_type_id FOREIGN KEY (uom_type_id) REFERENCES uom_type (uom_type_id)
);
CREATE TABLE product_store_group (
  product_store_group_id     VARCHAR(60) NOT NULL,
  description        TEXT,
  last_updated_stamp TIMESTAMP   ,
  created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_product_store_group_id PRIMARY KEY (product_store_group_id)
);

CREATE TABLE product_store (
  product_store_id     VARCHAR(60) NOT NULL,
  store_name VARCHAR(100),
  company_name  VARCHAR(100),
  product_store_group_id VARCHAR(60),
  description        TEXT,
  last_updated_stamp TIMESTAMP   ,
  created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_product_store_id PRIMARY KEY (product_store_id)
);

CREATE TABLE product_type (
  product_type_id     VARCHAR(60) NOT NULL,
  parent_type_id     VARCHAR(60),
  description        TEXT,
  last_updated_stamp TIMESTAMP   ,
  created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_product_type_id PRIMARY KEY (product_type_id),
  CONSTRAINT fk_parent_type_id FOREIGN KEY (parent_type_id) REFERENCES product_type (product_type_id)

);
CREATE TABLE product (
  product_id     VARCHAR(60) NOT NULL,
  product_type_id     VARCHAR(60),
  facility_id			VARCHAR(60),
  product_name		VARCHAR(100),
  introductionDate TIMESTAMP,
  quantity_uom_id	VARCHAR(60),
  weight_uom_id	VARCHAR(60),
  width_uom_id	VARCHAR(60),
  length_uom_id	VARCHAR(60),
  height_uom_id	VARCHAR(60),
  created_by_user_login_id VARCHAR(60),
  description        TEXT,
  last_updated_stamp TIMESTAMP   ,
  created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_product_id PRIMARY KEY (product_id),
  CONSTRAINT fk_product_type_id FOREIGN KEY (product_type_id) REFERENCES product_type (product_type_id),  
  CONSTRAINT fk_created_by_user_login_id FOREIGN KEY (created_by_user_login_id) REFERENCES user_login (user_login_id),  
  CONSTRAINT fk_quantity_uom_id FOREIGN KEY (quantity_uom_id) REFERENCES uom (uom_id), 
  CONSTRAINT fk_weight_uom_id FOREIGN KEY (weight_uom_id) REFERENCES uom (uom_id), 
  CONSTRAINT fk_length_uom_id FOREIGN KEY (length_uom_id) REFERENCES uom (uom_id), 
  CONSTRAINT fk_width_uom_id FOREIGN KEY (width_uom_id) REFERENCES uom (uom_id), 
  CONSTRAINT fk_height_uom_id FOREIGN KEY (height_uom_id) REFERENCES uom (uom_id) 
);

CREATE TABLE facility_type (
  facility_type_id     VARCHAR(60) NOT NULL,
  parent_type_id			VARCHAR(60),
  description        TEXT,
  last_updated_stamp TIMESTAMP   ,
  created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_facility_type_id PRIMARY KEY (facility_type_id),
  CONSTRAINT fk_parent_type_id FOREIGN KEY (parent_type_id) REFERENCES facility_type (facility_type_id)

);





CREATE TABLE inventory_item(
	inventory_item_id	UUID NOT NULL default uuid_generate_v1(),
	product_id	VARCHAR(60),
	status_id VARCHAR(60),
	datetime_received TIMESTAMP,
	datetime_manufactured TIMESTAMP,
	expire_date TIMESTAMP,
	activation_valid_thru TIMESTAMP,
	facility_id VARCHAR(60),
	lot_id VARCHAR(60),
	uom_id VARCHAR(60),
	unit_cost DECIMAL(18,6),
	currency_uom_id VARCHAR(60),
	quantity_on_hand_total DECIMAL(18,6),
	available_to_promise_total DECIMAL(18,6),
	description TEXT,
	last_updated_stamp TIMESTAMP   ,
	created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT pk_inventory_item_id PRIMARY KEY (inventory_item_id),
	CONSTRAINT fk_inventory_item_product_id FOREIGN KEY (product_id) REFERENCES product (product_id),
	CONSTRAINT fk_inventory_item_status_id FOREIGN KEY (status_id) REFERENCES status_item (status_id),
	CONSTRAINT fk_inventory_item_currency_uom_id FOREIGN KEY(currency_uom_id) REFERENCES uom(uom_id),
	CONSTRAINT fk_inventory_item_facility_id FOREIGN KEY (facility_id) REFERENCES facility (facility_id)
);



CREATE TABLE inventory_item_detail(
	inventory_item_id	UUID NOT NULL,
	inventory_item_detail_seq_id VARCHAR(60),
	effective_date TIMESTAMP,
	quantity_on_hand_diff DECIMAL(18,6),
	available_to_promise_diff DECIMAL(18,6),
	accounting_quantity_diff DECIMAL(18,6),
	order_id VARCHAR(60),
	order_item_seq_id VARCHAR(60) NOT NULL,
	last_updated_stamp TIMESTAMP   ,
	created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT pk_inventory_item_detail PRIMARY KEY (inventory_item_id,inventory_item_detail_seq_id),
	CONSTRAINT fk_inventory_item_detail_inventory_item_id FOREIGN KEY(inventory_item_id) REFERENCES inventory_item(inventory_item_id),
	CONSTRAINT fk_inventory_item_detail_order_id_order_item_seq_id FOREIGN KEY(order_id,order_item_seq_id) REFERENCES order_item(order_id,order_item_seq_id)	
);




CREATE TABLE product_price(
	product_id VARCHAR(60) NOT NULL,
	currency_uom_id VARCHAR(60),
	from_date TIMESTAMP,
	thru_date TIMESTAMP,
	product_store_group_id VARCHAR(60),
	price DECIMAL(18,3),
	created_by_user_login_id VARCHAR(60),
	created_date TIMESTAMP,
	last_updated_stamp TIMESTAMP   ,
	created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT pk_product_price PRIMARY KEY(product_id,from_date,currency_uom_id),
	CONSTRAINT fk_product_price_currency_uom_id FOREIGN KEY(currency_uom_id) REFERENCES uom(uom_id),
	CONSTRAINT fk_product_price_created_by_user_login_id FOREIGN KEY(created_by_user_login_id) REFERENCES user_login(user_login_id),
	CONSTRAINT fk_product_price_product_store_group_id FOREIGN KEY(product_store_group_id) REFERENCES product_store_group(product_store_group_id)	
);

CREATE TABLE facility (
  facility_id     VARCHAR(60) NOT NULL,
  facility_type_id     VARCHAR(60),
  parent_facility_id			VARCHAR(60),
  facility_name			VARCHAR(100),
  product_store_id		VARCHAR(60),
  opened_date			TIMESTAMP,
  closed_date			TIMESTAMP,
  description        TEXT,
  last_updated_stamp TIMESTAMP   ,
  created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_facility_id PRIMARY KEY (facility_id),
  CONSTRAINT fk_facility_type_id FOREIGN KEY (facility_type_id) REFERENCES facility_type (facility_type_id),
  CONSTRAINT fk_parent_facility_id FOREIGN KEY (parent_facility_id) REFERENCES facility (facility_id),
  CONSTRAINT fk_product_store_id FOREIGN KEY (product_store_id) REFERENCES product_store (product_store_id)
);

CREATE TABLE product_facility (
  product_id     VARCHAR(60) NOT NULL,
  facility_id     VARCHAR(60) NOT NULL,
  apt_inventory_count DECIMAL(18,6),
  last_inventory_count DECIMAL(18,6),
  description        TEXT,
  last_updated_stamp TIMESTAMP   ,
  created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_product_facility PRIMARY KEY (product_id, facility_id),
  CONSTRAINT fk_product_facility_facility_id FOREIGN KEY (facility_id) REFERENCES facility (facility_id),
  CONSTRAINT fk_product_facility_product_id FOREIGN KEY (product_id) REFERENCES product (product_id)  
);



