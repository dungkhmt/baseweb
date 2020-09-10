create table edu_department
(
    department_id      varchar(60),
    department_name    varchar(200),
    last_updated_stamp timestamp,
    created_stamp      timestamp,
    constraint pk_edu_department primary key (department_id)
);

create table edu_course
(
    course_id          varchar(60),
    course_name        varchar(200),
    credit             int,
    last_updated_stamp timestamp,
    created_stamp      timestamp,
    constraint pk_edu_course primary key (course_id)
);
create table edu_semester
(
    semester_id        varchar(60),
    semester_name      varchar(200),
    last_updated_stamp timestamp,
    created_stamp      timestamp,
    constraint pk_semester primary key (semester_id)
);

create table edu_class
(
    class_id           varchar(60),
    class_name         varchar(200),
    course_id          varchar(60),
    class_type         varchar(60),
    department_id      varchar(60),
    semester_id        varchar(60),
    session_id         varchar(200),
    last_updated_stamp timestamp,
    created_stamp      timestamp,
    constraint pk_edu_class primary key (class_id),
    constraint fk_edu_class foreign key (department_id) references edu_department (department_id),
    constraint fk_edu_class_semester_id foreign key (semester_id) references edu_semester (semester_id),
    constraint fk_edu_class_course_id foreign key (course_id) references edu_course (course_id)
);

create table edu_teacher
(
    teacher_id         varchar(60),
    teacher_name       varchar(200),
    email              varchar(60),
    max_credit         int,
    last_updated_stamp timestamp,
    created_stamp      timestamp,
    constraint pk_edu_teacher primary key (teacher_id)
);

create table edu_course_teacher_preference
(
    course_id          varchar(60),
    teacher_id         varchar(60),
    class_type         varchar(60),
    last_updated_stamp timestamp,
    created_stamp      timestamp,
    constraint pk_edu_course_teacher_preference primary key (course_id, teacher_id, class_type),
    constraint fk_edu_course_teacher_preference_course_id foreign key (course_id) references edu_course (course_id),
    constraint fk_edu_course_teacher_preference_teacher_id foreign key (teacher_id) references edu_teacher (teacher_id)
);
create table edu_class_teacher_asignment
(
    class_id           varchar(60),
    teacher_id         varchar(60),
    last_updated_stamp timestamp,
    created_stamp      timestamp,
    constraint pk_class_teacher_assignment primary key (class_id, teacher_id),
    constraint fk_class_teacher_assignment_teacher_id foreign key (teacher_id) references edu_teacher (teacher_id),
    constraint fk_class_teacher_assignment_class_id foreign key (class_id) references edu_class (class_id)
);

create table edu_class_student(
    class_id varchar(60),
    student_id varchar(60),
    status_id varchar(60),
    last_updated_stamp timestamp,
    created_stamp      timestamp,
    constraint pk_class_student primary key(class_id,student_id),
    constraint fk_class_student_class_id foreign key(class_id) references edu_class(class_id),
    constraint fk_class_student_student_id foreign key(student_id) references user_login(user_login_id)
    constraint fk_class_student_status_id foreign key(status_id) references status_item(status_id)
);

create table edu_class_assignment(
    assignment_id uuid not null default uuid_generate_v1(),
    assignment_name varchar(200),
    class_id varchar(60),
    description text,
    last_updated_stamp timestamp,
    created_stamp      timestamp,
    constraint pk_class_assignment_assignment_id primary key(assignment_id),
    constraint fk_class_assignment_class_id foreign key(class_id) references edu_class(class_id)
);

create table edu_student_assignment_submission(
    student_assignment_submission_id uuid not null default uuid_generate_v1(),
    assignment_id uuid,
    student_id varchar(60),
    submission_date_time timestamp,
    link_source text,
    last_updated_stamp timestamp,
    created_stamp      timestamp,
    constraint pk_student_assignment_submission_id primary key(student_assignment_submission_id),
    constraint fk_student_assignment_submission_student_id foreign key(student_id) references user_login(user_login_id),
    constraint fk_student_assignment_submission_assignment_id foreign key(assignment_id) references edu_class_assignment(assignment_id)
);


