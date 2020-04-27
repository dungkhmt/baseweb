-- group
INSERT INTO public.security_group
(group_id, description)
VALUES('ROLE_SALESMAN', 'salesman group');
INSERT INTO public.security_group
(group_id, description)
VALUES('ROLE_SALESSUP', 'sales supervisor group');
INSERT INTO public.security_group
(group_id, description)
VALUES('ROLE_SALES_ADMIN', 'sales admin');

-- permission
INSERT INTO public.security_permission
(permission_id, description)
VALUES('PERM_RETAILOUTLET_CREATE', 'Create retail outlets');
INSERT INTO public.security_permission
(permission_id, description)
VALUES('PERM_RETAILOUTLET_VIEW', 'View retail outlets');
INSERT INTO public.security_permission
(permission_id, description)
VALUES('PERM_RETAILOUTLET_VIEW_ALL', 'View retail outlets');

-- application
INSERT INTO public.application
(application_id, application_type_id, module_id, permission_id, description)
VALUES('MENU_RETAIL_OUTLET', 'MENU', NULL, NULL, 'Menu  about retail outlets');
INSERT INTO public.application
(application_id, application_type_id, module_id, permission_id, description)
VALUES('MENU_CREATE_RETAIL_OUTLET', 'MENU', 'MENU_RETAIL_OUTLET', 'PERM_RETAILOUTLET_CREATE', 'Menu  create retail outlets');
INSERT INTO public.application
(application_id, application_type_id, module_id, permission_id, description)
VALUES('MENU_VIEW_RETAIL_OUTLET', 'MENU', 'MENU_RETAIL_OUTLET', 'PERM_RETAILOUTLET_VIEW', 'Menu  view list retail outlets');
INSERT INTO public.application
(application_id, application_type_id, module_id, permission_id, description)
VALUES('MENU_VIEW_ALL_RETAIL_OUTLET', 'MENU', 'MENU_RETAIL_OUTLET', 'PERM_RETAILOUTLET_VIEW_ALL', 'Menu  view list of all retail outlets');

--- group permission
INSERT INTO public.security_group_permission
(group_id, permission_id)
VALUES('ROLE_SALESMAN', 'PERM_RETAILOUTLET_CREATE');
INSERT INTO public.security_group_permission
(group_id, permission_id)
VALUES('ROLE_SALESMAN', 'PERM_RETAILOUTLET_VIEW');

INSERT INTO public.security_group_permission
(group_id, permission_id)
VALUES('ROLE_SALESSUP', 'PERM_RETAILOUTLET_CREATE');
INSERT INTO public.security_group_permission
(group_id, permission_id)
VALUES('ROLE_SALESSUP', 'PERM_RETAILOUTLET_VIEW');

INSERT INTO public.security_group_permission
(group_id, permission_id)
VALUES('ROLE_SALES_ADMIN', 'PERM_RETAILOUTLET_CREATE');
INSERT INTO public.security_group_permission
(group_id, permission_id)
VALUES('ROLE_SALES_ADMIN', 'PERM_RETAILOUTLET_VIEW');
INSERT INTO public.security_group_permission
(group_id, permission_id)
VALUES('ROLE_SALES_ADMIN', 'PERM_RETAILOUTLET_VIEW_ALL');

INSERT INTO public.security_group_permission
(group_id, permission_id)
VALUES('ROLE_FULL_ADMIN', 'PERM_RETAILOUTLET_CREATE');
INSERT INTO public.security_group_permission
(group_id, permission_id)
VALUES('ROLE_FULL_ADMIN', 'PERM_RETAILOUTLET_VIEW');
INSERT INTO public.security_group_permission
(group_id, permission_id)
VALUES('ROLE_FULL_ADMIN', 'PERM_RETAILOUTLET_VIEW_ALL');

-- user_login security group
INSERT INTO user_login_security_group (user_login_id, group_id)
VALUES ('dungpq', 'ROLE_SALES_ADMIN');
INSERT INTO user_login_security_group (user_login_id, group_id)
VALUES ('ninhpham', 'ROLE_SALESSUP');
