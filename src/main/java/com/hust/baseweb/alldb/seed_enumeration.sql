INSERT INTO public.enumeration_type
(enumeration_type_id, parent_type_id, description, last_updated_stamp, created_stamp)
VALUES('PROD_PROMO_RULE', NULL, NULL, NULL, '2020-02-15 21:27:25.628');
INSERT INTO public.enumeration_type
(enumeration_type_id, parent_type_id, description, last_updated_stamp, created_stamp)
VALUES('PRODUCT_TRANSPORT_CATEGORY', NULL, NULL, NULL, '2020-03-09 15:59:16.836');
INSERT INTO public.enumeration_type
(enumeration_type_id, parent_type_id, description, last_updated_stamp, created_stamp)
VALUES('DISTANCE_SOURCE', NULL, NULL, NULL, '2020-03-22 23:53:59.100');


INSERT INTO public.enumeration
(enum_id, enum_type_id, enum_code, sequence_id, description, last_updated_stamp, created_stamp)
VALUES('PROD_PROMO_DISCOUNT_PERCENTAGE', 'PROD_PROMO_RULE', NULL, NULL, NULL, NULL, '2020-02-15 21:28:45.612');
INSERT INTO public.enumeration
(enum_id, enum_type_id, enum_code, sequence_id, description, last_updated_stamp, created_stamp)
VALUES('KHO', 'PRODUCT_TRANSPORT_CATEGORY', NULL, NULL, NULL, NULL, '2020-03-09 16:01:14.820');
INSERT INTO public.enumeration
(enum_id, enum_type_id, enum_code, sequence_id, description, last_updated_stamp, created_stamp)
VALUES('LANH', 'PRODUCT_TRANSPORT_CATEGORY', NULL, NULL, NULL, NULL, '2020-03-09 16:01:14.820');
INSERT INTO public.enumeration
(enum_id, enum_type_id, enum_code, sequence_id, description, last_updated_stamp, created_stamp)
VALUES('DONG', 'PRODUCT_TRANSPORT_CATEGORY', NULL, NULL, NULL, NULL, '2020-03-09 16:01:14.820');
INSERT INTO public.enumeration
(enum_id, enum_type_id, enum_code, sequence_id, description, last_updated_stamp, created_stamp)
VALUES('GOOGLE', 'DISTANCE_SOURCE', NULL, NULL, NULL, NULL, '2020-03-22 23:55:50.746');
INSERT INTO public.enumeration
(enum_id, enum_type_id, enum_code, sequence_id, description, last_updated_stamp, created_stamp)
VALUES('HAVERSINE', 'DISTANCE_SOURCE', NULL, NULL, NULL, NULL, '2020-03-22 23:55:50.746');
INSERT INTO public.enumeration
(enum_id, enum_type_id, enum_code, sequence_id, description, last_updated_stamp, created_stamp)
VALUES('OPEN_STREET_MAP', 'DISTANCE_SOURCE', NULL, NULL, NULL, NULL, '2020-03-22 23:55:50.746');
