create table s_product
(
    product_id   Integer not null,
    product_name VARCHAR(200),
    product_code VARCHAR(60),
    constraint pk_product primary key (product_id)
);

create table s_customer
(
    customer_id   Integer,
    customer_name VARCHAR(200),
    customer_code VARCHAR(60)

);

create table s_order_header
(
    order_id                 Integer not null,
    order_code               VARCHAR(60),
    created_by_user_login_id VARCHAR(60),
    customer_id              Integer,
    constraint pk_s_order_header primary key (order_id),
    constraint fk_s_order_header_user_login foreign key (created_by_user_login_id) references user_login (user_login_id),
    constraint fk_s_order_header_customer foreign key (customer_id) references s_customer (customer_id)
);

create table s_order_item
(
    order_item_id Integer,
    order_id      Integer,
    product_id    Integer,
    quantity      Integer,
    constraint pk_s_order_item primary key (order_item_id),
    constraint fk_s_order_item_order_id foreign key (order_id) references s_order_header (order_id),
    constraint fk_s_order_item_product_id foreign key (product_id) references s_product (product_id)
);

create table s_product_price
(
    product_price_id Integer not null,
    product_id       Integer,
    price            decimal(18, 3),
    constraint pk_s_product_price primary key (product_price_id)
);

create table s_inventory_item
(
    inventory_item_id Integer not null,
    product_id        Integer,
    quantity          Integer,
    date_received     TIMESTAMP,
    constraint pk_s_inventory_item primary key (inventory_item_id)
);

create table s_inventory_item_detail
(
    inventory_item_detail_id Integer not null,
    inventory_item_id        Integer,
    effective_date           TIMESTAMP,
    quantity_on_hand_diff    DECIMAL(18, 6),
    order_item_id            Integer,
    constraint pk_s_inventory_item_detail primary key (inventory_item_detail_id),
    constraint fk_s_inventory_item_detail_order_item foreign key (order_item_id) references s_order_item (order_item_id),
    constraint fk_s_inventory_item_detail_inventory_item foreign key (inventory_item_id) references s_inventory_item (inventory_item_id),
);

