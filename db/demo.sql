INSERT INTO party(id, party_type_id, created_by_user_login_id, updated_by_user_login_id)
VALUES
    ('164f0e68-5a01-11ea-b26d-14dda9bea6d7', 2, 'e66c1e0c-59fb-11ea-b26b-14dda9bea6d7',
        'e66c1e0c-59fb-11ea-b26b-14dda9bea6d7'),
    ('47aacbac-5c39-11ea-98a0-14dda9bea6d7', 2, 'e66c1e0c-59fb-11ea-b26b-14dda9bea6d7',
        'e66c1e0c-59fb-11ea-b26b-14dda9bea6d7'),
    ('c2e65572-5d18-11ea-a7e6-14dda9bea6d7', 1, 'e66c1e0c-59fb-11ea-b26b-14dda9bea6d7',
        'e66c1e0c-59fb-11ea-b26b-14dda9bea6d7');

INSERT INTO person(id, first_name, middle_name, last_name, gender_id, birth_date)
VALUES
    ('c2e65572-5d18-11ea-a7e6-14dda9bea6d7', 'Tùng', 'Thanh', 'Cao', 1, '1997-1-1');

INSERT INTO customer(id, name)
VALUES
    ('164f0e68-5a01-11ea-b26d-14dda9bea6d7', 'test customer 1'),
    ('47aacbac-5c39-11ea-98a0-14dda9bea6d7', 'test customer 2');

INSERT INTO user_login(id, username, password, person_id)
VALUES
    ('51074f18-5851-11ea-98c8-14dda9bea6d7', 'tungcao',
    '$2a$10$eInQwsiNJW9ZRQPb7aXYIOYIEYJ4TLsYFuTvHcaAd.XDqJ.b/dkR.',
    'c2e65572-5d18-11ea-a7e6-14dda9bea6d7'),
    ('c618b8fc-5872-11ea-adab-14dda9bea6d7', 'test',
    '$2a$10$eInQwsiNJW9ZRQPb7aXYIOYIEYJ4TLsYFuTvHcaAd.XDqJ.b/dkR.',
    '8ed51e8e-59fe-11ea-b26c-14dda9bea6d7');

INSERT INTO user_login_security_group(user_login_id, security_group_id)
VALUES
    ('51074f18-5851-11ea-98c8-14dda9bea6d7', 2),
    ('51074f18-5851-11ea-98c8-14dda9bea6d7', 3);


INSERT INTO facility(id, name, facility_type_id, address)
VALUES
    ('28fb8f4a-5a02-11ea-b26e-14dda9bea6d7', 'warehouse', 1, 'Ha Noi'),
    ('3e5b7814-5a02-11ea-b26f-14dda9bea6d7', 'test customer facility 1', 2, 'Ha Noi'),
    ('c0721b28-5a02-11ea-b272-14dda9bea6d7', 'test customer facility 2', 2, 'Ha Noi');

INSERT INTO facility_customer(id, customer_id)
VALUES
    ('3e5b7814-5a02-11ea-b26f-14dda9bea6d7', '164f0e68-5a01-11ea-b26d-14dda9bea6d7'),
    ('c0721b28-5a02-11ea-b272-14dda9bea6d7', '47aacbac-5c39-11ea-98a0-14dda9bea6d7');

INSERT INTO product(id, name, created_by_user_login_id, unit_uom_id)
VALUES
    (1, 'test product 1', 'e66c1e0c-59fb-11ea-b26b-14dda9bea6d7', 'package'),
    (2, 'test product 2', 'e66c1e0c-59fb-11ea-b26b-14dda9bea6d7', 'box');

SELECT setval('product_id_seq', 2, true);

INSERT INTO inventory_item(
    id, product_id, facility_id, quantity, unit_cost, currency_uom_id)
VALUES
    (1, 1, '28fb8f4a-5a02-11ea-b26e-14dda9bea6d7', 10, 100000, 'vnd');

SELECT setval('inventory_item_id_seq', 1, true);
