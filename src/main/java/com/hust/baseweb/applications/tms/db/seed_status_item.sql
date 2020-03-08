insert into status_type(status_type_id, description) values('DELIVERY_STATUS','Delivery status');
insert into status_item(status_id,status_type_id,description) values
('SHIPMENT_TRIP_CREATED','DELIVERY_STATUS','Tạo mới'),
('SHIPMENT_TRIP_CANCELLED','DELIVERY_STATUS','Hủy'),
('SHIPMENT_TRIP_COMPLETED','DELIVERY_STATUS','Hoàn thành'),
('SHIPMENT_ITEM_ON_TRIP','DELIVERY_STATUS','Hàng xếp chuyến'),
('SHIPMENT_ITEM_DELIVERED','DELIVERY_STATUS','Đã giao xong'),
('SHIPMENT_ITEM_NOT_DELIVERED','DELIVERY_STATUS','Hàng không được giao'),
('SHIPMENT_ITEM_CANCELLED','DELIVERY_STATUS','Hủy')
;