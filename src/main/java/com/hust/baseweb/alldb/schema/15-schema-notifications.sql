create table notification_type(
    notification_type_id varchar(100),
    notification_type_name varchar(200),
    last_updated_stamp timestamp NULL,
	created_stamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,

    constraint pk_notification_type_id primary key(notification_type_id)
);

create table notifications(
    notification_id uuid not null default uuid_generate_v1(),
    notification_name varchar(500),
    notification_type_id varchar(100),
    to_user_login_id varchar(60),
    url varchar(200),
    status_id varchar(60),
    last_updated_stamp timestamp NULL,
	created_stamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,

    constraint pk_notification_id primary key(notification_id),
    constraint fk_notification_user_login_id foreign key(to_user_login_id) references user_login(user_login_id),
    constraint fk_notification_notification_type_id foreign key(notification_type_id) references notification_type(notification_type_id)

);
