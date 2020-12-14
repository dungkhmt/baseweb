create table edu_course_topic(
    topic_id varchar(60),
    topic_name varchar(200),
    edu_course_id varchar(10),
    last_updated_stamp            TIMESTAMP,
    created_stamp                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

);
create table question(
    question_id varchar(60),
    topic_id varchar(60),
    question_content text,
    last_updated_stamp            TIMESTAMP,
    created_stamp                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

);

create table choice_answer(
    choice_answer_id varchar(60),
    choice_answer_content varchar(1024),
    question_id varchar(60),
    is_correct_answer char,
    last_updated_stamp            TIMESTAMP,
    created_stamp                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

);

create table edu_test(
    test_id varchar(60),

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

