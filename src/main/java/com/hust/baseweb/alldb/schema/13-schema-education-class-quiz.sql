
create table quiz_course_topic(
    quiz_course_topic_id varchar(50),
    quiz_course_topic_name varchar(200),
    course_id varchar(10),
    last_updated_stamp            TIMESTAMP,
    created_stamp                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_quiz_course_topic primary key(quiz_course_topic_id),
    constraint fk_quiz_course_topic_course_id foreign key(course_id) references edu_course(id)
);

create table quiz_question(
    question_id uuid not null default uuid_generate_v1(),
    course_topic_id varchar(60),
    level_id varchar(50),
    question_content text,
    attachment varchar(500),
    status_id varchar(30),
    last_updated_stamp            TIMESTAMP,
    created_stamp                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_quiz_question_id primary key(question_id),
    constraint fk_quiz_question_course_id foreign key(course_topic_id) references quiz_course_topic(quiz_course_topic_id)
);

create table quiz_choice_answer(
    choice_answer_id uuid not null default uuid_generate_v1(),
    choice_answer_content text,
    question_id uuid,
    is_correct_answer char,
    last_updated_stamp            TIMESTAMP,
    created_stamp                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_quiz_choice_answer primary key(choice_answer_id),
    constraint fk_quiz_choice_answer_question_id foreign key(question_id) references quiz_question(question_id)
);

create table log_user_login_course_chapter_material(
    user_login_course_chapter_material_id uuid not null default uuid_generate_v1(),
    user_login_id varchar(60),
    edu_course_material_id uuid,
    last_updated_stamp            TIMESTAMP,
    created_stamp                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_user_login_course_chapter_material_id primary key(user_login_course_chapter_material_id),
    constraint fk_user_login_course_chapter_material_userlogin foreign key(user_login_id) references user_login(user_login_id),
    constraint fk_user_login_course_chapter_material_course_chapter_material foreign key(edu_course_material_id) references edu_course_chapter_material(edu_course_material_id)
);

create table log_user_login_quiz_question(
    log_user_login_quiz_question_id uuid not null default uuid_generate_v1(),
    user_login_id varchar(60),
    question_id uuid,
    last_updated_stamp            TIMESTAMP,
    created_stamp                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    constraint pk_log_user_login_quiz_question_id primary key(log_user_login_quiz_question_id),
    constraint fk_log_user_login_quiz_question_userlogin foreign key(user_login_id) references user_login(user_login_id),
    constraint fk_log_user_login_quiz_question_question_id foreign key(question_id) references quiz_question(question_id)
);

create table edu_quiz_test(
    test_id varchar(60),
    test_name varchar(200),
    schedule_datetime timestamp,
    duration int,
    course_id varchar(10),
    status_id varchar(30),
    created_by_user_login_id varchar(60),
    constraint pk_edu_quiz_test_id primary key(test_id),
    constraint fk_edu_quiz_test_created_by_user_login_id foreign key(created_by_user_login_id) references user_login(user_login_id),
    constraint fk_edu_quiz_test_course_id foreign key(course_id) references edu_course(id)
);

create table edu_test_question(
    question_id varchar(60),
    test_id varchar(60),
);
create table edu_test_participant(
    test_id varchar(60),
    participant_user_login_id varchar(60),
);

create table edu_test_participant_answer(
    
);

