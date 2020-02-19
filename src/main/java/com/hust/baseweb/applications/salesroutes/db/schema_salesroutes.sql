create table sales_route_config
(
    sales_route_config_id UUID NOT NULL default uuid_generate_v1(),
    days                  VARCHAR(60),
    repeat_week           Integer,
    status_id             VARCHAR(60),
    description           TEXT,
    last_updated_stamp    TIMESTAMP,
    created_stamp         TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    constraint pk_sales_route_config primary key (sales_route_config_id),
    constraint fk_sales_route_config_status foreign key (status_id) references status_item (status_id)
);

create table sales_route_config_customer
(
    sales_route_config_customer_id UUID NOT NULL default uuid_generate_v1(),
    sales_route_config_id          UUID NOT NULL,
    party_customer_id              UUID NOT NULL,
    from_date                      TIMESTAMP,
    thru_date                      TIMESTAMP,
    start_execute_date             VARCHAR(60),
    last_updated_stamp             TIMESTAMP,
    created_stamp                  TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    constraint pk_sales_route_config_customer primary key (sales_route_config_customer_id),
    constraint fk_sales_route_config_customer_sales_route_config_id foreign key (sales_route_config_id) references sales_route_config (sales_route_config_id),
    constraint fk_sales_route_config_customer_party_customer_id foreign key (party_customer_id) references party_customer (party_id)
);

create table sales_planning_period_config
(
    sales_planning_period_config_id UUID NOT NULL default uuid_generate_v1(),
    from_date                       VARCHAR(60),
    to_date                         VARCHAR(60),
    created_by                      VARCHAR(60),
    status_id                       VARCHAR(60),
    constraint pk_sales_planning_period_config primary key (sales_planning_period_config_id),
    constraint fk_sales_planning_period_config_created_by foreign key (created_by) references user_login (user_login_id),
    constraint fk_sales_planning_period_config_status foreign key (status_id) references status_item (status_id)
);

create table sales_planning_period
(
    sales_planning_period_id        UUID NOT NULL default uuid_generate_v1(),
    sales_planning_period_config_id UUID NOT NULL,
    sales_route_config_customer_id  UUID,
    constraint pk_sales_planning_period primary key (sales_planning_period_config_id),
    constraint fk_sales_planning_period_planning_period_config foreign key (sales_planning_period_config_id) references sales_planning_period_config (sales_planning_period_config_id),
    constraint fk_sales_planning_period_customer_config foreign key (sales_route_config_customer_id) references sales_route_config_customer (sales_route_config_customer_id)
);

create table sales_route_detail
(
    sales_route_detail_id          UUID NOT NULL,
    sales_route_config_customer_id UUID NOT NULL,
    party_salesman_id              UUID NOT NULL,
    party_customer_id              UUID NOT NULL,
    sequence                       Integer,
    execute_date                   VARCHAR(60),
    last_updated_stamp             TIMESTAMP,
    created_stamp                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_sales_route_detail primary key (sales_route_detail_id),
    constraint fk_sales_route_detail_sales_route_config_customer foreign key (sales_route_config_customer_id) references sales_route_config_customer (sales_route_config_customer_id),
    constraint fk_sales_route_detail_party_customer foreign key (party_customer_id) references party_customer (party_id),
    constraint fk_sales_route_detail_salesman_id foreign key (party_salesman_id) references party_salesman (party_id)
);