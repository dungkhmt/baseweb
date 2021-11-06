create table edu_resouce_domain(
    resource_domain_id uuid not null default uuid_generate_v1(),
    recourse_domain_name varchar(500),
	last_updated_stamp timestamp NULL,
	created_stamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    constraint pk_edu_recourse_domain primary key(resource_domain_id)
)

create table edu_resource(
    recourse_id uuid not null default uuid_generate_v1(),
    recourse_link varchar(500),
    recourse_domain_id uuid,
    created_by_user_login_id varchar(60),
    description text,
    status_id varchar(200),
	last_updated_stamp timestamp NULL,
	created_stamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    constraint pk_edu_recourse primary key(recourse_id),
    constraint fk_edu_recourse_domain foreign key(recourse_domain_id) references  edu_resouce_domain(resource_domain_id),
    constraint fk_edu_recourse_created_by_user_login_id foreign key(created_by_user_login_id) references user_login(user_login_id)
)
