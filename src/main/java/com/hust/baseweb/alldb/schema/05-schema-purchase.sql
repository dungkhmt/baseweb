create table supplier(
    party_id uuid not null default uuid_generate_v1(),
    suppier_name varchar(200),
    supplier_code varchar(60),
    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    constraint pk_supplier primary key(party_id),
    constraint fk_supplier_party_id foreign key(party_id) references party(party_id)
);

create table product_price_supplier(
    product_price_supplier_id uuid not null default uuid_generate_v1(),
    party_supplier_id uuid not null,
    product_id varchar(60),
    unitPrice  int,
    from_date timestamp,
    thru_date timestamp,
    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    constraint pk_product_price_supplier primary key(product_price_supplier_id),
    constraint fk_product_price_supplier_product_id foreign key(product_id) references product(product_id)
);