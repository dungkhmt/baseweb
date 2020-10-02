-- group
INSERT INTO public.security_group
    (group_id, description)
VALUES ('ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER', 'Group education: teacher');

INSERT INTO public.security_group
    (group_id, description)
VALUES ('ROLE_EDUCATION_LEARNING_MANAGEMENT_STUDENT', 'Group education: student');

--permission
INSERT INTO public.security_permission
    (permission_id, description)
VALUES ('EDUCATION_TEACHING_MANAGEMENT_TEACHER', 'Permission education teacher');

INSERT INTO public.security_permission
    (permission_id, description)
VALUES ('EDUCATION_LEARNING_MANAGEMENT_STUDENT', 'Permission education student');

--application
INSERT INTO public.application
(application_id, application_type_id, module_id, permission_id, description)
VALUES ('MENU_EDUCATION_LEARNING_MANAGEMENT_STUDENT', 'MENU', NULL, 'EDUCATION_LEARNING_MANAGEMENT_STUDENT',
'Menu learning management for students');

INSERT INTO public.application
(application_id, application_type_id, module_id, permission_id, description)
VALUES ('MENU_EDUCATION_LEARNING_MANAGEMENT_STUDENT_REGISTER_CLASS', 'MENU', 'MENU_EDUCATION_LEARNING_MANAGEMENT_STUDENT', 'EDUCATION_LEARNING_MANAGEMENT_STUDENT',
'Menu learning management for students: register to a class');

INSERT INTO public.application
(application_id, application_type_id, module_id, permission_id, description)
VALUES ('MENU_EDUCATION_LEARNING_MANAGEMENT_STUDENT_VIEW_LIST_CLASS', 'MENU', 'MENU_EDUCATION_LEARNING_MANAGEMENT_STUDENT', 'EDUCATION_LEARNING_MANAGEMENT_STUDENT',
'Menu learning management for students: view list of class');


INSERT INTO public.application
(application_id, application_type_id, module_id, permission_id, description)
VALUES ('MENU_EDUCATION_TEACHING_MANAGEMENT_TEACHER', 'MENU', NULL, 'EDUCATION_TEACHING_MANAGEMENT_TEACHER',
'Menu teaching management for teachers');

INSERT INTO public.application
(application_id, application_type_id, module_id, permission_id, description)
VALUES ('MENU_EDUCATION_TEACHING_MANAGEMENT_TEACHER_VIEW_LIST_CLASS', 'MENU', 'MENU_EDUCATION_TEACHING_MANAGEMENT_TEACHER', 'EDUCATION_TEACHING_MANAGEMENT_TEACHER',
'Menu teaching management for teachers: view list class');

INSERT INTO public.application
(application_id, application_type_id, module_id, permission_id, description)
VALUES ('MENU_EDUCATION_TEACHING_MANAGEMENT_TEACHER_CREATE_CLASS', 'MENU', 'MENU_EDUCATION_TEACHING_MANAGEMENT_TEACHER', 'EDUCATION_TEACHING_MANAGEMENT_TEACHER',
'Menu teaching management for teachers: create new class');

-- group permission
INSERT INTO public.security_group_permission
    (group_id, permission_id)
VALUES ('ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER', 'EDUCATION_TEACHING_MANAGEMENT_TEACHER');

INSERT INTO public.security_group_permission
    (group_id, permission_id)
VALUES ('ROLE_EDUCATION_LEARNING_MANAGEMENT_STUDENT', 'EDUCATION_LEARNING_MANAGEMENT_STUDENT');

