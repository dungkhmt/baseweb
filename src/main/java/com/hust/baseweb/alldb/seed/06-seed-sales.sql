INSERT INTO public.status_type
(status_type_id, parent_type_id, description, last_updated_stamp, created_stamp)
VALUES ('ORDER_STATUS', NULL, 'Order Status', NULL, '2020-02-01 21:35:10.048');
INSERT INTO public.status_type
(status_type_id, parent_type_id, description, last_updated_stamp, created_stamp)
VALUES ('DELIVERY_STATUS', NULL, 'Delivery status', NULL, '2020-03-08 08:43:46.697');

insert into status_item(status_id, status_type_id, status_code, description)
values ('ORDER_CREATED', 'ORDER_STATUS', 'CREATED', 'tạo mới'),
       ('ORDER_CANCELLED', 'ORDER_STATUS', 'CANCELLED', 'đã hủy');


insert into sales_route_visit_frequency(visit_frequency_id, description) values
('FW1','1 tuần thăm 1 lần'),
('FW2','1 tuần thăm 2 lần'),
('FW3','1 tuần thăm 3 lần'),
('FW4','1 tuần thăm 4 lần'),
('FW5','1 tuần thăm 5 lần'),
('FW6','1 tuần thăm 6 lần'),
('FW7','1 tuần thăm 7 lần'),
('F2W','2 tuần thăm 1 lần'),
('F3W','3 tuần thăm 1 lần'),
('F4W','4 tuần thăm 1 lần');

insert into sales_route_config(days,repeat_week,visit_frequency_id) values
('2,4,6','1','FW3'),
('2,4','1','FW2'),
('3,5','1','FW2'),
('4,6','1','FW2'),
('4','2','F2W'),
('5','3','F3W'),
('6','4','F4W');
