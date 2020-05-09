create table backlog_project(
    backlog_project_id varchar(60),
    backlog_project_name varchar(200),
    last_updated_stamp timestamp,
    created_stamp timestamp,
    constraint pk_backlog_project primary key(backlog_project_id)
);

create table backlog_task_category(
    backlog_task_category_id varchar(60),
    backlog_task_category_name varchar(200),
    last_updated_stamp timestamp,
    created_stamp timestamp,
    constraint pk_backlog_task_category primary key(backlog_task_category_id)
);

create table backlog_task(
    backlog_task_id varchar(60),
    backlog_task_name varchar(200),
    backlog_task_category_id varchar(60),
    backlog_description text,
    backlog_project_id varchar(60),
    created_date timestamp,
    created_by_user_login_id varchar(60),
    due_date timestamp,
    status_id varchar(60),
    priority_id varchar(10),
    last_updated_stamp timestamp,
    created_stamp timestamp,
    constraint pk_backlog_task primary key(backlog_task_id),
    constraint fk_backlog_task_category_id foreign key(backlog_task_category_id) references backlog_task_category(backlog_task_category_id)
);

create table backlog_task_assignment(
    backlog_task_assignment_id uuid not null default uuid_generate_v1(),
    backlog_task_id varchar(60),
    assigned_to_party_id uuid not null,
    start_date timestamp,
    finished_date timestamp,
    status_id varchar(60),
    last_updated_stamp timestamp,
    created_stamp timestamp,
    constraint pk_backlog_task_assignment primary key(backlog_task_assignment_id),
    constraint fk_backlog_task_assignment_to_assigned_party_id foreign key(assigned_to_party_id) references party(party_id),
    constraint fk_backlog_task_assignment_backlog_taks_id foreign key(backlog_task_id) references backlog_task(backlog_task_id),
    constraint fk_backlog_task_assignment_status_id foreign key(status_id) references status_item(status_id)
);

