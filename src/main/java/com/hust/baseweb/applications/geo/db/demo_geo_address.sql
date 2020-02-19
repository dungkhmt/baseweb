INSERT INTO public.contact_mech_purpose_type
    (contact_mech_purpose_type_id, description)
VALUES ('SHIPPING_LOCATION', 'Shipping Destination Address');

INSERT INTO public.contact_mech_purpose_type
    (contact_mech_purpose_type_id, description)
VALUES ('PRIMARY_LOCATION', 'Primary Address');


INSERT INTO public.geo_point
    (geo_point_id, longitude, latitude, last_updated_stamp, created_stamp)
VALUES ('c08818e8-4d6d-11ea-967f-54bf64436441', '105.723729', '21.000712', NULL, '2020-02-12 15:00:36.514');
INSERT INTO public.geo_point
    (geo_point_id, longitude, latitude, last_updated_stamp, created_stamp)
VALUES ('4b6745f4-4d70-11ea-9682-54bf64436441', '105.780846', '21.052961', NULL, '2020-02-12 15:18:48.495');
INSERT INTO public.geo_point
    (geo_point_id, longitude, latitude, last_updated_stamp, created_stamp)
VALUES ('9d0e8cd6-4d71-11ea-9685-54bf64436441', '105.801801', '21.007741', NULL, '2020-02-12 15:28:14.984');

INSERT INTO public.postal_address
(contact_mech_id, address, postal_code, geo_point_id, country_geo_id, state_province_geo_id, city, last_updated_stamp,
 created_stamp)
VALUES ('f581f14a-4d6d-11ea-9680-54bf64436441',
        'Khu đô thị VinHomes Thăng Long, đại lộ Thăng Long, phố Lê Trọng Tấn, thành phố Hà Nội', NULL,
        'c08818e8-4d6d-11ea-967f-54bf64436441', NULL, NULL, NULL, NULL, '2020-02-12 15:02:05.392');
INSERT INTO public.postal_address
(contact_mech_id, address, postal_code, geo_point_id, country_geo_id, state_province_geo_id, city, last_updated_stamp,
 created_stamp)
VALUES ('9bd89006-4d70-11ea-9683-54bf64436441',
        'Khu B1 Trung tâm thương mại Vincom Plaza Bắc Từ Liêm, CC Green Stars, số 234 Phạm Văn Đồng, phường Cổ nhuế 1, quận Bắc Từ Liêm, thành phố Hà Nội',
        NULL, '4b6745f4-4d70-11ea-9682-54bf64436441', NULL, NULL, NULL, NULL, '2020-02-12 15:21:03.456');
INSERT INTO public.postal_address
(contact_mech_id, address, postal_code, geo_point_id, country_geo_id, state_province_geo_id, city, last_updated_stamp,
 created_stamp)
VALUES ('b6a4f66c-4d71-11ea-9686-54bf64436441',
        'Tầng hầm B1, N05, khu đô thị Trung Hòa Nhân Chính, phường Hoàng Đạo Thúy, quận Cầu Giấy, thành phố Hà Nội',
        NULL, '9d0e8cd6-4d71-11ea-9685-54bf64436441', NULL, NULL, NULL, NULL, '2020-02-12 15:28:57.913');
