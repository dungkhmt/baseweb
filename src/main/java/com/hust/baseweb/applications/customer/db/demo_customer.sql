INSERT INTO public.party
(party_id, party_type_id, external_id, description, status_id, created_date, created_by_user_login, last_modified_date, last_modified_by_user_login, is_unread, last_updated_stamp, created_stamp, party_code)
VALUES('8161d37e-4026-11ea-9be3-54bf64436441', 'PARTY_DISTRIBUTOR', NULL, NULL, NULL, NULL, NULL, NULL, NULL, false, '2020-01-26 17:27:51.182', '2020-01-26 17:27:51.182', NULL);
INSERT INTO public.party
(party_id, party_type_id, external_id, description, status_id, created_date, created_by_user_login, last_modified_date, last_modified_by_user_login, is_unread, last_updated_stamp, created_stamp, party_code)
VALUES('875704ac-4026-11ea-9be4-54bf64436441', 'PARTY_DISTRIBUTOR', NULL, NULL, NULL, NULL, NULL, NULL, NULL, false, '2020-01-26 17:28:01.179', '2020-01-26 17:28:01.179', NULL);
INSERT INTO public.party
(party_id, party_type_id, external_id, description, status_id, created_date, created_by_user_login, last_modified_date, last_modified_by_user_login, is_unread, last_updated_stamp, created_stamp, party_code)
VALUES('5ae20c48-4d6c-11ea-967e-54bf64436441', 'PARTY_RETAILOUTLET', NULL, NULL, 'PARTY_ENABLED', NULL, 'admin', NULL, NULL, NULL, NULL, '2020-02-12 14:50:36.479', NULL);
INSERT INTO public.party
(party_id, party_type_id, external_id, description, status_id, created_date, created_by_user_login, last_modified_date, last_modified_by_user_login, is_unread, last_updated_stamp, created_stamp, party_code)
VALUES('d8d8fb18-4d6f-11ea-9681-54bf64436441', 'PARTY_RETAILOUTLET', NULL, NULL, 'PARTY_ENABLED', NULL, 'admin', NULL, NULL, NULL, NULL, '2020-02-12 15:15:36.302', NULL);
INSERT INTO public.party
(party_id, party_type_id, external_id, description, status_id, created_date, created_by_user_login, last_modified_date, last_modified_by_user_login, is_unread, last_updated_stamp, created_stamp, party_code)
VALUES('5b63a1ea-4d71-11ea-9684-54bf64436441', 'PARTY_RETAILOUTLET', NULL, NULL, 'PARTY_ENABLED', NULL, 'admin', NULL, NULL, NULL, NULL, '2020-02-12 15:26:24.812', NULL);

INSERT INTO public.party_customer
(party_id, customer_name, status_id, description, last_updated_stamp, created_stamp, party_type_id)
VALUES('8161d37e-4026-11ea-9be3-54bf64436441', 'Nhà phân phối Sóc Sơn', NULL, NULL, NULL, '2020-01-26 17:30:54.982', NULL);
INSERT INTO public.party_customer
(party_id, customer_name, status_id, description, last_updated_stamp, created_stamp, party_type_id)
VALUES('875704ac-4026-11ea-9be4-54bf64436441', 'Nhà phân phối Đông Anh', NULL, NULL, NULL, '2020-01-26 17:30:54.982', NULL);
INSERT INTO public.party_customer
(party_id, customer_name, status_id, description, last_updated_stamp, created_stamp, party_type_id)
VALUES('5ae20c48-4d6c-11ea-967e-54bf64436441', 'VinMart Thăng Long A', NULL, NULL, NULL, '2020-02-12 15:13:10.113', NULL);
INSERT INTO public.party_customer
(party_id, customer_name, status_id, description, last_updated_stamp, created_stamp, party_type_id)
VALUES('d8d8fb18-4d6f-11ea-9681-54bf64436441', 'VinMart Bắc Từ Liêm', NULL, NULL, NULL, '2020-02-12 15:18:13.122', NULL);
INSERT INTO public.party_customer
(party_id, customer_name, status_id, description, last_updated_stamp, created_stamp, party_type_id)
VALUES('5b63a1ea-4d71-11ea-9684-54bf64436441', 'VinMart Trung Hòa', NULL, NULL, NULL, '2020-02-12 15:27:28.832', NULL);

INSERT INTO public.party_contact_mech_purpose
(party_id, contact_mech_id, contact_mech_purpose_type_id, from_date, thru_date, last_updated_stamp, created_stamp)
VALUES('5ae20c48-4d6c-11ea-967e-54bf64436441', 'f581f14a-4d6d-11ea-9680-54bf64436441', 'PRIMARY_LOCATION', '2020-02-12 15:04:40.675', NULL, NULL, '2020-02-12 15:04:40.675');
INSERT INTO public.party_contact_mech_purpose
(party_id, contact_mech_id, contact_mech_purpose_type_id, from_date, thru_date, last_updated_stamp, created_stamp)
VALUES('d8d8fb18-4d6f-11ea-9681-54bf64436441', '9bd89006-4d70-11ea-9683-54bf64436441', 'PRIMARY_LOCATION', '2020-02-12 15:21:35.321', NULL, NULL, '2020-02-12 15:21:35.321');
INSERT INTO public.party_contact_mech_purpose
(party_id, contact_mech_id, contact_mech_purpose_type_id, from_date, thru_date, last_updated_stamp, created_stamp)
VALUES('5b63a1ea-4d71-11ea-9684-54bf64436441', 'b6a4f66c-4d71-11ea-9686-54bf64436441', 'PRIMARY_LOCATION', '2020-02-12 15:30:03.371', NULL, NULL, '2020-02-12 15:30:03.371');

