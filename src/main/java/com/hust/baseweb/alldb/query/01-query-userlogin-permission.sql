--lấy ra các group của userlogin admin
select * from user_login_security_group where user_login_id = 'admin';


--lấy ra các permission của userlogin admin
select * from security_group_permission where group_id in (select group_id from user_login_security_group where user_login_id = 'admin');


--lấy ra các application la menu (application type la menu) của userlogin admin
select * from application where permission_id in
(select permission_id from security_group_permission
where group_id in (select group_id from user_login_security_group where user_login_id = 'leanhtuan'))
and application_type_id = 'MENU';


-- lay ra danh sach day du cua nguoi dung he thong (party_id, ho ten day du, user_login_id)
select pe.first_name, pe.middle_name, pe.last_name, u.user_login_id, p.party_id from party as p, user_login as u, person as pe
where p.party_id = pe.party_id and u.party_id = p.party_id;

