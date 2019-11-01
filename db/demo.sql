INSERT INTO party (party_id, party_type_id, external_id, description, status_id, created_date, created_by_user_login, last_modified_date, last_modified_by_user_login, is_unread, last_updated_stamp, created_stamp, party_code)
VALUES ('402881896d2a9e83016d2aaa50490001', 'PERSON', NULL, NULL, 'PARTY_ENABLED', NOW(), 'admin', NULL, NULL, FALSE,NULL, NOW(), NULL);
INSERT  INTO person (party_id, first_name, middle_name, last_name, gender, birth_date, last_updated_stamp, created_stamp)
VALUES ('402881896d2a9e83016d2aaa50490001','Nguyễn','Văn','Sêu','M',NOW(),null,NOW());
INSERT INTO user_login (user_login_id, current_password, password_hint, is_system, enabled, has_logged_out, require_password_change, disabled_date_time, successive_failed_logins, last_updated_stamp, created_stamp, party_id)
VALUES ( 'nguyenvanseu', '$2a$10$3l5d7fPeBuwUSbXYBiSiOuohtbgyDu2J/C5TWGHmszoD4a6mKFmey', NULL, FALSE, TRUE, FALSE, FALSE, NULL, NULL, NOW(), NOW(), '402881896d2a9e83016d2aaa50490001');
INSERT INTO user_login_security_group (user_login_id, group_id, last_updated_stamp, created_stamp) VALUES ('nguyenvanseu', 'ROLE_SALE_MANAGER', NOW(), NOW());

INSERT INTO party (party_id, party_type_id, external_id, description, status_id, created_date, created_by_user_login, last_modified_date, last_modified_by_user_login, is_unread, last_updated_stamp, created_stamp, party_code)
VALUES ('402881896d2a9e83016d2aaa504f0002', 'PERSON', NULL, NULL, 'PARTY_ENABLED', NOW(), 'admin', NULL, NULL, FALSE, NULL, NOW(), NULL);
INSERT  INTO person (party_id, first_name, middle_name, last_name, gender, birth_date, last_updated_stamp, created_stamp)
VALUES ('402881896d2a9e83016d2aaa504f0002','Trần','Thị','Toán','M',NOW(),null,NOW());
INSERT INTO user_login (user_login_id, current_password, password_hint, is_system, enabled, has_logged_out, require_password_change, disabled_date_time, successive_failed_logins, last_updated_stamp, created_stamp, party_id)
VALUES ( 'tranthitoan', '$2a$10$3l5d7fPeBuwUSbXYBiSiOuohtbgyDu2J/C5TWGHmszoD4a6mKFmey', NULL, FALSE, TRUE, FALSE, FALSE, NULL, NULL, NOW(), NOW(), '402881896d2a9e83016d2aaa504f0002');
INSERT INTO user_login_security_group (user_login_id, group_id, last_updated_stamp, created_stamp) VALUES ('tranthitoan', 'ROLE_ACCOUNTANT', NOW(), NOW());