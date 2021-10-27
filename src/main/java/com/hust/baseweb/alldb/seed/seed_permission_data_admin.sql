INSERT INTO public.security_group
(group_id, description, last_updated_stamp, created_stamp, group_name)
VALUES('ROLE_DATA_ADMIN', 'data admin group', NULL, '2021-10-22 14:08:52.934', NULL);

INSERT INTO public.security_permission
(permission_id, description, last_updated_stamp, created_stamp)
VALUES('DATA_ADMIN', 'Administrate data', NULL, '2021-10-22 14:08:52.934');

INSERT INTO public.security_group_permission
(group_id, permission_id, last_updated_stamp, created_stamp)
VALUES('ROLE_FULL_ADMIN', 'DATA_ADMIN', NULL, '2021-10-22 14:08:52.934');

INSERT INTO public.security_group_permission
(group_id, permission_id, last_updated_stamp, created_stamp)
VALUES('ROLE_DATA_ADMIN', 'DATA_ADMIN', NULL, '2021-10-22 14:08:52.934');

INSERT INTO public.application
(application_id, application_type_id, module_id, permission_id, description, last_updated_stamp, created_stamp)
VALUES('MENU_DATA_ADMIN', 'MENU', NULL, 'DATA_ADMIN', 'Menu data admin', '2021-10-22 15:39:05.031', '2021-10-22 14:08:52.934');

INSERT INTO public.application
(application_id, application_type_id, module_id, permission_id, description, last_updated_stamp, created_stamp)
VALUES('MENU_DATA_ADMIN_NOTIFICATIONS', 'MENU', 'MENU_DATA_ADMIN', 'DATA_ADMIN', 'Menu data admin', NULL, '2021-10-22 14:08:52.934');

INSERT INTO public.application
(application_id, application_type_id, module_id, permission_id, description, last_updated_stamp, created_stamp)
VALUES('MENU_DATA_ADMIN_VIEW_COURSE_VIDEO', 'MENU', 'MENU_DATA_ADMIN', 'DATA_ADMIN', 'Menu admin view course video', NULL, '2021-10-22 15:35:30.537');

