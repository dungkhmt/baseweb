
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

create table edu_test(
    test_id varchar(60),
    test_name varchar(200)

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

