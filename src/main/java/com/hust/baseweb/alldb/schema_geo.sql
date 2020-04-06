
create table province(
    province_id  VARCHAR(60),
    province_name VARCHAR(200),
    last_updated_stamp  TIMESTAMP,
    created_stamp       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_province primary key(province_id)
);

create table district(
    district_id VARCHAR(60),
    district_name VARCHAR(200),
    province_id   VARCHAR(60),
    last_updated_stamp  TIMESTAMP,
    created_stamp       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_district primary key(district_id),
    constraint fk_district_province_id foreign key(province_id) references province(province_id)
);

create table commune(
    commune_id VARCHAR(60),
    commune_name VARCHAR(200),
    district_id VARCHAR(60),
    last_updated_stamp  TIMESTAMP,
    created_stamp       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_commune primary key(commune_id),
    constraint fk_commune_district_id foreign key(district_id) references district(district_id)
);