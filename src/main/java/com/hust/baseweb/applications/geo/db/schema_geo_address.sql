create table geo_type
(
    geo_type_id        VARCHAR(60) NOT NULL,
    parent_type_id     VARCHAR(60),
    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_geo_type primary key (geo_type_id)

);

create table geo
(
    geo_id             VARCHAR(60) NOT NULL,
    geo_type_id        VARCHAR(60),
    geo_name           VARCHAR(100),
    geo_code           VARCHAR(60),
    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_geo primary key (geo_id),
    constraint fk_geo_geo_type_id foreign key (geo_type_id) references geo_type (geo_type_id)
);

create table geo_point
(
    geo_point_id       UUID        NOT NULL default uuid_generate_v1(),

    longitude          VARCHAR(30) not null,
    latitude           VARCHAR(30) not null,

    last_updated_stamp TIMESTAMP,
    created_stamp      TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    constraint pk_geo_point primary key (geo_point_id)

);

create table distance_traveltime_geo_points
(
    from_geo_point_id  uuid not null
        constraint distance_traveltime_geo_points_geo_point_geo_point_id_from_fk
            references geo_point,
    to_geo_point_id    uuid not null
        constraint distance_traveltime_geo_points_geo_point_geo_point_id_to_fk
            references geo_point,
    distance           numeric,
    travel_time        numeric,
    last_updated_stamp timestamp,
    created_stamp      timestamp default current_timestamp
);

create unique index distance_traveltime_geo_points_from_geo_point_id_to_geo_point_id_uindex
    on distance_traveltime_geo_points (from_geo_point_id, to_geo_point_id);

alter table distance_traveltime_geo_points
    add constraint distance_traveltime_geo_points_pk
        primary key (from_geo_point_id, to_geo_point_id);



create table postal_address
(
    contact_mech_id       UUID NOT NULL default uuid_generate_v1(),
    location_code         VARCHAR(60),
    address               VARCHAR(200),
    postal_code           VARCHAR(60),
    geo_point_id          UUID,
    country_geo_id        VARCHAR(60),
    state_province_geo_id VARCHAR(60),
    city                  VARCHAR(200),
    last_updated_stamp    TIMESTAMP,
    created_stamp         TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    constraint pk_postal_address primary key (contact_mech_id),
    constraint fk_postal_address_geo_point_id foreign key (geo_point_id) references geo_point (geo_point_id),
    constraint fk_postal_address_country_geo_id foreign key (country_geo_id) references geo (geo_id),
    constraint fk_postal_address_state_province_geo_id foreign key (state_province_geo_id) references geo (geo_id)
);

create table contact_mech_purpose_type
(
    contact_mech_purpose_type_id VARCHAR(60) NOT NULL,
    description                  TEXT,
    last_updated_stamp           TIMESTAMP,
    created_stamp                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_contact_mech_purpose_type primary key (contact_mech_purpose_type_id)
);

create table party_contact_mech_purpose
(
    party_id                     UUID NOT NULL,
    contact_mech_id              UUID,
    contact_mech_purpose_type_id VARCHAR(60),
    from_date                    TIMESTAMP,
    thru_date                    TIMESTAMP,
    last_updated_stamp           TIMESTAMP,
    created_stamp                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_party_contact_mech_purpose primary key (party_id, contact_mech_id, contact_mech_purpose_type_id, from_date),
    constraint fk_party_contact_mech_purpose_party_id foreign key (party_id) references party (party_id),
    constraint fk_party_contact_mech_purpose_contact_mech_id foreign key (contact_mech_id) references postal_address (contact_mech_id),
    constraint fk_party_contact_mech_purpose_contact_mech_purpose_type_id foreign key (contact_mech_purpose_type_id) references contact_mech_purpose_type (contact_mech_purpose_type_id)
);