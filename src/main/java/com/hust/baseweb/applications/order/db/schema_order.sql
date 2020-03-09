CREATE TABLE role_type
(
    role_type_id       VARCHAR(60) NOT NULL,
    parent_type_id     VARCHAR(60),
    description        TEXT,
    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_role_type_id PRIMARY KEY (role_type_id),
    CONSTRAINT fk_parent_type_id FOREIGN KEY (parent_type_id) REFERENCES role_type (role_type_id)
);

CREATE TABLE sales_channel
(
    sales_channel_id   VARCHAR(60) NOT NULL,
    sales_channel_name VARCHAR(100),
    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_sales_channel_id PRIMARY KEY (sales_channel_id)
);



CREATE TABLE status_type
(
    status_type_id     VARCHAR(60) NOT NULL,
    parent_type_id     VARCHAR(60),
    description        TEXT,
    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_status_type_id PRIMARY KEY (status_type_id),
    CONSTRAINT fk_parent_type_id FOREIGN KEY (parent_type_id) REFERENCES status_type (status_type_id)
);

CREATE TABLE status_item
(
    status_id          VARCHAR(60) NOT NULL,
    status_type_id     VARCHAR(60),
    status_code        VARCHAR(60),
    description        TEXT,
    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_status_id PRIMARY KEY (status_id),
    CONSTRAINT fk_status_type_id FOREIGN KEY (status_type_id) REFERENCES status_type (status_type_id)
);

CREATE TABLE order_type
(
    order_type_id      VARCHAR(60) NOT NULL,
    parent_type_id     VARCHAR(60),
    description        TEXT,
    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_order_type_id PRIMARY KEY (order_type_id),
    CONSTRAINT fk_parent_type FOREIGN KEY (parent_type_id) REFERENCES order_type (order_type_id)
);
































CREATE TABLE order_header
(
    order_id             VARCHAR(60) NOT NULL,
    order_type_id        VARCHAR(60),
    original_facility_id VARCHAR(60),
    product_store_id     VARCHAR(60),
    sales_channel_id     VARCHAR(60),
    created_by           VARCHAR(60),
    order_date           TIMESTAMP,
    currency_uom_id      VARCHAR(60),
    ship_to_address_id UUID,
    ship_to_address     VARCHAR (200),
    grand_total          DECIMAL(18, 2),
    description          TEXT,
    last_updated_stamp   TIMESTAMP,
    created_stamp        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_order PRIMARY KEY (order_id),
    CONSTRAINT fk_order_type_id FOREIGN KEY (order_type_id) REFERENCES order_type (order_type_id),
    CONSTRAINT fk_original_facility_id FOREIGN KEY (original_facility_id) REFERENCES facility (facility_id),
    constraint fk_order_address_id foreign key(ship_to_address_id) references postal_address(contact_mech_id),
    CONSTRAINT fk_product_store_id FOREIGN KEY (product_store_id) REFERENCES facility (facility_id)

);






//done
CREATE TABLE order_item_type
(
    order_item_type_id VARCHAR(60) NOT NULL,
    parent_type_id     VARCHAR(60) NOT NULL,
    description        TEXT,
    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_order_item_type_id PRIMARY KEY (order_item_type_id),
    CONSTRAINT fk_parent_type_id FOREIGN KEY (parent_type_id) REFERENCES order_item_type (order_item_type_id)
);

CREATE TABLE order_item
(
    order_id           VARCHAR(60) NOT NULL,
    order_item_seq_id  VARCHAR(60),
    order_item_type_id VARCHAR(60),
    product_id         VARCHAR(60),
    unit_price         DECIMAL(18, 2),
    quantity           DECIMAL(18, 6),
    status_id          VARCHAR(60),
    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_order_item_id PRIMARY KEY (order_id, order_item_seq_id),
    CONSTRAINT fk_order_item_type_id FOREIGN KEY (order_item_type_id) REFERENCES order_item_type (order_item_type_id),
    CONSTRAINT fk_order_item_product_id FOREIGN KEY (product_id) REFERENCES product (product_id),
    CONSTRAINT fk_order_item_order_id FOREIGN KEY (order_id) REFERENCES order_header (order_id),
    CONSTRAINT fk_status_id FOREIGN KEY (status_id) REFERENCES status_item (status_id)
);

CREATE TABLE order_role
(
    order_id           VARCHAR(60),
    party_id           UUID,
    role_type_id       VARCHAR(60),
    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_order_role PRIMARY KEY (order_id, party_id, role_type_id),
    CONSTRAINT fk_order_role_order_id FOREIGN KEY (order_id) REFERENCES order_header (order_id),
    CONSTRAINT fk_order_role_party_id FOREIGN KEY (party_id) REFERENCES party (party_id),
    CONSTRAINT fk_order_role_role_type_id FOREIGN KEY (role_type_id) REFERENCES role_type (role_type_id)
);


CREATE TABLE order_status
(
    order_status_id      VARCHAR(60),
    status_id            VARCHAR(60),
    order_id             VARCHAR(60),
    status_datetime      TIMESTAMP,
    status_user_login_id VARCHAR(60),
    last_updated_stamp   TIMESTAMP,
    created_stamp        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_order_status_id PRIMARY KEY (order_status_id),
    CONSTRAINT fk_order_status_status_id FOREIGN KEY (status_id) REFERENCES status_item (status_id),
    CONSTRAINT fk_order_status_order_id FOREIGN KEY (order_id) REFERENCES order_header (order_id),
    CONSTRAINT fk_status_user_login_id FOREIGN KEY (status_user_login_id) REFERENCES user_login (user_login_id)
);



create table product_promo_type
(
    product_promo_type_id VARCHAR(60) NOT NULL,
    parent_type_id        VARCHAR(60),
    last_updated_stamp    TIMESTAMP,
    created_stamp         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_product_promo_type primary key (product_promo_type_id),
    constraint fk_product_promo_parent_type foreign key (parent_type_id) references product_promo_type (product_promo_type_id)
);

CREATE TABLE product_promo
(
    product_promo_id      UUID         NOT NULL default uuid_generate_v1(),
    promo_name            varchar(100) NULL,
    promo_text            varchar(255) NULL,
    product_promo_type_id VARCHAR(60),
    from_date             TIMESTAMP,
    thru_date             TIMESTAMP,
    last_updated_stamp    TIMESTAMP,
    created_stamp         TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    constraint pk_product_promo primary key (product_promo_id),
    constraint fk_product_promo_product_promo_type_id foreign key (product_promo_type_id) references product_promo_type (product_promo_type_id)
);



CREATE TABLE product_promo_rule
(
    product_promo_rule_id      UUID         NOT NULL default uuid_generate_v1(),
    product_promo_id           UUID         NOT NULL,
    product_promo_rule_enum_id varchar(60)  NOT NULL,
    rule_name                  varchar(100) NULL,
    json_params                TEXT,
    last_updated_stamp         timestamptz  NULL,
    last_updated_tx_stamp      timestamptz  NULL,
    created_stamp              timestamptz  NULL,
    created_tx_stamp           timestamptz  NULL,
    CONSTRAINT pk_product_promo_rule PRIMARY KEY (product_promo_rule_id),
    CONSTRAINT fk_product_promo_rule_product_promo FOREIGN KEY (product_promo_id) REFERENCES product_promo (product_promo_id),
    constraint fk_product_promo_rule_enum foreign key (product_promo_rule_enum_id) references enumeration (enum_id)
);



CREATE TABLE product_promo_product
(
    product_promo_rule_id UUID        NOT NULL,
    product_id            varchar(60) NOT NULL,
    last_updated_stamp    TIMESTAMP,
    created_stamp         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_product_promo_product PRIMARY KEY (product_promo_rule_id, product_id),
    CONSTRAINT fk_product_promo_product_rule FOREIGN KEY (product_promo_rule_id) REFERENCES product_promo_rule (product_promo_rule_id),
    CONSTRAINT fk_product_promo_product_product FOREIGN KEY (product_id) REFERENCES product (product_id)
);

CREATE TABLE tax_authority_rate_type
(
    tax_auth_rate_type_id varchar(60) not null,
    description           text,
    last_updated_stamp    TIMESTAMP,
    created_stamp         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_tax_auth_rate_type primary key (tax_auth_rate_type_id)
);

CREATE TABLE tax_authority_rate_product
(
    tax_authority_rate_seq_id UUID not null default uuid_generate_v1(),
    tax_auth_rate_type_id     varchar(60),
    product_id                varchar(60),
    tax_percentage            decimal(18, 6),
    from_date                 TIMESTAMP,
    thru_date                 TIMESTAMP     DEFAULT NULL,
    description               text,
    last_updated_stamp        TIMESTAMP,
    created_stamp             TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    constraint pk_tax_auth_rate_prod primary key (tax_authority_rate_seq_id),
    constraint fk_tax_auth_rate_prod_product_id foreign key (product_id) references product (product_id),
    constraint fk_tax_auth_rate_prod_tax_auth_type_id foreign key (tax_auth_rate_type_id) references tax_authority_rate_type (tax_auth_rate_type_id)
);

CREATE TABLE order_adjustment_type
(
    order_adjustment_type_id varchar(60) NOT NULL,
    parent_type_id           varchar(60) DEFAULT NULL,
    description              TEXT,
    last_updated_stamp       TIMESTAMP,
    created_stamp            TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    constraint pk_order_adjustment_type PRIMARY KEY (order_adjustment_type_id),
    constraint fk_order_adjustment_type foreign key (parent_type_id) references order_adjustment_type (order_adjustment_type_id)
);

CREATE TABLE order_adjustment
(
    order_adjustment_id       UUID NOT NULL  default uuid_generate_v1(),
    order_adjustment_type_id  varchar(60),
    order_id                  VARCHAR(60),
    order_item_seq_id         VARCHAR(60),
    product_promo_rule_id     UUID NOT NULL,
    product_id                VARCHAR(60),
    tax_authority_rate_seq_id UUID,
    description               TEXT,
    amount                    decimal(18, 3) DEFAULT NULL,
    quantity                  Integer,
    last_updated_stamp        TIMESTAMP,
    created_stamp             TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    constraint pk_order_adjustment primary key (order_adjustment_id),
    constraint fk_order_adjustment_order_adjustment_type_id foreign key (order_adjustment_type_id) references order_adjustment_type (order_adjustment_type_id),
    constraint fk_order_adjustment_order_order_item foreign key (order_id, order_item_seq_id) references order_item (order_id, order_item_seq_id),
    constraint fk_order_adjustment_tax_auth_rate_type_id foreign key (tax_authority_rate_seq_id) references tax_authority_rate_product (tax_authority_rate_seq_id),
    constraint fk_order_adjustment_product_promo_product foreign key (product_promo_rule_id, product_id) references product_promo_product (product_promo_rule_id, product_id)
);

