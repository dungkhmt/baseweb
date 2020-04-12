INSERT INTO public.status_type
(status_type_id, parent_type_id, description, last_updated_stamp, created_stamp)
VALUES ('ORDER_STATUS', NULL, 'Order Status', NULL, '2020-02-01 21:35:10.048');
INSERT INTO public.status_type
(status_type_id, parent_type_id, description, last_updated_stamp, created_stamp)
VALUES ('DELIVERY_STATUS', NULL, 'Delivery status', NULL, '2020-03-08 08:43:46.697');

insert into status_item(status_id, status_type_id, status_code, description)
values ('ORDER_CREATED', 'ORDER_STATUS', 'CREATED', 'tạo mới'),
       ('ORDER_CANCELLED', 'ORDER_STATUS', 'CANCELLED', 'đã hủy');