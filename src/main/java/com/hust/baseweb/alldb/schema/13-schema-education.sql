-- Drop table

-- DROP TABLE public.edu_department;

create TABLE public.edu_department (
	id varchar(10) NOT NULL,
	department_name varchar(50) NULL,
	CONSTRAINT pk_edu_department PRIMARY KEY (id)
);



-- Drop table

-- DROP TABLE public.edu_course;

create TABLE public.edu_course (
	id varchar(10) NOT NULL,
	course_name varchar(80) NOT NULL,
	credit int2 NOT NULL,
	last_updated_stamp timestamp NULL,
	created_stamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT edu_course_check CHECK ((created_stamp <= last_updated_stamp)),
	CONSTRAINT pk_edu_course PRIMARY KEY (id)
);



-- Drop table

-- DROP TABLE public.edu_semester;

create TABLE public.edu_semester (
	id int2 NOT NULL,
	semester_name varchar(60) NULL,
	last_updated_stamp timestamp NULL,
	created_stamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	is_active bool NOT NULL DEFAULT false,
	CONSTRAINT edu_semester_check CHECK ((created_stamp <= last_updated_stamp)),
	CONSTRAINT pk_semester PRIMARY KEY (id)
);



-- public.edu_class definition

-- Drop table

-- DROP TABLE edu_class;

CREATE TABLE edu_class (
	id uuid NOT NULL DEFAULT uuid_generate_v1(),
	code int4 NOT NULL,
	semester_id int2 NOT NULL,
	course_id varchar(10) NOT NULL,
	class_type varchar(10) NOT NULL,
	department_id varchar(10) NOT NULL,
	teacher_id varchar(255) NULL,
	last_updated_stamp timestamp NULL,
	created_stamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT edu_class_check CHECK ((created_stamp <= last_updated_stamp)),
	CONSTRAINT pk_edu_class PRIMARY KEY (id)
);


-- public.edu_class foreign keys

ALTER TABLE public.edu_class ADD CONSTRAINT fk_class__user_login FOREIGN KEY (teacher_id) REFERENCES user_login(user_login_id);
ALTER TABLE public.edu_class ADD CONSTRAINT fk_class_course FOREIGN KEY (course_id) REFERENCES edu_course(id);
ALTER TABLE public.edu_class ADD CONSTRAINT fk_class_department FOREIGN KEY (department_id) REFERENCES edu_department(id);
ALTER TABLE public.edu_class ADD CONSTRAINT fk_class_semester FOREIGN KEY (semester_id) REFERENCES edu_semester(id);



-- public.edu_class_registration definition

-- Drop table

-- DROP TABLE edu_class_registration;

CREATE TABLE edu_class_registration (
	class_id uuid NOT NULL,
	student_id varchar(255) NOT NULL,
	status varchar(20) NOT NULL,
	last_updated_stamp timestamp NULL,
	created_stamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT edu_class_registration_check CHECK ((created_stamp <= last_updated_stamp)),
	CONSTRAINT pk_class_registration PRIMARY KEY (class_id, student_id)
);


-- public.edu_class_registration foreign keys

ALTER TABLE public.edu_class_registration ADD CONSTRAINT fk_class_registration__user_login FOREIGN KEY (student_id) REFERENCES user_login(user_login_id);
ALTER TABLE public.edu_class_registration ADD CONSTRAINT fk_class_registration_class FOREIGN KEY (class_id) REFERENCES edu_class(id);



-- public.edu_assignment definition

-- Drop table

-- DROP TABLE edu_assignment;

CREATE TABLE edu_assignment (
	id uuid NOT NULL DEFAULT uuid_generate_v1(),
	assignment_name varchar(255) NOT NULL,
	subject text NULL,
	class_id uuid NOT NULL,
	last_updated_stamp timestamp NULL,
	created_stamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	dead_line timestamp NOT NULL,
	CONSTRAINT edu_assignment_check CHECK ((created_stamp <= last_updated_stamp)),
	CONSTRAINT edu_assignment_check1 CHECK ((dead_line > created_stamp)),
	CONSTRAINT pk_edu_assignment PRIMARY KEY (id)
);


-- public.edu_assignment foreign keys

ALTER TABLE public.edu_assignment ADD CONSTRAINT fk_assignment_class FOREIGN KEY (class_id) REFERENCES edu_class(id);



-- public.edu_assignment_submission definition

-- Drop table

-- DROP TABLE edu_assignment_submission;

CREATE TABLE edu_assignment_submission (
	id uuid NOT NULL DEFAULT uuid_generate_v1(),
	assignment_id uuid NOT NULL,
	student_id varchar(255) NOT NULL,
	original_file_name varchar(255) NOT NULL,
	last_updated_stamp timestamp NULL,
	created_stamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT edu_assignment_submission_check CHECK ((created_stamp <= last_updated_stamp)),
	CONSTRAINT pk_assignment_submission PRIMARY KEY (id)
);


-- public.edu_assignment_submission foreign keys

ALTER TABLE public.edu_assignment_submission ADD CONSTRAINT fk_assignment_submission__user_login FOREIGN KEY (student_id) REFERENCES user_login(user_login_id);
ALTER TABLE public.edu_assignment_submission ADD CONSTRAINT fk_assignment_submission_assignment FOREIGN KEY (assignment_id) REFERENCES edu_assignment(id);



/*create table edu_teacher
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
);*/
