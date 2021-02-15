
create table contest_problem
(
    problem_id varchar(60) not null,
    problem_name varchar(200),
    problem_statement text,
    created_by_user_login_id varchar(60),

    last_updated_stamp         timestamp DEFAULT CURRENT_TIMESTAMP,
    created_stamp              timestamp DEFAULT CURRENT_TIMESTAMP,
    constraint pk_contest_problem primary key (problem_id),
    constraint fk_contest_problem foreign key (created_by_user_login_id) references user_login(user_login_id)
);

create table contest_problem_test(
    problem_test_id uuid not null default uuid_generate_v1(),
    problem_id varchar(60),
    problem_test_filename varchar(200),
    problem_test_point int,

    last_updated_stamp         timestamp DEFAULT CURRENT_TIMESTAMP,
    created_stamp              timestamp DEFAULT CURRENT_TIMESTAMP,
    constraint pk_contest_problem_test primary key (problem_test_id),
    constraint fk_contest_problem_test_problem_id foreign key (problem_id) references contest_problem(problem_id)

);

create table contest(
    contest_id varchar(60),
    contest_name varchar(200),
    last_updated_stamp         timestamp DEFAULT CURRENT_TIMESTAMP,
    created_stamp              timestamp DEFAULT CURRENT_TIMESTAMP,
    constraint pk_contest_id primary key contest_id,

);
