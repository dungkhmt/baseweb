--lấy ra các group của userlogin admin
select * from user_login_security_group where user_login_id = 'admin';


--lấy ra các permission của userlogin admin
select * from security_group_permission where group_id in (select group_id from user_login_security_group where user_login_id = 'admin');


--lấy ra các application la menu (application type la menu) của userlogin admin
select * from application where permission_id in
(select permission_id from security_group_permission
where group_id in (select group_id from user_login_security_group where user_login_id = 'leanhtuan'))
and application_type_id = 'MENU';
