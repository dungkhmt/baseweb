INSERT INTO user_login(id, username, password)
    VALUES (
        '51074f18-5851-11ea-98c8-14dda9bea6d7', 'tung',
        '$2a$10$eInQwsiNJW9ZRQPb7aXYIOYIEYJ4TLsYFuTvHcaAd.XDqJ.b/dkR.');

INSERT INTO user_login(id, username, password)
    VALUES (
        'c618b8fc-5872-11ea-adab-14dda9bea6d7', 'test',
        '$2a$10$eInQwsiNJW9ZRQPb7aXYIOYIEYJ4TLsYFuTvHcaAd.XDqJ.b/dkR.');

INSERT INTO security_group(id, name) VALUES (1, 'ADMIN');
INSERT INTO security_group(id, name) VALUES (2, 'SALE_SUPERVISOR');

INSERT INTO security_permission(id, name) VALUES (1, 'ADD_USER');
INSERT INTO security_permission(id, name) VALUES (2, 'CREATE_ORDER');

INSERT INTO user_login_security_group(user_login_id, security_group_id)
    VALUES ('51074f18-5851-11ea-98c8-14dda9bea6d7', 1);
INSERT INTO user_login_security_group(user_login_id, security_group_id)
    VALUES ('51074f18-5851-11ea-98c8-14dda9bea6d7', 2);

INSERT INTO security_group_permission(security_group_id, security_permission_id)
    VALUES (1, 1);
INSERT INTO security_group_permission(security_group_id, security_permission_id)
    VALUES (2, 1);

INSERT INTO security_group_permission(security_group_id, security_permission_id)
    VALUES (2, 2);