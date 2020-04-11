create table department
(
    department_id           UUID not null default uuid_generate_v1(),
    deaprtment_name         VARCHAR(100),
    start_date              TIMESTAMP,
    created_by_userLogin_id VARCHAR(255),
    last_updated_stamp      TIMESTAMP,
    created_stamp           TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    constraint pk_department primary key (department_id),
    constraint fk_department_create_by_user_login_id foreign key (created_by_userLogin_id) references user_login (user_login_id)
);
