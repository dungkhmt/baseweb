insert into status_type(status_type_id,description) values
('ORDER_STATUS','Order Status');

insert into status_item(status_id,status_type_id,status_code,description) values
('ORDER_CREATED','ORDER_STATUS','CREATED','tạo mới'),
('ORDER_CANCELLED','ORDER_STATUS','CANCELLED','đã hủy')
;

