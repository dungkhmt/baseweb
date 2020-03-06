INSERT INTO party_type(id, name)
VALUES
    (1, 'PERSON'),
    (2, 'CUSTOMER');

INSERT INTO party(id, party_type_id, created_by_user_login_id, updated_by_user_login_id)
VALUES
    ('8ed51e8e-59fe-11ea-b26c-14dda9bea6d7', 1, 'e66c1e0c-59fb-11ea-b26b-14dda9bea6d7',
        'e66c1e0c-59fb-11ea-b26b-14dda9bea6d7');

INSERT INTO gender(id, name)
VALUES
    (1, 'MALE'),
    (2, 'FEMALE'),
    (3, 'UNKNOWN');

INSERT INTO person(id, first_name, middle_name, last_name, gender_id, birth_date)
VALUES
    ('8ed51e8e-59fe-11ea-b26c-14dda9bea6d7', 'Tùng', 'Quang', 'Tạ', 1, '1997-12-29');

INSERT INTO user_login(id, username, password, person_id)
VALUES
    ('e66c1e0c-59fb-11ea-b26b-14dda9bea6d7', 'admin',
        '$2a$10$eInQwsiNJW9ZRQPb7aXYIOYIEYJ4TLsYFuTvHcaAd.XDqJ.b/dkR.',
        '8ed51e8e-59fe-11ea-b26c-14dda9bea6d7');

INSERT INTO security_group(id, name)
VALUES
    (1, 'ADMIN'),
    (2, 'PRODUCT_MANAGER'),
    (3, 'SALES_MANAGER'),
    (4, 'FACILITY_MANAGER'),
    (5, 'INVENTORY_MANAGER'),
    (6, 'EXPORT_MANAGER');

INSERT INTO security_permission(id, name)
VALUES
    (1, 'VIEW_EDIT_PARTY'),
    (2, 'VIEW_EDIT_USER_LOGIN'),
    (3, 'VIEW_EDIT_SECURITY_GROUP'),
    (4, 'VIEW_EDIT_SECURITY_PERMISSION'),
    (5, 'VIEW_EDIT_ORDER'),
    (6, 'VIEW_EDIT_PRODUCT'),
    (7, 'VIEW_EDIT_FACILITY'),
    (8, 'IMPORT'),
    (9, 'EXPORT');

INSERT INTO user_login_security_group(user_login_id, security_group_id)
VALUES
    ('e66c1e0c-59fb-11ea-b26b-14dda9bea6d7', 1),
    ('e66c1e0c-59fb-11ea-b26b-14dda9bea6d7', 2),
    ('e66c1e0c-59fb-11ea-b26b-14dda9bea6d7', 3),
    ('e66c1e0c-59fb-11ea-b26b-14dda9bea6d7', 4),
    ('e66c1e0c-59fb-11ea-b26b-14dda9bea6d7', 5),
    ('e66c1e0c-59fb-11ea-b26b-14dda9bea6d7', 6);

INSERT INTO security_group_permission(security_group_id, security_permission_id)
VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (1, 4),
    (2, 6),
    (3, 5),
    (4, 7),
    (5, 8),
    (6, 9);


INSERT INTO weight_uom(id)
VALUES ('kg'), ('g'), ('mg');

INSERT INTO unit_uom(id)
VALUES ('package'), ('box'), ('bottle');

INSERT INTO facility_type(id, name)
VALUES
    (1, 'WAREHOUSE'),
    (2, 'CUSTOMER_STORE');

INSERT INTO currency_uom(id)
VALUES ('vnd'), ('usd');

INSERT INTO sale_order_item_status(id, name)
VALUES
    (1, 'CREATED'),
    (2, 'EXPORTED'),
    (3, 'SHIPPING'),
    (4, 'COMPLETED');

INSERT INTO day_of_week(day)
VALUES (0), (1), (2), (3), (4), (5), (6);
