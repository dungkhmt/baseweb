CREATE TABLE inventory_item(
	inventory_item_id	VARCHAR(60) NOT NULL,
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
	inventory_item_id	VARCHAR(60) NOT NULL,
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




