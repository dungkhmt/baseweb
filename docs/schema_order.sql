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
CREATE TABLE order_type (
  order_type_id     VARCHAR(60) NOT NULL,
  parent_type_id     VARCHAR(60),
  description        TEXT,
  last_updated_stamp TIMESTAMP   ,
  created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_order_type_id PRIMARY KEY (order_type_id),
  CONSTRAINT fk_parent_type FOREIGN KEY (parent_type_id) REFERENCES order_type (order_type_id)
);

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

CREATE TABLE facility_type (
  facility_type_id     VARCHAR(60) NOT NULL,
  parent_type_id			VARCHAR(60),
  description        TEXT,
  last_updated_stamp TIMESTAMP   ,
  created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_facility_type_id PRIMARY KEY (facility_type_id),
  CONSTRAINT fk_parent_type_id FOREIGN KEY (parent_type_id) REFERENCES facility_type (facility_type_id)

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
	order_item_type_id VARCHAR(60),
	product_id VARCHAR(60),
	unit_price DECIMAL(18,2),
	quantity	DECIMAL(18,6),
	status_id VARCHAR(60),
	last_updated_stamp TIMESTAMP   ,
	created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_order_item_id PRIMARY KEY (order_id,order_item_seq_id),
  CONSTRAINT fk_order_item_type_id FOREIGN KEY (order_item_type_id) REFERENCES order_item_type (order_item_type_id),
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














