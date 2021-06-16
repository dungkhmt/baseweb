create table teacher(
    teacher_id varchar(60),
    teacher_name varchar(200),
    user_login_id varchar(60),
    last_updated_stamp            TIMESTAMP,
    created_stamp                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_teacher primary key(teacher_id)
);
create table teacher_course(
    teacher_id varchar(60),
    course_id varchar(60),
    priority int,

    last_updated_stamp            TIMESTAMP,
    created_stamp                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_teacher_course primary key(teacher_id, course_id),
    constraint fk_teacher_course_teacher_id foreign key(teacher_id) references teacher(teacher_id),
    constraint fk_teacher_course_course_id foreign key(course_id) references edu_course(id)
);

create table class_teacher_assignment_plan(
    plan_id uuid not null default uuid_generate_v1(),
    plan_name varchar(200),
    created_by_user_login_id varchar(60),
    last_updated_stamp            TIMESTAMP,
    created_stamp                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    constraint pk_class_teacher_assignment_plan primary key(plan_id),
    constraint fk_class_teacher_assignment_plan_created_by_user_login_id foreign key(created_by_user_login_id) references user_login(user_login_id)
);

create table class_teacher_assignment_class_info(
    class_id varchar(60),
    plan_id uuid,
    school_name varchar(200),
    semester_id varchar(60),
    course_id varchar(60),
    class_name varchar(200),
    credit_info varchar(30),
    class_note varchar(200),
    program varchar(60),
    semester_type varchar(30),
    enrollment int,
    max_enrollment int,
    class_type varchar(30),
    time_table varchar(200),
    lesson varchar(200),
    department_id varchar(60),
    teacher_id varchar(60),
    created_by_user_login_id varchar(60),

    last_updated_stamp            TIMESTAMP,
    created_stamp                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    constraint pk_class_teacher_assignment_class_info primary key(class_id, plan_id),
    constraint fk_class_teacher_assignment_class_info_created_by_user_login foreign key(created_by_user_login_id) references user_login(user_login_id)


);
